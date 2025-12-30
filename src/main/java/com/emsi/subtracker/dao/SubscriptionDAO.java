package com.emsi.subtracker.dao;

import com.emsi.subtracker.dao.base.BaseDAO;
import com.emsi.subtracker.models.Abonnement;
import java.util.List; // Import List

public interface SubscriptionDAO extends BaseDAO<Abonnement, Integer> {
    List<Abonnement> findAll(int userId); // Filter by user

    Abonnement save(Abonnement abonnement); // userId is inside Abonnement

    List<Abonnement> findAll(); // Deprecated or for admin use

    void deleteAll(int userId);
}
