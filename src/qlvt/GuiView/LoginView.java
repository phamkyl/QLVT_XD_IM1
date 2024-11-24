package qlvt.GuiView;

import qlvt.Controller.LoginController;
import qlvt.model.Employee;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private JTextField txtMaNhanVien;
    private JPasswordField txtMatKhau;
    private JTextField txtMaChiNhanh; // New field for branch ID
    private LoginController controller;

    public LoginView(LoginController controller) {
        this.controller = controller;
        initialize();
    }

    private void initialize() {
        setTitle("Đăng Nhập");
        setSize(750, 484);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Create panels
        JPanel panel1 = createTitlePanel(Color.WHITE);
        JPanel panel2 = createInputPanel(Color.WHITE);
        JPanel panel3 = createImagePanel(Color.WHITE);
        JPanel panel4 = createButtonPanel(Color.WHITE);

        // Create main layout
        // Create a panel to hold panel2 and panel3 horizontally
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(panel3, BorderLayout.WEST); // Add panel3 (image) to the left
        centerPanel.add(panel2, BorderLayout.CENTER); // Add panel2 (input fields) to the right

        // Create main layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(panel1, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER); // Add centerPanel to the center
        mainPanel.add(panel4, BorderLayout.SOUTH); // Add button panel at the bottom


        add(mainPanel);
        setVisible(true);
    }

    private JPanel createTitlePanel(Color white) {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 255, 255));
        panel.setPreferredSize(new Dimension(600, 60));

        JLabel titleLabel = new JLabel("QUẢN LÍ VẬT TƯ XÂY DỰNG TOÀN MINH");
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        panel.add(titleLabel);
        return panel;
    }

    private JPanel createInputPanel(Color white) {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 255, 255));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Nhập mã nhân viên
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Mã nhân viên:"), gbc);

        txtMaNhanVien = new JTextField(20);
        gbc.gridx = 2;
        panel.add(txtMaNhanVien, gbc);

        // Nhập mã chi nhánh
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Mã chi nhánh:"), gbc);

        txtMaChiNhanh = new JTextField(20);
        gbc.gridx = 2;
        panel.add(txtMaChiNhanh, gbc);

        // Nhập mật khẩu
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Mật khẩu:"), gbc);

        txtMatKhau = new JPasswordField(20);
        gbc.gridx = 2;
        panel.add(txtMatKhau, gbc);

        return panel;
    }

    private JPanel createImagePanel(Color white) {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 255, 255));
        panel.setLayout(new GridBagLayout()); // Use GridBagLayout for better positioning
        GridBagConstraints gbc = new GridBagConstraints();

        // Load the image and resize it
        ImageIcon originalImage = new ImageIcon("image/logo.jpg"); // Update with actual image path
        Image image = originalImage.getImage().getScaledInstance(334, 267, Image.SCALE_SMOOTH); // Resize image (width, height)
        ImageIcon scaledImage = new ImageIcon(image);

        JLabel imageLabel = new JLabel(scaledImage);

        // Center the image in the panel
        gbc.gridx = 21;
        gbc.gridy = 105;
        gbc.insets = new Insets(3, 3, 3, 3); // Add padding around the image
        panel.add(imageLabel, gbc);

        return panel;
    }


    private JPanel createButtonPanel(Color white) {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 255, 255));
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnLogin = new JButton("Đăng Nhập");
        btnLogin.setBackground(new Color(34, 139, 34));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));

        JButton btnCancel = new JButton("Thoát");
        btnCancel.setBackground(new Color(237, 28, 36));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.setFont(new Font("Arial", Font.BOLD, 14));

        // Add action listener for login button
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String maNhanVien = txtMaNhanVien.getText();
                String matKhau = new String(txtMatKhau.getPassword());
                String maChiNhanh = txtMaChiNhanh.getText(); // Get branch ID from the new field
                controller.login(maNhanVien, matKhau, maChiNhanh); // Pass branch ID to the controller
            }
        });

        // Add action listener for cancel button
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the application
            }
        });

        panel.add(btnLogin);
        panel.add(btnCancel);

        return panel;
    }

    public void showLoginSuccess(Employee employee) {
        // Logic to show successful login
        JOptionPane.showMessageDialog(this, "Đăng nhập thành công: " + employee.getHoTen());
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    // Method to set the controller
    public void setController(LoginController controller) {
        this.controller = controller;
    }
}
