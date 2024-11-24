package qlvt.GuiView;

import qlvt.connect.PhieuXuatDAO;
import qlvt.connect.PurchaseOrderDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MainView_IM extends JFrame {
    private String userRole;
    private String userName;
    private int maChiNhanh; // ID chi nhánh
    private JPanel panel;

    // Constructor để khởi tạo MainView với thông tin người dùng
    public MainView_IM(String userRole, String userName, int maChiNhanh) {
        this.userRole = userRole;
        this.userName = userName;
        this.maChiNhanh = maChiNhanh; // Gán ID chi nhánh
        initialize(); // Gọi phương thức khởi tạo giao diện
    }

    public MainView_IM() {
        initialize();
    }

    private void initialize() {
        setTitle("Giao Diện Chính");
        setSize(1280, 720); // Kích thước cửa sổ
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        // Tạo panel bên trái: chứa thông tin cá nhân và các nút chức năng
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);

        // Phần thông tin cá nhân
        // Tạo JPanel chứa thông tin cá nhân
        JPanel personalInfoPanel = new JPanel();
        personalInfoPanel.setLayout(new BoxLayout(personalInfoPanel, BoxLayout.Y_AXIS)); // Sắp xếp dọc
        personalInfoPanel.setBackground(Color.WHITE);
        personalInfoPanel.setBorder(BorderFactory.createTitledBorder("Thông tin cá nhân"));
        /*
        // Tạo icon ảnh đại diện với kích thước nhỏ hơn
        ImageIcon icon = new ImageIcon("image/account.png");
        Image img = icon.getImage(); // Chuyển đổi ảnh gốc thành đối tượng Image
        Image scaledImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Giảm kích thước ảnh xuống 50x50
        icon = new ImageIcon(scaledImg); // Tạo lại ImageIcon với ảnh đã được thu nhỏ

        // Tạo JLabel chứa icon ảnh đại diện
        JLabel imageAccount = new JLabel(icon);
        imageAccount.setHorizontalAlignment(JLabel.CENTER); // Canh giữa hình ảnh
        personalInfoPanel.add(imageAccount);

         */

        // Tạo JPanel cho dòng chứa thông tin
        JPanel infoRowPanel = new JPanel();
        infoRowPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Căn chỉnh từ trái sang phải
        infoRowPanel.setBackground(Color.WHITE);

        // Dữ liệu cá nhân
        Object[][] personalInfo = {
                {"Tên", userName},
                {"Vai trò", userRole},
                {"Chi nhánh", String.valueOf(maChiNhanh)}
        };

        // Tạo và thêm các cặp JLabel vào panel
        for (Object[] row : personalInfo) {
            JPanel infoRow = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Từng dòng thông tin
            infoRow.setBackground(Color.WHITE);

            JLabel labelKey = new JLabel(row[0] + ": ");
            labelKey.setFont(new Font("Arial", Font.BOLD, 14));
            labelKey.setForeground(new Color(50, 50, 50));

            JLabel labelValue = new JLabel(row[1].toString());
            labelValue.setFont(new Font("Arial", Font.PLAIN, 14));
            labelValue.setForeground(new Color(80, 80, 80));

            infoRow.add(labelKey);
            infoRow.add(labelValue);

            infoRowPanel.add(infoRow);
        }

// Thêm thông tin vào panel chính
        personalInfoPanel.add(infoRowPanel);


        // Phần các nút chức năng
        JPanel optionsPanel = new JPanel(new GridLayout(7, 1, 10, 10));
        optionsPanel.setBackground(Color.WHITE);
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //
        // Thêm các nút chức năng vào optionsPanel dựa trên phân quyền
        showOptions(optionsPanel);

        // Thêm các thành phần vào leftPanel
        leftPanel.add(personalInfoPanel, BorderLayout.NORTH);
        leftPanel.add(optionsPanel, BorderLayout.CENTER);
       // leftPanel.add()

        // Tạo panel bên phải: hiển thị các chức năng chi tiết
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.LIGHT_GRAY);

        JLabel defaultLabel = new JLabel("Chọn một chức năng từ bên trái để hiển thị chi tiết", JLabel.CENTER);
        defaultLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        rightPanel.add(defaultLabel, BorderLayout.CENTER);

        // Thêm sự kiện nút để cập nhật giao diện bên phải
        addButtonEvents(optionsPanel, rightPanel);

        // Sử dụng JSplitPane để chia giao diện
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(400); // Đặt vị trí thanh chia
        splitPane.setDividerSize(0); // Độ dày thanh chia
        splitPane.setOneTouchExpandable(false); // Bật nút mở rộng/thu nhỏ

        // Thêm splitPane vào JFrame
        add(splitPane);
    }

    // Phương thức thêm các nút chức năng vào optionsPanel dựa trên phân quyền
    public void showOptions(JPanel optionsPanel) {
        Map<String, String[]> roleOptions = new HashMap<>();
        roleOptions.put("admin", new String[]{"QL nhân viên", "QL chi nhánh", "QL kho", "QL vật tư",
                "QL nhà cung cấp", "QL khách hàng", "QL đơn hàng","QL Nhap-xuat", "QL BC_TK"});
        roleOptions.put("manager", new String[]{"QL kho", "QL vật tư", "QL khách hàng", "QL đơn hàng"});
        roleOptions.put("employee", new String[]{"QL nhân viên", "QL chi nhánh", "QL kho", "QL vật tư",
                "QL nhà cung cấp", "QL khách hàng", "QL đơn hàng","QL Nhap-xuat", "QL BC_TK"});

        // Map icon path với chức năng
        Map<String, String> iconPaths = new HashMap<>();
        iconPaths.put("QL nhân viên", "image/team.png");
        iconPaths.put("QL chi nhánh", "image/organization.png");
        iconPaths.put("QL kho", "image/warehouse.png");
        iconPaths.put("QL vật tư", "image/supplies.png");
        iconPaths.put("QL nhà cung cấp", "image/manufacture.png");
        iconPaths.put("QL khách hàng", "image/customer-loyalty.png");
        iconPaths.put("QL đơn hàng", "image/cargo.png");
        iconPaths.put("QL Nhap-xuat","image/export.png");
        iconPaths.put("QL BC_TK", "image/report.png");

        if (userRole != null && roleOptions.containsKey(userRole)) {
            String[] options = roleOptions.get(userRole);
            for (String option : options) {
                JButton button = new JButton(option);

                // Gán font chữ và màu sắc
                button.setFont(new Font("Arial", Font.PLAIN, 14));
                button.setBackground(new Color(3, 166, 120));
                button.setForeground(Color.WHITE);

                // Gán icon nếu có
                if (iconPaths.containsKey(option)) {
                    String iconPath = iconPaths.get(option);
                    try {
                        ImageIcon icon = new ImageIcon(iconPath);
                        Image scaledIcon = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                        button.setIcon(new ImageIcon(scaledIcon));
                    } catch (Exception e) {
                        System.err.println("Không thể tải icon cho chức năng: " + option);
                    }
                }

                // Thêm nút vào panel
                optionsPanel.add(button);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vai trò không hợp lệ!");
        }
    }


    // Thêm sự kiện cho các nút trong optionsPanel
    private void addButtonEvents(JPanel optionsPanel, JPanel rightPanel) {
        for (Component comp : optionsPanel.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                button.addActionListener(e -> {
                    rightPanel.removeAll();  // Xóa hết các component hiện tại trong rightPanel
                    String buttonText = button.getText(); // Lấy tên của nút chức năng

                    switch (buttonText) {
                        case "QL chi nhánh":
                            openBranchManagementView(rightPanel);
                            break;
                        case "QL nhân viên":
                            if (!userRole.equals("admin")) {
                                JOptionPane.showMessageDialog(this, "Bạn không có quyền truy cập chức năng này!");
                                return;
                            }
                            openEmployeeManagementView(rightPanel);
                            break;
                        case "QL vật tư" :
                            openMaterialManagementView(rightPanel);
                            break;
                        case "QL nhà cung cấp":
                            openSupplierManagementView(rightPanel);
                            break;
                        case "QL khách hàng":
                            try {
                                openCustomerManagementView(rightPanel);
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                            break;
                        case "QL kho":
                            try {
                                WarehouseManagementView(rightPanel);
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                            break;
                        case "QL đơn hàng":
                            openOrderManagementView(rightPanel);
                            break;
                        case "QL Nhap-xuat":
                            OpenImportExportView(rightPanel);
                            break;
                        case "QL BC_TK":
                            try {
                                OpenREPORTSTATISTICS(rightPanel);
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }

                            break;


                        default:
                            JLabel functionLabel = new JLabel("Chức năng: " + buttonText, JLabel.CENTER);
                            functionLabel.setFont(new Font("Arial", Font.BOLD, 18));
                            rightPanel.add(functionLabel, BorderLayout.CENTER);
                            break;
                    }

                    rightPanel.revalidate(); // Cập nhật lại giao diện của rightPanel
                    rightPanel.repaint();    // Vẽ lại rightPanel
                });
            }
        }
    }



 // chi nhanh
  private void openBranchManagementView(JPanel rightPanel) {
      // Tạo mới view quản lý chi nhánh
      BranchManagementView branchManagementView = new BranchManagementView(this, maChiNhanh);
      branchManagementView.setVisible(true);

      // Tạo JScrollPane để cuộn nếu nội dung quá lớn
      JScrollPane scrollPane = new JScrollPane(branchManagementView);

      // Đặt kích thước cho JScrollPane
      scrollPane.setPreferredSize(new Dimension(rightPanel.getWidth(), 700));

      // Cập nhật rightPanel với scrollPane mới
      updateRightPanelWithView(rightPanel, scrollPane);
  }

  // vat tu
    private void openMaterialManagementView(JPanel rightPanel) {
        MaterialManagementView materialManagementView = new MaterialManagementView(this);
        materialManagementView.setVisible(true);
        // Tạo JScrollPane để cuộn nếu nội dung quá lớn
        JScrollPane scrollPane = new JScrollPane(materialManagementView);

        // Đặt kích thước cho JScrollPane
        scrollPane.setPreferredSize(new Dimension(rightPanel.getWidth(), 700));

        updateRightPanelWithView(rightPanel, scrollPane);
    }
// nha cung cap
    private void openSupplierManagementView(JPanel rightPanel) {
        // Tạo một SupplierManagementView mới
        SupplierManagementView supplierManagementView = new SupplierManagementView(this);
        supplierManagementView.setVisible(true);


        // Tạo JScrollPane chứa SupplierManagementView để cuộn khi cần thiết
        JScrollPane scrollPane = new JScrollPane(supplierManagementView);

        // Đặt kích thước cho JScrollPane
        scrollPane.setPreferredSize(new Dimension(rightPanel.getWidth(), 600)); // Kích thước tùy chỉnh, có thể thay đổi

        // Cập nhật lại rightPanel với JScrollPane chứa SupplierManagementView
        updateRightPanelWithView(rightPanel, scrollPane);
    }

    // nhan vien
    private void openEmployeeManagementView(JPanel rightPanel) {
        EmployeeManagementView employeeManagementView = new EmployeeManagementView(this, maChiNhanh); // Pass branch ID
        employeeManagementView.setVisible(true);
        // Tạo JScrollPane chứa SupplierManagementView để cuộn khi cần thiết
        JScrollPane scrollPane = new JScrollPane(employeeManagementView);

        // Đặt kích thước cho JScrollPane
        scrollPane.setPreferredSize(new Dimension(rightPanel.getWidth(), 600)); // Kích thước tùy chỉnh, có thể thay đổi

        updateRightPanelWithView(rightPanel, scrollPane);

    }


    // khach hang
    private void openCustomerManagementView(JPanel rightPanel) throws SQLException {
        // Tạo giao diện quản lý khách hàng
        CustomerManagementView customerManagementView = new CustomerManagementView(this);
        customerManagementView.setVisible(true);

        // Sử dụng JScrollPane thay vì ScrollPane
        JScrollPane scrollPane = new JScrollPane(customerManagementView);

        // Đặt kích thước cho JScrollPane
        scrollPane.setPreferredSize(new Dimension(rightPanel.getWidth(), 600)); // Kích thước tùy chỉnh

        // Cập nhật lại rightPanel với JScrollPane chứa CustomerManagementView
        updateRightPanelWithView(rightPanel, scrollPane);
    }

    private void WarehouseManagementView(JPanel rightPanel) throws SQLException {
        // Tạo giao diện quản lý khách hàng
        WarehouseManagementView WarehouseManagementView = new WarehouseManagementView(this,maChiNhanh);
        WarehouseManagementView.setVisible(true);

        // Sử dụng JScrollPane thay vì ScrollPane
        JScrollPane scrollPane = new JScrollPane(WarehouseManagementView);

        // Đặt kích thước cho JScrollPane
        scrollPane.setPreferredSize(new Dimension(rightPanel.getWidth(), 600)); // Kích thước tùy chỉnh

        // Cập nhật lại rightPanel với JScrollPane chứa CustomerManagementView
        updateRightPanelWithView(rightPanel, scrollPane);
    }




    private void openOrderManagementView(JPanel rightPanel) {
        OrderManagementView orderManagementView = new OrderManagementView(this);
        orderManagementView.setVisible(true);
        JScrollPane scrollPane = new JScrollPane(orderManagementView);

        // Đặt kích thước cho JScrollPane
        scrollPane.setPreferredSize(new Dimension(rightPanel.getWidth(), 600)); // Kích thước tùy chỉnh

        // Cập nhật lại rightPanel với JScrollPane chứa CustomerManagementView
        updateRightPanelWithView(rightPanel, scrollPane);
    }

    private void OpenImportExportView(JPanel rightPanel) {
        // Tạo ImportExportView, truyền đúng đối tượng cần thiết
        PurchaseOrderDAO purchaseOrderDAO = new PurchaseOrderDAO();  // Khởi tạo DAO
        PhieuXuatDAO phieuXuatDAO = new PhieuXuatDAO();  // Khởi tạo DAO
        ImportExportView importExportView = new ImportExportView(this, purchaseOrderDAO, phieuXuatDAO);

        // Đặt ImportExportView vào JScrollPane
        JScrollPane scrollPane = new JScrollPane(importExportView);

        // Đặt kích thước cho JScrollPane (có thể thay đổi theo nhu cầu)
        scrollPane.setPreferredSize(new Dimension(rightPanel.getWidth(), 600));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);  // Hiển thị thanh cuộn dọc nếu cần

        // Đảm bảo rằng rightPanel sẽ được cập nhật với JScrollPane mới
        updateRightPanelWithView(rightPanel, scrollPane);
    }

    private void OpenREPORTSTATISTICS(JPanel rightPanel) throws SQLException {
        // Create the BaocaoView instance
        BaocaoView baocaoView = new BaocaoView(this);

        // Make the BaocaoView visible
        baocaoView.setVisible(true);

        // Create a JScrollPane to make the BaocaoView scrollable
        JScrollPane scrollPane = new JScrollPane(baocaoView);

        // Set the preferred size for the scroll pane
        scrollPane.setPreferredSize(new Dimension(rightPanel.getWidth(), 600));

        // Enable vertical scrolling and horizontal scrolling if needed
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Update the right panel with the scrollable view
        updateRightPanelWithView(rightPanel, scrollPane);
    }

    private void OpenDefaut(JPanel rightPanel) throws SQLException {
        // Create the BaocaoView instance
        BaocaoView baocaoView = new BaocaoView(this);

        // Make the BaocaoView visible
        baocaoView.setVisible(true);

        // Create a JScrollPane to make the BaocaoView scrollable
        JScrollPane scrollPane = new JScrollPane(baocaoView);

        // Set the preferred size for the scroll pane
        scrollPane.setPreferredSize(new Dimension(rightPanel.getWidth(), 600));

        // Enable vertical scrolling and horizontal scrolling if needed
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Update the right panel with the scrollable view
        updateRightPanelWithView(rightPanel, scrollPane);
    }








    private void updateRightPanelWithView(JPanel rightPanel, JScrollPane newView) {
        // Đặt layout phù hợp cho rightPanel (nếu chưa có)
        if (!(rightPanel.getLayout() instanceof BorderLayout)) {
            rightPanel.setLayout(new BorderLayout());
        }

        // Xóa tất cả các thành phần cũ
        rightPanel.removeAll();

        // Thêm JScrollPane vào rightPanel
        rightPanel.add(newView, BorderLayout.CENTER);

        // Làm mới giao diện
        rightPanel.revalidate();
        rightPanel.repaint();

    }





}
