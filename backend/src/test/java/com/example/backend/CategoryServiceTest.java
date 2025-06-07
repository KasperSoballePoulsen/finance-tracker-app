package com.example.backend;

import com.example.backend.bll.CategoryService;
import com.example.backend.dal.model.Category;
import com.example.backend.dal.model.TransactionType;
import com.example.backend.dal.repository.CategoryRepository;
import com.example.backend.dal.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll(); // SLET TRANSAKTIONER FØRST
        categoryRepository.deleteAll();    // Så må du gerne slette kategorier

        categoryRepository.save(new Category("Food", TransactionType.EXPENSE));
        categoryRepository.save(new Category("Transport", TransactionType.EXPENSE));
        categoryRepository.save(new Category("Salary", TransactionType.EARNING));
    }

    @Test
    void getAllCategories_returnsAllSavedCategories() {
        List<Category> all = categoryService.getAllCategories();

        assertEquals(3, all.size());

        boolean hasFood = all.stream().anyMatch(c -> c.getName().equals("Food") && c.getType() == TransactionType.EXPENSE);
        boolean hasTransport = all.stream().anyMatch(c -> c.getName().equals("Transport") && c.getType() == TransactionType.EXPENSE);
        boolean hasSalary = all.stream().anyMatch(c -> c.getName().equals("Salary") && c.getType() == TransactionType.EARNING);

        assertTrue(hasFood);
        assertTrue(hasTransport);
        assertTrue(hasSalary);
    }

    @Test
    void getCategoriesByType_returnsOnlyMatchingType() {
        List<Category> earnings = categoryService.getCategoriesByType(TransactionType.EARNING);
        assertEquals(1, earnings.size());
        assertEquals("Salary", earnings.get(0).getName());

        List<Category> expenses = categoryService.getCategoriesByType(TransactionType.EXPENSE);
        assertEquals(2, expenses.size());
    }

    @Test
    void createCategory_savesAndReturnsNewCategory() {
        Category newCategory = new Category("Bonus", TransactionType.EARNING);
        Category saved = categoryService.createCategory(newCategory);

        assertNotNull(saved.getId());
        assertEquals("Bonus", saved.getName());
        assertEquals(TransactionType.EARNING, saved.getType());

        List<Category> all = categoryRepository.findAll();
        assertEquals(4, all.size()); // 3 fra setup + 1 ny
    }
}
