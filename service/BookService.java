package com.citybookshop.service;

import com.citybookshop.model.Book;
import com.citybookshop.util.Constants;
import com.citybookshop.util.FileHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Implements Searchable interface - demonstrates Polymorphism through interface
public class BookService implements Searchable {

    private List<Book> books;

    public BookService() {
        FileHandler.ensureDataDirectory(Constants.DATA_DIR);
        this.books = new ArrayList<>();
        loadBooksFromFile();
    }

    // Load all books from file
    private void loadBooksFromFile() {
        books.clear();
        List<String> lines = FileHandler.readLines(Constants.BOOKS_FILE);
        for (String line : lines) {
            Book book = Book.fromFileString(line);
            if (book != null) {
                books.add(book);
            }
        }
    }

    // Save all books back to file
    private void saveBooksToFile() {
        List<String> lines = new ArrayList<>();
        for (Book book : books) {
            lines.add(book.toFileString());
        }
        FileHandler.writeLines(Constants.BOOKS_FILE, lines);
    }

    // Add a new book
    public boolean addBook(Book book) {
        // Check for duplicate ISBN
        for (Book b : books) {
            if (b.getIsbn().equalsIgnoreCase(book.getIsbn())) {
                return false; // duplicate
            }
        }
        books.add(book);
        saveBooksToFile();
        return true;
    }

    // Get all books
    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    // Get stock details of a book by ISBN
    public Book getBookByIsbn(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equalsIgnoreCase(isbn)) {
                return book;
            }
        }
        return null;
    }

    // Update book stock
    public boolean updateStock(String isbn, int newQuantity) {
        for (Book book : books) {
            if (book.getIsbn().equalsIgnoreCase(isbn)) {
                book.setStockQuantity(newQuantity);
                saveBooksToFile();
                return true;
            }
        }
        return false;
    }

    // --- Searchable interface implementations (Polymorphism) ---

    @Override
    public List<Book> searchByName(String name) {
        return books.stream()
                .filter(b -> b.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> searchByCategory(String category) {
        return books.stream()
                .filter(b -> b.getCategory().toLowerCase().contains(category.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> searchByPriceRange(double minPrice, double maxPrice) {
        return books.stream()
                .filter(b -> b.getPrice() >= minPrice && b.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> searchByAuthor(String author) {
        return books.stream()
                .filter(b -> b.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Reload from file (refresh)
    public void refresh() {
        loadBooksFromFile();
    }
}
