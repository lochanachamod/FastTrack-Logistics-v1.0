package notifications;

import javax.swing.JOptionPane;

public class EmailService {
    public static void sendEmail(String to, String subject, String body) {
        // Simulated email sending for academic project
        System.out.println("=== EMAIL NOTIFICATION ===");
        System.out.println("To: " + to);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + body);
        System.out.println("=========================");

        // For demonstration, show a dialog
        JOptionPane.showMessageDialog(null,
                "Email sent to: " + to + "\nSubject: " + subject,
                "Email Service", JOptionPane.INFORMATION_MESSAGE);
    }
}