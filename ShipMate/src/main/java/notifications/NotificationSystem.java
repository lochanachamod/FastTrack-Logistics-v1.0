package notifications;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NotificationSystem extends JPanel {
    private final JComboBox<String> notificationTypeCombo;
    private final JTextArea messageArea;
    private final JCheckBox emailCheckBox;
    private final JCheckBox smsCheckBox;
    private final JTextField recipientField;

    public NotificationSystem() {
        setLayout(new BorderLayout(10, 10));

        // Top panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Notification Type:"));
        notificationTypeCombo = new JComboBox<>(new String[]{
                "Status Update", "Delay Alert", "ETA Change", "Delivery Confirmation"
        });
        topPanel.add(notificationTypeCombo);

        // Center panel
        messageArea = new JTextArea(10, 30);
        JScrollPane scrollPane = new JScrollPane(messageArea);

        // Bottom panel
        JPanel bottomPanel = new JPanel(new GridLayout(3, 1, 5, 5));

        // Recipient info
        JPanel recipientPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        recipientPanel.add(new JLabel("Recipient Email/Phone:"));
        recipientField = new JTextField(25);
        recipientPanel.add(recipientField);

        // Delivery methods
        JPanel methodPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        methodPanel.add(new JLabel("Delivery Method:"));
        emailCheckBox = new JCheckBox("Email");
        smsCheckBox = new JCheckBox("SMS");
        methodPanel.add(emailCheckBox);
        methodPanel.add(smsCheckBox);

        // Send button
        JButton sendButton = new JButton("Send Notification");
        sendButton.addActionListener(this::sendNotification);

        // Assemble components
        bottomPanel.add(recipientPanel);
        bottomPanel.add(methodPanel);
        bottomPanel.add(sendButton);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void sendNotification(ActionEvent e) {
        String type = (String) notificationTypeCombo.getSelectedItem();
        String message = messageArea.getText();
        String recipient = recipientField.getText();

        if (message.isEmpty() || recipient.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (emailCheckBox.isSelected()) {
                EmailService.sendEmail(recipient, type, message);
            }
            if (smsCheckBox.isSelected()) {
                SMSService.sendSMS(recipient, message);
            }

            JOptionPane.showMessageDialog(this, "Notification sent successfully!");
            messageArea.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
