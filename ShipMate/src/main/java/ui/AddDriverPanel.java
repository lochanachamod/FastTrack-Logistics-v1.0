package ui;

import model.Driver;
import service.DriverService;

import javax.swing.*;
import java.awt.*;

public class AddDriverPanel extends JPanel {
    public AddDriverPanel() {
        setLayout(new GridLayout(5, 2));

        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField scheduleField = new JTextField();
        JButton addButton = new JButton("Add Driver");

        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Phone:"));
        add(phoneField);
        add(new JLabel("Schedule:"));
        add(scheduleField);
        add(new JLabel());
        add(addButton);

        addButton.addActionListener(e -> {
            Driver driver = new Driver(0, nameField.getText(), phoneField.getText(), scheduleField.getText());
            new DriverService().addDriver(driver);
            JOptionPane.showMessageDialog(this, "Driver added successfully!");
        });
    }
}
