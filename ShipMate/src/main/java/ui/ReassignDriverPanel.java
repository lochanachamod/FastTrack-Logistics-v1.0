package ui;

import model.Driver;
import model.Shipment;
import service.DriverService;
import service.ShipmentService;
import util.Notifier;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ReassignDriverPanel extends JPanel {
    private ShipmentService shipmentService = new ShipmentService();
    private DriverService driverService = new DriverService();

    private JComboBox<Shipment> shipmentCombo;
    private JComboBox<Driver> driverCombo;
    private JLabel currentDriverLabel;
    private JButton reassignButton;

    public ReassignDriverPanel() {
        setLayout(new GridLayout(4, 2, 10, 10));

        // Initialize components
        shipmentCombo = new JComboBox<>();
        driverCombo = new JComboBox<>();
        currentDriverLabel = new JLabel("-");
        reassignButton = new JButton("Reassign Driver");

        // Add components to panel
        add(new JLabel("Select Shipment:"));
        add(shipmentCombo);
        add(new JLabel("Current Driver:"));
        add(currentDriverLabel);
        add(new JLabel("Select New Driver:"));
        add(driverCombo);
        add(new JLabel());
        add(reassignButton);

        // Load data
        loadShipments();
        loadDrivers();

        // Add listeners
        shipmentCombo.addActionListener(e -> updateCurrentDriverDisplay());
        reassignButton.addActionListener(e -> reassignDriver());
    }

    private void loadShipments() {
        // This should be replaced with actual method to get all shipments
        // For now using search as placeholder - you'll need to implement getAllShipments()
        shipmentCombo.removeAllItems();
        for (int i = 1; i <= 20; i++) { // Example range - replace with actual data
            Shipment s = shipmentService.searchShipmentById(i);
            if (s != null) {
                shipmentCombo.addItem(s);
            }
        }
    }

    private void loadDrivers() {
        driverCombo.removeAllItems();
        List<Driver> drivers = driverService.getAllDrivers();
        for (Driver d : drivers) {
            driverCombo.addItem(d);
        }
    }

    private void updateCurrentDriverDisplay() {
        Shipment selected = (Shipment) shipmentCombo.getSelectedItem();
        if (selected != null) {
            int driverId = selected.getDriverId();
            if (driverId > 0) {
                // Find driver by ID
                List<Driver> drivers = driverService.getAllDrivers();
                for (Driver d : drivers) {
                    if (d.getId() == driverId) {
                        currentDriverLabel.setText(d.getName());
                        return;
                    }
                }
            }
            currentDriverLabel.setText("No driver assigned");
        }
    }

    private void reassignDriver() {
        Shipment selectedShipment = (Shipment) shipmentCombo.getSelectedItem();
        Driver selectedDriver = (Driver) driverCombo.getSelectedItem();

        if (selectedShipment == null || selectedDriver == null) {
            JOptionPane.showMessageDialog(this, "Please select both a shipment and a driver");
            return;
        }

        // Update the shipment with new driver ID
        selectedShipment.setDriverId(selectedDriver.getId());

        // You'll need to add an updateShipment method to ShipmentService
        // For now this is a placeholder
        // shipmentService.updateShipment(selectedShipment);

        // Notify user and driver
        JOptionPane.showMessageDialog(this,
                "Driver " + selectedDriver.getName() +
                        " has been assigned to shipment #" + selectedShipment.getId());

        Notifier.notifyDriver(selectedDriver,
                "You have been assigned to shipment #" + selectedShipment.getId());

        // Refresh display
        updateCurrentDriverDisplay();
    }
}