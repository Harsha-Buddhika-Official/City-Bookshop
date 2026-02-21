package com.citybookshop.model;

public class Category {
    private String categoryId;
    private String categoryName;
    private String description;

    public Category(String categoryId, String categoryName, String description) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.description = description;
    }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String toFileString() {
        return categoryId + "|" + categoryName + "|" + description;
    }

    public static Category fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length >= 3) {
            return new Category(parts[0], parts[1], parts[2]);
        }
        return null;
    }

    @Override
    public String toString() {
        return categoryId + " - " + categoryName;
    }
}
