package com.example.backend.api;

import com.example.backend.bll.CategoryService;
import com.example.backend.dal.model.Category;
import com.example.backend.dal.model.TransactionType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService service) {
        this.categoryService = service;
    }

    @GetMapping
    public ResponseEntity<List<Category>> getCategories(@RequestParam(required = false) TransactionType type) {
        List<Category> categories = (type == null)
                ? categoryService.getAllCategories()
                : categoryService.getCategoriesByType(type);
        return ResponseEntity.ok(categories);
    }

}
