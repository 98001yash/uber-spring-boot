package com.company.project.uber.uperApp.services;

import com.company.project.uber.uperApp.dto.DriverDto;
import com.company.project.uber.uperApp.dto.RideDto;
import com.company.project.uber.uperApp.dto.RideRequestDto;
import com.company.project.uber.uperApp.dto.RiderDto;
import com.company.project.uber.uperApp.entities.Rider;
import com.company.project.uber.uperApp.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface RiderService {

    RideRequestDto requestRide(RideRequestDto rideRequestDto);

    RideDto cancelRide(Long rideId);

    DriverDto rateDriver(Long rideId, Integer rating);

    RiderDto getMyProfile();

    Page<RideDto> getAllMyRides(PageRequest pageRequest);

    Rider createNewRider(User user);

    Rider getCurrentRider();
}

