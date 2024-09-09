package com.company.project.uber.uperApp.strategies;

import com.company.project.uber.uperApp.entities.Payment;

public interface PaymentStrategy {

    Double PLATFORM_COMMISSION = 0.3;
    void processPayment(Payment payment);


}
