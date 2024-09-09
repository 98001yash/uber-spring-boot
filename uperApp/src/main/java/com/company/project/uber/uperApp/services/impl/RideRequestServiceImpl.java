package com.company.project.uber.uperApp.services.impl;

import com.company.project.uber.uperApp.entities.RideRequest;
import com.company.project.uber.uperApp.exceptions.ResourceNotFoundException;
import com.company.project.uber.uperApp.repositories.RideRequestRepositories;
import com.company.project.uber.uperApp.services.RideRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideRequestServiceImpl implements RideRequestService {

    private final RideRequestRepositories  rideRequestRepository;

    @Override
    public RideRequest findRideRequestById(Long rideRequestId) {
        return rideRequestRepository.findById(rideRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("RideRequest not found with id: "+rideRequestId));
    }

    @Override
    public void update(RideRequest rideRequest) {
        rideRequestRepository.findById(rideRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException("RideRequest not found with id: "+rideRequest.getId()));
        rideRequestRepository.save(rideRequest);
    }
}
