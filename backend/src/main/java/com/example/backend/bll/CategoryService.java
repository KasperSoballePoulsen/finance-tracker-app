package com.example.backend.bll;

import com.example.backend.dal.model.Category;
import com.example.backend.dal.model.TransactionType;
import com.example.backend.dal.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository repo;

    public CategoryService(CategoryRepository repo) {
        this.repo = repo;
    }

    public List<Category> getAllCategories() {
        return repo.findAll();
    }

    public List<Category> getCategoriesByType(TransactionType type) {
        List<Category> categories = repo.getCategoriesByType(type);
        return categories;
    }
}
