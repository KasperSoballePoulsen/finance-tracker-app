package com.example.backend.dal.initializer;

import com.example.backend.dal.model.Category;
import com.example.backend.dal.model.Transaction;
import com.example.backend.dal.model.TransactionType;
import com.example.backend.dal.repository.CategoryRepository;
import com.example.backend.dal.repository.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@Profile("!test")
public class DatabaseInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    public DatabaseInitializer(CategoryRepository categoryRepository, TransactionRepository transactionRepository) {
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void run(String... args) {
        if (categoryRepository.count() == 0 && transactionRepository.count() == 0) {
            Category food = categoryRepository.save(new Category("Food", TransactionType.EXPENSE));
            Category transport = categoryRepository.save(new Category("Transport", TransactionType.EXPENSE));
            Category rent = categoryRepository.save(new Category("Rent", TransactionType.EXPENSE));
            Category entertainment = categoryRepository.save(new Category("Entertainment", TransactionType.EXPENSE));
            Category utilities = categoryRepository.save(new Category("Utilities", TransactionType.EXPENSE));
            Category noExpense = categoryRepository.save(new Category("No Category", TransactionType.EXPENSE));

            Category salary = categoryRepository.save(new Category("Salary", TransactionType.EARNING));
            Category freelance = categoryRepository.save(new Category("Freelance", TransactionType.EARNING));
            Category investment = categoryRepository.save(new Category("Investment", TransactionType.EARNING));
            Category gift = categoryRepository.save(new Category("Gift", TransactionType.EARNING));
            Category noEarning = categoryRepository.save(new Category("No Category", TransactionType.EARNING));


            List<Transaction> transactions = List.of(
                    new Transaction(new BigDecimal("1000"), LocalDate.of(2024, 1, 15), "Groceries", TransactionType.EXPENSE, food),
                    new Transaction(new BigDecimal("60"), LocalDate.of(2024, 1, 20), "Bus pass", TransactionType.EXPENSE, transport),
                    new Transaction(new BigDecimal("8000"), LocalDate.of(2024, 2, 1), "Monthly rent", TransactionType.EXPENSE, rent),
                    new Transaction(new BigDecimal("300"), LocalDate.of(2024, 2, 14), "Movie night", TransactionType.EXPENSE, entertainment),
                    new Transaction(new BigDecimal("1200"), LocalDate.of(2024, 3, 5), "Electricity bill", TransactionType.EXPENSE, utilities),
                    new Transaction(new BigDecimal("200"), LocalDate.of(2024, 3, 10), "Misc expense", TransactionType.EXPENSE, noExpense),
                    new Transaction(new BigDecimal("25000"), LocalDate.of(2024, 1, 31), "Monthly salary", TransactionType.EARNING, salary),
                    new Transaction(new BigDecimal("5000"), LocalDate.of(2024, 2, 15), "Freelance project", TransactionType.EARNING, freelance),
                    new Transaction(new BigDecimal("3000"), LocalDate.of(2024, 3, 10), "Stock dividends", TransactionType.EARNING, investment),
                    new Transaction(new BigDecimal("1500"), LocalDate.of(2024, 4, 1), "Birthday gift", TransactionType.EARNING, gift),
                    new Transaction(new BigDecimal("250"), LocalDate.of(2024, 4, 10), "Unknown source", TransactionType.EARNING, noEarning),
                    new Transaction(new BigDecimal("900"), LocalDate.of(2025, 1, 10), "Groceries", TransactionType.EXPENSE, food),
                    new Transaction(new BigDecimal("70"), LocalDate.of(2025, 1, 18), "Taxi", TransactionType.EXPENSE, transport),
                    new Transaction(new BigDecimal("8200"), LocalDate.of(2025, 2, 1), "Rent", TransactionType.EXPENSE, rent),
                    new Transaction(new BigDecimal("450"), LocalDate.of(2025, 2, 14), "Concert", TransactionType.EXPENSE, entertainment),
                    new Transaction(new BigDecimal("1000"), LocalDate.of(2025, 3, 6), "Water bill", TransactionType.EXPENSE, utilities),
                    new Transaction(new BigDecimal("300"), LocalDate.of(2025, 3, 9), "No category expense", TransactionType.EXPENSE, noExpense),
                    new Transaction(new BigDecimal("27000"), LocalDate.of(2025, 1, 31), "Job salary", TransactionType.EARNING, salary),
                    new Transaction(new BigDecimal("4000"), LocalDate.of(2025, 2, 20), "Contract job", TransactionType.EARNING, freelance),
                    new Transaction(new BigDecimal("2800"), LocalDate.of(2025, 3, 11), "Fund returns", TransactionType.EARNING, investment),
                    new Transaction(new BigDecimal("1200"), LocalDate.of(2025, 4, 2), "Cash gift", TransactionType.EARNING, gift),
                    new Transaction(new BigDecimal("1100"), LocalDate.of(2025, 5, 10), "Groceries", TransactionType.EXPENSE, food),
                    new Transaction(new BigDecimal("90"), LocalDate.of(2025, 5, 22), "Bus card", TransactionType.EXPENSE, transport),
                    new Transaction(new BigDecimal("8100"), LocalDate.of(2025, 6, 1), "Monthly rent", TransactionType.EXPENSE, rent),
                    new Transaction(new BigDecimal("600"), LocalDate.of(2025, 6, 15), "Festival", TransactionType.EXPENSE, entertainment),
                    new Transaction(new BigDecimal("1300"), LocalDate.of(2025, 6, 20), "Gas bill", TransactionType.EXPENSE, utilities),
                    new Transaction(new BigDecimal("220"), LocalDate.of(2025, 6, 22), "Extra expense", TransactionType.EXPENSE, noExpense)
            );

            transactionRepository.saveAll(transactions);
        }
    }
}
