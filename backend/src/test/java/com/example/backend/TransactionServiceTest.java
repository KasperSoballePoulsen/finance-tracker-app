package com.example.backend;

import com.example.backend.bll.TransactionService;
import com.example.backend.dal.model.Category;
import com.example.backend.dal.model.Transaction;
import com.example.backend.dal.model.TransactionType;
import com.example.backend.dal.repository.CategoryRepository;
import com.example.backend.dal.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
        categoryRepository.deleteAll();


        Category food = categoryRepository.save(new Category("Food", TransactionType.EXPENSE));
        Category salary = categoryRepository.save(new Category("Salary", TransactionType.EARNING));


        transactionRepository.save(new Transaction(new BigDecimal("99.99"), LocalDate.of(2025, 1, 1), "Pizza", TransactionType.EXPENSE, food));
        transactionRepository.save(new Transaction(new BigDecimal("150.00"), LocalDate.of(2025, 1, 2), "Groceries", TransactionType.EXPENSE, food));
        transactionRepository.save(new Transaction(new BigDecimal("200.00"), LocalDate.of(2025, 2, 10), "Lunch", TransactionType.EXPENSE, food));
        transactionRepository.save(new Transaction(new BigDecimal("5000.00"), LocalDate.of(2025, 1, 31), "Monthly Salary", TransactionType.EARNING, salary));
        transactionRepository.save(new Transaction(new BigDecimal("3000.00"), LocalDate.of(2025, 2, 28), "Bonus", TransactionType.EARNING, salary));
    }

    @Test
    void getAllTransactions_returnsAll5Transactions() {
        List<Transaction> result = transactionService.getAllTransactions();

        assertEquals(5, result.size());

        Transaction t = result.stream()
                .filter(tx -> tx.getDescription().equals("Pizza"))
                .findFirst()
                .orElseThrow();

        assertEquals(new BigDecimal("99.99"), t.getAmount());
        assertEquals(TransactionType.EXPENSE, t.getType());
        assertEquals("Food", t.getCategory().getName());

        long foodCount = result.stream()
                .filter(tx -> tx.getCategory().getName().equals("Food"))
                .count();
        long salaryCount = result.stream()
                .filter(tx -> tx.getCategory().getName().equals("Salary"))
                .count();

        assertEquals(3, foodCount);
        assertEquals(2, salaryCount);
    }


}




