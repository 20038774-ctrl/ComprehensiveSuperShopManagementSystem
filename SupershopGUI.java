package com.supershop;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class SupershopGUI extends JFrame {
    private final ProductManager manager;
    private final DefaultTableModel model;
    private final JTable table;

    public SupershopGUI(ProductManager manager) {
        this.manager = manager;
        setTitle("Comprehensive Supershop Management System - Coles Inspired");
        setSize(1050, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        model = new DefaultTableModel(new String[]{"ID", "Name", "Category", "Price", "Stock", "Supplier", "Special"}, 0);
        table = new JTable(model);
        refreshTable(manager.getProducts());

        JLabel heading = new JLabel("Comprehensive Supershop Management System", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 22));
        heading.setForeground(new Color(220, 0, 0));

        JPanel buttons = new JPanel(new GridLayout(2, 5, 8, 8));
        String[] names = {"Add", "Delete", "Search Name", "Category Query", "Sort Price", "Sort Name", "Find ID", "Low Stock", "Save", "Exit"};
        for (String name : names) {
            JButton b = new JButton(name);
            b.addActionListener(e -> handle(name));
            buttons.add(b);
        }

        add(heading, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    private void handle(String action) {
        try {
            switch (action) {
                case "Add" -> addProductDialog();
                case "Delete" -> deleteSelected();
                case "Search Name" -> searchName();
                case "Category Query" -> categoryQuery();
                case "Sort Price" -> { manager.sortByPriceSelectionSort(); refreshTable(manager.getProducts()); show("Sorted by price using Selection Sort."); }
                case "Sort Name" -> { manager.sortByNameMergeSort(); refreshTable(manager.getProducts()); show("Sorted by name using Merge Sort."); }
                case "Find ID" -> findId();
                case "Low Stock" -> lowStock();
                case "Save" -> { manager.save(); show("Data saved to CSV file."); }
                case "Exit" -> { manager.save(); System.exit(0); }
            }
        } catch (Exception ex) {
            show("Error: " + ex.getMessage());
        }
    }

    private void addProductDialog() {
        JTextField id = new JTextField(); JTextField name = new JTextField(); JTextField category = new JTextField();
        JTextField price = new JTextField(); JTextField stock = new JTextField(); JTextField supplier = new JTextField();
        JCheckBox special = new JCheckBox("Catalogue special / discount");
        JPanel p = new JPanel(new GridLayout(0, 2));
        p.add(new JLabel("ID")); p.add(id); p.add(new JLabel("Name")); p.add(name); p.add(new JLabel("Category")); p.add(category);
        p.add(new JLabel("Price")); p.add(price); p.add(new JLabel("Stock")); p.add(stock); p.add(new JLabel("Supplier")); p.add(supplier);
        p.add(new JLabel("Special")); p.add(special);
        if (JOptionPane.showConfirmDialog(this, p, "Add Product", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            Product product = new Product(id.getText(), name.getText(), category.getText(), Double.parseDouble(price.getText()), Integer.parseInt(stock.getText()), supplier.getText(), special.isSelected());
            if (manager.addProduct(product)) { refreshTable(manager.getProducts()); show("Product added successfully."); }
            else show("Product ID already exists.");
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { show("Select a row to delete."); return; }
        String id = model.getValueAt(row, 0).toString();
        manager.deleteById(id); refreshTable(manager.getProducts()); show("Product deleted.");
    }

    private void searchName() {
        String keyword = JOptionPane.showInputDialog(this, "Enter product name keyword:");
        if (keyword != null) refreshTable(manager.linearSearchByName(keyword));
    }

    private void categoryQuery() {
        String category = JOptionPane.showInputDialog(this, "Enter category:");
        if (category != null) refreshTable(manager.searchByCategory(category));
    }

    private void findId() {
        String id = JOptionPane.showInputDialog(this, "Enter product ID:");
        if (id != null) {
            Product product = manager.binarySearchById(id);
            if (product == null) show("Product not found.");
            else refreshTable(new ArrayList<>(java.util.List.of(product)));
        }
    }

    private void lowStock() {
        String threshold = JOptionPane.showInputDialog(this, "Show products with stock less than or equal to:");
        if (threshold != null) refreshTable(manager.lowStockProducts(Integer.parseInt(threshold)));
    }

    private void refreshTable(java.util.List<Product> products) {
        model.setRowCount(0);
        for (Product p : products) model.addRow(new Object[]{p.getId(), p.getName(), p.getCategory(), p.getPrice(), p.getStock(), p.getSupplier(), p.isSpecial()});
    }

    private void show(String msg) { JOptionPane.showMessageDialog(this, msg); }
}
