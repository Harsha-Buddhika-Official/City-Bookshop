package com.citybookshop.model;

// Encapsulation: All fields private, accessed via getters/setters
public class Book {
    private String isbn;
    private String name;
    private String author;
    private String category;
    private double price;
    private int stockQuantity;
    private String description;

    public Book(String isbn, String name, String author, String category,
                double price, int stockQuantity, String description) {
        this.isbn = isbn;
        this.name = name;
        this.author = author;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.description = description;
    }

    // Getters and Setters (Encapsulation)
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isInStock() {
        return stockQuantity > 0;
    }

    // Serialize to file string (| delimiter to avoid conflicts with description commas)
    public String toFileString() {
        return isbn + "|" + name + "|" + author + "|" + category + "|" +
               price + "|" + stockQuantity + "|" + description;
    }

    // Deserialize from file string
    public static Book fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length >= 7) {
            return new Book(parts[0], parts[1], parts[2], parts[3],
                    Double.parseDouble(parts[4]), Integer.parseInt(parts[5]), parts[6]);
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("ISBN: %s | Name: %s | Author: %s | Category: %s | Price: $%.2f | Stock: %d",
                isbn, name, author, category, price, stockQuantity);
    }
}
