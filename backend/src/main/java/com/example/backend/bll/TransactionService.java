package com.example.backend.bll;

import com.example.backend.dal.model.Transaction;
import com.example.backend.dal.repository.TransactionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TransactionService {
    private final TransactionRepository repo;

    public TransactionService(TransactionRepository repo) {
        this.repo = repo;
    }

    public Transaction createTransaction(Transaction transaction) {
        return repo.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return repo.findAll();

    }

    public Transaction getTransactionById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException());
    }

    public Transaction updateTransaction(Long id, Transaction updatedTransaction) {
        return repo.findById(id)
                .map(transaction -> {
                    transaction.setAmount(updatedTransaction.getAmount());
                    transaction.setDate(updatedTransaction.getDate());
                    transaction.setDescription(updatedTransaction.getDescription());
                    transaction.setType(updatedTransaction.getType());
                    transaction.setCategory(updatedTransaction.getCategory());

                    return repo.save(transaction);
                })
                .orElseThrow(() -> new NoSuchElementException());

    }

    public void deleteTransaction(Long id) {
        if (!repo.existsById(id)) {
            throw new NoSuchElementException("Transaction not found");
        }
        repo.deleteById(id);
    }
}
