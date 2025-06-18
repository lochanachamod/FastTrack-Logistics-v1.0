package ui;

import model.Driver;
import notifications.DriverAppNotificationService;
import notifications.DriverNotificationSystem;
import notifications.DriverSMSService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DriverNotificationsPanel extends JPanel {

    private JTextField driverIdField;
    private JTextField driverNameField;
    private JComboBox<String> messageTypeComboBox;
    private JTextArea messageArea;
    private JCheckBox smsCheckBox;
    private JCheckBox appCheckBox;
    private final DriverNotificationSystem notifier;

    public DriverNotificationsPanel() {
        this.notifier = new DriverNotificationSystem();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(buildDriverSelectionPanel(), BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);
        add(buildSouthPanel(), BorderLayout.SOUTH);
    }

    /* ---------- PANEL BUILDERS ---------- */

    private JPanel buildDriverSelectionPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Driver Information"));

        panel.add(new JLabel("Driver ID:"));
        driverIdField = new JTextField();
        panel.add(driverIdField);

        panel.add(new JLabel("Driver Name:"));
        driverNameField = new JTextField();
        panel.add(driverNameField);

        return panel;
    }

    private JPanel buildCenterPanel() {
        // Stack message type + message body vertically
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        /* Message-type chooser */
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        typePanel.setBorder(BorderFactory.createTitledBorder("Message Type"));
        String[] types = {"New Delivery Assignments", "Route Changes", "Urgent Deliveries"};
        messageTypeComboBox = new JComboBox<>(types);
        messageTypeComboBox.setPreferredSize(new Dimension(250, 25));
        typePanel.add(messageTypeComboBox);

        /* Message body */
        JPanel msgPanel = new JPanel(new BorderLayout());
        msgPanel.setBorder(BorderFactory.createTitledBorder("Notification Message"));
        messageArea = new JTextArea(5, 30);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        msgPanel.add(new JScrollPane(messageArea), BorderLayout.CENTER);

        center.add(typePanel);
        center.add(Box.createVerticalStrut(8)); // a little breathing room
        center.add(msgPanel);

        return center;
    }

    private JPanel buildSouthPanel() {
        // Delivery method check-boxes on top, Send button at bottom
        JPanel south = new JPanel(new BorderLayout());

        JPanel methodPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        methodPanel.setBorder(BorderFactory.createTitledBorder("Delivery Method"));
        smsCheckBox = new JCheckBox("SMS", true);
        appCheckBox = new JCheckBox("In-App Notification", true);
        methodPanel.add(smsCheckBox);
        methodPanel.add(appCheckBox);

        JButton sendButton = new JButton("Send Notification");
        sendButton.addActionListener(this::sendNotification);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(sendButton);

        south.add(methodPanel, BorderLayout.CENTER);
        south.add(buttonPanel, BorderLayout.SOUTH);

        return south;
    }

    /* ---------- ACTION ---------- */

    private void sendNotification(ActionEvent e) {
        String driverIdTxt   = driverIdField.getText().trim();
        String driverNameTxt = driverNameField.getText().trim();
        String msgType       = (String) messageTypeComboBox.getSelectedItem();
        String customMsg     = messageArea.getText().trim();

        if (driverIdTxt.isEmpty() || driverNameTxt.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both Driver ID and Name",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (customMsg.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a message",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(driverIdTxt);
            Driver driver = new Driver(id, driverNameTxt, "", "");   // phone & schedule left blank

            String fullMessage = "[" + msgType + "]\n" + customMsg;

            notifier.clearServices();
            if (smsCheckBox.isSelected()) {
                notifier.addNotificationService(new DriverSMSService());
            }
            if (appCheckBox.isSelected()) {
                notifier.addNotificationService(new DriverAppNotificationService());
            }

            notifier.sendNotification(driver, fullMessage);

            JOptionPane.showMessageDialog(this,
                    msgType + " notification sent to " + driverNameTxt,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            messageArea.setText("");   // reset message box
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Driver ID must be a number",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}

