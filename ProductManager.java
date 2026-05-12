package com.supershop;

import java.io.*;
import java.util.*;

public class ProductManager {
    private final ArrayList<Product> products = new ArrayList<>();
    private final File dataFile;

    public ProductManager(String filePath) {
        this.dataFile = new File(filePath);
    }

    public ArrayList<Product> getProducts() { return products; }

    public void load() throws IOException {
        products.clear();
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            seedData();
            save();
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(dataFile))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; }
                if (!line.trim().isEmpty()) products.add(Product.fromCsv(line));
            }
        }
    }

    public void save() throws IOException {
        dataFile.getParentFile().mkdirs();
        try (PrintWriter pw = new PrintWriter(new FileWriter(dataFile))) {
            pw.println("id,name,category,price,stock,supplier,special");
            for (Product p : products) pw.println(p.toCsv());
        }
    }

    public boolean addProduct(Product product) {
        if (findById(product.getId()) != null) return false;
        products.add(product);
        return true;
    }

    public boolean deleteById(String id) {
        Product p = findById(id);
        if (p == null) return false;
        products.remove(p);
        return true;
    }

    public Product findById(String id) {
        for (Product p : products) if (p.getId().equalsIgnoreCase(id)) return p;
        return null;
    }

    public ArrayList<Product> linearSearchByName(String keyword) {
        ArrayList<Product> results = new ArrayList<>();
        for (Product p : products) {
            if (p.getName().toLowerCase().contains(keyword.toLowerCase())) results.add(p);
        }
        return results;
    }

    public ArrayList<Product> searchByCategory(String category) {
        ArrayList<Product> results = new ArrayList<>();
        for (Product p : products) {
            if (p.getCategory().equalsIgnoreCase(category)) results.add(p);
        }
        return results;
    }

    public void sortByPriceSelectionSort() {
        for (int i = 0; i < products.size() - 1; i++) {
            int min = i;
            for (int j = i + 1; j < products.size(); j++) {
                if (products.get(j).getPrice() < products.get(min).getPrice()) min = j;
            }
            Collections.swap(products, i, min);
        }
    }

    public void sortByNameMergeSort() {
        ArrayList<Product> sorted = mergeSort(products);
        products.clear();
        products.addAll(sorted);
    }

    private ArrayList<Product> mergeSort(ArrayList<Product> list) {
        if (list.size() <= 1) return new ArrayList<>(list);
        int mid = list.size() / 2;
        ArrayList<Product> left = mergeSort(new ArrayList<>(list.subList(0, mid)));
        ArrayList<Product> right = mergeSort(new ArrayList<>(list.subList(mid, list.size())));
        return merge(left, right);
    }

    private ArrayList<Product> merge(ArrayList<Product> left, ArrayList<Product> right) {
        ArrayList<Product> result = new ArrayList<>();
        int i = 0, j = 0;
        while (i < left.size() && j < right.size()) {
            if (left.get(i).getName().compareToIgnoreCase(right.get(j).getName()) <= 0) result.add(left.get(i++));
            else result.add(right.get(j++));
        }
        while (i < left.size()) result.add(left.get(i++));
        while (j < right.size()) result.add(right.get(j++));
        return result;
    }

    public Product binarySearchById(String id) {
        products.sort(Comparator.comparing(Product::getId));
        int low = 0, high = products.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            int cmp = products.get(mid).getId().compareToIgnoreCase(id);
            if (cmp == 0) return products.get(mid);
            if (cmp < 0) low = mid + 1;
            else high = mid - 1;
        }
        return null;
    }

    public ArrayList<Product> lowStockProducts(int threshold) {
        ArrayList<Product> results = new ArrayList<>();
        for (Product p : products) if (p.getStock() <= threshold) results.add(p);
        return results;
    }

    private void seedData() {
        products.add(new Product("P1001", "Coles Milk Full Cream 2L", "Dairy", 3.10, 45, "Coles Brand", true));
        products.add(new Product("P1002", "Bananas 1kg", "Fresh Produce", 4.50, 80, "Queensland Farms", false));
        products.add(new Product("P1003", "Bread Wholemeal 700g", "Bakery", 2.80, 35, "Coles Bakery", true));
        products.add(new Product("P1004", "Eggs Free Range 12 Pack", "Dairy", 6.20, 22, "Sunny Farm", false));
        products.add(new Product("P1005", "Chicken Breast 1kg", "Meat", 12.50, 18, "Aussie Poultry", false));
        products.add(new Product("P1006", "Pasta 500g", "Pantry", 1.90, 110, "Coles Brand", true));
        products.add(new Product("P1007", "Rice Long Grain 5kg", "Pantry", 15.00, 12, "SunRice", false));
        products.add(new Product("P1008", "Dishwashing Liquid 1L", "Household", 3.80, 60, "CleanCo", true));
    }
}
