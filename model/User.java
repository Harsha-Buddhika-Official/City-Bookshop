package com.citybookshop.model;

// Abstraction: Abstract class defining common user structure
public abstract class User {
    // Encapsulation: private fields with getters/setters
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String accountType;

    public User(String username, String password, String fullName, String email, String accountType) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.accountType = accountType;
    }

    // Abstract method - Polymorphism (each subclass implements differently)
    public abstract String getRole();
    public abstract String getPermissions();

    // Encapsulation: getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public boolean authenticate(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    // Serialization to file format
    public String toFileString() {
        return username + "," + password + "," + fullName + "," + email + "," + accountType;
    }

    @Override
    public String toString() {
        return "User{username='" + username + "', fullName='" + fullName + "', role='" + getRole() + "'}";
    }
}
