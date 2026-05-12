package com.supershop;

public class Product {
    private String id;
    private String name;
    private String category;
    private double price;
    private int stock;
    private String supplier;
    private boolean special;

    public Product(String id, String name, String category, double price, int stock, String supplier, boolean special) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.supplier = supplier;
        this.special = special;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public String getSupplier() { return supplier; }
    public boolean isSpecial() { return special; }

    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setPrice(double price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }
    public void setSupplier(String supplier) { this.supplier = supplier; }
    public void setSpecial(boolean special) { this.special = special; }

    public String toCsv() {
        return String.join(",", escape(id), escape(name), escape(category), String.valueOf(price), String.valueOf(stock), escape(supplier), String.valueOf(special));
    }

    private String escape(String value) {
        return value.replace(",", " ");
    }

    public static Product fromCsv(String line) {
        String[] p = line.split(",", -1);
        return new Product(p[0], p[1], p[2], Double.parseDouble(p[3]), Integer.parseInt(p[4]), p[5], Boolean.parseBoolean(p[6]));
    }

    @Override
    public String toString() {
        return String.format("%-8s %-25s %-15s $%-8.2f %-6d %-18s %-5s", id, name, category, price, stock, supplier, special ? "Yes" : "No");
    }
}
