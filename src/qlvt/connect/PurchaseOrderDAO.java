package qlvt.connect;

import qlvt.model.ChiTietPhieuNhap;
import qlvt.model.PhieuNhap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderDAO {

    private static DistributedDatabaseConnection dbConnection;

    public PurchaseOrderDAO() {
        dbConnection = new DistributedDatabaseConnection();
    }

    // Method to add a purchase order
    public void addPurchaseOrder(PhieuNhap order) {
        String sql = "INSERT INTO PhieuNhap (MaPhieuNhap, NgayNhap, MaNhanVien, MaKho, MaNhaCungCap) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, order.getMaPhieuNhap());
            pstmt.setDate(2, order.getNgayNhap());
            pstmt.setInt(3, order.getMaNhanVien());
            pstmt.setInt(4, order.getMaKho());
            pstmt.setInt(5, order.getMaNhaCungCap());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update a purchase order
    public void updatePurchaseOrder(PhieuNhap order) {
        String sql = "UPDATE PhieuNhap SET NgayNhap = ?, MaNhanVien = ?, MaKho = ?, MaNhaCungCap = ? WHERE MaPhieuNhap = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, order.getNgayNhap());
            pstmt.setInt(2, order.getMaNhanVien());
            pstmt.setInt(3, order.getMaKho());
            pstmt.setInt(4, order.getMaNhaCungCap());
            pstmt.setInt(5, order.getMaPhieuNhap());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete a purchase order
    public void deletePurchaseOrder(String maPhieuNhap) {
        String sql = "DELETE FROM PhieuNhap WHERE MaPhieuNhap = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maPhieuNhap);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to get all purchase orders
    public List<PhieuNhap> getAllPurchaseOrders() {
        List<PhieuNhap> orders = new ArrayList<>();
        String sql = "SELECT * FROM PhieuNhap";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                PhieuNhap order = new PhieuNhap(
                        rs.getInt("MaPhieuNhap"),
                        rs.getDate("NgayNhap"),
                        rs.getInt("MaNhanVien"),
                        rs.getInt("MaKho"),
                        rs.getInt("MaNhaCungCap")
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    // Method to get purchase order details by ID
    public List<ChiTietPhieuNhap> getPurchaseOrderDetailsById(int maPhieuNhap) {
        List<ChiTietPhieuNhap> details = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietPhieuNhap WHERE MaPhieuNhap = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maPhieuNhap);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ChiTietPhieuNhap detail = new ChiTietPhieuNhap(
                            rs.getInt("MaPhieuNhap"),
                            rs.getInt("MaVatTu"),
                            rs.getInt("soLuongNhap"),
                            rs.getBigDecimal("GiaNhap")
                    );
                    details.add(detail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details;
    }

    // Method to add detail to a purchase order
    public void addDetail(ChiTietPhieuNhap detail) {
        String sql = "INSERT INTO ChiTietPhieuNhap (MaPhieuNhap, MaVatTu, soLuongNhap, GiaNhap) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, detail.getMaPhieuNhap());
            pstmt.setInt(2, detail.getMaVatTu());
            pstmt.setInt(3, detail.getSoLuongNhap());
            pstmt.setBigDecimal(4, detail.getGiaNhap());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update detail of a purchase order
    public void updateDetail(ChiTietPhieuNhap detail) {
        String sql = "UPDATE ChiTietPhieuNhap SET soLuongNhap = ?, GiaNhap = ? WHERE MaPhieuNhap = ? AND MaVatTu = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, detail.getSoLuongNhap());
            pstmt.setBigDecimal(2, detail.getGiaNhap());
            pstmt.setInt(3, detail.getMaPhieuNhap());
            pstmt.setInt(4, detail.getMaVatTu());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete detail from a purchase order
    public void deleteDetail(int maPhieuNhap, int maVatTu) {
        String sql = "DELETE FROM ChiTietPhieuNhap WHERE MaPhieuNhap = ? AND MaVatTu = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maPhieuNhap);
            pstmt.setInt(2, maVatTu);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
