package db.service;

import db.Database;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class ReportService {
    private final Connection connection;

    public ReportService(Connection connection) {
        this.connection = connection;
    }

    public Map<String, Object> generateMonthlyReport(int year, int month) throws SQLException {
        Date[] dateRange = getMonthDateRange(year, month);
        Map<String, Object> report = new HashMap<>();

        report.put("period", String.format("%04d-%02d", year, month));
        report.put("driverStats", getDriverAssignmentStats(dateRange[0], dateRange[1]));
        report.put("recentShipments", getRecentShipments(5));

        return report;
    }

    public List<Map<String, Object>> getDriverAssignmentStats(Date startDate, Date endDate) throws SQLException {
        List<Map<String, Object>> driverStats = new ArrayList<>();

        String sql = "SELECT d.id, d.name, COUNT(s.id) as shipment_count " +
                "FROM driver d LEFT JOIN shipment s ON d.id = s.driverId " +
                "WHERE s.deliveryDate BETWEEN ? AND ? " +
                "GROUP BY d.id, d.name " +
                "ORDER BY shipment_count DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(startDate.getTime()));
            stmt.setDate(2, new java.sql.Date(endDate.getTime()));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> stats = new HashMap<>();
                stats.put("driverId", rs.getInt("id"));
                stats.put("driverName", rs.getString("name"));
                stats.put("shipmentCount", rs.getInt("shipment_count"));
                driverStats.add(stats);
            }
        }
        return driverStats;
    }

    public List<Map<String, Object>> getRecentShipments(int limit) throws SQLException {
        List<Map<String, Object>> shipments = new ArrayList<>();

        String sql = "SELECT s.id, s.sender, s.receiver, s.status, s.currentLocation, " +
                "d.name as driverName, s.deliveryDate " +
                "FROM shipment s LEFT JOIN driver d ON s.driverId = d.id " +
                "ORDER BY s.id DESC LIMIT ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> shipment = new HashMap<>();
                shipment.put("id", rs.getInt("id"));
                shipment.put("sender", rs.getString("sender"));
                shipment.put("receiver", rs.getString("receiver"));
                shipment.put("status", rs.getString("status"));
                shipment.put("location", rs.getString("currentLocation"));
                shipment.put("driver", rs.getString("driverName"));
                shipment.put("deliveryDate", rs.getDate("deliveryDate"));
                shipments.add(shipment);
            }
        }
        return shipments;
    }

    private Date[] getMonthDateRange(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1, 0, 0, 0);
        Date startDate = cal.getTime();

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date endDate = cal.getTime();

        return new Date[]{startDate, endDate};
    }
}