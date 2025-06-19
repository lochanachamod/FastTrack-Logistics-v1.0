package ui;

import model.Driver;
import model.Shipment;
import service.DriverService;
import service.ShipmentService;
import util.Notifier;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class AddShipmentPanel extends JPanel {
    private ShipmentService shipmentService = new ShipmentService();
    private DriverService driverService = new DriverService();
    private JComboBox<String> driverCombo;
    private List<Driver> drivers;

    public AddShipmentPanel() {
        setLayout(new GridLayout(10, 2, 8, 8));

        JTextField senderField = new JTextField();
        JTextField receiverField = new JTextField();
        JTextField contentsField = new JTextField();
        JTextField statusField = new JTextField("Scheduled");
        JTextField locationField = new JTextField("Dispatch Center");

        JTextField dateField = new JTextField("yyyy-MM-dd");
        String[] slots = { "Morning", "Afternoon", "Evening" };
        JComboBox<String> slotCombo = new JComboBox<>(slots);

        driverCombo = new JComboBox<>();
        loadDrivers();

        JButton addButton = new JButton("Add Shipment");

        add(new JLabel("Sender:"));
        add(senderField);
        add(new JLabel("Receiver:"));
        add(receiverField);
        add(new JLabel("Contents:"));
        add(contentsField);
        add(new JLabel("Status:"));
        add(statusField);
        add(new JLabel("Location:"));
        add(locationField);
        add(new JLabel("Delivery Date (yyyy-MM-dd):"));
        add(dateField);
        add(new JLabel("Delivery Slot:"));
        add(slotCombo);
        add(new JLabel("Assign Driver:"));
        add(driverCombo);
        add(new JLabel());
        add(addButton);

        addButton.addActionListener(e -> {
            try {
                int driverId = drivers.get(driverCombo.getSelectedIndex()).getId();
                LocalDate deliveryDate = LocalDate.parse(dateField.getText());
                String deliverySlot = (String) slotCombo.getSelectedItem();

                Shipment shipment = new Shipment(
                        0,
                        senderField.getText(),
                        receiverField.getText(),
                        contentsField.getText(),
                        statusField.getText(),
                        locationField.getText(),
                        deliveryDate,
                        deliverySlot,
                        driverId
                );

                shipmentService.addShipment(shipment);

                // ✅ Notifications
                Notifier.notifyCustomer("Shipment scheduled for " + receiverField.getText() +
                        " on " + deliveryDate + " (" + deliverySlot + ")");
                Notifier.notifyDriver("Assigned to deliver to " + receiverField.getText() +
                        " on " + deliveryDate + " (" + deliverySlot + ")");

                // Reset fields
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
}
