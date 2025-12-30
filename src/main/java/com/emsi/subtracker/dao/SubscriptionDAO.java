package com.emsi.subtracker.dao;

<<<<<<< HEAD
import com.emsi.subtracker.dao.base.BaseDAO;
import com.emsi.subtracker.models.Abonnement;
import java.util.List; // Import List

public interface SubscriptionDAO extends BaseDAO<Abonnement, Integer> {
    List<Abonnement> findAll(int userId); // Filter by user

    Abonnement save(Abonnement abonnement); // userId is inside Abonnement

    List<Abonnement> findAll(); // Deprecated or for admin use

    void deleteAll(int userId);
=======
import com.emsi.subtracker.models.Subscription;
import com.emsi.subtracker.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionDAO {

    public List<Subscription> getAllSubscriptions(int userId) {
        List<Subscription> list = new ArrayList<>();
        String query = "SELECT * FROM subscriptions WHERE user_id = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(new Subscription(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getDate("date_start").toLocalDate(),
                        rs.getString("category"),
                        rs.getString("frequency"),
                        rs.getInt("user_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void addSubscription(Subscription sub) {
        String query = "INSERT INTO subscriptions (name, price, date_start, category, frequency, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, sub.getName());
            pstmt.setDouble(2, sub.getPrice());
            pstmt.setDate(3, Date.valueOf(sub.getDateStart()));
            pstmt.setString(4, sub.getCategory());
            pstmt.setString(5, sub.getFrequency());
            pstmt.setInt(6, sub.getUserId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateSubscription(Subscription sub) {
        String query = "UPDATE subscriptions SET name=?, price=?, date_start=?, category=?, frequency=? WHERE id=?";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, sub.getName());
            pstmt.setDouble(2, sub.getPrice());
            pstmt.setDate(3, Date.valueOf(sub.getDateStart()));
            pstmt.setString(4, sub.getCategory());
            pstmt.setString(5, sub.getFrequency());
            pstmt.setInt(6, sub.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteSubscription(int id) {
        String query = "DELETE FROM subscriptions WHERE id=?";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
>>>>>>> 981914bfcf7f22d4c8c16c2ebb471e388aa49dbd
}
