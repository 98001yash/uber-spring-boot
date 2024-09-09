package com.company.project.uber.uperApp.strategies.Impl;
import com.company.project.uber.uperApp.entities.Driver;
import com.company.project.uber.uperApp.entities.Payment;
import com.company.project.uber.uperApp.entities.Rider;
import com.company.project.uber.uperApp.entities.enums.PaymentStatus;
import com.company.project.uber.uperApp.entities.enums.TransactionMethod;
import com.company.project.uber.uperApp.repositories.PaymentRepository;
import com.company.project.uber.uperApp.services.PaymentService;
import com.company.project.uber.uperApp.services.WalletService;
import com.company.project.uber.uperApp.strategies.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletPaymentStrategy implements PaymentStrategy {

    private final WalletService walletService;
  private final PaymentRepository paymentRepository;


    @Override
    @Transactional
    public void processPayment(Payment payment) {
        Driver driver = payment.getRide().getDriver();
        Rider rider = payment.getRide().getRider();

        walletService.deductMoneyFromWallet(rider.getUser(),
                payment.getAmount(), null,payment.getRide(), TransactionMethod.RIDE);

        double driversCut = payment.getAmount() * (1 - PLATFORM_COMMISSION);

        walletService.addMoneyToWallet(driver.getUser(),
                driversCut,null,payment.getRide(),TransactionMethod.RIDE);


        payment.setPaymentStatus(PaymentStatus.CONFIRMED);
        paymentRepository.save(payment);
    }
}
