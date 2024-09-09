package com.company.project.uber.uperApp.services.impl;

import com.company.project.uber.uperApp.dto.DriverDto;
import com.company.project.uber.uperApp.dto.RiderDto;
import com.company.project.uber.uperApp.entities.Driver;
import com.company.project.uber.uperApp.entities.Rating;
import com.company.project.uber.uperApp.entities.Ride;
import com.company.project.uber.uperApp.entities.Rider;
import com.company.project.uber.uperApp.exceptions.ResourceNotFoundException;
import com.company.project.uber.uperApp.exceptions.RuntimeConflictException;
import com.company.project.uber.uperApp.repositories.DriverRepositories;
import com.company.project.uber.uperApp.repositories.RatingRepository;
import com.company.project.uber.uperApp.repositories.RiderRepositories;
import com.company.project.uber.uperApp.services.RatingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final ModelMapper modelMapper;
    private final RatingRepository ratingRepository;
    private final DriverRepositories driverRepositories;
    private final RiderRepositories riderRepositories;


    @Override
    public DriverDto rateDriver(Ride ride, Integer rating) {
        Driver driver = ride.getDriver();
        Rating ratingObj = ratingRepository.findByRide(ride)
                .orElseThrow(()->new ResourceNotFoundException("Rating not found with id: "+ride.getId()));

        if(ratingObj.getDriverRating()!=null)
            throw new RuntimeConflictException("Driver has already been rated caanot rate again..");


        ratingObj.setDriverRating(rating);

        ratingRepository.save(ratingObj);
       Double newRating =  ratingRepository.findByDriver(driver)
               .stream()
               .mapToDouble(Rating:: getDriverRating)
               .average().orElse(0.0);
       driver.setRating(newRating);

       Driver savedDriver = driverRepositories.save(driver);
    return modelMapper.map(savedDriver,DriverDto.class);
    }

    @Override
    public RiderDto rateRider(Ride ride, Integer rating) {

        Rider rider = ride.getRider();
        Rating ratingObj = ratingRepository.findByRide(ride)
                .orElseThrow(()->new ResourceNotFoundException("Rating not found with id: "+ride.getId()));

        if(ratingObj.getRiderRating()!=null)
            throw new RuntimeConflictException("Rider has already been rated caanot rate again..");


        ratingObj.setRiderRating(rating);

        ratingRepository.save(ratingObj);
        Double newRating =  ratingRepository.findByRider(rider)
                .stream()
                .mapToDouble(Rating:: getRiderRating)
                .average().orElse(0.0);
        rider.setRating(newRating);

        Rider savedRider = riderRepositories.save(rider);
   return modelMapper.map(savedRider, RiderDto.class);
    }

    @Override
    public void createNewRating(Ride ride) {
    Rating rating = Rating.builder()
            .rider(ride.getRider())
            .driver(ride.getDriver())
            .ride(ride)
            .build();
    ratingRepository.save(rating);
    }
}
