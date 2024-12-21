package com.example.view.game;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.example.controller.GameController;
import com.example.model.Direction;
import com.example.model.Level;
import com.example.model.MapMatrix;

/**
 * It is the subclass of ListenerPanel, so that it should implement those four
 * methods: do move left, up, down ,right.
 * The class contains a grids, which is the corresponding GUI view of the matrix
 * variable in MapMatrix.
 */
public class GamePanel extends ListenerPanel {
    private Level level;
    private GridComponent[][] grids;
    private MapMatrix model;
    private GameController controller;
    private JLabel stepLabel;
    private int steps;
    private final int GRID_SIZE = 50;
    private Timer timer;
    private JLabel timeLabel;
    private Hero hero;
    private boolean isPaused = false;

    public GamePanel(Level level) {
        this.level=level;
        this.setVisible(true);
        this.setFocusable(true);
        this.setLayout(null);
        this.model = level.getMap();
        this.setSize(model.getWidth() * GRID_SIZE + 4, model.getHeight() * GRID_SIZE + 4);
        this.grids = new GridComponent[model.getHeight()][model.getWidth()];
        timer = new Timer(1000, e -> {
            level.setSeconds(level.getSeconds() + 1);
            updateTimeLabel();
        });
        timer.start();
        initialGame();

    }

    private void updateTimeLabel() {
        if (timeLabel != null) {
            int minutes = level.getSeconds() / 60;
            int secs = level.getSeconds() % 60;
            timeLabel.setText(String.format("Time: %02d:%02d", minutes, secs));
        }
    }

    public void setTimeLabel(JLabel timeLabel) {
        this.timeLabel = timeLabel;
    }

    public JLabel getTimeLabel() {
        return this.timeLabel;
    }

    public void resetTimer() {
        level.setSeconds(0);
        updateTimeLabel();
    }

    public void stopTimer() {
        timer.stop();
    }

    public void startTimer() {
        timer.start();
    }

    public final void initialGame() {
        this.removeAll();
        this.grids = new GridComponent[level.getMap().getHeight()][level.getMap().getWidth()];
        if (timer != null) {
            timer.stop();
        }
        updateTimeLabel();
        timer = new Timer(1000, _ -> {
            level.setSeconds(level.getSeconds() + 1);
            updateTimeLabel();
        });
        timer.start();
        for (int i = 0; i < grids.length; i++) {
            for (int j = 0; j < grids[i].length; j++) {
                // Units digit maps to id attribute in GridComponent. (The no change value)
                grids[i][j] = new GridComponent(i, j, level.getMap().getId(i, j) % 10, this.GRID_SIZE);
                grids[i][j].setLocation(j * GRID_SIZE + 2, i * GRID_SIZE + 2);
                // Ten digit maps to Box or Hero in corresponding location in the GridComponent.
                // (Changed value)
                switch (level.getMap().getId(i, j) / 10) {
                    case 1 -> grids[i][j].setBoxInGrid(new Box(GRID_SIZE - 10, GRID_SIZE - 10));
                    case 2 -> {
                        this.hero = new Hero(GRID_SIZE - 16, GRID_SIZE - 16, i, j);
                        grids[i][j].setHeroInGrid(hero);
                    }
                }
                this.add(grids[i][j]);
            }
        }
        SwingUtilities.invokeLater(() -> {
            this.revalidate();
            this.repaint();
        });
    }

    public void decreaseStep() {
        this.steps--;
        this.stepLabel.setText(String.format("Step: %d", this.steps));
    }
    @Override
    public void doMoveRight() {
        if (isPaused) return;
        System.out.println("Click VK_RIGHT");
        if (controller.doMove(hero.getRow(), hero.getCol(), Direction.RIGHT)) {
            this.afterMove();
        }
    }

    @Override
    public void doMoveLeft() {
        if (isPaused) return;
        System.out.println("Click VK_LEFT");
        if (controller.doMove(hero.getRow(), hero.getCol(), Direction.LEFT)) {
            this.afterMove();
        }
    }

    @Override
    public void doMoveUp() {
        if (isPaused) return;
        System.out.println("Click VK_Up");
        if (controller.doMove(hero.getRow(), hero.getCol(), Direction.UP)) {
            this.afterMove();
        }
    }

    @Override
    public void doMoveDown() {
        if (isPaused) return;
        System.out.println("Click VK_DOWN");
        if (controller.doMove(hero.getRow(), hero.getCol(), Direction.DOWN)) {
            this.afterMove();
        }
    }

    public void doUndo() {
        if (isPaused) return;
        System.out.println("Click BACK_SPACE");
        controller.undoMove();
    }

    public void doPause() {
        isPaused = !isPaused;
        if (isPaused) {
            stopTimer();
            disableGameButtons();
        } else {
            startTimer();
            enableGameButtons();
        }

        // 更新暂停按钮文本
        JButton pauseBtn = findPauseButton();
        if (pauseBtn != null) {
            pauseBtn.setText(isPaused ? "Play" : "Pause");
        }

        requestFocusInWindow(); // 保持焦点
    }

    // 查找暂停按钮
    private JButton findPauseButton() {
        Container parent = getParent();
        if (parent != null) {
            for (Component comp : parent.getComponents()) {
                if (comp instanceof JButton && ("Pause".equals(((JButton) comp).getText())
                        || "Play".equals(((JButton) comp).getText()))) {
                    return (JButton) comp;
                }
            }
        }
        return null;
    }

    // 禁用游戏按钮
    private void disableGameButtons() {
        Container parent = getParent();
        if (parent != null) {
            for (Component comp : parent.getComponents()) {
                if (comp instanceof JButton) {
                    JButton btn = (JButton) comp;
                    String text = btn.getText();
                    if (text.equals("↑") || text.equals("↓") ||
                            text.equals("←") || text.equals("→") ||
                            text.equals("Return")) {
                        btn.setEnabled(false);
                    }
                }
            }
        }
    }

    // 启用游戏按钮
    private void enableGameButtons() {
        Container parent = getParent();
        if (parent != null) {
            for (Component comp : parent.getComponents()) {
                if (comp instanceof JButton) {
                    JButton btn = (JButton) comp;
                    String text = btn.getText();
                    if (text.equals("↑") || text.equals("↓") ||
                            text.equals("←") || text.equals("→") ||
                            text.equals("Return")) {
                        btn.setEnabled(true);
                    }
                }
            }
        }
    }

    protected void processKeyEvent(KeyEvent e) {
        // 空格键需要特殊处理，不受 enabled 状态影响
        if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_SPACE) {
            doPause();
            e.consume(); // 防止事件继续传播
            return;
        }
        // 其他按键在暂停时不响应
        if (isPaused) {
            return;
        }
        super.processKeyEvent(e);
    }

    public void afterMove() {
        Arrays.stream(level.getMap().getMatrix()).forEach(row -> {
            System.out.println(Arrays.toString(row));
        });
        this.level.setStep(level.getStep() + 1);
        this.stepLabel.setText(String.format("Step: %d", this.level.getStep()));
    }

    public void setStepLabel(JLabel stepLabel) {
        this.stepLabel = stepLabel;
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }

    public GridComponent getGridComponent(int row, int col) {
        return grids[row][col];
    }

    public GridComponent[][] getGrids() {
        return grids;
    }

    public void setGrids(GridComponent[][] grids) {
        this.grids = grids;
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

    public JLabel getStepLabel() {
        return stepLabel;
    }

    public int getGRID_SIZE() {
        return GRID_SIZE;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

}
