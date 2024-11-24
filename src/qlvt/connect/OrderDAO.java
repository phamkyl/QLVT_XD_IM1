package qlvt.connect;

import qlvt.model.ChiTietDonHang;
import qlvt.model.Order;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderDAO {

    private static final Logger LOGGER = Logger.getLogger(OrderDAO.class.getName());
    private final DistributedDatabaseConnection dbConnection;

    public OrderDAO() {
        dbConnection = new DistributedDatabaseConnection();
    }

    // Method to add an order
    public void addOrder(Order order) {
        String sql = "INSERT INTO DonHang (MaDonHang, NgayDat, MaKhachHang, TinhTrangDonHang) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, order.getMaDonHang());
            pstmt.setDate(2, order.getNgayDat());
            pstmt.setInt(3, order.getMaKhachHang());
            pstmt.setString(4, order.getTinhTrangDonHang());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding order", e);
        }
    }

    // Method to update an order
    public void updateOrder(Order order) {
        String sql = "UPDATE DonHang SET NgayDat = ?, MaKhachHang = ?, TinhTrangDonHang = ? WHERE MaDonHang = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, order.getNgayDat());
            pstmt.setInt(2, order.getMaKhachHang());
            pstmt.setString(3, order.getTinhTrangDonHang());
            pstmt.setInt(4, order.getMaDonHang());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating order", e);
        }
    }

    // Method to delete an order
    public void deleteOrder(int maDonHang) {
        String sql = "DELETE FROM DonHang WHERE MaDonHang = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maDonHang);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting order", e);
        }
    }

    // Method to get all orders
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM DonHang";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("MaDonHang"),
                        rs.getInt("MaKhachHang"),
                        rs.getDate("NgayDat"),
                        rs.getString("TinhTrangDonHang")
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving all orders", e);
        }
        return orders;
    }

    // Method to get order details by ID
    public List<ChiTietDonHang> getOrderDetailsById(int maDonHang) {
        List<ChiTietDonHang> details = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietDonHang WHERE MaDonHang = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maDonHang);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ChiTietDonHang detail = new ChiTietDonHang(
                            rs.getInt("MaDonHang"),
                            rs.getInt("MaVatTu"),
                            rs.getInt("SoLuong"),
                            rs.getBigDecimal("Gia")
                    );
                    details.add(detail);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving order details", e);
        }
        return details;
    }

    // Method to add order details
    public void addDetail(ChiTietDonHang detail) {
        String sql = "INSERT INTO ChiTietDonHang (MaDonHang, MaVatTu, SoLuong, Gia) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, detail.getMaDonHang());
            pstmt.setInt(2, detail.getMaVatTu());
            pstmt.setInt(3, detail.getSoLuong());
            pstmt.setBigDecimal(4, detail.getGia());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding order detail", e);
        }
    }

    // Method to update order details
    public void updateDetail(ChiTietDonHang detail) {
        String sql = "UPDATE ChiTietDonHang SET SoLuong = ?, Gia = ? WHERE MaDonHang = ? AND MaVatTu = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, detail.getSoLuong());
            pstmt.setBigDecimal(2, detail.getGia());
            pstmt.setInt(3, detail.getMaDonHang());
            pstmt.setInt(4, detail.getMaVatTu());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating order detail", e);
        }
    }

    // Method to delete order details
    public void deleteDetail(int maDonHang, int maVatTu) {
        String sql = "DELETE FROM ChiTietDonHang WHERE MaDonHang = ? AND MaVatTu = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maDonHang);
            pstmt.setInt(2, maVatTu);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting order detail", e);
        }
    }

    // Method to get an order by ID
    public Order getOrderById(int maDonHang) {
        Order order = null;
        String sql = "SELECT * FROM DonHang WHERE MaDonHang = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maDonHang);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    order = new Order(
                            rs.getInt("MaDonHang"),
                            rs.getInt("MaKhachHang"),
                            rs.getDate("NgayDat"),
                            rs.getString("TinhTrangDonHang")
                    );
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving order by ID", e);
        }
        return order;
    }

    // Method to get order by ID


}
