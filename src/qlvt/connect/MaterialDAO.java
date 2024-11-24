package qlvt.connect;

import qlvt.model.Material;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaterialDAO {

    private DistributedDatabaseConnection dbConnection;

    public MaterialDAO() {
        dbConnection = new DistributedDatabaseConnection();
    }

    // Lấy danh sách vật tư
    public List<Material> getAllMaterials() throws SQLException {
        List<Material> materials = new ArrayList<>();
        String query = "SELECT * FROM VatTu";

        try (Connection connection = dbConnection.connectToServer(DistributedDatabaseConnection.SERVER1_URL);
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                materials.add(new Material(
                        rs.getInt("MaVatTu"),
                        rs.getString("TenVatTu"),
                        rs.getString("MoTa"),
                        rs.getString("DonViTinh"),
                        rs.getDouble("Gia"),
                        rs.getInt("MaNhaCungCap")
                ));
            }
        }
        return materials;
    }

    // Thêm vật tư
    public void addMaterial(Material material) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM VatTu WHERE MaVatTu = ?";
        String insertQuery = "INSERT INTO VatTu (MaVatTu, TenVatTu, MoTa, DonViTinh, Gia, MaNhaCungCap) VALUES (?, ?, ?, ?, ?, ?)";
        String serverURL = getServerURLByBranch(material.getMaNhaCungCap());

        try (Connection connection = dbConnection.connectToServer(serverURL);
             PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
             PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {

            // Kiểm tra xem MaVatTu đã tồn tại chưa
            checkStatement.setInt(1, material.getMaVatTu());
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count > 0) {
                throw new SQLException("Mã vật tư " + material.getMaVatTu() + " đã tồn tại.");
            }

            // Nếu không tồn tại, thực hiện chèn
            insertStatement.setInt(1, material.getMaVatTu());
            insertStatement.setString(2, material.getTenVatTu());
            insertStatement.setString(3, material.getMoTa());
            insertStatement.setString(4, material.getDonViTinh());
            insertStatement.setDouble(5, material.getGia());
            insertStatement.setInt(6, material.getMaNhaCungCap());

            insertStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm vật tư: " + e.getMessage());
            throw e; // Ném lại ngoại lệ để xử lý ở nơi gọi
        }
    }

    // Cập nhật vật tư
    public void updateMaterial(Material material) throws SQLException {
        String query = "UPDATE VatTu SET TenVatTu = ?, MoTa = ?, DonViTinh = ?, Gia = ?, MaNhaCungCap = ? WHERE MaVatTu = ?";

        try (Connection connection = dbConnection.connectToServer(DistributedDatabaseConnection.SERVER1_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, material.getTenVatTu());
            preparedStatement.setString(2, material.getMoTa());
            preparedStatement.setString(3, material.getDonViTinh());
            preparedStatement.setDouble(4, material.getGia());
            preparedStatement.setInt(5, material.getMaNhaCungCap());
            preparedStatement.setInt(6, material.getMaVatTu());
            preparedStatement.executeUpdate();
        }
    }

    // Xóa vật tư
    public void deleteMaterial(int maVatTu) throws SQLException {
        String query = "DELETE FROM VatTu WHERE MaVatTu = ?";

        try (Connection connection = dbConnection.connectToServer(DistributedDatabaseConnection.SERVER1_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, maVatTu);
            preparedStatement.executeUpdate();
        }
    }

    // Method to get the server URL based on branch
    private String getServerURLByBranch(int maChiNhanh) {
        switch (maChiNhanh) {
            case 1:
                return DistributedDatabaseConnection.SERVER2_URL; // Connect to SERVER2_URL for branch 1
            case 2:
                return DistributedDatabaseConnection.SERVER3_URL; // Connect to SERVER3_URL for branch 2
            // case 3:
            // return DistributedDatabaseConnection.SERVER4_URL; // Connect to SERVER4_URL for branch 3
            // Add more cases as needed for additional branches
            default:
                throw new IllegalArgumentException("Chi nhánh không hợp lệ: " + maChiNhanh);
        }
    }
}
