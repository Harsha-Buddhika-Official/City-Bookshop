package com.citybookshop.model;

// Inheritance: Manager extends User (also inherits Cashier capabilities)
public class Manager extends User {

    public Manager(String username, String password, String fullName, String email) {
        super(username, password, fullName, email, "MANAGER");
    }

    // Polymorphism: overriding abstract methods differently from Cashier
    @Override
    public String getRole() {
        return "Manager";
    }

    @Override
    public String getPermissions() {
        return "View Books, Search Books, Search by Category/Name/Price, " +
                "Create Accounts, Add Books, Add Categories";
    }

    // Factory method to parse from file
    public static Manager fromFileString(String line) {
        String[] parts = line.split(",");
        if (parts.length >= 4) {
            return new Manager(parts[0], parts[1], parts[2], parts[3]);
        }
        return null;
    }
}
