package util;

import javax.swing.*;
import model.Driver;

public class Notifier {
    public static void notifyCustomer(String message) {
        JOptionPane.showMessageDialog(null, "📦 CUSTOMER NOTICE:\n" + message, "Customer Notification", JOptionPane.INFORMATION_MESSAGE);
        System.out.println("[Customer Notification] " + message);
    }

    public static void notifyDriver(String message) {
        JOptionPane.showMessageDialog(null, "🚚 DRIVER NOTICE:\n" + message, "Driver Notification", JOptionPane.INFORMATION_MESSAGE);
        System.out.println("[Driver Notification] " + message);
    }
    public static void notifyDriver(Driver driver, String message) {
        // In a real application, this would send an email or SMS
        // For now, we'll just show a dialog and log to console
        System.out.println("Notification to driver " + driver.getName() +
                " (" + driver.getPhone() + "): " + message);

        JOptionPane.showMessageDialog(null,
                "Notification sent to driver " + driver.getName() + ":\n" + message,
                "Driver Notification",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
