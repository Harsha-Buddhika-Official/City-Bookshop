package com.citybookshop.ui;

import com.citybookshop.model.*;
import com.citybookshop.service.BookService;
import com.citybookshop.service.CategoryService;
import com.citybookshop.service.UserService;
import com.citybookshop.util.Constants;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// Inheritance: ManagerDashboard extends CashierDashboard
// Manager has all cashier capabilities + additional manager-only features
public class ManagerDashboard extends CashierDashboard {

    private UserService userService;
    private CategoryService categoryService;

    public ManagerDashboard(User user, UserService userService) {
        super(user, true); // skip cashier init
        this.userService = userService;
        this.categoryService = new CategoryService();
        initManagerUI();
        loadBooks(bookService.getAllBooks());
    }

    private void initManagerUI() {
        setTitle("City Bookshop - Manager Dashboard [" + currentUser.getFullName() + "]");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top bar (inherited method)
        mainPanel.add(buildTopBar("Manager Dashboard"), BorderLayout.NORTH);

        // Center split: search + table
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.add(buildSearchPanel(), BorderLayout.WEST);
        centerPanel.add(buildTablePanel(), BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Manager-only action buttons at the bottom
        mainPanel.add(buildManagerActionPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel buildManagerActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(new Color(230, 240, 255));
        panel.setBorder(BorderFactory.createTitledBorder("Manager Actions"));

        JButton addBookBtn = new JButton("+ Add New Book");
        styleButton(addBookBtn, new Color(0, 128, 0));
        addBookBtn.addActionListener(e -> showAddBookDialog());

        JButton addCategoryBtn = new JButton("+ Add Category");
        styleButton(addCategoryBtn, new Color(0, 100, 160));
        addCategoryBtn.addActionListener(e -> showAddCategoryDialog());

        JButton createAccountBtn = new JButton("+ Create Account");
        styleButton(createAccountBtn, new Color(150, 0, 150));
        createAccountBtn.addActionListener(e -> showCreateAccountDialog());

        JButton viewAccountsBtn = new JButton("View All Accounts");
        styleButton(viewAccountsBtn, new Color(100, 100, 100));
        viewAccountsBtn.addActionListener(e -> showAllAccounts());

        panel.add(addBookBtn);
        panel.add(addCategoryBtn);
        panel.add(createAccountBtn);
        panel.add(viewAccountsBtn);

        return panel;
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
    }

    private void showAddBookDialog() {
        JDialog dialog = new JDialog(this, "Add New Book", true);
        dialog.setSize(420, 420);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField isbnField = new JTextField(15);
        JTextField nameField = new JTextField(15);
        JTextField authorField = new JTextField(15);
        List<String> catNames = categoryService.getCategoryNames();
        JComboBox<String> categoryBox = new JComboBox<>(catNames.toArray(new String[0]));
        JTextField priceField = new JTextField(15);
        JTextField stockField = new JTextField(15);
        JTextField descField = new JTextField(15);

        String[][] fields = {
                {"ISBN:", null}, {"Name:", null}, {"Author:", null},
                {"Category:", null}, {"Price ($):", null}, {"Stock Qty:", null}, {"Description:", null}
        };
        Component[] inputs = {isbnField, nameField, authorField, categoryBox, priceField, stockField, descField};

        String[] labels = {"ISBN:", "Name:", "Author:", "Category:", "Price ($):", "Stock Qty:", "Description:"};
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            panel.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1; gbc.weightx = 1.0;
            panel.add(inputs[i], gbc);
        }

        JButton saveBtn = new JButton("Save Book");
        styleButton(saveBtn, new Color(0, 128, 0));
        gbc.gridx = 0; gbc.gridy = labels.length; gbc.gridwidth = 2; gbc.insets = new Insets(15, 5, 5, 5);
        panel.add(saveBtn, gbc);

        saveBtn.addActionListener(e -> {
            try {
                String isbn = isbnField.getText().trim();
                String name = nameField.getText().trim();
                String author = authorField.getText().trim();
                String category = (String) categoryBox.getSelectedItem();
                double price = Double.parseDouble(priceField.getText().trim());
                int stock = Integer.parseInt(stockField.getText().trim());
                String desc = descField.getText().trim();

                if (isbn.isEmpty() || name.isEmpty() || author.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "ISBN, Name and Author are required.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Book newBook = new Book(isbn, name, author, category, price, stock, desc);
                if (bookService.addBook(newBook)) {
                    JOptionPane.showMessageDialog(dialog, "Book added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadBooks(bookService.getAllBooks());
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "ISBN already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid price or stock value.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(new JScrollPane(panel));
        dialog.setVisible(true);
    }

    private void showAddCategoryDialog() {
        JDialog dialog = new JDialog(this, "Add New Category", true);
        dialog.setSize(350, 220);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField idField = new JTextField(categoryService.generateCategoryId());
        idField.setEditable(false);
        JTextField nameField = new JTextField(15);
        JTextField descField = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Category ID:"), gbc);
        gbc.gridx = 1; panel.add(idField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; panel.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; panel.add(descField, gbc);

        JButton saveBtn = new JButton("Save Category");
        styleButton(saveBtn, new Color(0, 100, 160));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.insets = new Insets(15, 5, 5, 5);
        panel.add(saveBtn, gbc);

        saveBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String desc = descField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Category name is required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Category cat = new Category(idField.getText(), name, desc);
            if (categoryService.addCategory(cat)) {
                JOptionPane.showMessageDialog(dialog, "Category added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Category ID already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void showCreateAccountDialog() {
        JDialog dialog = new JDialog(this, "Create New Account", true);
        dialog.setSize(380, 280);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JTextField fullNameField = new JTextField(15);
        JTextField emailField = new JTextField(15);
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"Cashier", "Manager"});

        String[] labels = {"Username:", "Password:", "Full Name:", "Email:", "Role:"};
        Component[] inputs = {usernameField, passwordField, fullNameField, emailField, roleBox};
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            panel.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1; gbc.weightx = 1.0;
            panel.add(inputs[i], gbc);
        }

        JButton saveBtn = new JButton("Create Account");
        styleButton(saveBtn, new Color(150, 0, 150));
        gbc.gridx = 0; gbc.gridy = labels.length; gbc.gridwidth = 2; gbc.insets = new Insets(15, 5, 5, 5);
        panel.add(saveBtn, gbc);

        saveBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String fullName = fullNameField.getText().trim();
            String email = emailField.getText().trim();
            String role = (String) roleBox.getSelectedItem();

            if (username.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Username, Password, and Full Name are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Polymorphism: Creating appropriate User subclass based on role
            User newUser;
            if ("Manager".equals(role)) {
                newUser = new Manager(username, password, fullName, email);
            } else {
                newUser = new Cashier(username, password, fullName, email);
            }

            if (userService.createAccount(newUser)) {
                JOptionPane.showMessageDialog(dialog, "Account created successfully!\nRole: " + role, "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void showAllAccounts() {
        JDialog dialog = new JDialog(this, "All User Accounts", true);
        dialog.setSize(500, 350);
        dialog.setLocationRelativeTo(this);

        String[] cols = {"Username", "Full Name", "Email", "Role"};
        DefaultListModel<String> listModel = new DefaultListModel<>();
        Object[][] data = userService.getAllUsers().stream()
                .map(u -> new Object[]{u.getUsername(), u.getFullName(), u.getEmail(), u.getRole()})
                .toArray(Object[][]::new);

        JTable table = new JTable(data, cols);
        table.setEnabled(false);

        dialog.add(new JScrollPane(table));
        dialog.setVisible(true);
    }
}
