package ui;

import model.Shipment;
import service.ShipmentService;

import javax.swing.*;
import java.awt.*;

public class TrackShipmentPanel extends JPanel {
    private ShipmentService shipmentService = new ShipmentService();

    public TrackShipmentPanel() {
        setLayout(new GridLayout(9, 2, 10, 10));

        JTextField idField = new JTextField();
        JLabel currentStatus = new JLabel("-");
        JLabel currentLocation = new JLabel("-");
        JLabel deliveryDateLabel = new JLabel("-");
        JLabel slotLabel = new JLabel("-");

        JTextField newLocationField = new JTextField();
        JTextField delayField = new JTextField();
        JCheckBox deliveredOnTimeCheckbox = new JCheckBox("Delivered On Time");

        JButton searchButton = new JButton("Search");
        JButton updateButton = new JButton("Update Progress");

        add(new JLabel("Shipment ID:"));
        add(idField);
        add(new JLabel("Current Status:"));
        add(currentStatus);
        add(new JLabel("Current Location:"));
        add(currentLocation);
        add(new JLabel("Delivery Date:"));
        add(deliveryDateLabel);
        add(new JLabel("Time Slot:"));
        add(slotLabel);
        add(new JLabel("New Location:"));
        add(newLocationField);
        add(new JLabel("Delay Reason (if any):"));
        add(delayField);
        add(deliveredOnTimeCheckbox);
        add(new JLabel()); // spacer
        add(searchButton);
        add(updateButton);

        final Shipment[] currentShipment = new Shipment[1];

        searchButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                Shipment shipment = shipmentService.searchShipmentById(id);
                if (shipment != null) {
                    currentShipment[0] = shipment;
                    currentStatus.setText(shipment.getStatus());
                    currentLocation.setText(shipment.getCurrentLocation());
                    deliveryDateLabel.setText(shipment.getDeliveryDate().toString());
                    slotLabel.setText(shipment.getDeliverySlot());
                } else {
                    JOptionPane.showMessageDialog(this, "Shipment not found.");
                    currentShipment[0] = null;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid shipment ID.");
            }
        });

        updateButton.addActionListener(e -> {
            if (currentShipment[0] == null) {
                JOptionPane.showMessageDialog(this, "Please search for a shipment first.");
                return;
            }

            String newLocation = newLocationField.getText().trim();
            boolean onTime = deliveredOnTimeCheckbox.isSelected();
            String delayNote = delayField.getText().trim();

            if (newLocation.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the new location.");
                return;
            }

            String finalStatus;
            if (onTime) {
                finalStatus = "Delivered";
            } else if (!delayNote.isEmpty()) {
                finalStatus = "Delayed: " + delayNote;
            } else {
                finalStatus = "In Transit";
            }

            shipmentService.updateShipmentStatus(currentShipment[0].getId(), finalStatus, newLocation);

            // Notify user
            JOptionPane.showMessageDialog(this, "Shipment progress updated.");
            System.out.println("[Shipment Update] ID: " + currentShipment[0].getId() +
                    " | Status: " + finalStatus + " | Location: " + newLocation);

            // Reflect on UI
            currentStatus.setText(finalStatus);
            currentLocation.setText(newLocation);

            // Reset
            newLocationField.setText("");
            delayField.setText("");
            deliveredOnTimeCheckbox.setSelected(false);
        });
    }
}
