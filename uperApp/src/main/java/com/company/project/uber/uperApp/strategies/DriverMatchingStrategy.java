package com.company.project.uber.uperApp.strategies;

import com.company.project.uber.uperApp.entities.Driver;
import com.company.project.uber.uperApp.entities.RideRequest;

import java.util.List;

public interface DriverMatchingStrategy {

    List<Driver> findMatchingDriver(RideRequest rideRequest);
}