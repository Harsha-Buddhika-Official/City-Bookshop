package com.citybookshop.service;

import com.citybookshop.model.Cashier;
import com.citybookshop.model.Manager;
import com.citybookshop.model.User;
import com.citybookshop.util.Constants;
import com.citybookshop.util.FileHandler;

import java.util.ArrayList;
import java.util.List;

public class UserService {

    private List<User> users;

    public UserService() {
        FileHandler.ensureDataDirectory(Constants.DATA_DIR);
        this.users = new ArrayList<>();
        loadUsersFromFile();
        ensureDefaultAdmin();
    }

    private void loadUsersFromFile() {
        users.clear();
        List<String> lines = FileHandler.readLines(Constants.USERS_FILE);
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 5) {
                String role = parts[4];
                // Polymorphism: creating different User subclass objects
                if (Constants.ROLE_MANAGER.equals(role)) {
                    users.add(new Manager(parts[0], parts[1], parts[2], parts[3]));
                } else {
                    users.add(new Cashier(parts[0], parts[1], parts[2], parts[3]));
                }
            }
        }
    }

    private void saveUsersToFile() {
        List<String> lines = new ArrayList<>();
        for (User user : users) {
            lines.add(user.toFileString());
        }
        FileHandler.writeLines(Constants.USERS_FILE, lines);
    }

    // Ensure there's at least one default manager account
    private void ensureDefaultAdmin() {
        boolean hasManager = users.stream()
                .anyMatch(u -> u.getAccountType().equals(Constants.ROLE_MANAGER));
        if (!hasManager) {
            Manager defaultManager = new Manager("admin", "admin123", "Default Admin", "admin@citybookshop.com");
            users.add(defaultManager);
            saveUsersToFile();
        }
    }

    // Authenticate user - Polymorphism: works for any User subtype
    public User authenticate(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.authenticate(password)) {
                return user;
            }
        }
        return null;
    }

    // Create new account (Manager only feature)
    public boolean createAccount(User newUser) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(newUser.getUsername())) {
                return false; // Username already exists
            }
        }
        users.add(newUser);
        saveUsersToFile();
        return true;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public boolean usernameExists(String username) {
        return users.stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username));
    }
}
