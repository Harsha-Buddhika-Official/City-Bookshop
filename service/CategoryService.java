package com.citybookshop.service;

import com.citybookshop.model.Category;
import com.citybookshop.util.Constants;
import com.citybookshop.util.FileHandler;

import java.util.ArrayList;
import java.util.List;

public class CategoryService {

    private List<Category> categories;

    public CategoryService() {
        FileHandler.ensureDataDirectory(Constants.DATA_DIR);
        this.categories = new ArrayList<>();
        loadCategoriesFromFile();
        ensureDefaultCategories();
    }

    private void loadCategoriesFromFile() {
        categories.clear();
        List<String> lines = FileHandler.readLines(Constants.CATEGORIES_FILE);
        for (String line : lines) {
            Category cat = Category.fromFileString(line);
            if (cat != null) {
                categories.add(cat);
            }
        }
    }

    private void saveCategoriesToFile() {
        List<String> lines = new ArrayList<>();
        for (Category cat : categories) {
            lines.add(cat.toFileString());
        }
        FileHandler.writeLines(Constants.CATEGORIES_FILE, lines);
    }

    private void ensureDefaultCategories() {
        if (categories.isEmpty()) {
            categories.add(new Category("CAT001", "Fiction", "Fictional stories and novels"));
            categories.add(new Category("CAT002", "Non-Fiction", "Factual and educational books"));
            categories.add(new Category("CAT003", "Science", "Science and technology books"));
            categories.add(new Category("CAT004", "History", "Historical books and biographies"));
            categories.add(new Category("CAT005", "Children", "Books for children"));
            saveCategoriesToFile();
        }
    }

    public boolean addCategory(Category category) {
        for (Category c : categories) {
            if (c.getCategoryId().equalsIgnoreCase(category.getCategoryId())) {
                return false;
            }
        }
        categories.add(category);
        saveCategoriesToFile();
        return true;
    }

    public List<Category> getAllCategories() {
        return new ArrayList<>(categories);
    }

    public List<String> getCategoryNames() {
        List<String> names = new ArrayList<>();
        for (Category cat : categories) {
            names.add(cat.getCategoryName());
        }
        return names;
    }

    public String generateCategoryId() {
        return "CAT" + String.format("%03d", categories.size() + 1);
    }
}
