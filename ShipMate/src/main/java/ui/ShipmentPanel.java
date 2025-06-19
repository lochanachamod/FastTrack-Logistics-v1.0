package ui;

import model.Driver;
import model.Shipment;
import service.DriverService;
import service.ShipmentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class ShipmentPanel extends JPanel {
    private ShipmentService shipmentService = new ShipmentService();
    private DriverService driverService = new DriverService();
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> driverCombo;
    private List<Driver> drivers;

    public ShipmentPanel() {
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"ID", "Sender", "Receiver", "Contents", "Status", "Location", "Date", "Slot", "Driver ID"}, 0);
        table = new JTable(tableModel);
        loadShipments();

        JPanel formPanel = new JPanel(new GridLayout(10, 2));
        JTextField senderField = new JTextField();
        JTextField receiverField = new JTextField();
        JTextField contentsField = new JTextField();
        JTextField statusField = new JTextField("Scheduled");
        JTextField locationField = new JTextField("Dispatch Center");
        JTextField dateField = new JTextField("yyyy-MM-dd");
        JComboBox<String> slotCombo = new JComboBox<>(new String[]{"Morning", "Afternoon", "Evening"});

        driverCombo = new JComboBox<>();
        loadDrivers();

        JButton addButton = new JButton("Add Shipment");

        formPanel.add(new JLabel("Sender:"));
        formPanel.add(senderField);
        formPanel.add(new JLabel("Receiver:"));
        formPanel.add(receiverField);
        formPanel.add(new JLabel("Contents:"));
        formPanel.add(contentsField);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusField);
        formPanel.add(new JLabel("Current Location:"));
        formPanel.add(locationField);
        formPanel.add(new JLabel("Delivery Date:"));
        formPanel.add(dateField);
        formPanel.add(new JLabel("Delivery Slot:"));
        formPanel.add(slotCombo);
        formPanel.add(new JLabel("Assign Driver:"));
        formPanel.add(driverCombo);
        formPanel.add(new JLabel());
        formPanel.add(addButton);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(formPanel, BorderLayout.SOUTH);

        JButton exportButton = new JButton("📤 Export Shipments to CSV");
        add(exportButton, BorderLayout.NORTH);

        exportButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Shipments CSV");
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
                    JOptionPane.showMessageDialog(this, "Shipments exported successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Export failed: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });



        addButton.addActionListener(e -> {
            try {
                int driverId = drivers.get(driverCombo.getSelectedIndex()).getId();
                Shipment shipment = new Shipment(
                        0,
                        senderField.getText(),
                        receiverField.getText(),
                        contentsField.getText(),
                        statusField.getText(),
                        locationField.getText(),
                        LocalDate.parse(dateField.getText()),
                        (String) slotCombo.getSelectedItem(),
                        driverId
                );
                shipmentService.addShipment(shipment);
                loadShipments();
                // ✅ Notifications
                util.Notifier.notifyCustomer("Shipment scheduled for " + shipment.getReceiver() +
                        " on " + shipment.getDeliveryDate() + " (" + shipment.getDeliverySlot() + ")");
                util.Notifier.notifyDriver("Assigned to deliver to " + shipment.getReceiver() +
                        " on " + shipment.getDeliveryDate() + " (" + shipment.getDeliverySlot() + ")");


                senderField.setText("");
                receiverField.setText("");
                contentsField.setText("");
                statusField.setText("Scheduled");
                locationField.setText("Dispatch Center");
                dateField.setText("yyyy-MM-dd");
                slotCombo.setSelectedIndex(0);
                driverCombo.setSelectedIndex(0);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void loadDrivers() {
        drivers = driverService.getAllDrivers();
        driverCombo.removeAllItems();
        for (Driver d : drivers) {
            driverCombo.addItem(d.getName() + " (ID: " + d.getId() + ")");
        }
    }

    private void loadShipments() {
        tableModel.setRowCount(0);
        List<Shipment> shipments = shipmentService.getAllShipments();
        for (Shipment s : shipments) {
            tableModel.addRow(new Object[]{
                    s.getId(), s.getSender(), s.getReceiver(), s.getContents(), s.getStatus(),
                    s.getCurrentLocation(), s.getDeliveryDate(), s.getDeliverySlot(), s.getDriverId()
            });
        }
    }
}
