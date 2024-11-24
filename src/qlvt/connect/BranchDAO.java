package qlvt.connect;

import qlvt.model.Branch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BranchDAO {

    private DistributedDatabaseConnection dbConnection;

    public BranchDAO() {
        dbConnection = new DistributedDatabaseConnection();
    }

    // Lấy chi nhánh dựa trên mã chi nhánh
    // Lấy chi nhánh dựa trên mã chi nhánh
    public Branch getBranchById(int maChiNhanh) throws SQLException {
        String query = "SELECT * FROM ChiNhanh WHERE MaChiNhanh = ?";
        Branch branch = null;

        // Lấy URL server dựa trên mã chi nhánh
        String serverURL = getServerURLByBranch(maChiNhanh);

        try (Connection connection = dbConnection.connectToServer(serverURL);
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, maChiNhanh);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                branch = new Branch(
                        rs.getInt("MaChiNhanh"),
                        rs.getString("TenChiNhanh"),
                        rs.getString("DiaChi")
                );
            }
        }
        return branch; // Trả về đối tượng Branch hoặc null nếu không tìm thấy
    }


    // Lấy danh sách tất cả các chi nhánh
    public List<Branch> getAllBranches(int maChiNhanh) throws SQLException {
        List<Branch> branches = new ArrayList<>();
        String query = "SELECT * FROM ChiNhanh WHERE MaChiNhanh = ?";

        // Giả sử chúng ta chỉ cần kết nối với một trong các server chính để lấy danh sách tất cả chi nhánh
        String serverURL = getServerURLByBranch(maChiNhanh); // Get server URL based on branch

        try (Connection connection = dbConnection.connectToServer(serverURL);
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, maChiNhanh); // Set branch ID in the query
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                branches.add(new Branch(
                        rs.getInt("MaChiNhanh"),
                        rs.getString("TenChiNhanh"),
                        rs.getString("DiaChi")
                ));
            }
        }
        return branches; // Trả về danh sách chi nhánh
    }

    // Thêm chi nhánh mới
    public void addBranch(Branch branch) throws SQLException {
        String query = "INSERT INTO ChiNhanh (MaChiNhanh, TenChiNhanh, DiaChi) VALUES (?, ?, ?)";
        String serverURL = getServerURLByBranch(branch.getMaChiNhanh()); // Lấy URL server dựa trên mã chi nhánh

        try (Connection connection = dbConnection.connectToServer(serverURL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, branch.getMaChiNhanh());
            preparedStatement.setString(2, branch.getTenChiNhanh());
            preparedStatement.setString(3, branch.getDiaChi());
            preparedStatement.executeUpdate(); // Thực hiện thêm mới
        }
    }

    // Cập nhật thông tin chi nhánh
    public void updateBranch(Branch branch) throws SQLException {
        String query = "UPDATE ChiNhanh SET TenChiNhanh = ?, DiaChi = ? WHERE MaChiNhanh = ?";
        String serverURL = getServerURLByBranch(branch.getMaChiNhanh()); // Lấy URL server dựa trên mã chi nhánh

        try (Connection connection = dbConnection.connectToServer(serverURL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, branch.getTenChiNhanh());
            preparedStatement.setString(2, branch.getDiaChi());
            preparedStatement.setInt(3, branch.getMaChiNhanh());
            preparedStatement.executeUpdate(); // Thực hiện cập nhật
        }
    }

    // Xóa chi nhánh
    public void deleteBranch(int maChiNhanh) throws SQLException {
        String query = "DELETE FROM ChiNhanh WHERE MaChiNhanh = ?";
        String serverURL = getServerURLByBranch(maChiNhanh); // Lấy URL server dựa trên mã chi nhánh

        try (Connection connection = dbConnection.connectToServer(serverURL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, maChiNhanh);
            preparedStatement.executeUpdate(); // Thực hiện xóa
        }
    }

    // Lấy URL của server dựa trên mã chi nhánh
    // Lấy URL của server dựa trên mã chi nhánh
    private String getServerURLByBranch(int maChiNhanh) {
        switch (maChiNhanh) {
            case 1:
                return DistributedDatabaseConnection.SERVER2_URL; // URL server chứa chi nhánh 1
            case 2:
                return DistributedDatabaseConnection.SERVER3_URL; // URL server chứa chi nhánh 2
            case 3:
              //  return DistributedDatabaseConnection.SERVER4_URL; // URL server chứa chi nhánh 3
            // Add more branches and corresponding server URLs if needed
            default:
                throw new IllegalArgumentException("Chi nhánh không hợp lệ: " + maChiNhanh);
        }
    }

}
