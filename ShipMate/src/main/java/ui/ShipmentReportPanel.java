package ui;

import db.service.ReportService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;

public class ShipmentReportPanel extends JPanel {
    private final ReportService reportService;
    private JComboBox<Month> monthComboBox;
    private JComboBox<Integer> yearComboBox;
    private JButton generateBtn;
    private JTable reportTable;
    private JLabel metricsLabel;

    public ShipmentReportPanel(Connection connection) {
        this.reportService = new ReportService(connection);
        initComponents();
        setupLayout();
    }

    private void initComponents() {
        // Month selection
        monthComboBox = new JComboBox<>(Month.values());
        monthComboBox.setSelectedItem(LocalDate.now().getMonth());

        // Year selection
        yearComboBox = new JComboBox<>();
        int currentYear = LocalDate.now().getYear();
        for (int i = currentYear; i >= currentYear - 5; i--) {
            yearComboBox.addItem(i);
        }

        // Generate button
        generateBtn = new JButton("Generate Report");
        generateBtn.addActionListener(e -> generateReport());

        // Report table
        reportTable = new JTable();
        reportTable.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Driver Name", "Shipments Assigned"}
        ));

        // Metrics display
        metricsLabel = new JLabel("Select a month and year to generate report");
        metricsLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        controlPanel.add(new JLabel("Month:"));
        controlPanel.add(monthComboBox);
        controlPanel.add(new JLabel("Year:"));
        controlPanel.add(yearComboBox);
        controlPanel.add(generateBtn);

        // Table with scroll
        JScrollPane tableScroll = new JScrollPane(reportTable);
        tableScroll.setPreferredSize(new Dimension(600, 300));

        // Main layout
        add(controlPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
        add(metricsLabel, BorderLayout.SOUTH);
    }

    @SuppressWarnings("unchecked")
    private void generateReport() {
        try {
            Month month = (Month) monthComboBox.getSelectedItem();
            int year = (int) yearComboBox.getSelectedItem();

            Map<String, Object> report = reportService.generateMonthlyReport(year, month.getValue());
            List<Map<String, Object>> driverStats = (List<Map<String, Object>>) report.get("driverStats");

            // Update table
            DefaultTableModel model = (DefaultTableModel) reportTable.getModel();
            model.setRowCount(0);

            for (Map<String, Object> driver : driverStats) {
                model.addRow(new Object[]{
                        driver.get("driverName"),
                        driver.get("shipmentCount")
                });
            }

            // Update status
            metricsLabel.setText(String.format(
                    "Report for %s %d - %d drivers active",
                    month,
                    year,
                    driverStats.size()
            ));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error generating report: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}