package com.example.backend.bll;

import com.example.backend.dal.model.Category;
import com.example.backend.dal.repository.CategoryRepository;
import org.springframework.stereotype.Service; // husk at importere!

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
}
