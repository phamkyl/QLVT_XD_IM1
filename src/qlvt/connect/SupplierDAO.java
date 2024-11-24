package qlvt.connect;

import qlvt.model.Supplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {

    private DistributedDatabaseConnection dbConnection;

    public SupplierDAO() {
        dbConnection = new DistributedDatabaseConnection();
    }

    // Phương thức để tìm nhà cung cấp dựa trên mã nhà cung cấp
    public Supplier getSupplierById(int maNhaCungCap) throws SQLException {
        String query = "SELECT * FROM NhaCungCap WHERE MaNhaCungCap = ?";
        try (Connection connection = dbConnection.connectToServer(DistributedDatabaseConnection.SERVER1_URL);
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, maNhaCungCap);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Supplier(
                        rs.getInt("MaNhaCungCap"),
                        rs.getString("TenNhaCungCap"),
                        rs.getString("DiaChi"),
                        rs.getString("SoDienThoai"),
                        rs.getString("Email")
                );
            }
        }
        return null; // Trả về null nếu không tìm thấy
    }

    // Lấy danh sách tất cả nhà cung cấp
    public List<Supplier> getAllSuppliers() throws SQLException {
        List<Supplier> suppliers = new ArrayList<>();
        String query = "SELECT * FROM NhaCungCap"; // Cập nhật với cấu trúc bảng thực tế

        try (Connection connection = dbConnection.connectToServer(DistributedDatabaseConnection.SERVER1_URL);
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                suppliers.add(new Supplier(
                        rs.getInt("MaNhaCungCap"),
                        rs.getString("TenNhaCungCap"),
                        rs.getString("DiaChi"),
                        rs.getString("SoDienThoai"),
                        rs.getString("Email")
                ));
            }
        }
        return suppliers;
    }

    // Thêm nhà cung cấp
    public void addSupplier(Supplier supplier) throws SQLException {
        String query = "INSERT INTO NhaCungCap (MaNhaCungCap, TenNhaCungCap, DiaChi, SoDienThoai, Email) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = dbConnection.connectToServer(DistributedDatabaseConnection.SERVER1_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, supplier.getMaNhaCungCap());
            preparedStatement.setString(2, supplier.getTenNhaCungCap());
            preparedStatement.setString(3, supplier.getDiaChi());
            preparedStatement.setString(4, supplier.getSoDienThoai());
            preparedStatement.setString(5, supplier.getEmail());
            preparedStatement.executeUpdate();
        }
    }

    // Cập nhật nhà cung cấp
    public void updateSupplier(Supplier supplier) throws SQLException {
        String query = "UPDATE NhaCungCap SET TenNhaCungCap = ?, DiaChi = ?, SoDienThoai = ?, Email = ? WHERE MaNhaCungCap = ?";

        try (Connection connection = dbConnection.connectToServer(DistributedDatabaseConnection.SERVER1_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, supplier.getTenNhaCungCap());
            preparedStatement.setString(2, supplier.getDiaChi());
            preparedStatement.setString(3, supplier.getSoDienThoai());
            preparedStatement.setString(4, supplier.getEmail());
            preparedStatement.setInt(5, supplier.getMaNhaCungCap());
            preparedStatement.executeUpdate();
        }
    }

    // Xóa nhà cung cấp
    public void deleteSupplier(int maNhaCungCap) throws SQLException {
        String query = "DELETE FROM NhaCungCap WHERE MaNhaCungCap = ?";

        try (Connection connection = dbConnection.connectToServer(DistributedDatabaseConnection.SERVER1_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, maNhaCungCap);
            preparedStatement.executeUpdate();
        }
    }
}
