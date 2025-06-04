package com.example.backend.dal.repository;

import com.example.backend.dal.model.Category;
import com.example.backend.dal.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> getCategoriesByType(TransactionType type);
}
