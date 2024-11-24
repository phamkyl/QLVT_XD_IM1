package qlvt.connect;



import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderDAODetail {

    private static DistributedDatabaseConnection dbConnection;

    public PurchaseOrderDAODetail() {
        dbConnection = new DistributedDatabaseConnection();
    }

    // Method to retrieve purchase order details by order ID
    public List<Object[]> getPurchaseOrderDetails(int orderId) {
        List<Object[]> details = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietPhieuNhap WHERE MaPhieuNhap = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                details.add(new Object[]{
                        rs.getInt("MaVatTu"),
                        rs.getInt("SoLuongNhap"),
                        rs.getBigDecimal("GiaNhap")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details;
    }

    // Method to add a purchase order detail
    public void addPurchaseOrderDetail(int orderId, int materialId, int quantityReceived, BigDecimal unitPrice) {
        String sql = "INSERT INTO ChiTietPhieuNhap (MaPhieuNhap, MaVatTu, SoLuongNhap, GiaNhap) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            pstmt.setInt(2, materialId);
            pstmt.setInt(3, quantityReceived);
            pstmt.setBigDecimal(4, unitPrice);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update a purchase order detail
    public void updatePurchaseOrderDetail(int orderId, int materialId, int quantityReceived, BigDecimal unitPrice) {
        String sql = "UPDATE ChiTietPhieuNhap SET SoLuongNhap = ?, GiaNhap = ? WHERE MaPhieuNhap = ? AND MaVatTu = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantityReceived);
            pstmt.setBigDecimal(2, unitPrice);
            pstmt.setInt(3, orderId);
            pstmt.setInt(4, materialId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
