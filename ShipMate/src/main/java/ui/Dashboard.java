package ui;

import db.Database;
import notifications.NotificationSystem;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class Dashboard extends JPanel {
    public Dashboard() {
        setLayout(new BorderLayout());
        JLabel title = new JLabel("Welcome to FastTrack Logistics Dashboard", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        try {
            Connection conn = Database.connect();
            tabs.addTab("Shipments", new ShipmentPanel());
            tabs.addTab("Drivers", new DriverPanel());
            tabs.addTab("Shipment Report", new ShipmentReportPanel(conn));
            tabs.addTab("Reassign Driver", new ReassignDriverPanel());
            tabs.addTab("Delete Shipment", new DeleteShipmentPanel());
            tabs.addTab("Edit Shipment", new EditShipmentPanel());
            tabs.addTab("Track Shipment", new TrackShipmentPanel());

            // Notifications tabs
            JTabbedPane notificationTabs = new JTabbedPane();
            notificationTabs.addTab("Customer Notifications", new NotificationSystem());
            notificationTabs.addTab("Driver Notifications", new DriverNotificationsPanel());

            tabs.addTab("Notifications", notificationTabs);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        add(tabs, BorderLayout.CENTER);
    }
}