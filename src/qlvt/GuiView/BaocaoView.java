package qlvt.GuiView;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import qlvt.connect.BaoCaoDao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Map;

public class BaocaoView extends JPanel {
    private BaoCaoDao baoCaoDao;

    public BaocaoView(MainView_IM mainViewIm) throws SQLException {
        // Khởi tạo DAO
        baoCaoDao = new BaoCaoDao();

        // Thiết lập layout chính
        setLayout(new BorderLayout());

        // Tiêu đề chính
        JLabel titleLabel = new JLabel("Thống Kê Báo Cáo", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(3, 166, 120));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        /*
        // button
        JButton ReportDeitail = new JButton("Chi tiêt");
        ReportDeitail.setFont(new Font("Arial", Font.BOLD, 24));
        ReportDeitail.setBackground(new Color(3, 166, 120));
        ReportDeitail.setForeground(new Color(249, 250, 249));
        ReportDeitail.setSize(50,30);

        ReportDeitail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create an instance of ReportGUI2 and show the dialog
                new ReportGUI2((Frame) SwingUtilities.getWindowAncestor(ReportDeitail)).setVisible(true);
                // This passes the parent frame to the ReportGUI2 constructor
            }
        });

         */




        //JPanel title + button
        JPanel Space = new JPanel();
        JPanel TilteButton = new JPanel(new GridLayout(1, 1, 10, 10));
        TilteButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        //TilteButton.add(Space);
        TilteButton.add(titleLabel);
        //TilteButton.add(ReportDeitail);

        add(TilteButton, BorderLayout.NORTH);

        // Bảng dashboard thông tin
        JPanel dashboardPanel = new JPanel(new GridLayout(1, 4, 10, 10)); // Sắp xếp 1 hàng, 3 cột với khoảng cách 10px
        dashboardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(dashboardPanel, BorderLayout.SOUTH); // Đặt vào phía dưới giao diện

        try {
            int totalVatTu = baoCaoDao.getTotalVatTu();
            Map<String, Integer> phieuNhapXuatCount = baoCaoDao.getPhieuNhapXuatCount();


            dashboardPanel.add(createInfoPanel("Tổng Số Vật Tư", String.valueOf(totalVatTu)));
            dashboardPanel.add(createInfoPanel("Số Phiếu Nhập", String.valueOf(phieuNhapXuatCount.getOrDefault("PhieuNhap", 0))));
            dashboardPanel.add(createInfoPanel("Số Phiếu Xuất", String.valueOf(phieuNhapXuatCount.getOrDefault("PhieuXuat", 0))));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }




        // Biểu đồ thống kê
        JPanel chartContainer = new JPanel(new GridLayout(2 , 2, 10, 10));
        chartContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        chartContainer.add(createChiPhiChart());
        chartContainer.add(createPhieuNhapXuatChart());
        chartContainer.add(createNhanVienTheoChiNhanhChart());
        //chartContainer.add(createTonKhoTheoKhoVaVatTuChart());
        chartContainer.add(createDoanhThuTheoThangChart());
        add(chartContainer, BorderLayout.CENTER);
    }

    // tạo biể

