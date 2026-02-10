package com.revworkforce.main;

import com.revworkforce.ui.LoginMenu;

public class App {
    public static void main(String[] args) {
        System.out.println("Welcome to RevWorkForce!");
        LoginMenu loginMenu = new LoginMenu();
        loginMenu.display();
    }
}
