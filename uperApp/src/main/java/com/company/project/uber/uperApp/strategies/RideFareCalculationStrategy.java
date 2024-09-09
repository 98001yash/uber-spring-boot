package com.company.project.uber.uperApp.strategies;

import com.company.project.uber.uperApp.entities.RideRequest;

public interface RideFareCalculationStrategy {

    double RIDE_FARE_MULTIPLIER = 10;

    double calculateFare(RideRequest rideRequest);

}
