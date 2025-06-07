package com.example.backend;

import com.example.backend.api.dto.SummaryDTO;
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
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

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
    void createTransaction_savesTransactionWithExistingCategory() {
        Category existingCategory = categoryRepository.findAll()
                .stream()
                .filter(c -> c.getName().equals("Food"))
                .findFirst()
                .orElseThrow();

        Transaction newTransaction = new Transaction(
                new BigDecimal("250.00"),
                LocalDate.of(2025, 3, 15),
                "Dinner",
                TransactionType.EXPENSE,
                existingCategory
        );

        Transaction saved = transactionService.createTransaction(newTransaction);


        assertEquals(new BigDecimal("250.00"), saved.getAmount());
        assertEquals("Dinner", saved.getDescription());
        assertEquals(TransactionType.EXPENSE, saved.getType());
        assertEquals("Food", saved.getCategory().getName());


        List<Transaction> all = transactionRepository.findAll();
        assertEquals(6, all.size());
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

    @Test
    void getTransactionById_returnsCorrectTransactionFromSetup() {

        Transaction existing = transactionRepository.findAll().stream()
                .filter(t -> t.getDescription().equals("Pizza"))
                .findFirst()
                .orElseThrow();


        Transaction result = transactionService.getTransactionById(existing.getId());


        assertEquals(existing.getId(), result.getId());
        assertEquals("Pizza", result.getDescription());
        assertEquals(new BigDecimal("99.99"), result.getAmount());
    }

    @Test
    void updateTransaction_updatesExistingTransaction() {

        Transaction existing = transactionRepository.findAll().stream()
                .filter(t -> t.getDescription().equals("Pizza"))
                .findFirst()
                .orElseThrow();


        Category newCategory = categoryRepository.findAll().stream()
                .filter(c -> c.getName().equals("Salary"))
                .findFirst()
                .orElseThrow();


        Transaction updated = new Transaction(
                new BigDecimal("888.88"),
                LocalDate.of(2025, 6, 7),
                "Updated Pizza",
                TransactionType.EARNING,
                newCategory
        );

        Transaction result = transactionService.updateTransaction(existing.getId(), updated);

        assertEquals(existing.getId(), result.getId());
        assertEquals("Updated Pizza", result.getDescription());
        assertEquals(new BigDecimal("888.88"), result.getAmount());
        assertEquals(TransactionType.EARNING, result.getType());
        assertEquals("Salary", result.getCategory().getName());
    }

    @Test
    void deleteTransaction_deletesExistingTransaction() {

        Transaction existing = transactionRepository.findAll().stream()
                .filter(t -> t.getDescription().equals("Pizza"))
                .findFirst()
                .orElseThrow();


        transactionService.deleteTransaction(existing.getId());


        boolean stillExists = transactionRepository.existsById(existing.getId());
        assertFalse(stillExists);
    }

    @Test
    void deleteTransaction_throwsWhenIdNotFound() {
        Long nonExistentId = 9999L;

        assertThrows(NoSuchElementException.class, () -> {
            transactionService.deleteTransaction(nonExistentId);
        });
    }

    @Test
    void getTransactionsByType_returnsOnlyMatchingType() {
        List<Transaction> earnings = transactionService.getTransactionsByType("EARNING");

        assertFalse(earnings.isEmpty());


        boolean allAreEarnings = earnings.stream()
                .allMatch(t -> t.getType() == TransactionType.EARNING);
        assertTrue(allAreEarnings);


        assertEquals(2, earnings.size());
    }

    @Test
    void getTransactionsByType_throwsOnInvalidType() {
        String invalidType = "INVALID";

        assertThrows(NoSuchElementException.class, () -> {
            transactionService.getTransactionsByType(invalidType);
        });
    }

    @Test
    void getSummaryStatistics_returnsCorrectSummariesFor2025() {
        List<SummaryDTO> result = transactionService.getSummaryStatistics(2025);


        BigDecimal foodExpensesInJan = BigDecimal.ZERO;
        for (SummaryDTO dto : result) {
            if (dto.getMonth().equals("2025-01") &&
                    dto.getCategory().equals("Food") &&
                    dto.getType().equals("EXPENSE")) {
                foodExpensesInJan = foodExpensesInJan.add(dto.getTotal());
                break;
            }
        }

        assertEquals(0, foodExpensesInJan.compareTo(new BigDecimal("249.99")));


        BigDecimal janBalance = null;
        for (SummaryDTO dto : result) {
            if (dto.getMonth().equals("2025-01") &&
                    dto.getCategory().equals("TOTAL") &&
                    dto.getType().equals("BALANCE")) {
                janBalance = dto.getTotal();
                break;
            }
        }
        assertNotNull(janBalance);
        assertEquals(0, janBalance.compareTo(new BigDecimal("4750.01")));



        BigDecimal yearlyBalance = null;
        for (SummaryDTO dto : result) {
            if (dto.getMonth().equals("YEARLY") &&
                    dto.getCategory().equals("TOTAL") &&
                    dto.getType().equals("BALANCE")) {
                yearlyBalance = dto.getTotal();
                break;
            }
        }
        assertNotNull(yearlyBalance);
        assertEquals(0, yearlyBalance.compareTo(new BigDecimal("7550.01")));

    }

    @Test
    void calculateSum_addsAllBigDecimalsCorrectly() {
        List<BigDecimal> values = List.of(
                new BigDecimal("10.00"),
                new BigDecimal("20.55"),
                new BigDecimal("0.45")
        );

        BigDecimal expected = new BigDecimal("31.00");

        BigDecimal result = transactionService.calculateSum(values); // hvis metoden er beskyttet

        assertEquals(0, result.compareTo(expected));
    }














}




