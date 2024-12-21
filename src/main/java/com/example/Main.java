package com.example;

import javax.swing.SwingUtilities;

import com.example.view.level.LevelFrame;
import com.example.view.login.LoginFrame;

public class Main {
    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame(280, 280);
            loginFrame.setVisible(true);
            LevelFrame levelFrame = new LevelFrame(500, 200);
            levelFrame.setVisible(false);
            loginFrame.setLevelFrame(levelFrame);
        });
    }
}
