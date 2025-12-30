package com.emsi.subtracker.services;

import com.emsi.subtracker.dao.UserDAO;
import com.emsi.subtracker.dao.impl.UserDAOImpl;
import com.emsi.subtracker.models.User;

public class UserService {
    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAOImpl();
    }

    public User authenticate(String username, String password) {
        if (userDAO.checkCredentials(username, password)) {
            return userDAO.findByUsername(username).orElse(null);
        }
        return null;
    }

    public User register(String username, String email, String password) throws Exception {
        if (userDAO.findByUsername(username).isPresent()) {
            throw new Exception("Username already exists");
        }
        if (userDAO.findByEmail(email).isPresent()) {
            throw new Exception("Email already exists");
        }

        User newUser = new User(username, password, email);
        return userDAO.create(newUser);
    }

    public void updateUser(User user) throws Exception {
        // Optional: Check if new username/email already exists if they were changed
        userDAO.update(user);
    }

    public void changePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        userDAO.update(user);
    }

    public void deleteUser(User user) {
        userDAO.delete(user.getId());
    }
}
