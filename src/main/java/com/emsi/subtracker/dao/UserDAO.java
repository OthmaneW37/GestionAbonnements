package com.emsi.subtracker.dao;

import com.emsi.subtracker.models.User;
import com.emsi.subtracker.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public User checkLogin(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"));
            }
        } catch (SQLException | RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException("Login Error: " + e.getMessage());
        }
        return null;
    }

    public boolean register(String username, String password) {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException | RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException("Registration Error: " + e.getMessage());
        }
    }
}
