package com.company.project.uber.uperApp.repositories;

import com.company.project.uber.uperApp.entities.Payment;
import com.company.project.uber.uperApp.entities.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
   Optional<Payment> findByRide(Ride ride);
}
