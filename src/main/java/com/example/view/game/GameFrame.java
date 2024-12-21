package com.example.view.game;

import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.controller.GameController;
import com.example.model.Level;
import com.example.model.Saving;
import com.example.view.FrameUtil;
import com.example.view.level.LevelFrame;

public class GameFrame extends JFrame {
    private Level level;

    private GameController controller;
    private final JButton restartBtn;
    private final JButton loadBtn;
    private final JButton saveBtn;
    private JButton upBtn;
    private JButton downBtn;
    private JButton leftBtn;
    private JButton rightBtn;
    private JButton undoBtn;
    private GamePanel gamePanel;
    private JLabel timeLabel;
    private final LevelFrame levelFrame;
    private JButton pauseBtn;
    private final JLabel stepLabel;

    public GameFrame(LevelFrame levelFrame, int width, int height, Saving saving, int levelId) {
        this.levelFrame = levelFrame;
        this.level = saving.getLevels().get(levelId);
        this.setTitle("2024 CS109 Project");
        this.setLayout(null);
        this.setSize(width, height);
        gamePanel = new GamePanel(level);
        gamePanel.setLocation(30, height / 2 - gamePanel.getHeight() / 2);
        this.add(gamePanel);
        this.controller = new GameController(gamePanel, level);

        int buttonBaseX = gamePanel.getWidth() + 100;
        int leftButtonX = gamePanel.getWidth() + 80;
        int rightButtonX = leftButtonX + 130;
        int buttonWidth = 100;
        int buttonHeight = 40;

        this.restartBtn = FrameUtil.createButton(this, "Restart", new Point(leftButtonX, 140), buttonWidth,
                buttonHeight);
        this.pauseBtn = FrameUtil.createButton(this, "Pause", new Point(leftButtonX, 190), buttonWidth, buttonHeight);
        this.loadBtn = FrameUtil.createButton(this, "Load", new Point(leftButtonX, 240), buttonWidth, buttonHeight);
        this.undoBtn = FrameUtil.createButton(this, "Return", new Point(leftButtonX, 290), buttonWidth, buttonHeight);
        this.saveBtn = FrameUtil.createButton(this, "Save", new Point(leftButtonX, 340), buttonWidth, buttonHeight);

        int arrowSize = 50;
        int arrowBaseY = 160;
        this.upBtn = FrameUtil.createButton(this, "↑", new Point(rightButtonX + 25, arrowBaseY), arrowSize, arrowSize);
        this.leftBtn = FrameUtil.createButton(this, "←", new Point(rightButtonX - 25, arrowBaseY + 50), arrowSize,
                arrowSize);
        this.rightBtn = FrameUtil.createButton(this, "→", new Point(rightButtonX + 75, arrowBaseY + 50), arrowSize,
                arrowSize);
        this.downBtn = FrameUtil.createButton(this, "↓", new Point(rightButtonX + 25, arrowBaseY + 100), arrowSize,
                arrowSize);

        Font buttonFont = new Font("微软雅黑", Font.PLAIN, 16);
        Component[] buttons = { restartBtn, pauseBtn, loadBtn, upBtn, downBtn, leftBtn, rightBtn, undoBtn, saveBtn};
        for (Component btn : buttons) {
            if (btn instanceof JButton) {
                btn.setFont(buttonFont);
            }
        }

        this.stepLabel = FrameUtil.createJLabel(this, "Start", new Font("serif", Font.ITALIC, 22),
                new Point(buttonBaseX, 90),
                150,
                30);
        gamePanel.setStepLabel(stepLabel);

        this.timeLabel = FrameUtil.createJLabel(
                this,
                "Time: 00:00",
                new Font("serif", Font.ITALIC, 22),
                new Point(buttonBaseX, 50),
                150,
                30);
        gamePanel.setTimeLabel(timeLabel);

        stepLabel.setText("Step: " + level.getStep());

        this.restartBtn.addActionListener(_ -> {
            level.setStep(0);
            controller.restartGame();
            controller.setModel(controller.getLevel().getMap());
            controller.getView().resetTimer();
            SwingUtilities.invokeLater(() -> {
                this.revalidate();
                this.repaint();
            });

            gamePanel.requestFocusInWindow();// enable key listener
        });
        this.loadBtn.addActionListener(_ -> {
            // load button
            LoadPanel loadPanel = new LoadPanel(this);
            loadPanel.setLocationRelativeTo(null);
            gamePanel.requestFocusInWindow();// enable key listener
        });

        this.upBtn.addActionListener(_ -> {
            gamePanel.doMoveUp();
            gamePanel.requestFocusInWindow();
        });

        this.downBtn.addActionListener(_ -> {
            gamePanel.doMoveDown();
            gamePanel.requestFocusInWindow();
        });

        this.leftBtn.addActionListener(_ -> {
            gamePanel.doMoveLeft();
            gamePanel.requestFocusInWindow();
        });

        this.rightBtn.addActionListener(_ -> {
            gamePanel.doMoveRight();
            gamePanel.requestFocusInWindow();
        });

        this.undoBtn.addActionListener(_ -> {
            controller.undoMove();
            gamePanel.requestFocusInWindow();
        });

        this.pauseBtn.addActionListener(_ -> {
            gamePanel.doPause();
            gamePanel.requestFocusInWindow();
        });

        this.saveBtn.addActionListener(_ -> {
            // save
            controller.getView().stopTimer();
            saving.getLevels().set(levelId, level);

            StringBuilder intitialSavings = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new FileReader("config.json"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    intitialSavings.append(line);
                }
            } catch (FileNotFoundException e) {
                System.out.println("文件不存在");
            } catch (IOException e) {
                System.out.println("文件无法写入");
            }
            List<Saving> savings = JSON.parseArray(intitialSavings.toString(), Saving.class);
            for (int i = 0; i < savings.size() && saving.equals(savings.get(i)); i++) {
                savings.set(i, saving);
            }

            String finalSavings = JSON.toJSONString(savings, SerializerFeature.PrettyFormat);

            try {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("config.json", false))) {
                    writer.write(finalSavings);
                    System.out.println(finalSavings);
                }
            } catch (IOException e) {
                System.out.println("写入错误");
            }

            levelFrame.setVisible(true);
            this.setVisible(false);

        });
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public GameController getController() {
        return controller;
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }

    public JButton getRestartBtn() {
        return restartBtn;
    }

    public JButton getLoadBtn() {
        return loadBtn;
    }

    public JButton getSaveBtn() {
        return saveBtn;
    }

    public LevelFrame getLevelFrame() {
        return levelFrame;
    }

    public JLabel getTimeLabel() {
        return timeLabel;
    }

    public void setTimeLabel(JLabel timeLabel) {
        this.timeLabel = timeLabel;
    }

    public JButton getPauseBtn() {
        return pauseBtn;
    }

    public void setPauseBtn(JButton pauseBtn) {
        this.pauseBtn = pauseBtn;
    }

    public JLabel getStepLabel() {
        return stepLabel;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }
}
