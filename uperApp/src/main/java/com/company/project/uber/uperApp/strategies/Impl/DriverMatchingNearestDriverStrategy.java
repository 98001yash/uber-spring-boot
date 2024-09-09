package com.company.project.uber.uperApp.strategies.Impl;

import com.company.project.uber.uperApp.entities.Driver;
import com.company.project.uber.uperApp.entities.RideRequest;
import com.company.project.uber.uperApp.repositories.DriverRepositories;
import com.company.project.uber.uperApp.strategies.DriverMatchingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DriverMatchingNearestDriverStrategy implements DriverMatchingStrategy {

    private final DriverRepositories  driverRepository;

    @Override
    public List<Driver> findMatchingDriver(RideRequest rideRequest) {
        return driverRepository.findTenNearestDrivers(rideRequest.getPickupLocation());
    }
}

