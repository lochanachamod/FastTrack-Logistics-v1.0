package service;

import db.Database;
import model.Driver;

import java.sql.*;
import java.util.*;

public class DriverService {
    public void addDriver(Driver driver) {
        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO driver(name, phone, schedule) VALUES (?, ?, ?)")) {
            pstmt.setString(1, driver.getName());
            pstmt.setString(2, driver.getPhone());
            pstmt.setString(3, driver.getSchedule());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Driver> getAllDrivers() {
        List<Driver> list = new ArrayList<>();
        try (Connection conn = Database.connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM driver")) {
            while (rs.next()) {
                list.add(new Driver(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("schedule")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateDriver(Driver driver) {
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE driver SET name = ?, phone = ?, schedule = ? WHERE id = ?")) {
            pstmt.setString(1, driver.getName());
            pstmt.setString(2, driver.getPhone());
            pstmt.setString(3, driver.getSchedule());
            pstmt.setInt(4, driver.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDriver(int id) {
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM driver WHERE id = ?")) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}