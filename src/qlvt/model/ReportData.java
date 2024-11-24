package qlvt.model;

import java.math.BigDecimal;

public class ReportData {
    private BigDecimal totalRevenue;
    private BigDecimal totalCost;
    private BigDecimal grossProfit;
    private BigDecimal netProfit;
    private BigDecimal revenueByBranch;
    private BigDecimal revenueByProduct;
    private BigDecimal revenueByCustomer;
    private int stockRemaining;
    private String longestStockProduct;

    // Constructor
    public ReportData(BigDecimal totalRevenue, BigDecimal totalCost, BigDecimal grossProfit, BigDecimal netProfit,
                      BigDecimal revenueByBranch, BigDecimal revenueByProduct, BigDecimal revenueByCustomer,
                      int stockRemaining, String longestStockProduct) {
        this.totalRevenue = totalRevenue;
        this.totalCost = totalCost;
        this.grossProfit = grossProfit;
        this.netProfit = netProfit;
        this.revenueByBranch = revenueByBranch;
        this.revenueByProduct = revenueByProduct;
        this.revenueByCustomer = revenueByCustomer;
        this.stockRemaining = stockRemaining;
        this.longestStockProduct = longestStockProduct;
    }

    // Getters and setters
    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getGrossProfit() {
        return grossProfit;
    }

    public void setGrossProfit(BigDecimal grossProfit) {
        this.grossProfit = grossProfit;
    }

    public BigDecimal getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(BigDecimal netProfit) {
        this.netProfit = netProfit;
    }

    public BigDecimal getRevenueByBranch() {
        return revenueByBranch;
    }

    public void setRevenueByBranch(BigDecimal revenueByBranch) {
        this.revenueByBranch = revenueByBranch;
    }

    public BigDecimal getRevenueByProduct() {
        return revenueByProduct;
    }

    public void setRevenueByProduct(BigDecimal revenueByProduct) {
        this.revenueByProduct = revenueByProduct;
    }

    public BigDecimal getRevenueByCustomer() {
        return revenueByCustomer;
    }

    public void setRevenueByCustomer(BigDecimal revenueByCustomer) {
        this.revenueByCustomer = revenueByCustomer;
    }

    public int getStockRemaining() {
        return stockRemaining;
    }

    public void setStockRemaining(int stockRemaining) {
        this.stockRemaining = stockRemaining;
    }

    public String getLongestStockProduct() {
        return longestStockProduct;
    }

    public void setLongestStockProduct(String longestStockProduct) {
        this.longestStockProduct = longestStockProduct;
    }

    @Override
    public String toString() {
        return "ReportData{" +
                "totalRevenue=" + totalRevenue +
                ", totalCost=" + totalCost +
                ", grossProfit=" + grossProfit +
                ", netProfit=" + netProfit +
                ", revenueByBranch=" + revenueByBranch +
                ", revenueByProduct=" + revenueByProduct +
                ", revenueByCustomer=" + revenueByCustomer +
                ", stockRemaining=" + stockRemaining +
                ", longestStockProduct='" + longestStockProduct + '\'' +
                '}';
    }
}
