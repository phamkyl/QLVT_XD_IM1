package qlvt.Controller;

import qlvt.GuiView.LoginView;
import qlvt.GuiView.MainView_IM;
import qlvt.connect.EmployeeDAO;
import qlvt.model.Employee;

import javax.swing.*;

public class LoginController {
    private final EmployeeDAO employeeDAO;
    private final LoginView loginView;
    private static Employee currentEmployee;

    public LoginController(LoginView view) {
        this.employeeDAO = new EmployeeDAO();
        this.loginView = view;
    }

    public void login(String maNhanVien, String matKhau, String maChiNhanhStr,String Chucvu) {
        try {
            // Convert maChiNhanhStr to Integer
            int maChiNhanh = Integer.parseInt(maChiNhanhStr);

            // Check login credentials
            Employee employee = employeeDAO.getEmployeeByCredentials(maNhanVien, matKhau,Chucvu);

            // If login is successful
            if (employee != null && employee.getMaChiNhanh() == maChiNhanh) {
                String phanQuyen = employee.getPhanQuyen();

                loginView.showLoginSuccess(employee);

                // Switch to main view based on role
                SwingUtilities.invokeLater(() -> {
                    MainView_IM mainView;
                    JPanel optionsPanel = new JPanel(); // Create a new JPanel for options

                    switch (phanQuyen) {
                        case "admin": // quản lý 2 chi nhánh
                            mainView = new MainView_IM("admin", employee.getHoTen(), maChiNhanh, employee.getChucVu());
                            break;
                        case "ADMIN0": // quản lý 1 chi nhánh
                            mainView = new MainView_IM("ADMIN0", employee.getHoTen(), maChiNhanh,employee.getChucVu());
                            break;
                        case "employee": // nhân viên 1 chi nhánh
                            mainView = new MainView_IM("employee", employee.getHoTen(), maChiNhanh,employee.getChucVu());
                            break;
                        default:
                            loginView.showError("Không xác định quyền!");
                            return; // Do not switch to main view
                    }

                    mainView.showOptions(optionsPanel); // Update options for each role
                    mainView.setVisible(true); // Show the main view
                    loginView.dispose(); // Close the login form
                });

            } else {
                loginView.showError("Sai mã nhân viên, mật khẩu hoặc mã chi nhánh!");
            }
        } catch (NumberFormatException e) {
            loginView.showError("Mã chi nhánh phải là số nguyên!");
        } catch (Exception e) {
            e.printStackTrace();
            loginView.showError("Lỗi khi đăng nhập: " + e.getMessage());
        }
    }

    public static Employee getCurrentEmployee() {
        return currentEmployee; // Phương thức lấy nhân viên hiện tại
    }
}