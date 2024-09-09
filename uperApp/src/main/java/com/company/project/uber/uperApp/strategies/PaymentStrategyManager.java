package com.company.project.uber.uperApp.strategies;


import com.company.project.uber.uperApp.entities.enums.PaymentMethod;
import com.company.project.uber.uperApp.strategies.Impl.CashPaymentStrategy;
import com.company.project.uber.uperApp.strategies.Impl.WalletPaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentStrategyManager {

    private final WalletPaymentStrategy walletPaymentStrategy;
    private final CashPaymentStrategy  cashPaymentStrategy;

    public PaymentStrategy paymentStrategy(PaymentMethod paymentMethod) {
        return switch(paymentMethod){
            case WALLET ->walletPaymentStrategy;
            case CASH ->cashPaymentStrategy;
        };
    }
}
