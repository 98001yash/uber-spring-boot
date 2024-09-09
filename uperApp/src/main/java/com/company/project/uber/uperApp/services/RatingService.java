package com.company.project.uber.uperApp.services;

import com.company.project.uber.uperApp.dto.DriverDto;
import com.company.project.uber.uperApp.dto.RiderDto;
import com.company.project.uber.uperApp.entities.Driver;
import com.company.project.uber.uperApp.entities.Ride;
import com.company.project.uber.uperApp.entities.Rider;

public interface RatingService {
    DriverDto rateDriver(Ride ride, Integer rating);
    RiderDto rateRider(Ride ride, Integer rating);


    void createNewRating(Ride ride);
}
