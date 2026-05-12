package com.supershop;

import java.util.*;

public class ConsoleApp {
    private final Scanner sc = new Scanner(System.in);
    private final ProductManager manager;

    public ConsoleApp(ProductManager manager) { this.manager = manager; }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Comprehensive Supershop Management System - TBI ===");
            System.out.println("1. View all products");
            System.out.println("2. Add product");
            System.out.println("3. Delete product");
            System.out.println("4. Search by name");
            System.out.println("5. Query by category");
            System.out.println("6. Sort by price (Selection Sort)");
            System.out.println("7. Sort by name (Merge Sort)");
            System.out.println("8. Binary search by product ID");
            System.out.println("9. Low stock report");
            System.out.println("0. Save and exit");
            System.out.print("Choose: ");
            String choice = sc.nextLine();
            try {
                switch (choice) {
                    case "1" -> printProducts(manager.getProducts());
                    case "2" -> addProduct();
                    case "3" -> deleteProduct();
                    case "4" -> searchByName();
                    case "5" -> queryByCategory();
                    case "6" -> { manager.sortByPriceSelectionSort(); printProducts(manager.getProducts()); }
                    case "7" -> { manager.sortByNameMergeSort(); printProducts(manager.getProducts()); }
                    case "8" -> binarySearch();
                    case "9" -> lowStock();
                    case "0" -> { manager.save(); running = false; }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void addProduct() {
        System.out.print("ID: "); String id = sc.nextLine();
        System.out.print("Name: "); String name = sc.nextLine();
        System.out.print("Category: "); String category = sc.nextLine();
        System.out.print("Price: "); double price = Double.parseDouble(sc.nextLine());
        System.out.print("Stock: "); int stock = Integer.parseInt(sc.nextLine());
        System.out.print("Supplier: "); String supplier = sc.nextLine();
        System.out.print("On special? true/false: "); boolean special = Boolean.parseBoolean(sc.nextLine());
        boolean ok = manager.addProduct(new Product(id, name, category, price, stock, supplier, special));
        System.out.println(ok ? "Product added." : "Product ID already exists.");
    }

    private void deleteProduct() {
        System.out.print("Enter ID to delete: ");
        System.out.println(manager.deleteById(sc.nextLine()) ? "Deleted." : "Product not found.");
    }

    private void searchByName() {
        System.out.print("Enter name keyword: ");
        printProducts(manager.linearSearchByName(sc.nextLine()));
    }

    private void queryByCategory() {
        System.out.print("Enter category: ");
        printProducts(manager.searchByCategory(sc.nextLine()));
    }

    private void binarySearch() {
        System.out.print("Enter exact product ID: ");
        Product p = manager.binarySearchById(sc.nextLine());
        if (p == null) System.out.println("Product not found."); else printProducts(new ArrayList<>(List.of(p)));
    }

    private void lowStock() {
        System.out.print("Enter threshold: ");
        printProducts(manager.lowStockProducts(Integer.parseInt(sc.nextLine())));
    }

    private void printProducts(List<Product> products) {
        System.out.printf("%-8s %-25s %-15s %-9s %-6s %-18s %-5s%n", "ID", "Name", "Category", "Price", "Stock", "Supplier", "Sale");
        for (Product p : products) System.out.println(p);
        if (products.isEmpty()) System.out.println("No records found.");
    }
}
