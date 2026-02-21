package com.citybookshop.service;

import com.citybookshop.model.Book;
import java.util.List;

// Interface for Abstraction - defines contract for search operations
public interface Searchable {
    List<Book> searchByName(String name);
    List<Book> searchByCategory(String category);
    List<Book> searchByPriceRange(double minPrice, double maxPrice);
    List<Book> searchByAuthor(String author);
}
