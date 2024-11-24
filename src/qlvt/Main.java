package qlvt;

import qlvt.Controller.LoginController;
import qlvt.GuiView.LoginView;

public class Main {
    public static void main(String[] args) {
        // Create and initialize the login view
        LoginView loginView = new LoginView(null); // Pass null initially
        LoginController controller = new LoginController(loginView);
        loginView.setController(controller); // Set the controller
        loginView.setVisible(true);
    }
}
