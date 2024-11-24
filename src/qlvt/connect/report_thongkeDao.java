package qlvt.connect;

import qlvt.model.ReportData;

import java.math.BigDecimal;
import java.sql.*;

public class report_thongkeDao {
    private static DistributedDatabaseConnection dbConnection;

    // Constructor to initialize the database connection
    public report_thongkeDao() {
        if (dbConnection == null) {
            dbConnection = new DistributedDatabaseConnection();
        }
    }

    public ReportData getReportData() throws SQLException {
        // Initialize ReportData object with BigDecimal type for monetary fields
        ReportData reportData = new ReportData(
                BigDecimal.ZERO, // totalRevenue
                BigDecimal.ZERO, // totalCost
                BigDecimal.ZERO, // grossProfit
                BigDecimal.ZERO, // netProfit
                BigDecimal.ZERO, // revenueByBranch
                BigDecimal.ZERO, // revenueByProduct
                BigDecimal.ZERO, // revenueByCustomer
                0,               // stockRemaining
                ""               // longestStockProduct
        );

        // Connection to the distributed database system
        try (Connection conn = dbConnection.getConnection()) {

            // Fetch total revenue
            String revenueQuery = "SELECT SUM(HoaDon.TongTien) FROM HoaDon";
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(revenueQuery);
                if (rs.next()) {
                    reportData.setTotalRevenue(rs.getBigDecimal(1));
                }
            }

            // Fetch total cost
            String costQuery = "SELECT SUM(ChiTietPhieuNhap.SoLuongNhap * ChiTietPhieuNhap.GiaNhap) FROM ChiTietPhieuNhap";
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(costQuery);
                if (rs.next()) {
                    reportData.setTotalCost(rs.getBigDecimal(1));
                }
            }

            // Fetch gross profit
            String grossProfitQuery = "SELECT (SUM(HoaDon.TongTien) - SUM(ChiTietPhieuNhap.SoLuongNhap * ChiTietPhieuNhap.GiaNhap)) FROM HoaDon, ChiTietPhieuNhap";
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(grossProfitQuery);
                if (rs.next()) {
                    reportData.setGrossProfit(rs.getBigDecimal(1));
                }
            }

            // Fetch net profit
            String netProfitQuery = "SELECT SUM(HoaDon.TongTien) - SUM(ChiTietPhieuNhap.SoLuongNhap * ChiTietPhieuNhap.GiaNhap) FROM HoaDon, ChiTietPhieuNhap";
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(netProfitQuery);
                if (rs.next()) {
                    reportData.setNetProfit(rs.getBigDecimal(1));
                }
            }

            // Fetch revenue by branch
            String revenueByBranchQuery = "SELECT ChiNhanh.TenChiNhanh, SUM(HoaDon.TongTien) FROM HoaDon " +
                    "JOIN PhieuXuat ON HoaDon.MaDonHang = PhieuXuat.MaDonHang " +
                    "JOIN Kho ON PhieuXuat.MaKho = Kho.MaKho " +
                    "JOIN ChiNhanh ON Kho.MaChiNhanh = ChiNhanh.MaChiNhanh " +
                    "GROUP BY ChiNhanh.TenChiNhanh";
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(revenueByBranchQuery);
                while (rs.next()) {
                    String branchName = rs.getString(1);
                    BigDecimal revenue = rs.getBigDecimal(2);
                    // You could store this data in a map or list in your ReportData object
                    // For example:
                    // reportData.setRevenueByBranch(revenue); // You might need to adjust this logic as per your need
                }
            }

            // Additional queries for revenue by product, customer, or other statistics can follow the same pattern

        }

        return reportData;
    }
}
