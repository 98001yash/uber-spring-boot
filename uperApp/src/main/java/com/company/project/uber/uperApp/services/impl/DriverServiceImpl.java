package com.company.project.uber.uperApp.services.impl;

import com.company.project.uber.uperApp.dto.DriverDto;
import com.company.project.uber.uperApp.dto.RideDto;
import com.company.project.uber.uperApp.dto.RiderDto;
import com.company.project.uber.uperApp.entities.Driver;
import com.company.project.uber.uperApp.entities.Ride;
import com.company.project.uber.uperApp.entities.RideRequest;
import com.company.project.uber.uperApp.entities.User;
import com.company.project.uber.uperApp.entities.enums.RideRequestStatus;
import com.company.project.uber.uperApp.entities.enums.RideStatus;
import com.company.project.uber.uperApp.exceptions.ResourceNotFoundException;
import com.company.project.uber.uperApp.repositories.DriverRepositories;
import com.company.project.uber.uperApp.services.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final RideRequestService rideRequestService;
    private final DriverRepositories driverRepositories;
    private final RideService rideService;
    private final ModelMapper modelMapper;
    private final PaymentService paymentService;
    private final RatingService ratingService;

    @Override
    @Transactional
    public RideDto acceptRide(Long rideRequestId) {
        RideRequest rideRequest = rideRequestService.findRideRequestById(rideRequestId);

        if(!rideRequest.getRideRequestStatus().equals(RideRequestStatus.PENDING)) {
            throw new RuntimeException("RideRequest cannot be accepted, status is "+ rideRequest.getRideRequestStatus());
        }

        Driver currentDriver = getCurrentDriver();
        if(!currentDriver.getAvailable()) {
            throw new RuntimeException("Driver cannot accept ride due to unavailability");
        }

        Driver savedDriver = updateDriverAvailability(currentDriver, false);

        Ride ride = rideService.createNewRide(rideRequest, savedDriver);
        return modelMapper.map(ride, RideDto.class);
    }

    @Override
    public RideDto cancelRide(Long rideId) {
        Ride ride = rideService.getRideById(rideId);

        Driver driver = getCurrentDriver();
        if(!driver.equals(ride.getDriver())) {
            throw new RuntimeException("Driver cannot start a ride as he has not accepted it earlier");
        }

        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)) {
            throw new RuntimeException("Ride cannot be cancelled, invalid status: "+ride.getRideStatus());
        }

        rideService.updateRideStatus(ride, RideStatus.CANCELLED);
        updateDriverAvailability(driver, true);

        return modelMapper.map(ride, RideDto.class);
    }

    @Override
    public RideDto startRide(Long rideId, String otp) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();

        if(!driver.equals(ride.getDriver())) {
            throw new RuntimeException("Driver cannot start a ride as he has not accepted it earlier");
        }

        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)) {
            throw new RuntimeException("Ride status is not CONFIRMED hence cannot be started, status: "+ride.getRideStatus());
        }

        if(!otp.equals(ride.getOtp())) {
            throw new RuntimeException("Otp is not valid, otp: "+otp);
        }

        ride.setStartedAt(LocalDateTime.now());
        Ride savedRide = rideService.updateRideStatus(ride, RideStatus.ONGOING);

        paymentService.createNewPayment(savedRide);
        ratingService.createNewRating(savedRide);

        return modelMapper.map(savedRide, RideDto.class);
    }

    @Override
    @Transactional
    public RideDto endRide(Long rideId) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();

        if(!driver.equals(ride.getDriver())) {
            throw new RuntimeException("Driver cannot start a ride as he has not accepted it earlier");
        }

        if(!ride.getRideStatus().equals(RideStatus.ONGOING)) {
            throw new RuntimeException("Ride status is not ONGOING hence cannot be ended, status: "+ride.getRideStatus());
        }

        ride.setEndedAt(LocalDateTime.now());
        Ride savedRide = rideService.updateRideStatus(ride, RideStatus.ENDED);
        updateDriverAvailability(driver, true);

        paymentService.processPayment(ride);

        return modelMapper.map(savedRide, RideDto.class);
    }

    @Override
    public RiderDto rateRider(Long rideId, Integer rating) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();

        if(!driver.equals(ride.getDriver())) {
            throw new RuntimeException("Driver is not the owner of this Ride");
        }

        if(!ride.getRideStatus().equals(RideStatus.ENDED)) {
            throw new RuntimeException("Ride status is not Ended hence cannot start rating, status: "+ride.getRideStatus());
        }

        return ratingService.rateRider(ride, rating);
    }

    @Override
    public DriverDto getMyProfile() {
        Driver currentDriver = getCurrentDriver();
        return modelMapper.map(currentDriver, DriverDto.class);
    }

    @Override
    public Page<RideDto> getAllMyRides(PageRequest pageRequest) {
        Driver currentDriver = getCurrentDriver();
        return rideService.getAllRidesOfDriver(currentDriver, pageRequest).map(
                ride -> modelMapper.map(ride, RideDto.class)
        );
    }

    @Override
    public Driver getCurrentDriver() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return driverRepositories.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not associated with user with " +
                        "id "+user.getId()));
    }

    @Override
    public Driver updateDriverAvailability(Driver driver, boolean available) {
        driver.setAvailable(available);
        return driverRepositories.save(driver);
    }

    @Override
    public Driver createNewDriver(Driver driver) {
        return driverRepositories.save(driver);
    }

}
