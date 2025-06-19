package service;

import db.Database;
import model.Shipment;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;

public class ShipmentService {
    private static final Logger logger = Logger.getLogger(ShipmentService.class.getName());

    public void addShipment(Shipment shipment) {
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO shipment(sender, receiver, contents, status, currentLocation, deliveryDateText, deliverySlot, driverId) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

            pstmt.setString(1, shipment.getSender());
            pstmt.setString(2, shipment.getReceiver());
            pstmt.setString(3, shipment.getContents());
            pstmt.setString(4, shipment.getStatus());
            pstmt.setString(5, shipment.getCurrentLocation());
            pstmt.setString(6, shipment.getDeliveryDate().toString()); // Save as TEXT "YYYY-MM-DD"
            pstmt.setString(7, shipment.getDeliverySlot());
            pstmt.setInt(8, shipment.getDriverId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Error adding shipment: " + e.getMessage());
        }
    }

    public List<Shipment> getAllShipments() {
        List<Shipment> list = new ArrayList<>();
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM shipment")) {

            while (rs.next()) {
                LocalDate deliveryDate;
                try {
                    String dateStr = rs.getString("deliveryDateText");
                    deliveryDate = (dateStr != null) ? LocalDate.parse(dateStr.trim()) : LocalDate.now();
                } catch (Exception ex) {
                    deliveryDate = LocalDate.now();
                }

                String slot = rs.getString("deliverySlot") != null ? rs.getString("deliverySlot") : "Unscheduled";

                Shipment s = new Shipment(
                        rs.getInt("id"),
                        rs.getString("sender"),
                        rs.getString("receiver"),
                        rs.getString("contents"),
                        rs.getString("status"),
                        rs.getString("currentLocation"),
                        deliveryDate,
                        slot,
                        rs.getInt("driverId")
                );
                list.add(s);
            }
        } catch (SQLException e) {
            logger.severe("Error getting all shipments: " + e.getMessage());
        }
        return list;
    }

    public Shipment searchShipmentById(int id) {
        String sql = "SELECT * FROM shipment WHERE id = ?";
        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                LocalDate deliveryDate;
                try {
                    String dateStr = rs.getString("deliveryDateText");
                    deliveryDate = (dateStr != null) ? LocalDate.parse(dateStr.trim()) : LocalDate.now();
                } catch (Exception ex) {
                    deliveryDate = LocalDate.now();
                }

                String slot = rs.getString("deliverySlot") != null ? rs.getString("deliverySlot") : "Unscheduled";

                return new Shipment(
                        rs.getInt("id"),
                        rs.getString("sender"),
                        rs.getString("receiver"),
                        rs.getString("contents"),
                        rs.getString("status"),
                        rs.getString("currentLocation"),
                        deliveryDate,
                        slot,
                        rs.getInt("driverId")
                );
            }
        } catch (SQLException e) {
            logger.severe("Error searching shipment by ID: " + e.getMessage());
        }
        return null;
    }

    public void updateShipmentStatus(int id, String status, String location) {
        String sql = "UPDATE shipment SET status = ?, currentLocation = ? WHERE id = ?";
        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setString(2, location);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Error updating shipment status: " + e.getMessage());
        }
    }

    public List<Integer> getAllShipmentIds() {
        List<Integer> ids = new ArrayList<>();
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id FROM shipment")) {
            while (rs.next()) {
                ids.add(rs.getInt("id"));
            }
        } catch (SQLException e) {
            logger.severe("Error getting shipment IDs: " + e.getMessage());
        }
        return ids;
    }

    public void updateShipment(Shipment shipment) {
        String sql = "UPDATE shipment SET sender = ?, receiver = ?, contents = ?, " +
                "status = ?, currentLocation = ?, deliveryDate = ?, " +
                "deliverySlot = ?, driverId = ? WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, shipment.getSender());
            pstmt.setString(2, shipment.getReceiver());
            pstmt.setString(3, shipment.getContents());
            pstmt.setString(4, shipment.getStatus());
            pstmt.setString(5, shipment.getCurrentLocation());
            pstmt.setString(6, shipment.getDeliveryDate().toString());
            pstmt.setString(7, shipment.getDeliverySlot());
            pstmt.setInt(8, shipment.getDriverId());
            pstmt.setInt(9, shipment.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Error updating shipment: " + e.getMessage());
        }
    }

    public boolean deleteShipment(int id) {
        String sql = "DELETE FROM shipment WHERE id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.severe("Error deleting shipment with ID: " + id + " - " + e.getMessage());
        }
        return false;
    }

    private Shipment parseShipment(ResultSet rs) throws SQLException {
        LocalDate deliveryDate = null;
        try {
            String dateStr = rs.getString("deliveryDateText");
            if (dateStr != null) {
                deliveryDate = LocalDate.parse(dateStr.trim());
            }
        } catch (Exception e) {
            logger.warning("Failed to parse deliveryDateText: " + e.getMessage());
        }

        String slot = rs.getString("deliverySlot") != null ? rs.getString("deliverySlot") : "Unscheduled";

        return new Shipment(
                rs.getInt("id"),
                rs.getString("sender"),
                rs.getString("receiver"),
                rs.getString("contents"),
                rs.getString("status"),
                rs.getString("currentLocation"),
                deliveryDate,
                slot,
                rs.getInt("driverId")
        );
    }
}