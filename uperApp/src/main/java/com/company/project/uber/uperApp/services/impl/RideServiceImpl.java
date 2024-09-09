package com.company.project.uber.uperApp.services.impl;


import com.company.project.uber.uperApp.entities.Driver;
import com.company.project.uber.uperApp.entities.Ride;
import com.company.project.uber.uperApp.entities.RideRequest;
import com.company.project.uber.uperApp.entities.Rider;
import com.company.project.uber.uperApp.entities.enums.RideRequestStatus;
import com.company.project.uber.uperApp.entities.enums.RideStatus;
import com.company.project.uber.uperApp.exceptions.ResourceNotFoundException;
import com.company.project.uber.uperApp.repositories.RideRepositories;
import com.company.project.uber.uperApp.services.RideRequestService;
import com.company.project.uber.uperApp.services.RideService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideRepositories rideRepositories;
    private final RideRequestService rideRequestService;
    private final ModelMapper modelMapper;

    @Override
    public Ride getRideById(Long rideId) {
        return rideRepositories.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found with id: "+rideId));
    }


    @Override
    public Ride createNewRide(RideRequest rideRequest, Driver driver) {
        rideRequest.setRideRequestStatus(RideRequestStatus.CONFIRMED);

        Ride ride = modelMapper.map(rideRequest, Ride.class);
        ride.setRideStatus(RideStatus.CONFIRMED);
        ride.setDriver(driver);
        ride.setOtp(generateRandomOTP());
        ride.setId(null);

        rideRequestService.update(rideRequest);
        return rideRepositories.save(ride);
    }

    @Override
    public Ride updateRideStatus(Ride ride, RideStatus rideStatus) {
        ride.setRideStatus(rideStatus);
        return rideRepositories.save(ride);
    }

    @Override
    public Page<Ride> getAllRidesOfRider(Rider rider, PageRequest pageRequest) {
        return rideRepositories.findByRider(rider, pageRequest);
    }

    @Override
    public Page<Ride> getAllRidesOfDriver(Driver driver, PageRequest pageRequest) {
        return rideRepositories.findByDriver(driver, pageRequest);
    }

    private String generateRandomOTP() {
        Random random = new Random();
        int otpInt = random.nextInt(10000);  //0 to 9999
        return String.format("%04d", otpInt);
    }
}
