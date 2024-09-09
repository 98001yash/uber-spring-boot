package com.company.project.uber.uperApp.services.impl;


import com.company.project.uber.uperApp.entities.Payment;
import com.company.project.uber.uperApp.entities.Ride;
import com.company.project.uber.uperApp.entities.enums.PaymentStatus;
import com.company.project.uber.uperApp.exceptions.ResourceNotFoundException;
import com.company.project.uber.uperApp.repositories.PaymentRepository;
import com.company.project.uber.uperApp.services.PaymentService;
import com.company.project.uber.uperApp.strategies.PaymentStrategyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentStrategyManager paymentStrategyManager;


    @Override
    public void processPayment(Ride ride) {
        Payment payment = paymentRepository.findByRide(ride)
                        .orElseThrow(()->new ResourceNotFoundException("payment not found for ride with id: "+ride.getId()));
     paymentStrategyManager.paymentStrategy(payment.getPaymentMethod()).processPayment(payment);
    }

    @Override
    public Payment createNewPayment(Ride ride) {
       Payment payment = Payment.builder()
               .ride(ride)
               .paymentMethod(ride.getPaymentMethod())
               .amount(ride.getFare())
               .paymentStatus(PaymentStatus.PENDING)
               .build();
       return paymentRepository.save(payment);
    }

    @Override
    public void updatePaymentStatus(Payment payment, PaymentStatus status) {
        payment.setPaymentStatus(status);
        paymentRepository.save(payment);
    }
}
