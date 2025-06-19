package ui;

import model.Shipment;
import service.ShipmentService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class EditShipmentPanel extends JPanel {

    private JComboBox<String> shipmentIdComboBox;
    private JTextField senderField, receiverField, contentsField, statusField, locationField, deliveryDateField, slotField;
    private JButton updateButton;
    private ShipmentService shipmentService;

    public EditShipmentPanel() {
        shipmentService = new ShipmentService();
        initializeUI();
        loadShipmentIds();
    }

    private void initializeUI() {
        setLayout(new GridLayout(9, 2, 5, 5));

        add(new JLabel("Select Shipment ID:"));
        shipmentIdComboBox = new JComboBox<>();
        add(shipmentIdComboBox);

        add(new JLabel("Sender:"));
        senderField = new JTextField();
        add(senderField);

        add(new JLabel("Receiver:"));
        receiverField = new JTextField();
        add(receiverField);

        add(new JLabel("Contents:"));
        contentsField = new JTextField();
        add(contentsField);

        add(new JLabel("Status:"));
        statusField = new JTextField();
        add(statusField);

        add(new JLabel("Current Location:"));
        locationField = new JTextField();
        add(locationField);

        add(new JLabel("Delivery Date (YYYY-MM-DD):"));
        deliveryDateField = new JTextField();
        add(deliveryDateField);

        add(new JLabel("Delivery Slot:"));
        slotField = new JTextField();
        add(slotField);

        updateButton = new JButton("Update Shipment");
        updateButton.addActionListener(e -> updateShipment());
        add(updateButton);

        JButton loadButton = new JButton("Load Shipment");
        loadButton.addActionListener(e -> loadShipmentDetails());
        add(loadButton);
    }

    private void loadShipmentIds() {
        shipmentIdComboBox.removeAllItems();
        List<Integer> ids = shipmentService.getAllShipmentIds();
        for (Integer id : ids) {
            shipmentIdComboBox.addItem(String.valueOf(id));
        }
    }

    private void loadShipmentDetails() {
        String selectedId = (String) shipmentIdComboBox.getSelectedItem();
        if (selectedId == null || selectedId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a Shipment ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int shipmentId = Integer.parseInt(selectedId);
        Shipment shipment = shipmentService.searchShipmentById(shipmentId);
        if (shipment == null) {
            JOptionPane.showMessageDialog(this, "Shipment not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        senderField.setText(shipment.getSender());
        receiverField.setText(shipment.getReceiver());
        contentsField.setText(shipment.getContents());
        statusField.setText(shipment.getStatus());
        locationField.setText(shipment.getCurrentLocation());
        deliveryDateField.setText(shipment.getDeliveryDate().toString());
        slotField.setText(shipment.getDeliverySlot());
    }

    private void updateShipment() {
        String selectedId = (String) shipmentIdComboBox.getSelectedItem();
        if (selectedId == null || selectedId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a Shipment ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int shipmentId = Integer.parseInt(selectedId);
        Shipment shipment = shipmentService.searchShipmentById(shipmentId);
        if (shipment == null) {
            JOptionPane.showMessageDialog(this, "Shipment not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LocalDate deliveryDate = LocalDate.parse(deliveryDateField.getText().trim());
            shipment.setSender(senderField.getText().trim());
            shipment.setReceiver(receiverField.getText().trim());
            shipment.setContents(contentsField.getText().trim());
            shipment.setStatus(statusField.getText().trim());
            shipment.setCurrentLocation(locationField.getText().trim());
            shipment.setDeliveryDate(deliveryDate);
            shipment.setDeliverySlot(slotField.getText().trim());

            shipmentService.updateShipment(shipment);
            JOptionPane.showMessageDialog(this, "Shipment updated successfully!");
            clearFields();
            loadShipmentIds();
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating shipment: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        senderField.setText("");
        receiverField.setText("");
        contentsField.setText("");
        statusField.setText("");
        locationField.setText("");
        deliveryDateField.setText("");
        slotField.setText("");
    }
}