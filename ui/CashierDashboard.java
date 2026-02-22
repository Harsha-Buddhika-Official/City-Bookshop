package com.citybookshop.ui;

import com.citybookshop.model.Book;
import com.citybookshop.model.User;
import com.citybookshop.service.BookService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CashierDashboard extends JFrame {

    protected User currentUser;
    protected BookService bookService;
    protected JTable bookTable;
    protected DefaultTableModel tableModel;

    public CashierDashboard(User user) {
        this.currentUser = user;
        this.bookService = new BookService();
        initUI();
        loadBooks(bookService.getAllBooks());
    }

    // Protected so ManagerDashboard can call with its own setup
    protected CashierDashboard(User user, boolean skipInit) {
        this.currentUser = user;
        this.bookService = new BookService();
    }

    private void initUI() {
        setTitle("City Bookshop - Cashier Dashboard [" + currentUser.getFullName() + "]");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top bar
        mainPanel.add(buildTopBar("Cashier Dashboard"), BorderLayout.NORTH);

        // Search panel
        mainPanel.add(buildSearchPanel(), BorderLayout.WEST);

        // Book table
        mainPanel.add(buildTablePanel(), BorderLayout.CENTER);

        add(mainPanel);
    }

    protected JPanel buildTopBar(String role) {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(34, 85, 136));
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel("City Bookshop - " + role);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        JLabel userLabel = new JLabel("Logged in: " + currentUser.getFullName() + " (" + currentUser.getRole() + ")");
        userLabel.setForeground(Color.WHITE);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFocusPainted(false);
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        rightPanel.add(userLabel);
        rightPanel.add(logoutBtn);

        topBar.add(titleLabel, BorderLayout.WEST);
        topBar.add(rightPanel, BorderLayout.EAST);
        return topBar;
    }

    protected JPanel buildSearchPanel() {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Books"));
        searchPanel.setPreferredSize(new Dimension(200, 0));

        JTextField nameField = new JTextField(12);
        JTextField categoryField = new JTextField(12);
        JTextField authorField = new JTextField(12);
        JTextField minPriceField = new JTextField(6);
        JTextField maxPriceField = new JTextField(6);
        JTextField isbnField = new JTextField(12);

        searchPanel.add(new JLabel("Book Name:"));
        searchPanel.add(nameField);
        searchPanel.add(Box.createVerticalStrut(5));
        searchPanel.add(new JLabel("Category:"));
        searchPanel.add(categoryField);
        searchPanel.add(Box.createVerticalStrut(5));
        searchPanel.add(new JLabel("Author:"));
        searchPanel.add(authorField);
        searchPanel.add(Box.createVerticalStrut(5));
        searchPanel.add(new JLabel("Min Price ($):"));
        searchPanel.add(minPriceField);
        searchPanel.add(new JLabel("Max Price ($):"));
        searchPanel.add(maxPriceField);
        searchPanel.add(Box.createVerticalStrut(5));
        searchPanel.add(new JLabel("ISBN (Stock):"));
        searchPanel.add(isbnField);
        searchPanel.add(Box.createVerticalStrut(10));

        JButton searchBtn = new JButton("Search");
        searchBtn.setBackground(new Color(0, 0, 0));
        searchBtn.setForeground(Color.BLACK);
        searchBtn.setFocusPainted(false);
        searchBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String cat = categoryField.getText().trim();
            String author = authorField.getText().trim();
            String isbn = isbnField.getText().trim();
            String minP = minPriceField.getText().trim();
            String maxP = maxPriceField.getText().trim();

            List<Book> results;

            if (!isbn.isEmpty()) {
                Book b = bookService.getBookByIsbn(isbn);
                results = b != null ? List.of(b) : List.of();
                if (b != null) {
                    JOptionPane.showMessageDialog(this,
                            "Stock for ISBN " + isbn + ": " + b.getStockQuantity() + " unit(s)",
                            "Stock Details", JOptionPane.INFORMATION_MESSAGE);
                }
            } else if (!name.isEmpty()) {
                results = bookService.searchByName(name);
            } else if (!cat.isEmpty()) {
                results = bookService.searchByCategory(cat);
            } else if (!author.isEmpty()) {
                results = bookService.searchByAuthor(author);
            } else if (!minP.isEmpty() && !maxP.isEmpty()) {
                try {
                    results = bookService.searchByPriceRange(Double.parseDouble(minP), Double.parseDouble(maxP));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid price range.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                results = bookService.getAllBooks();
            }

            loadBooks(results);
        });

        JButton clearBtn = new JButton("Show All");
        clearBtn.setFocusPainted(false);
        clearBtn.addActionListener(e -> {
            nameField.setText("");
            categoryField.setText("");
            authorField.setText("");
            isbnField.setText("");
            minPriceField.setText("");
            maxPriceField.setText("");
            bookService.refresh();
            loadBooks(bookService.getAllBooks());
        });

        searchPanel.add(searchBtn);
        searchPanel.add(Box.createVerticalStrut(5));
        searchPanel.add(clearBtn);

        return searchPanel;
    }

    protected JPanel buildTablePanel() {
        String[] columns = { "ISBN", "Name", "Author", "Category", "Price ($)", "Stock", "Description" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        bookTable = new JTable(tableModel);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookTable.setRowHeight(22);
        bookTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        bookTable.getColumnModel().getColumn(1).setPreferredWidth(160);
        bookTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        bookTable.getColumnModel().getColumn(3).setPreferredWidth(90);
        bookTable.getColumnModel().getColumn(4).setPreferredWidth(70);
        bookTable.getColumnModel().getColumn(5).setPreferredWidth(50);
        bookTable.getColumnModel().getColumn(6).setPreferredWidth(200);

        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Book Inventory"));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    protected void loadBooks(List<Book> books) {
        tableModel.setRowCount(0);
        for (Book book : books) {
            tableModel.addRow(new Object[] {
                    book.getIsbn(),
                    book.getName(),
                    book.getAuthor(),
                    book.getCategory(),
                    String.format("%.2f", book.getPrice()),
                    book.getStockQuantity(),
                    book.getDescription()
            });
        }
    }
}
