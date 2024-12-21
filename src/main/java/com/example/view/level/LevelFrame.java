package com.example.view.level;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import com.example.model.Saving;
import com.example.view.game.GameFrame;

public class LevelFrame extends JFrame {

    private Saving saving;
    private GameFrame gameFrame;

    public LevelFrame(int width, int height) {
        this.setTitle("Level");
        this.setLayout(new BorderLayout());
        this.setSize(width, height);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        add(panel);
        JButton button = new JButton("Select Level");
        panel.add(button);
        JPopupMenu popupMenu = new JPopupMenu();
        for (int i = 0; i < 5; i++) {
            int levelIndex = i;
            popupMenu.add(new JMenuItem("Level " + (levelIndex + 1)) {
                {
                    addActionListener(_ -> startGame(levelIndex));
                }
            });
        }
        button.addActionListener(_ -> {
            popupMenu.show(button, 0, button.getHeight());
        });
        setVisible(true);

    }

    private void startGame(int id) {
        gameFrame = new GameFrame(this, 50*saving.getLevels().get(id).getMap().getWidth()+400, 450, saving, id);
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
        gameFrame = new GameFrame(this, 50*saving.getLevels().get(id).getMap().getWidth()+400, 450, saving, id);
        gameFrame.setVisible(true);
    }

}
