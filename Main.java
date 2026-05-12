package com.supershop;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        ProductManager manager = new ProductManager("data/products.csv");
        try { manager.load(); } catch (Exception e) { System.out.println("Load error: " + e.getMessage()); }

        String[] options = {"Graphical User Interface", "Text-Based Interface"};
        int choice = JOptionPane.showOptionDialog(null, "Choose your preferred mode", "Supershop System", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice == 1) new ConsoleApp(manager).start();
        else SwingUtilities.invokeLater(() -> new SupershopGUI(manager).setVisible(true));
    }
}
