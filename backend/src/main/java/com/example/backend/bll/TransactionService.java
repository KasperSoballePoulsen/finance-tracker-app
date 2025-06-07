package com.example.backend.bll;

import com.example.backend.api.dto.SummaryDTO;
import com.example.backend.dal.model.Category;
import com.example.backend.dal.model.Transaction;
import com.example.backend.dal.model.TransactionType;
import com.example.backend.dal.repository.CategoryRepository;
import com.example.backend.dal.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

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

    public List<Transaction> getTransactionsByType(String type) {
        try {
            TransactionType transactionType = TransactionType.valueOf(type);
            return transactionRepository.findByType(transactionType);
        } catch (IllegalArgumentException e) {
            throw new NoSuchElementException("Invalid transaction type: " + type);
        }
    }

    public List<SummaryDTO> getSummaryStatistics(int year) {
        List<Object[]> raw = transactionRepository.getSummaryStatistics(year);
        Map<String, BigDecimal> earningsPerMonth = new HashMap<>();
        Map<String, BigDecimal> expensesPerMonth = new HashMap<>();
        List<SummaryDTO> result = new ArrayList<>();

        for (Object[] row : raw) {
            String month = (String) row[0];
            String category = (String) row[1];
            String type = (String) row[2];
            BigDecimal total = (BigDecimal) row[3];

            result.add(new SummaryDTO(month, category, type, total));

            if ("EARNING".equalsIgnoreCase(type)) {
                BigDecimal current = earningsPerMonth.getOrDefault(month, BigDecimal.ZERO);
                earningsPerMonth.put(month, current.add(total));
            } else if ("EXPENSE".equalsIgnoreCase(type)) {
                BigDecimal current = expensesPerMonth.getOrDefault(month, BigDecimal.ZERO);
                expensesPerMonth.put(month, current.add(total));
            }
        }

        Set<String> allMonths = new HashSet<>();
        allMonths.addAll(earningsPerMonth.keySet());
        allMonths.addAll(expensesPerMonth.keySet());

        List<BigDecimal> monthlyBalances = new ArrayList<>();

        for (String month : allMonths) {
            BigDecimal earnings = earningsPerMonth.getOrDefault(month, BigDecimal.ZERO);
            BigDecimal expenses = expensesPerMonth.getOrDefault(month, BigDecimal.ZERO);
            BigDecimal balance = earnings.subtract(expenses);
            result.add(new SummaryDTO(month, "TOTAL", "BALANCE", balance));

            monthlyBalances.add(balance);
        }


        BigDecimal yearlyBalance = calculateSum(monthlyBalances);
        result.add(new SummaryDTO("YEARLY", "TOTAL", "BALANCE", yearlyBalance));

        return result;
    }

    /**
     * This method is made public only to allow unit testing.
     * In production, it should be private.
     */
    public BigDecimal calculateSum(List<BigDecimal> numbersToSum) {
        BigDecimal result = BigDecimal.ZERO;
        for (BigDecimal number : numbersToSum) {
            result = result.add(number);
        }
        return result;
    }




}
