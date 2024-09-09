package com.company.project.uber.uperApp.services.impl;

import com.company.project.uber.uperApp.dto.WalletTransactionDto;
import com.company.project.uber.uperApp.entities.WalletTransaction;
import com.company.project.uber.uperApp.repositories.WalletTransactionRepository;
import com.company.project.uber.uperApp.services.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class WalletTransactionServiceImpl implements WalletTransactionService {



    private final WalletTransactionRepository walletTransactionRepository;
    private final ModelMapper modelMapper;


    @Override
    public void createNewWalletTransaction(WalletTransaction walletTransaction) {
        walletTransactionRepository.save(walletTransaction);
    }
}
