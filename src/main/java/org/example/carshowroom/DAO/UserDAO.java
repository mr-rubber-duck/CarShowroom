package org.example.carshowroom.DAO;

import org.example.carshowroom.Entity.User;
import org.example.carshowroom.dbConnection.dbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private final dbConnection dbConn = new dbConnection();

    // Add a user to the database
    public void addUser(User user) {
        String query = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";

        try (Connection conn = dbConn.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());

            stmt.executeUpdate();
            System.out.println("✅ User added successfully!");

        } catch (SQLException e) {
            System.err.println("❌ Error adding user: " + e.getMessage());
        }
    }

    // Check login credentials
    public boolean checkLogin(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection conn = dbConn.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // if a record exists, login is valid

        } catch (SQLException e) {
            System.err.println("❌ Error checking login: " + e.getMessage());
        }

        return false;
    }
}
