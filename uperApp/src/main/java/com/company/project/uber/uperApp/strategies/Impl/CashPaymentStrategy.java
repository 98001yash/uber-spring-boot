package com.company.project.uber.uperApp.strategies.Impl;

import com.company.project.uber.uperApp.entities.Driver;
import com.company.project.uber.uperApp.entities.Payment;
import com.company.project.uber.uperApp.entities.Wallet;
import com.company.project.uber.uperApp.entities.enums.PaymentStatus;
import com.company.project.uber.uperApp.entities.enums.TransactionMethod;
import com.company.project.uber.uperApp.repositories.PaymentRepository;
import com.company.project.uber.uperApp.services.PaymentService;
import com.company.project.uber.uperApp.services.WalletService;
import com.company.project.uber.uperApp.strategies.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CashPaymentStrategy implements PaymentStrategy {

    private final WalletService walletService;
    private final PaymentRepository paymentRepository;
    @Override
    public void processPayment(Payment payment) {
        Driver driver = payment.getRide().getDriver();

        Wallet driverWallet = walletService.findByUser(driver.getUser());
        double platformCommission =payment.getAmount() * PLATFORM_COMMISSION;

        walletService.deductMoneyFromWallet(driver.getUser(),platformCommission,null,
                payment.getRide(), TransactionMethod.RIDE);

        payment.setPaymentStatus(PaymentStatus.CONFIRMED);
        paymentRepository.save(payment);



    }
}
