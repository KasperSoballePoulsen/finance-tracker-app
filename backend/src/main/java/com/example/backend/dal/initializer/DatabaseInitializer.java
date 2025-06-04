package com.example.backend.dal.initializer;

import com.example.backend.dal.model.Category;
import com.example.backend.dal.model.TransactionType;
import com.example.backend.dal.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public DatabaseInitializer(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {
        if (categoryRepository.count() == 0) {
            categoryRepository.save(new Category("Food", TransactionType.EXPENSE));
            categoryRepository.save(new Category("Transport", TransactionType.EXPENSE));
            categoryRepository.save(new Category("Job Salary", TransactionType.EARNING));

        }
    }
}