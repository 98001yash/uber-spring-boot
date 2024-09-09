package com.company.project.uber.uperApp.dto;

import com.company.project.uber.uperApp.entities.enums.TransactionMethod;
import com.company.project.uber.uperApp.entities.enums.TransactionType;
import lombok.Builder;
import lombok.Data;


import java.time.LocalDateTime;


@Data
@Builder
public class WalletTransactionDto {

    private Long id;

    private Double amount;

    private TransactionType transactionType;

    private TransactionMethod transactionMethod;


    private RideDto ride;

    private String transactionId;


    private WalletDto wallet;

    private LocalDateTime timeStamp;
}
