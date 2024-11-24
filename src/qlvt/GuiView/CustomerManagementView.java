package qlvt.GuiView;

import qlvt.connect.CustomerDAO;
import qlvt.model.Customer;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class CustomerManagementView extends JPanel {
    private DefaultTableModel model;
    private CustomerDAO customerDAO;
    private JTextField txtMaKhachHang, txtHoTen, txtDiaChi, txtSoDienThoai, txtEmail;
    private JTable customerTable;

    public CustomerManagementView(MainView_IM mainViewIm) throws SQLException {
        customerDAO = new CustomerDAO();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Title label
        JLabel titleLabel = new JLabel("QUẢN LÝ KHÁCH HÀNG");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(new Color(3, 166, 120));

        // Input panel
        txtMaKhachHang = new JTextField(20);
        txtHoTen = new JTextField(20);
        txtDiaChi = new JTextField(20);
        txtSoDienThoai = new JTextField(20);
        txtEmail = new JTextField(20);

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 6, 6));
        inputPanel.add(new JLabel("Mã Khách Hàng:"));
        inputPanel.add(txtMaKhachHang);
        inputPanel.add(new JLabel("Họ Tên:"));
        inputPanel.add(txtHoTen);
        inputPanel.add(new JLabel("Địa Chỉ:"));
        inputPanel.add(txtDiaChi);
        inputPanel.add(new JLabel("Số Điện Thoại:"));
        inputPanel.add(txtSoDienThoai);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(txtEmail);

        // Table panel
        model = new DefaultTableModel(new Object[]{"Mã Khách Hàng", "Họ Tên", "Địa Chỉ", "Số Điện Thoại", "Email"}, 0);
        customerTable = new JTable(model);

        // Add MouseListener to table
        customerTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = customerTable.getSelectedRow();
                if (selectedRow != -1) {
                    txtMaKhachHang.setText(model.getValueAt(selectedRow, 0).toString());
                    txtHoTen.setText(model.getValueAt(selectedRow, 1).toString());
                    txtDiaChi.setText(model.getValueAt(selectedRow, 2).toString());
                    txtSoDienThoai.setText(model.getValueAt(selectedRow, 3).toString());
                    txtEmail.setText(model.getValueAt(selectedRow, 4).toString());
                }
            }
        });

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new LineBorder(Color.BLACK, 1));
        tablePanel.add(titleLabel, BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(customerTable), BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Thêm");
        JButton editButton = new JButton("Sửa");
        JButton deleteButton = new JButton("Xóa");
        JButton refreshButton = new JButton("Làm Mới");
        JButton exitButton = new JButton("Thoát");

        for (JButton button : new JButton[]{addButton, editButton, deleteButton, refreshButton, exitButton}) {
            button.setBackground(new Color(3, 166, 120));
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.setFocusPainted(false);
        }

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(exitButton);

        // Add panels to main layout
        add(inputPanel);
        add(tablePanel);
        add(buttonPanel);

        // Button actions
        addButton.addActionListener(e -> addCustomer());
        editButton.addActionListener(e -> editCustomer());
        deleteButton.addActionListener(e -> deleteCustomer());
        refreshButton.addActionListener(e -> clearFields());
        exitButton.addActionListener(e -> System.exit(0));

        // Load customers into table
        loadCustomers();
    }

    private void loadCustomers() {
        try {
            List<Customer> customers = customerDAO.getAllCustomers();
            model.setRowCount(0); // Clear existing rows
            for (Customer customer : customers) {
                model.addRow(new Object[]{
                        customer.getMaKhachHang(),
                        customer.getHoTen(),
                        customer.getDiaChi(),
                        customer.getSoDienThoai(),
                        customer.getEmail()
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addCustomer() {
        try {
            Customer customer = new Customer(
                    Integer.parseInt(txtMaKhachHang.getText()),
                    txtHoTen.getText(),
                    txtDiaChi.getText(),
                    txtSoDienThoai.getText(),
                    txtEmail.getText()
            );
            customerDAO.addCustomer(customer);
            JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
            loadCustomers();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editCustomer() {
        try {
            int selectedRow = customerTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một hàng để sửa.");
                return;
            }

            String maKhachHang = model.getValueAt(selectedRow, 0).toString();
            Customer customer = new Customer(
                    Integer.parseInt(txtMaKhachHang.getText()),
                    txtHoTen.getText(),
                    txtDiaChi.getText(),
                    txtSoDienThoai.getText(),
                    txtEmail.getText()
            );
            customerDAO.updateCustomer(customer);
            JOptionPane.showMessageDialog(this, "Sửa khách hàng thành công!");
            loadCustomers();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCustomer() {
        try {
            int selectedRow = customerTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một hàng để xóa.");
                return;
            }

            String maKhachHang = model.getValueAt(selectedRow, 0).toString();
            customerDAO.deleteCustomer(Integer.parseInt(maKhachHang));
            JOptionPane.showMessageDialog(this, "Xóa khách hàng thành công!");
            loadCustomers();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        txtMaKhachHang.setText("");
        txtHoTen.setText("");
        txtDiaChi.setText("");
        txtSoDienThoai.setText("");
        txtEmail.setText("");
    }
}
