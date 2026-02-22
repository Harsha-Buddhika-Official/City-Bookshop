package com.citybookshop.util;

public class Constants {
    public static final String DATA_DIR = "data/";
    public static final String BOOKS_FILE = DATA_DIR + "books.txt";
    public static final String USERS_FILE = DATA_DIR + "users.txt";
    public static final String CATEGORIES_FILE = DATA_DIR + "categories.txt";

    public static final String ROLE_MANAGER = "MANAGER";
    public static final String ROLE_CASHIER = "CASHIER";

    // Prevent instantiation
    private Constants() {
    }
}
