package notifications;

import javax.swing.JOptionPane;

public class SMSService {
    public static void sendSMS(String phone, String message) {
        // Simulated SMS sending for academic project
        System.out.println("=== SMS NOTIFICATION ===");
        System.out.println("To: " + phone);
        System.out.println("Message: " + message);
        System.out.println("=======================");

        // For demonstration, show a dialog
        JOptionPane.showMessageDialog(null,
                "SMS sent to: " + phone + "\nMessage: " + message,
                "SMS Service", JOptionPane.INFORMATION_MESSAGE);
    }
}