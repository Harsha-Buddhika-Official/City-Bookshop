package com.citybookshop.model;

// Inheritance: Cashier extends User
public class Cashier extends User {

    public Cashier(String username, String password, String fullName, String email) {
        super(username, password, fullName, email, "CASHIER");
    }

    // Polymorphism: overriding abstract methods
    @Override
    public String getRole() {
        return "Cashier";
    }

    @Override
    public String getPermissions() {
        return "View Books, Search Books, Search by Category/Name/Price";
    }

    // Factory method to parse from file
    public static Cashier fromFileString(String line) {
        String[] parts = line.split(",");
        if (parts.length >= 4) {
            return new Cashier(parts[0], parts[1], parts[2], parts[3]);
        }
        return null;
    }
}
