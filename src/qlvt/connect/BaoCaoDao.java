package qlvt.connect;

import qlvt.model.ReportData;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class BaoCaoDao {

    private DistributedDatabaseConnection dbConnection;

    public BaoCaoDao() throws SQLException {
        dbConnection = new DistributedDatabaseConnection(); // Khởi tạo kết nối
    }

    // Lấy tổng số lượng vật tư trong kho
    public int getTotalVatTu() throws SQLException {
        String query = "SELECT COUNT(*) AS TotalVatTu FROM VatTu";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt("TotalVatTu");
            }
        }
        return 0;
    }

    // Lấy tổng giá trị hàng tồn kho theo từng kho
    public Map<String, Double> getTonKhoByKho() throws SQLException {
        String query = """
            SELECT k.TenKho, SUM(tk.SoLuongTon * vt.Gia) AS TongGiaTri
            FROM TonKho tk
            JOIN Kho k ON tk.MaKho = k.MaKho
            JOIN VatTu vt ON tk.MaVatTu = vt.MaVatTu
            GROUP BY k.TenKho
        """;

        Map<String, Double> result = new HashMap<>();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                result.put(resultSet.getString("TenKho"), resultSet.getDouble("TongGiaTri"));
            }
        }
        return result;
    }

    // Lấy chi phí nhập theo nhà cung cấp
    public Map<String, Double> getChiPhiNhapTheoNhaCungCap() throws SQLException {
        String query = """
            SELECT 
                NCC.TenNhaCungCap,
                SUM(CTPN.SoLuongNhap * CTPN.GiaNhap) AS TongChiPhi
            FROM 
                ChiTietPhieuNhap CTPN
            JOIN 
                PhieuNhap PN ON CTPN.MaPhieuNhap = PN.MaPhieuNhap
            JOIN 
                NhaCungCap NCC ON PN.MaNhaCungCap = NCC.MaNhaCungCap
            GROUP BY 
                NCC.TenNhaCungCap;
        """;

        Map<String, Double> result = new HashMap<>();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String nhaCungCap = resultSet.getString("TenNhaCungCap");
                double tongChiPhi = resultSet.getDouble("TongChiPhi");
                result.put(nhaCungCap, tongChiPhi);
            }
        }
        return result;
    }

    // Lấy tổng số lượng phiếu nhập và phiếu xuất
    public Map<String, Integer> getPhieuNhapXuatCount() throws SQLException {
        String queryNhap = "SELECT COUNT(*) AS TotalNhap FROM PhieuNhap";
        String queryXuat = "SELECT COUNT(*) AS TotalXuat FROM PhieuXuat";

        Map<String, Integer> result = new HashMap<>();
        try (Connection connection = dbConnection.getConnection()) {
            try (PreparedStatement statementNhap = connection.prepareStatement(queryNhap);
                 ResultSet resultSetNhap = statementNhap.executeQuery()) {
                if (resultSetNhap.next()) {
                    result.put("PhieuNhap", resultSetNhap.getInt("TotalNhap"));
                }
            }
            try (PreparedStatement statementXuat = connection.prepareStatement(queryXuat);
                 ResultSet resultSetXuat = statementXuat.executeQuery()) {
                if (resultSetXuat.next()) {
                    result.put("PhieuXuat", resultSetXuat.getInt("TotalXuat"));
                }
            }
        }
        return result;
    }

    // Lấy tổng số lượng nhân viên theo chi nhánh
    public Map<String, Integer> getNhanVienTheoChiNhanh() throws SQLException {
        String query = """
            SELECT 
                CN.TenChiNhanh,
                COUNT(NV.MaNhanVien) AS SoLuongNhanVien
            FROM 
                NhanVien NV
            JOIN 
                ChiNhanh CN ON NV.MaChiNhanh = CN.MaChiNhanh
            GROUP BY 
                CN.TenChiNhanh;
        """;

        Map<String, Integer> result = new HashMap<>();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String chiNhanh = resultSet.getString("TenChiNhanh");
                int soLuongNhanVien = resultSet.getInt("SoLuongNhanVien");
                result.put(chiNhanh, soLuongNhanVien);
            }
        }
        return result;
    }

    // Vat tu ton kho theo Kho
    public Map<String, Map<String, Integer>> getTonKhoTheoKhoVaVatTu() throws SQLException {
        String query = """
            SELECT k.TenKho, vt.TenVatTu, tk.SoLuongTon
            FROM TonKho tk
            JOIN Kho k ON tk.MaKho = k.MaKho
            JOIN VatTu vt ON tk.MaVatTu = vt.MaVatTu;
            """;

        Map<String, Map<String, Integer>> result = new HashMap<>();

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String tenKho = resultSet.getString("TenKho");
                String tenVatTu = resultSet.getString("TenVatTu");
                int soLuongTon = resultSet.getInt("SoLuongTon");

                // Thêm dữ liệu vào Map: tenKho -> (tenVatTu -> soLuongTon)
                result.computeIfAbsent(tenKho, k -> new HashMap<>()).put(tenVatTu, soLuongTon);
            }
        }

        return result;
    }



    // Add this method in BaoCaoDao class
    public Map<String, Double> getDoanhSoTheoThang() throws SQLException {
        String query = """
            SELECT
                YEAR(HoaDon.NgayLap) AS Nam,
                MONTH(HoaDon.NgayLap) AS Thang,
                SUM(HoaDon.TongTien) AS DoanhThu
            FROM
                HoaDon
            GROUP BY
                YEAR(HoaDon.NgayLap), MONTH(HoaDon.NgayLap)
            ORDER BY
                Nam DESC, Thang DESC;
    """;

        Map<String, Double> result = new HashMap<>();

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                // Lấy số tháng và năm từ kết quả
                int thang = resultSet.getInt("Thang");
                int nam = resultSet.getInt("Nam");
                double doanhThu = resultSet.getDouble("DoanhThu");

                // Tạo chuỗi tháng theo định dạng "Tháng X - Năm Y"
                String thangNam = "Tháng " + thang + " - Năm " + nam;

                // Thêm vào Map kết quả
                result.put(thangNam, doanhThu);
            }
        }

        return result;
    }



    // Add this method in BaoCaoDao class
    public Map<String, Double> getPhanBoVatTuTheoKho() throws SQLException {
        String query = """
        SELECT Kho.TenKho, SUM(VatTu.Soluong) AS TongVatTu
        FROM VatTu
        JOIN Kho ON VatTu.MaKho = Kho.MaKho
        GROUP BY Kho.TenKho
    """;

        Map<String, Double> result = new HashMap<>();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String tenKho = resultSet.getString("TenKho");
                double tongVatTu = resultSet.getDouble("TongVatTu");
                result.put(tenKho, tongVatTu);
            }
        }
        return result;
    }

    // Phương thức lấy số lượng nhân viên theo chi nhánh
    public Map<String, Integer> getEmployeeCountByBranch() throws SQLException {
        String query = """
        SELECT BranchName, COUNT(EmployeeID) AS EmployeeCount
        FROM Employees
        GROUP BY BranchName
        """;

        Map<String, Integer> result = new HashMap<>();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String branchName = resultSet.getString("BranchName");
                int employeeCount = resultSet.getInt("EmployeeCount");
                result.put(branchName, employeeCount);
            }
        }
        return result;
    }





}
