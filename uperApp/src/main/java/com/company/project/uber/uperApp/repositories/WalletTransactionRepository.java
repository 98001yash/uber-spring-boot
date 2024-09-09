package com.company.project.uber.uperApp.repositories;


import com.company.project.uber.uperApp.entities.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction,Long> {

}
