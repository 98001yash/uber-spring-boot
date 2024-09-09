package com.company.project.uber.uperApp.services;

import com.company.project.uber.uperApp.entities.Payment;
import com.company.project.uber.uperApp.entities.Ride;
import com.company.project.uber.uperApp.entities.enums.PaymentStatus;

public interface PaymentService {

    void processPayment(Ride ride);

    Payment createNewPayment(Ride ride);

    void updatePaymentStatus(Payment payment, PaymentStatus status);
}
