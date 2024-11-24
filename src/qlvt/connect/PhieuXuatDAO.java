package qlvt.connect;

import qlvt.model.ChiTietPhieuXuat;
import qlvt.model.PhieuXuat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhieuXuatDAO {

    private final DistributedDatabaseConnection dbConnection;

    public PhieuXuatDAO() {
        dbConnection = new DistributedDatabaseConnection();
    }

    // Phương thức thêm phiếu xuất
    public void addPhieuXuat(PhieuXuat phieuXuat) {
        String sql = "INSERT INTO PhieuXuat (MaPhieuXuat, NgayXuat, MaNhanVien, MaKho, MaDonHang) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, phieuXuat.getMaPhieuXuat());
            pstmt.setDate(2, phieuXuat.getNgayXuat());
            pstmt.setInt(3, phieuXuat.getMaNhanVien());
            pstmt.setInt(4, phieuXuat.getMaKho());
            pstmt.setInt(5, phieuXuat.getMaDonHang());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Cân nhắc sử dụng logger
        }
    }

    // Phương thức cập nhật phiếu xuất
    public void updatePhieuXuat(PhieuXuat phieuXuat) {
        String sql = "UPDATE PhieuXuat SET NgayXuat = ?, MaNhanVien = ?, MaKho = ?, MaDonHang = ? WHERE MaPhieuXuat = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, phieuXuat.getNgayXuat());
            pstmt.setInt(2, phieuXuat.getMaNhanVien());
            pstmt.setInt(3, phieuXuat.getMaKho());
            pstmt.setInt(4, phieuXuat.getMaDonHang());
            pstmt.setInt(5, phieuXuat.getMaPhieuXuat());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Cân nhắc sử dụng logger
        }
    }

    // Phương thức xóa phiếu xuất
    public void deletePhieuXuat(int maPhieuXuat) {
        String sql = "DELETE FROM PhieuXuat WHERE MaPhieuXuat = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maPhieuXuat);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Cân nhắc sử dụng logger
        }
    }

    // Phương thức lấy tất cả phiếu xuất
    public List<PhieuXuat> getAllPhieuXuat() {
        List<PhieuXuat> phieuXuats = new ArrayList<>();
        String sql = "SELECT * FROM PhieuXuat";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                PhieuXuat phieuXuat = new PhieuXuat(
                        rs.getInt("MaPhieuXuat"),
                        rs.getDate("NgayXuat"),
                        rs.getInt("MaNhanVien"),
                        rs.getInt("MaKho"),
                        rs.getInt("MaDonHang")
                );
                phieuXuats.add(phieuXuat);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Cân nhắc sử dụng logger
        }
        return phieuXuats;
    }

    // Phương thức lấy phiếu xuất theo ID
    public PhieuXuat getPhieuXuatById(int maPhieuXuat) {
        PhieuXuat phieuXuat = null;
        String sql = "SELECT * FROM PhieuXuat WHERE MaPhieuXuat = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maPhieuXuat);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    phieuXuat = new PhieuXuat(
                            rs.getInt("MaPhieuXuat"),
                            rs.getDate("NgayXuat"),
                            rs.getInt("MaNhanVien"),
                            rs.getInt("MaKho"),
                            rs.getInt("MaDonHang")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Cân nhắc sử dụng logger
        }
        return phieuXuat;
    }

    // Phương thức thêm chi tiết phiếu xuất
    public void addChiTietPhieuXuat(ChiTietPhieuXuat chiTietPhieuXuat) {
        String sql = "INSERT INTO ChiTietPhieuXuat (MaPhieuXuat, MaVatTu, SoLuongXuat, GiaXuat) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, chiTietPhieuXuat.getMaPhieuXuat());
            pstmt.setInt(2, chiTietPhieuXuat.getMaVatTu());
            pstmt.setInt(3, chiTietPhieuXuat.getSoLuong());
            pstmt.setBigDecimal(4, chiTietPhieuXuat.getGia());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Cân nhắc sử dụng logger
        }
    }

    // Phương thức cập nhật chi tiết phiếu xuất
    public void updateChiTietPhieuXuat(ChiTietPhieuXuat chiTietPhieuXuat) {
        String sql = "UPDATE ChiTietPhieuXuat SET SoLuongXuat = ?, GiaXuat  = ? WHERE MaPhieuXuat = ? AND MaVatTu = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, chiTietPhieuXuat.getSoLuong());
            pstmt.setBigDecimal(2, chiTietPhieuXuat.getGia());
            pstmt.setInt(3, chiTietPhieuXuat.getMaPhieuXuat());
            pstmt.setInt(4, chiTietPhieuXuat.getMaVatTu());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Cân nhắc sử dụng logger
        }
    }

    // Phương thức xóa chi tiết phiếu xuất
    public void deleteChiTietPhieuXuat(int maPhieuXuat, int maVatTu) {
        String sql = "DELETE FROM ChiTietPhieuXuat WHERE MaPhieuXuat = ? AND MaVatTu = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maPhieuXuat);
            pstmt.setInt(2, maVatTu);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Cân nhắc sử dụng logger
        }
    }

    // Phương thức lấy tất cả chi tiết phiếu xuất theo mã phiếu xuất
    public List<ChiTietPhieuXuat> getChiTietPhieuXuatByMaPhieuXuat(int maPhieuXuat) {
        List<ChiTietPhieuXuat> chiTietPhieuXuats = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietPhieuXuat WHERE MaPhieuXuat = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maPhieuXuat);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ChiTietPhieuXuat chiTiet = new ChiTietPhieuXuat(
                            rs.getInt("MaPhieuXuat"),
                            rs.getInt("MaVatTu"),
                            rs.getInt("SoLuongXuat"), // Kiểm tra cột này
                            rs.getBigDecimal("GiaXuat")
                    );
                    chiTietPhieuXuats.add(chiTiet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Cân nhắc sử dụng logger
        }
        return chiTietPhieuXuats;
    }

}
