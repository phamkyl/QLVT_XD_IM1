package qlvt.GuiView;

import qlvt.model.ReportData;
import qlvt.connect.report_thongkeDao;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportGUI2 extends JDialog {
    private JLabel lblTitle, lblDate, lblReporter, lblScope;
    private JLabel lblTotalRevenue, lblTotalCost, lblGrossProfit, lblNetProfit;
    private JLabel lblRevenueByBranch, lblRevenueByProduct, lblRevenueByCustomer;
    private JLabel lblStockRemaining, lblLongestStock;
    private JButton btnExportPDF, btnExportExcel;

    private report_thongkeDao reportDao;
    private ReportData reportData;

    public ReportGUI2(Frame parent) {
        reportDao = new report_thongkeDao();
        initialize();
    }

    private void initialize() {
        setTitle("Báo Cáo Kết Quả Kinh Doanh");
        setSize(800, 600);  // Adjust size as necessary
        setLocationRelativeTo(null);  // Center dialog on screen
        setLayout(new BorderLayout());

        JPanel headerPanel = createHeaderPanel();
        JPanel contentPanel = createContentPanel();
        JPanel footerPanel = createFooterPanel();

        // Title at the top
        JLabel titleLabel = new JLabel("Thống Kê Báo Cáo", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(3, 166, 120));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Add components
        add(titleLabel, BorderLayout.NORTH);
        add(headerPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        // Make dialog visible
        setVisible(true);

        // Fetch and display the report data
        loadReportData();
    }

    private void loadReportData() {
        try {
            // Fetch the report data from the DAO
            reportData = reportDao.getReportData();

            // Update the content labels with data
            lblTotalRevenue.setText("Tổng doanh thu: " + reportData.getTotalRevenue());
            lblTotalCost.setText("Tổng chi phí: " + reportData.getTotalCost());
           // lblGrossProfit.setText("Lợi nhuận gộp: " + reportData.getGrossProfit());
            //lblNetProfit.setText("Lợi nhuận ròng: " + reportData.getNetProfit());

            // Display revenue by branch, product, customer, and other relevant data
            lblRevenueByBranch.setText("Doanh thu theo chi nhánh: " + reportData.getRevenueByBranch());
            lblRevenueByProduct.setText("Doanh thu theo loại sản phẩm: " + reportData.getRevenueByProduct());
            lblRevenueByCustomer.setText("Doanh thu theo khách hàng: " + reportData.getRevenueByCustomer());
            lblStockRemaining.setText("Số lượng hàng tồn kho: " + reportData.getStockRemaining());
            lblLongestStock.setText("Sản phẩm tồn kho lâu nhất: " + reportData.getLongestStockProduct());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không thể tải báo cáo: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createTitledBorder("Thông Tin Chung"));

        lblTitle = createHeaderLabel("Báo cáo kết quả kinh doanh");
        lblDate = createHeaderLabel("Ngày lập báo cáo: " + getCurrentDate());
        lblReporter = createHeaderLabel("Người lập báo cáo: " + "Chưa xác định");
        lblScope = createHeaderLabel("Phạm vi báo cáo: Toàn công ty");

        headerPanel.add(lblTitle);
        headerPanel.add(lblDate);
        headerPanel.add(lblReporter);
        headerPanel.add(lblScope);

        return headerPanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createTitledBorder("Nội Dung Báo Cáo"));

        lblTotalRevenue = createContentLabel("Tổng doanh thu: 0");
        lblTotalCost = createContentLabel("Tổng chi phí: 0");
        lblGrossProfit = createContentLabel("Lợi nhuận gộp: 0");
        lblNetProfit = createContentLabel("Lợi nhuận ròng: 0");
        lblRevenueByBranch = createContentLabel("Doanh thu theo chi nhánh: Đang xử lý...");
        lblRevenueByProduct = createContentLabel("Doanh thu theo loại sản phẩm: Đang xử lý...");
        lblRevenueByCustomer = createContentLabel("Doanh thu theo khách hàng: Đang xử lý...");
        lblStockRemaining = createContentLabel("Số lượng hàng tồn kho: Đang xử lý...");
        lblLongestStock = createContentLabel("Sản phẩm tồn kho lâu nhất: Đang xử lý...");

        contentPanel.add(lblTotalRevenue);
        contentPanel.add(lblTotalCost);
        contentPanel.add(lblGrossProfit);
        contentPanel.add(lblNetProfit);
        contentPanel.add(lblRevenueByBranch);
        contentPanel.add(lblRevenueByProduct);
        contentPanel.add(lblRevenueByCustomer);
        contentPanel.add(lblStockRemaining);
        contentPanel.add(lblLongestStock);

        return contentPanel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        btnExportPDF = new JButton("Xuất PDF");
        btnExportExcel = new JButton("Xuất Excel");

        // Add action listeners for the buttons
        btnExportPDF.addActionListener(e -> exportToPDF());
        btnExportExcel.addActionListener(e -> exportToExcel());

        footerPanel.add(btnExportPDF);
        footerPanel.add(btnExportExcel);

        return footerPanel;
    }

    private void exportToPDF() {
        // Implement PDF export functionality here
        JOptionPane.showMessageDialog(this, "Xuất báo cáo dưới dạng PDF");
    }

    private void exportToExcel() {
        // Implement Excel export functionality here
        JOptionPane.showMessageDialog(this, "Xuất báo cáo dưới dạng Excel");
    }

    private JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JLabel createContentLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(new Date());
    }
}
