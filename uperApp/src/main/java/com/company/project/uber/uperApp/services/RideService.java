package com.company.project.uber.uperApp.services;

import com.company.project.uber.uperApp.dto.RideRequestDto;
import com.company.project.uber.uperApp.entities.Driver;
import com.company.project.uber.uperApp.entities.Ride;
import com.company.project.uber.uperApp.entities.RideRequest;
import com.company.project.uber.uperApp.entities.Rider;
import com.company.project.uber.uperApp.entities.enums.RideStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RideService {

    Ride getRideById(Long rideId);

    Ride createNewRide(RideRequest rideRequest, Driver driver);

    Ride updateRideStatus(Ride ride, RideStatus rideStatus);

    Page<Ride> getAllRidesOfRider(Rider rider, PageRequest pageRequest);

    Page<Ride> getAllRidesOfDriver(Driver driver, PageRequest pageRequest);
}
