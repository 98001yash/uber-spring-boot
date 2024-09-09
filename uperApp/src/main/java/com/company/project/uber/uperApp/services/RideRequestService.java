package com.company.project.uber.uperApp.services;

import com.company.project.uber.uperApp.entities.RideRequest;

public interface RideRequestService {

    RideRequest findRideRequestById(Long rideRequestId);

    void update(RideRequest rideRequest);
}
