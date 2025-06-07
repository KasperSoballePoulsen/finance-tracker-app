package com.example.backend.api;

import com.example.backend.bll.CategoryService;
import com.example.backend.dal.model.Category;
import com.example.backend.dal.model.TransactionType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Categories", description = "Endpoints for managing transaction categories")
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService service) {
        this.categoryService = service;
    }

    @Operation(
            summary = "Get all categories",
            description = "Returns a list of all categories. Optionally filter by transaction type (EARNING or EXPENSE)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid type filter", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<Category>> getCategories(
            @Parameter(description = "Optional filter by transaction type: EARNING or EXPENSE")
            @RequestParam(required = false) TransactionType type) {
        try {
            List<Category> categories = (type == null)
                    ? categoryService.getAllCategories()
                    : categoryService.getCategoriesByType(type);
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.status(400).build();
        }
    }

    @Operation(
            summary = "Create a new category",
            description = "Creates and saves a new transaction category"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Category> createCategory(
            @Parameter(description = "The category object to create")
            @RequestBody Category category) {
        try {
            Category created = categoryService.createCategory(category);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.status(400).build();
        }
    }
}
