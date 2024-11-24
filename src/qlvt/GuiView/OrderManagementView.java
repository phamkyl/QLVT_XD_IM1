package qlvt.GuiView;

import qlvt.connect.OrderDAO;
import qlvt.model.ChiTietDonHang;
import qlvt.model.Order;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class OrderManagementView extends JPanel {

    // Order Fields
    private JTextField txtMaDonHang, txtMaKhachHang, txtNgayDat, txtTinhTrangDonHang;
    private JButton btnAdd, btnUpdate, btnDelete, btnClose;
    private JTable orderTable, detailTable;
    private DefaultTableModel orderTableModel, detailTableModel;
    private OrderDAO orderDAO;

    // Detail Fields
    private JTextField txtMaChiTiet, txtMaVatTu, txtSoLuong, txtGia;
    private JButton btnAddDetail, btnUpdateDetail, btnDeleteDetail;
    private JButton btnGenerateInvoice;

    public OrderManagementView(MainView_IM mainViewIm) {
        orderDAO = new OrderDAO();
        initUI();
        loadOrders();
        setupTableSelectionListener();
        setPreferredSize(new Dimension(800, 600));
    }

    private void initUI() {
        // Initialize Order Fields
        txtMaDonHang = new JTextField(15);
        txtMaKhachHang = new JTextField(15);
        txtNgayDat = new JTextField(15); // Date input as text
        txtTinhTrangDonHang = new JTextField(15);

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.add(new JLabel("Mã Đơn Hàng:"));
        inputPanel.add(txtMaDonHang);
        inputPanel.add(new JLabel("Mã Khách Hàng:"));
        inputPanel.add(txtMaKhachHang);
        inputPanel.add(new JLabel("Ngày Đặt (YYYY-MM-DD):"));
        inputPanel.add(txtNgayDat);
        inputPanel.add(new JLabel("Tình Trạng Đơn Hàng:"));
        inputPanel.add(txtTinhTrangDonHang);

        // Initialize Buttons
        btnAdd = new JButton("Thêm");
        btnUpdate = new JButton("Cập Nhật");
        btnDelete = new JButton("Xóa");
        btnClose = new JButton("Đóng");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClose);

        // Action Listeners for Order Buttons
        btnAdd.addActionListener(e -> addOrder());
        btnUpdate.addActionListener(e -> updateOrder());
        btnDelete.addActionListener(e -> deleteOrder());

        // Initialize Order Table
        orderTableModel = new DefaultTableModel(new Object[]{"Mã Đơn Hàng", "Mã Khách Hàng", "Ngày Đặt", "Tình Trạng Đơn Hàng"}, 0);
        orderTable = new JTable(orderTableModel);

        // Initialize Detail Table
        detailTableModel = new DefaultTableModel(new Object[]{"Mã Chi Tiết", "Mã Vật Tư", "Số Lượng", "Giá"}, 0);
        detailTable = new JTable(detailTableModel);

        // Initialize Detail Fields
        txtMaChiTiet = new JTextField(15);
        txtMaVatTu = new JTextField(15);
        txtSoLuong = new JTextField(15);
        txtGia = new JTextField(15);

        JPanel detailInputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        detailInputPanel.add(new JLabel("Mã đơn hàng:"));
        detailInputPanel.add(txtMaChiTiet);
        detailInputPanel.add(new JLabel("Mã Vật Tư:"));
        detailInputPanel.add(txtMaVatTu);
        detailInputPanel.add(new JLabel("Số Lượng:"));
        detailInputPanel.add(txtSoLuong);
        detailInputPanel.add(new JLabel("Giá:"));
        detailInputPanel.add(txtGia);

        // Detail Buttons
        btnAddDetail = new JButton("Thêm Chi Tiết");
        btnUpdateDetail = new JButton("Cập Nhật Chi Tiết");
        btnDeleteDetail = new JButton("Xóa Chi Tiết");

        for (JButton button : new JButton[]{btnAddDetail, btnUpdateDetail, btnDeleteDetail,
                btnAdd,btnUpdate,btnDelete,btnClose}) {
            button.setBackground(new Color(3, 166, 120));
            button.setForeground(Color.WHITE);
            button.setSize(100, 60);
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.setFocusPainted(false);
        }

        JPanel detailButtonPanel = new JPanel();
        detailButtonPanel.add(btnAddDetail);
        detailButtonPanel.add(btnUpdateDetail);
        detailButtonPanel.add(btnDeleteDetail);


        // Action Listeners for Detail Buttons
        btnAddDetail.addActionListener(e -> addDetail());
        btnUpdateDetail.addActionListener(e -> updateDetail());
        btnDeleteDetail.addActionListener(e -> deleteDetail());

        // Detail Panel Layout
        JPanel detailPanel = new JPanel(new BorderLayout());
        detailPanel.add(detailInputPanel, BorderLayout.NORTH);
        detailPanel.add(new JScrollPane(detailTable), BorderLayout.CENTER);
        detailPanel.add(detailButtonPanel, BorderLayout.SOUTH);

        // Layout Main Panel
        setLayout(new BorderLayout());
        add(new JScrollPane(orderTable), BorderLayout.CENTER);
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
        add(detailPanel, BorderLayout.EAST);

        // Generate Invoice Button
        btnGenerateInvoice = new JButton("Hóa đơn");
        btnGenerateInvoice.setBackground(new Color(3, 166, 120));
        btnGenerateInvoice.setForeground(Color.WHITE);
        buttonPanel.add(btnGenerateInvoice);
        btnGenerateInvoice.addActionListener(e -> generateInvoice());
    }

    private void loadOrders() {
        orderTableModel.setRowCount(0);
        List<Order> orders = orderDAO.getAllOrders();
        for (Order order : orders) {
            orderTableModel.addRow(new Object[]{
                    order.getMaDonHang(),
                    order.getMaKhachHang(),
                    order.getNgayDat(),
                    order.getTinhTrangDonHang()
            });
        }
    }

    private void setupTableSelectionListener() {
        orderTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = orderTable.getSelectedRow();
                if (selectedRow != -1) {
                    // Populate text fields with the selected row's data
                    txtMaDonHang.setText(orderTableModel.getValueAt(selectedRow, 0).toString());
                    txtMaKhachHang.setText(orderTableModel.getValueAt(selectedRow, 1).toString());
                    txtNgayDat.setText(orderTableModel.getValueAt(selectedRow, 2).toString());
                    txtTinhTrangDonHang.setText(orderTableModel.getValueAt(selectedRow, 3).toString());

                    // Load order details
                    loadOrderDetails(Integer.parseInt(txtMaDonHang.getText()));
                }
            }
        });
    }

    private void loadOrderDetails(int maDonHang) {
        detailTableModel.setRowCount(0);
        List<ChiTietDonHang> details = orderDAO.getOrderDetailsById(maDonHang);
        for (ChiTietDonHang detail : details) {
            detailTableModel.addRow(new Object[]{
                    detail.getMaDonHang(),
                    detail.getMaVatTu(),
                    detail.getSoLuong(),
                    detail.getGia()
            });
        }
    }

    private void addOrder() {
        Order order = new Order(
                Integer.parseInt(txtMaDonHang.getText()),
                Integer.parseInt(txtMaKhachHang.getText()),
                Date.valueOf(txtNgayDat.getText()),
                txtTinhTrangDonHang.getText()
        );
        orderDAO.addOrder(order);
        loadOrders();
    }

    private void updateOrder() {
        Order order = new Order(
                Integer.parseInt(txtMaDonHang.getText()),
                Integer.parseInt(txtMaKhachHang.getText()),
                Date.valueOf(txtNgayDat.getText()),
                txtTinhTrangDonHang.getText()
        );
        orderDAO.updateOrder(order);
        loadOrders();
    }

    private void deleteOrder() {
        int maDonHang = Integer.parseInt(txtMaDonHang.getText());
        orderDAO.deleteOrder(maDonHang);
        loadOrders();
        detailTableModel.setRowCount(0); // Clear details when order is deleted
    }

    private void addDetail() {
        ChiTietDonHang detail = new ChiTietDonHang(
                Integer.parseInt(txtMaDonHang.getText()),
                Integer.parseInt(txtMaVatTu.getText()),
                Integer.parseInt(txtSoLuong.getText()),
                new BigDecimal(txtGia.getText())
        );
        orderDAO.addDetail(detail);
        loadOrderDetails(Integer.parseInt(txtMaDonHang.getText()));
    }

    private void updateDetail() {
        ChiTietDonHang detail = new ChiTietDonHang(
                Integer.parseInt(txtMaDonHang.getText()),
                Integer.parseInt(txtMaVatTu.getText()),
                Integer.parseInt(txtSoLuong.getText()),
                new BigDecimal(txtGia.getText())
        );
        orderDAO.updateDetail(detail);
        loadOrderDetails(Integer.parseInt(txtMaDonHang.getText()));
    }

    private void deleteDetail() {
        int maDonHang = Integer.parseInt(txtMaDonHang.getText());
        int maVatTu = Integer.parseInt(txtMaVatTu.getText());
        orderDAO.deleteDetail(maDonHang, maVatTu);
        loadOrderDetails(maDonHang);
    }

    private void generateInvoice() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow != -1) {
            int maDonHang = Integer.parseInt(orderTableModel.getValueAt(selectedRow, 0).toString());

            // Create an instance of InvoiceView with the selected order ID (maDonHang)
            InvoiceView invoiceView = new InvoiceView(maDonHang);  // Since InvoiceView is already a JDialog

            invoiceView.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            invoiceView.pack();
            invoiceView.setLocationRelativeTo(this);  // Center the dialog
            invoiceView.setVisible(true);  // Show the InvoiceView dialog
        }
    }



}
