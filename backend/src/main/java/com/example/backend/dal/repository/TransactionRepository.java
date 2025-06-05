package com.example.backend.dal.repository;

import com.example.backend.api.dto.SummaryDTO;
import com.example.backend.dal.model.Transaction;
import com.example.backend.dal.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByType(TransactionType transactionType);

    @Query(value = """
    SELECT FORMATDATETIME(t.date, 'yyyy-MM') AS month_str,
           c.name AS category,
           t.type AS type,
           SUM(t.amount) AS total
    FROM transaction t
    JOIN category c ON t.category_id = c.id
    WHERE YEAR(t.date) = :year
    GROUP BY FORMATDATETIME(t.date, 'yyyy-MM'), c.name, t.type
    """, nativeQuery = true)
    List<Object[]> getSummaryStatistics(@Param("year") int year);

}