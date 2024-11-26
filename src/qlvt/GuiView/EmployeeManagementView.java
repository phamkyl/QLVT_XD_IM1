package qlvt.GuiView;

import qlvt.model.Employee;
import qlvt.connect.EmployeeDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class EmployeeManagementView extends JPanel {
    private JTextField txtmaNhanVien;
    private JTextField txthoTen, txtmatKhau;
    private JTextField txtchucVu, txtmaChiNhanh, txtphanQuyen;
    private JTable employeeTable;
    private EmployeeDAO employeeDAO; // Data Access Object for employees
    private int maChiNhanh; // The branch ID as an integer

    public EmployeeManagementView(MainView_IM mainViewIm, int maChiNhanh) throws SQLException {
        this.maChiNhanh = maChiNhanh; // Initialize branch ID
        employeeDAO = new EmployeeDAO();
        setSize(800, 530);
        setVisible(true);
        initComponents();
    }

    private void initComponents() throws SQLException {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Set layout for the main JPanel

        // Initialize UI components
        txtmaNhanVien = new JTextField(20);
        txthoTen = new JTextField(20);
        txtchucVu = new JTextField(20);
        txtmaChiNhanh = new JTextField(20);
        txtphanQuyen = new JTextField(20);
        txtmatKhau = new JTextField(20);

        // Create a JLabel for the table title
        JLabel titleLabel = new JLabel("DANH SÁCH NHÂN VIÊN ");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(3, 166, 120));
        titleLabel.setBorder(new EmptyBorder(10, 10, 10, 10));

        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setVerticalAlignment(SwingConstants.CENTER);
        // Create a table model for the employee list
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Name", "Role", "Branch", "Permission", "Password"}, 0);
        employeeTable = new JTable(model);

        // Create a panel for the table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new LineBorder(Color.BLACK, 1));
        tablePanel.add(titleLabel, BorderLayout.NORTH);  // Add titleLabel to the top of tablePanel
        tablePanel.add(new JScrollPane(employeeTable), BorderLayout.CENTER); // Add table inside the scroll pane

        // Create buttons for actions
        JButton addButton = new JButton("Thêm");
        JButton editButton = new JButton("Sửa ");
        JButton deleteButton = new JButton("Xóa");
        JButton removeButton = new JButton("remove");

        // Change button colors
        for (JButton button : new JButton[]{addButton, editButton, deleteButton, removeButton}) {
            button.setBackground(new Color(3, 166, 120));
            button.setForeground(Color.WHITE);
            button.setSize(100, 60);
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.setFocusPainted(false);
        }
        if (maChiNhanh == 0) {
            // Disable buttons if maChiNhanh is 0
            addButton.setEnabled(false);
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
            removeButton.setEnabled(false);
        } else {
            // Enable buttons if maChiNhanh is not 0
            addButton.setEnabled(true);
            editButton.setEnabled(true);
            deleteButton.setEnabled(true);
            removeButton.setEnabled(true);

            // Add action listeners to buttons
            addButton.addActionListener(e -> addEmployee());
            editButton.addActionListener(e -> editEmployee());
            deleteButton.addActionListener(e -> deleteEmployee());
            removeButton.addActionListener(e -> removeButton());
        }

        // Action listeners for buttons
      /*  addButton.addActionListener(e -> addEmployee());
        editButton.addActionListener(e -> editEmployee());
        deleteButton.addActionListener(e -> deleteEmployee());
        removeButton.addActionListener(e -> removeButton());

       */

        // Create input panel for text fields
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 6, 6));
        inputPanel.add(new JLabel("  Mã nhân viên :"));
        inputPanel.add(txtmaNhanVien);
        inputPanel.add(new JLabel("  Họ tên :"));
        inputPanel.add(txthoTen);
        inputPanel.add(new JLabel("  Chức vụ :"));
        inputPanel.add(txtchucVu);
        inputPanel.add(new JLabel("  Mã chi nhánh :"));
        inputPanel.add(txtmaChiNhanh);
        inputPanel.add(new JLabel("  Phân quyền :"));
        inputPanel.add(txtphanQuyen);
        inputPanel.add(new JLabel("  Mật khẩu :"));
        inputPanel.add(txtmatKhau);
        JPanel buttonPanel = new JPanel();
        if (maChiNhanh != 0)
        {
        // Panel to hold the buttons

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(removeButton);
        }else
        {

        }


        // Create a panel to hold inputPanel, tablePanel, and buttonPanel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));  // Vertical layout for mainPanel
        mainPanel.add(inputPanel);  // Add input fields panel
        mainPanel.add(tablePanel);  // Add table panel (which already contains title and table)
        mainPanel.add(buttonPanel);  // Add buttons panel

        // Add the entire mainPanel to the parent panel
        add(mainPanel);
        if (maChiNhanh == 0)
        {
            loadEmployeeTong();
        }else
        {
            loadEmployees(); // Initial load of employees
        }


    }






    private void removeButton() {
        clearFields();
    }

  private  void loadEmployeeTong() throws SQLException {
      List<Employee> employees = employeeDAO.getAllEmployeesTong();
      DefaultTableModel model = (DefaultTableModel) employeeTable.getModel();
      model.setRowCount(0);  // Clear existing rows in the table

      // Add rows to the table model
      for (Employee employee : employees) {
          model.addRow(new Object[]{
                  employee.getMaNhanVien(),
                  employee.getHoTen(),
                  employee.getChucVu(),
                  employee.getMaChiNhanh(),
                  employee.getPhanQuyen(),
                  employee.getMatKhau()
          });
      }
  }

    private void loadEmployees() {
        try {

            // Get the list of employees for the specified branch
            List<Employee> employees = employeeDAO.getAllEmployees(maChiNhanh);

            // Get the table model from the JTable
            DefaultTableModel model = (DefaultTableModel) employeeTable.getModel();
            model.setRowCount(0);  // Clear existing rows in the table

            // Add rows to the table model
            for (Employee employee : employees) {
                model.addRow(new Object[]{
                        employee.getMaNhanVien(),
                        employee.getHoTen(),
                        employee.getChucVu(),
                        employee.getMaChiNhanh(),
                        employee.getPhanQuyen(),
                        employee.getMatKhau()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading employees: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addEmployee() {
        try {
            // Collect data from the input fields
            int maNhanVien = Integer.parseInt(txtmaNhanVien.getText());
            String hoTen = txthoTen.getText();
            String chucVu = txtchucVu.getText();
            int maChiNhanh = Integer.parseInt(txtmaChiNhanh.getText());
            String phanQuyen = txtphanQuyen.getText();
            String matKhau = txtmatKhau.getText();

            // Create an Employee object
            Employee employee = new Employee(maNhanVien, hoTen, chucVu, maChiNhanh, phanQuyen, matKhau);

            // Add the employee to the database
            employeeDAO.addEmployee(employee);

            // Reload the employee list
            loadEmployees();

            // Clear the input fields
            clearFields();
            JOptionPane.showMessageDialog(this, "Employee added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding employee: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editEmployee() {
        try {
            // Get the selected row from the table
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select an employee to edit.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get employee ID from the selected row
            int maNhanVien = (int) employeeTable.getValueAt(selectedRow, 0);

            // Collect data from the input fields
            String hoTen = txthoTen.getText();
            String chucVu = txtchucVu.getText();
            int maChiNhanh = Integer.parseInt(txtmaChiNhanh.getText());
            String phanQuyen = txtphanQuyen.getText();
            String matKhau = txtmatKhau.getText();

            // Create an Employee object with updated data
            Employee employee = new Employee(maNhanVien, hoTen, chucVu, maChiNhanh, phanQuyen, matKhau);

            // Update the employee in the database
            employeeDAO.updateEmployee(employee);

            // Reload the employee list
            loadEmployees();

            // Clear the input fields
            clearFields();
            JOptionPane.showMessageDialog(this, "Employee updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating employee: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteEmployee() {
        try {
            // Get the selected row from the table
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select an employee to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get employee ID from the selected row
            int maNhanVien = (int) employeeTable.getValueAt(selectedRow, 0);

            // Confirm deletion
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this employee?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // Delete the employee from the database
                employeeDAO.deleteEmployee(maNhanVien, maChiNhanh);

                // Reload the employee list
                loadEmployees();
                JOptionPane.showMessageDialog(this, "Employee deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting employee: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void clearFields() {
        txtmaNhanVien.setText("");
        txthoTen.setText("");
        txtchucVu.setText("");
        txtmaChiNhanh.setText("");
        txtphanQuyen.setText("");
        txtmatKhau.setText("");
    }
}
