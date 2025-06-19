package ui;

import service.ShipmentService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DeleteShipmentPanel extends JPanel {

    private JComboBox<String> shipmentIdComboBox;
    private JButton deleteButton;
    private ShipmentService shipmentService;

    public DeleteShipmentPanel() {
        shipmentService = new ShipmentService();
        initializeUI();
        loadShipmentIds();
    }

    private void initializeUI() {
        setLayout(new GridLayout(2, 2, 10, 10));

        add(new JLabel("Select Shipment ID to Delete:"));
        shipmentIdComboBox = new JComboBox<>();
        add(shipmentIdComboBox);

        deleteButton = new JButton("Delete Shipment");
        deleteButton.addActionListener(e -> deleteShipment());
        add(deleteButton);
    }

    private void loadShipmentIds() {
        shipmentIdComboBox.removeAllItems();
        List<Integer> ids = shipmentService.getAllShipmentIds();
        for (Integer id : ids) {
            shipmentIdComboBox.addItem(String.valueOf(id));
        }
    }

    private void deleteShipment() {
        String selectedId = (String) shipmentIdComboBox.getSelectedItem();
        if (selectedId == null || selectedId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a Shipment ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete Shipment ID: " + selectedId + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            int shipmentId = Integer.parseInt(selectedId);
            boolean deleted = shipmentService.deleteShipment(shipmentId);

            if (deleted) {
                JOptionPane.showMessageDialog(this, "Shipment deleted successfully!");
                loadShipmentIds();
            } else {
                JOptionPane.showMessageDialog(this, "Shipment could not be deleted.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Shipment ID.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error deleting shipment: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}