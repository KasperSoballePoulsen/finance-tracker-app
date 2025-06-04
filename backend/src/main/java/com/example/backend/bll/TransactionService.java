package com.example.backend.bll;

import com.example.backend.dal.model.Category;
import com.example.backend.dal.model.Transaction;
import com.example.backend.dal.repository.CategoryRepository;
import com.example.backend.dal.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public TransactionService(TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    public Transaction createTransaction(Transaction transaction) {
        Long categoryId = transaction.getCategory().getId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NoSuchElementException("Category not found"));

        transaction.setCategory(category);
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();

    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException());
    }

    public Transaction updateTransaction(Long id, Transaction updatedTransaction) {
        return transactionRepository.findById(id)
                .map(transaction -> {
                    transaction.setAmount(updatedTransaction.getAmount());
                    transaction.setDate(updatedTransaction.getDate());
                    transaction.setDescription(updatedTransaction.getDescription());
                    transaction.setType(updatedTransaction.getType());
                    transaction.setCategory(updatedTransaction.getCategory());

                    return transactionRepository.save(transaction);
                })
                .orElseThrow(() -> new NoSuchElementException());

    }

    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new NoSuchElementException("Transaction not found");
        }
        transactionRepository.deleteById(id);
    }
}
