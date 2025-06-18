import db.Database;
import ui.Dashboard;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Database.initialize();
        Database.patchShipmentTable();
        try {
            Database.connect();
            System.out.println("✅ SQLite driver loaded and DB connected!");
        } catch (Exception e) {
            System.out.println("❌ Database connection failed: " + e.getMessage());
        }
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("FastTrack Logistics System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setContentPane(new Dashboard());
            frame.setVisible(true);
        });
    }
}

