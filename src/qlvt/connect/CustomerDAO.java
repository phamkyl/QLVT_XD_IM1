package qlvt.connect;

import qlvt.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomerDAO {
    private DistributedDatabaseConnection dbConnection;

    public CustomerDAO() throws SQLException {
        dbConnection = new DistributedDatabaseConnection(); // Khởi tạo kết nối
    }

    // Phương thức lấy tất cả khách hàng
    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT TOP (1000) [MaKhachHang], [HoTen], [DiaChi], [SoDienThoai], [Email], [rowguid] FROM [VT_XAYDUNG].[dbo].[KhachHang]";

        // Sử dụng try-with-resources để tự động đóng kết nối
        try (Connection connection = dbConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int maKhachHang = resultSet.getInt("MaKhachHang");
                String hoTen = resultSet.getString("HoTen");
                String diaChi = resultSet.getString("DiaChi");
                String soDienThoai = resultSet.getString("SoDienThoai");
                String email = resultSet.getString("Email");


                customers.add(new Customer(maKhachHang, hoTen, diaChi, soDienThoai, email));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Lỗi khi lấy danh sách khách hàng: " + e.getMessage());
        }

        return customers;
    }

    // Phương thức thêm khách hàng
    public void addCustomer(Customer customer) throws SQLException {
        String query = "INSERT INTO [VT_XAYDUNG].[dbo].[KhachHang] ([HoTen], [DiaChi], [SoDienThoai], [Email], [rowguid]) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, customer.getHoTen());
            preparedStatement.setString(2, customer.getDiaChi());
            preparedStatement.setString(3, customer.getSoDienThoai());
            preparedStatement.setString(4, customer.getEmail());
            preparedStatement.setObject(5, customer.getRowguid());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Lỗi khi thêm khách hàng: " + e.getMessage());
        }
    }

    // Phương thức sửa thông tin khách hàng
    public void updateCustomer(Customer customer) throws SQLException {
        String query = "UPDATE [VT_XAYDUNG].[dbo].[KhachHang] SET [HoTen] = ?, [DiaChi] = ?, [SoDienThoai] = ?, [Email] = ? WHERE [MaKhachHang] = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, customer.getHoTen());
            preparedStatement.setString(2, customer.getDiaChi());
            preparedStatement.setString(3, customer.getSoDienThoai());
            preparedStatement.setString(4, customer.getEmail());
            preparedStatement.setInt(5, customer.getMaKhachHang());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Lỗi khi sửa khách hàng: " + e.getMessage());
        }
    }

    // Phương thức xóa khách hàng
    public void deleteCustomer(int maKhachHang) throws SQLException {
        String query = "DELETE FROM [VT_XAYDUNG].[dbo].[KhachHang] WHERE [MaKhachHang] = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, maKhachHang);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Lỗi khi xóa khách hàng: " + e.getMessage());
        }
    }

    // Phương thức tìm kiếm khách hàng theo tên
    public List<Customer> searchCustomersByName(String name) throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT [MaKhachHang], [HoTen], [DiaChi], [SoDienThoai], [Email], [rowguid] FROM [VT_XAYDUNG].[dbo].[KhachHang] WHERE [HoTen] LIKE ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + name + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int maKhachHang = resultSet.getInt("MaKhachHang");
                String hoTen = resultSet.getString("HoTen");
                String diaChi = resultSet.getString("DiaChi");
                String soDienThoai = resultSet.getString("SoDienThoai");
                String email = resultSet.getString("Email");

                customers.add(new Customer(maKhachHang, hoTen, diaChi, soDienThoai, email));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Lỗi khi tìm kiếm khách hàng: " + e.getMessage());
        }

        return customers;
    }
}
