package com.example.view.level;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.example.model.Saving;
import com.example.view.game.GameFrame;
import com.example.view.login.LoginFrame;

public class LevelFrame extends JFrame {

    private Saving saving;
    private GameFrame gameFrame;
    private LoginFrame loginFrame;

    public LevelFrame(int width, int height) {
        this.setTitle("Level");
        this.setLayout(new GridBagLayout());
        this.setSize(width, height);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();

        JButton button = new JButton("Select Level");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets.set(30, 30, 100, 30);
        add(button, gbc);

        JButton button2 = new JButton("Log Out");
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets.set(30, 30, 100, 30);
        button2.addActionListener(_ -> {
            loginFrame.setVisible(true);
            this.setVisible(false);
        });
        add(button2, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets.set(10, 10, 10, 10);
        gbc.gridy = 1;
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setLayout(new GridBagLayout());
        for (int i = 0; i < 5; i++) {
            int levelIndex = i;
            gbc.gridx = i;
            popupMenu.add(new JMenuItem("Level " + (levelIndex + 1)) {
                {
                    addActionListener(_ -> startGame(levelIndex));
                }
            }, gbc);
        }
        button.addActionListener(_ -> {
            popupMenu.show(button, 0, button.getHeight() + 10);
        });
        setVisible(true);

    }

    private void startGame(int id) {
        gameFrame = new GameFrame(this, 50 * saving.getLevels().get(id).getMap().getWidth() + 400, 450, saving, id);
        this.setVisible(false);
        gameFrame.setVisible(true);
    }

    public void setSaving(Saving saving) {
        this.saving = saving;
    }

    public Saving getSaving() {
        return saving;
    }

    public GameFrame getGameFrame() {
        return gameFrame;
    }

    public void setGameFrame(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
    }

    public void reLoadPage(int id) {
        gameFrame.getRestartBtn().doClick();
        gameFrame.getSaveBtn().doClick();
        gameFrame.setVisible(false);
        gameFrame.getLevel().setSeconds(0);
        gameFrame.dispose();
        gameFrame = new GameFrame(this, 50 * saving.getLevels().get(id).getMap().getWidth() + 400, 450, saving, id);
        gameFrame.setVisible(true);
    }

    public LoginFrame getLoginFrame() {
        return loginFrame;
    }

    public void setLoginFrame(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
    }

}
