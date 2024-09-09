package com.company.project.uber.uperApp.repositories;

import com.company.project.uber.uperApp.entities.RideRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideRequestRepositories extends JpaRepository<RideRequest, Long> {

}
