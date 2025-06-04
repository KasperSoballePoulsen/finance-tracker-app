package com.example.backend.dal.repository;

import com.example.backend.dal.model.Transaction;
import com.example.backend.dal.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByType(TransactionType transactionType);
}