    // Tạo biểu đồ chi phí nhập vật tư theo nhà cung cấp
    private JPanel createChiPhiChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            Map<String, Double> data = baoCaoDao.getChiPhiNhapTheoNhaCungCap();
            for (Map.Entry<String, Double> entry : data.entrySet()) {
                dataset.addValue(entry.getValue(), "Chi phí", entry.getKey());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Chi Phí Nhập Theo Nhà Cung Cấp",
                "Nhà Cung Cấp",
                "Chi Phí (VNĐ)",
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(300, 300));
        chartPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        return chartPanel;
    }

    // Tạo biểu đồ số phiếu nhập và xuất
    private JPanel createPhieuNhapXuatChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            Map<String, Integer> data = baoCaoDao.getPhieuNhapXuatCount();
            dataset.addValue(data.getOrDefault("PhieuNhap", 0), "Phiếu Nhập", "Nhập");
            dataset.addValue(data.getOrDefault("PhieuXuat", 0), "Phiếu Xuất", "Xuất");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Số Phiếu Nhập và Xuất",
                "Loại Phiếu",
                "Số Lượng",
                dataset
        );

        CategoryPlot plot = barChart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 123, 255)); // Xanh dương
        renderer.setSeriesPaint(1, new Color(220, 53, 69)); // Đỏ

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(300, 300));
        chartPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        return chartPanel;
    }

    // biểu đồ về số lượng nhân viên theo chi nhánh
    private JPanel createNhanVienTheoChiNhanhChart() {
        // Dataset cho biểu đồ
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            // Lấy dữ liệu từ DAO
            Map<String, Integer> data = baoCaoDao.getNhanVienTheoChiNhanh();
            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                dataset.addValue(entry.getValue(), "Số lượng nhân viên", entry.getKey());
            }
        } catch (SQLException e) {
            // Xử lý lỗi khi lấy dữ liệu
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        // Tạo biểu đồ cột
        JFreeChart barChart = ChartFactory.createBarChart(
                "Số Lượng Nhân Viên Theo Chi Nhánh", // Tiêu đề biểu đồ
                "Chi Nhánh", // Trục X
                "Số Lượng Nhân Viên", // Trục Y
                dataset // Dữ liệu
        );

        // Tùy chỉnh biểu đồ
        CategoryPlot plot = barChart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 123, 255)); // Màu xanh dương cho cột

        // Đóng gói biểu đồ vào ChartPanel
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(300, 300)); // Kích thước biểu đồ
        chartPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // Viền cho biểu đồ

        return chartPanel; // Trả về JPanel chứa biểu đồ
    }

    // vat tu theo kho
    /*
    private JPanel createTonKhoTheoKhoVaVatTuChart() {
        // Dataset cho biểu đồ
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            // Lấy dữ liệu từ DAO
            Map<String, Map<String, Integer>> data = baoCaoDao.getTonKhoTheoKhoVaVatTu();

            // Duyệt qua dữ liệu và thêm vào dataset
            for (Map.Entry<String, Map<String, Integer>> khoEntry : data.entrySet()) {
                String tenKho = khoEntry.getKey();
                Map<String, Integer> vatTuData = khoEntry.getValue();

                // Thêm số lượng tồn của từng vật tư vào dataset
                for (Map.Entry<String, Integer> vatTuEntry : vatTuData.entrySet()) {
                    String tenVatTu = vatTuEntry.getKey();
                    int soLuongTon = vatTuEntry.getValue();

                    // Sử dụng tên kho + tên vật tư làm label trục X
                    dataset.addValue(soLuongTon, "Số lượng tồn", tenKho + " - " + tenVatTu);
                }
            }
        } catch (SQLException e) {
            // Xử lý lỗi khi lấy dữ liệu
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        // Tạo biểu đồ cột
        JFreeChart barChart = ChartFactory.createBarChart(
                "Số Lượng Tồn Kho Theo Kho Và Vật Tư", // Tiêu đề biểu đồ
                "Kho - Vật Tư", // Trục X
                "Số Lượng Tồn", // Trục Y
                dataset // Dữ liệu
        );

        // Tùy chỉnh biểu đồ
        CategoryPlot plot = barChart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 123, 255)); // Màu xanh dương cho cột

        // Đóng gói biểu đồ vào ChartPanel
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(300, 300)); // Kích thước biểu đồ
        chartPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // Viền cho biểu đồ

        return chartPanel; // Trả về JPanel chứa biểu đồ
    }

     */

    // doanh thu theo thang
    private JPanel createDoanhThuTheoThangChart() {
        // Dataset cho biểu đồ
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            // Lấy dữ liệu từ DAO
            Map<String, Double> data = baoCaoDao.getDoanhSoTheoThang();

            // Duyệt qua dữ liệu và thêm vào dataset
            for (Map.Entry<String, Double> entry : data.entrySet()) {
                // entry.getKey() đã có định dạng "Tháng X - Năm Y", do đó không cần thay đổi
                dataset.addValue(entry.getValue(), "Doanh Thu", entry.getKey());
            }
        } catch (SQLException e) {
            // Xử lý lỗi khi lấy dữ liệu
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        // Tạo biểu đồ cột
        JFreeChart barChart = ChartFactory.createBarChart(
                "Doanh Thu Theo Tháng",   // Tiêu đề biểu đồ
                "Tháng",                   // Trục X
                "Doanh Thu (VNĐ)",         // Trục Y
                dataset                    // Dữ liệu
        );

        // Tùy chỉnh biểu đồ
        CategoryPlot plot = barChart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 123, 255)); // Màu xanh dương cho cột

        // Cải thiện thẩm mỹ: Thêm lưới cho biểu đồ
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        // Tùy chỉnh font chữ cho trục X, Y và tiêu đề
        barChart.getCategoryPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 12));
        barChart.getCategoryPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 12));
        barChart.getTitle().setFont(new Font("Arial", Font.BOLD, 16));

        // Đóng gói biểu đồ vào ChartPanel
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(300, 300)); // Kích thước biểu đồ lớn hơn để hiển thị rõ hơn
        chartPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // Viền cho biểu đồ

        return chartPanel; // Trả về JPanel chứa biểu đồ
    }







    // Tạo panel hiển thị thông tin
    private JPanel createInfoPanel(String title, String value) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        panel.setPreferredSize(new Dimension(50, 50));
        panel.setBackground(new Color(3, 166, 120));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(249, 250, 249));

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        valueLabel.setForeground(new Color(249, 250, 249));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);

        return panel;
    }
}
