package com.company.project.uber.uperApp.services.impl;

import com.company.project.uber.uperApp.entities.Ride;
import com.company.project.uber.uperApp.entities.User;
import com.company.project.uber.uperApp.entities.Wallet;
import com.company.project.uber.uperApp.entities.WalletTransaction;
import com.company.project.uber.uperApp.entities.enums.TransactionMethod;
import com.company.project.uber.uperApp.entities.enums.TransactionType;
import com.company.project.uber.uperApp.exceptions.ResourceNotFoundException;
import com.company.project.uber.uperApp.repositories.WalletRepository;
import com.company.project.uber.uperApp.services.WalletService;
import com.company.project.uber.uperApp.services.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

  private final WalletRepository walletRepository;
  private final WalletTransactionService walletTransactionService;


    @Override
    @Transactional
    public Wallet addMoneyToWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod) {
      Wallet wallet =findByUser(user);
      wallet.setBalance(wallet.getBalance()+amount);

      WalletTransaction walletTransaction = WalletTransaction.builder()
              .transactionId(transactionId)
              .ride(ride)
              .wallet(wallet)
              .transactionType(TransactionType.CREDIT)
              .transactionMethod(transactionMethod)
              .amount(amount)
              .build();
walletTransactionService.createNewWalletTransaction(walletTransaction);

      return walletRepository.save(wallet);
    }

  @Override
  @Transactional
  public Wallet deductMoneyFromWallet(User user, Double amount,
                                      String transactionId, Ride ride,
                                      TransactionMethod transactionMethod) {
    Wallet wallet =findByUser(user);
    wallet.setBalance(wallet.getBalance()-amount);

    WalletTransaction walletTransaction = WalletTransaction.builder()
            .transactionId(transactionId)
            .ride(ride)
            .wallet(wallet)
            .transactionType(TransactionType.DEBIT)
            .transactionMethod(transactionMethod)
            .amount(amount)
            .build();
    wallet.getTransactions().add(walletTransaction);
    return walletRepository.save(wallet);
  }

  @Override

    public void WithdrawAllMyMoneyFromWallet() {

    }

    @Override
    public Wallet findWalletById(Long walletId) {
       return walletRepository.findById(walletId)
               .orElseThrow(() -> new ResourceNotFoundException("wallet not found with id: "+walletId));
    }

    @Override
    public Wallet createNewWallet(User user) {
      Wallet wallet = new Wallet();
      wallet.setUser(user);
      return walletRepository.save(wallet);
    }

  @Override
  public Wallet findByUser(User user) {
    return walletRepository.findByUser(user)
            .orElseThrow(()-> new ResourceNotFoundException("wallet not found with id:"+user.getId()));
  }
}
