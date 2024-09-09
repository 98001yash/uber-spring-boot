package com.company.project.uber.uperApp.repositories;


import com.company.project.uber.uperApp.entities.Driver;
import com.company.project.uber.uperApp.entities.Rating;
import com.company.project.uber.uperApp.entities.Ride;
import com.company.project.uber.uperApp.entities.Rider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating,Long> {

    List<Rating> findByRider(Rider rider);
    List<Rating> findByDriver(Driver driver);

   Optional<Rating> findByRide(Ride ride);
}
