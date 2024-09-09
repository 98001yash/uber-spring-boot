package com.company.project.uber.uperApp.services;

import com.company.project.uber.uperApp.entities.Ride;
import com.company.project.uber.uperApp.entities.User;
import com.company.project.uber.uperApp.entities.Wallet;
import com.company.project.uber.uperApp.entities.enums.TransactionMethod;

public interface WalletService {
    Wallet addMoneyToWallet(User user, Double amount,
                            String transactionId, Ride ride,
                            TransactionMethod transactionMethod);

    Wallet deductMoneyFromWallet(User user, Double amount,
                                 String transactionId, Ride ride,
                                 TransactionMethod transactionMethod);
    void WithdrawAllMyMoneyFromWallet();

    Wallet findWalletById(Long walletId);

    Wallet createNewWallet(User user);
    Wallet findByUser(User user);
}
