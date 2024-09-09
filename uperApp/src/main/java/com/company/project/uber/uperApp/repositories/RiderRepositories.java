package com.company.project.uber.uperApp.repositories;

import com.company.project.uber.uperApp.entities.Rider;
import com.company.project.uber.uperApp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiderRepositories extends JpaRepository<Rider, Long> {
    Optional<Rider> findByUser(User user);
}
