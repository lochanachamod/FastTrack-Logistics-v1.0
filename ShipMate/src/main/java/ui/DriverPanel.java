package ui;

import model.Driver;
import service.DriverService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DriverPanel extends JPanel {
    private DriverService driverService = new DriverService();
    private JTable driverTable;
    private DefaultTableModel tableModel;

    private JTextField nameField = new JTextField();
    private JTextField phoneField = new JTextField();
    private JTextField scheduleField = new JTextField();
    private JButton addButton = new JButton("Add");
    private JButton updateButton = new JButton("Update");
    private JButton deleteButton = new JButton("Delete");

    private int selectedDriverId = -1;

    public DriverPanel() {
        setLayout(new BorderLayout());

        // === LEFT PANEL: Form ===
        JPanel formPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Driver Details"));

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Phone:"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("Schedule:"));
        formPanel.add(scheduleField);

        formPanel.add(addButton);
        formPanel.add(updateButton);
        formPanel.add(deleteButton);

        // === RIGHT PANEL: Table ===
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Phone", "Schedule"}, 0);
        driverTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(driverTable);

        loadDrivers();

        // === Split layout ===
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, tableScroll);
        splitPane.setDividerLocation(300);
        add(splitPane, BorderLayout.CENTER);

        JButton exportButton = new JButton("📤 Export Drivers to CSV");
        add(exportButton, BorderLayout.NORTH);

        exportButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Drivers CSV");
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    java.io.File file = fileChooser.getSelectedFile();
                    java.io.PrintWriter pw = new java.io.PrintWriter(file);

                    for (int i = 0; i < tableModel.getColumnCount(); i++) {
                        pw.print(tableModel.getColumnName(i));
                        if (i < tableModel.getColumnCount() - 1) pw.print(",");
                    }
                    pw.println();

                    for (int row = 0; row < tableModel.getRowCount(); row++) {
                        for (int col = 0; col < tableModel.getColumnCount(); col++) {
                            pw.print(tableModel.getValueAt(row, col));
                            if (col < tableModel.getColumnCount() - 1) pw.print(",");
                        }
                        pw.println();
                    }

                    pw.close();
                    JOptionPane.showMessageDialog(this, "Drivers exported successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Export failed: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });



        // === Button listeners ===
        addButton.addActionListener(e -> {
            Driver driver = new Driver(0, nameField.getText(), phoneField.getText(), scheduleField.getText());
            driverService.addDriver(driver);
            clearForm();
            loadDrivers();
        });

        updateButton.addActionListener(e -> {
            if (selectedDriverId != -1) {
                Driver updatedDriver = new Driver(selectedDriverId, nameField.getText(), phoneField.getText(), scheduleField.getText());
                driverService.updateDriver(updatedDriver);
                clearForm();
                loadDrivers();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a driver to update.");
            }
        });

        deleteButton.addActionListener(e -> {
            if (selectedDriverId != -1) {
                driverService.deleteDriver(selectedDriverId);
                clearForm();
                loadDrivers();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a driver to delete.");
            }
        });

        driverTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && driverTable.getSelectedRow() != -1) {
                int row = driverTable.getSelectedRow();
                selectedDriverId = (int) tableModel.getValueAt(row, 0);
                nameField.setText((String) tableModel.getValueAt(row, 1));
                phoneField.setText((String) tableModel.getValueAt(row, 2));
                scheduleField.setText((String) tableModel.getValueAt(row, 3));
            }
        });
    }

    private void loadDrivers() {
        tableModel.setRowCount(0);
        List<Driver> drivers = driverService.getAllDrivers();
        for (Driver d : drivers) {
            tableModel.addRow(new Object[]{d.getId(), d.getName(), d.getPhone(), d.getSchedule()});
        }
    }

    private void clearForm() {
        selectedDriverId = -1;
        nameField.setText("");
        phoneField.setText("");
        scheduleField.setText("");
        driverTable.clearSelection();
    }
}
