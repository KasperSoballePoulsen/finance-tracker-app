package com.example.backend.api;

import com.example.backend.api.dto.SummaryDTO;
import com.example.backend.bll.TransactionService;
import com.example.backend.dal.model.Transaction;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Transactions", description = "Endpoints for managing financial transactions")
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(
            summary = "Create a new transaction",
            description = "Creates a new income or expense transaction and saves it to the database"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        try {
            Transaction saved = transactionService.createTransaction(transaction);
            return ResponseEntity.status(201).body(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).build();
        }
    }



    @Operation(
            summary = "Get all transactions",
            description = "Returns all transactions. Optional filter by type (EARNING or EXPENSE)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of transactions"),
            @ApiResponse(responseCode = "400", description = "Bad request - invalid transaction type or other error", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<Transaction>> getTransactions(
            @Parameter(description = "Transaction type filter: EARNING, EXPENSE or ALL")
            @RequestParam(required = false) String type) {
        try {
            List<Transaction> transactions;
            if (type == null || type.equals("ALL")) {
                transactions = transactionService.getAllTransactions();
            } else {
                transactions = transactionService.getTransactionsByType(type);
            }
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.status(400).build();
        }
    }





    @Operation(
            summary = "Get a transaction by ID",
            description = "Returns a transaction object based on the given ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction found"),
            @ApiResponse(responseCode = "404", description = "Transaction not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request - invalid ID format or other error", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(
            @Parameter(description = "ID of the transaction to retrieve")
            @PathVariable Long id) {
        try {
            Transaction transaction = transactionService.getTransactionById(id);
            return ResponseEntity.ok(transaction);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            return ResponseEntity.status(400).build();
        }
    }



    @Operation(
            summary = "Update a transaction",
            description = "Updates an existing transaction by ID using the provided transaction data"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction updated successfully"),
            @ApiResponse(responseCode = "404", description = "Transaction not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input or request", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(
            @Parameter(description = "ID of the transaction to update") @PathVariable Long id,
            @Parameter(description = "Updated transaction object") @RequestBody Transaction updatedTransaction) {
        try {
            Transaction updated = transactionService.updateTransaction(id, updatedTransaction);
            return ResponseEntity.ok(updated);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            return ResponseEntity.status(400).build();
        }
    }





    @Operation(
            summary = "Delete a transaction",
            description = "Deletes a transaction by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Transaction deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Transaction not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid ID or bad request", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @Parameter(description = "ID of the transaction to delete") @PathVariable Long id) {
        try {
            transactionService.deleteTransaction(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(400).build();
        }
    }



    @Operation(
            summary = "Get summary statistics",
            description = "Returns monthly and yearly earnings/expenses balance for a given year"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Summary data retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid year format", content = @Content),
            @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content)
    })
    @GetMapping("/summary")
    public ResponseEntity<List<SummaryDTO>> getSummaryStatistics(
            @Parameter(description = "Year to generate summary for")
            @RequestParam int year) {
        try {
            List<SummaryDTO> summary = transactionService.getSummaryStatistics(year);
            return ResponseEntity.ok(summary);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

}

