package com.example.backend.bll;

import com.example.backend.dal.model.Category;
import com.example.backend.dal.model.TransactionType;
import com.example.backend.dal.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getCategoriesByType(TransactionType type) {
        List<Category> categories = categoryRepository.getCategoriesByType(type);
        return categories;
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }
}
