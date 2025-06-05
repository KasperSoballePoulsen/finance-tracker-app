package com.example.backend.api.dto;

import java.math.BigDecimal;

public class SummaryDTO {
    private String month;
    private String category;
    private String type;
    private BigDecimal total;


    public SummaryDTO() {}
    public SummaryDTO(String month, String category, String type, BigDecimal total) {
        this.month = month;
        this.category = category;
        this.type = type;
        this.total = total;
    }

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
}
