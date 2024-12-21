package com.example.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Stack;

import javax.swing.JOptionPane;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.example.model.Direction;
import com.example.model.GameState;
import com.example.model.Level;
import com.example.model.MapMatrix;
import com.example.view.game.Box;
import com.example.view.game.GamePanel;
import com.example.view.game.GridComponent;
import com.example.view.game.Hero;

/**
 * It is a bridge to combine GamePanel(view) and MapMatrix(model) in one game.
 * You can design several methods about the game logic in this class.
 */
public class GameController {
    private GamePanel view;
    private Level level;
    private MapMatrix model;
    private Stack<GameState> history;  // 添加历史记录栈
    private boolean isPaused = false;

    public GameController(GamePanel view, Level level) {
        this.view = view;
        this.level = level;
        model = level.getMap();
        this.history = new Stack<>();  // 初始化栈
        view.setController(this);
    }

    private void saveState() {
        int[][] currentMap = new int[model.getHeight()][model.getWidth()];
        for (int i = 0; i < model.getHeight(); i++) {
            currentMap[i] = model.getMatrix()[i].clone();
        }
        Hero hero = view.getHero();
        GameState state = new GameState(
                currentMap,
                hero.getRow(),
                hero.getCol()
        );
        history.push(state);
    }

    public boolean undoMove() {
        if (history.isEmpty()) {
            return false;
        }

        GameState previousState = history.pop();
        // 恢复地图状态
        int[][] previousMap = previousState.getMap();
        int[][] currentMap = model.getMatrix();

        // 找到当前hero和box的位置，移除它们
        Hero hero = view.getHero();
        int currentHeroRow = hero.getRow();
        int currentHeroCol = hero.getCol();
        GridComponent currentHeroGrid = view.getGridComponent(currentHeroRow, currentHeroCol);
        Hero h = currentHeroGrid.removeHeroFromGrid();

        // 处理所有的box变化
        for (int i = 0; i < model.getHeight(); i++) {
            for (int j = 0; j < model.getWidth(); j++) {
                if ((currentMap[i][j] / 10 == 1) && (previousMap[i][j] / 10 != 1)) {
                    // 如果当前位置有box但是上一步没有，需要移除
                    GridComponent grid = view.getGridComponent(i, j);
                    grid.removeBoxFromGrid();
                }
                if ((currentMap[i][j] / 10 != 1) && (previousMap[i][j] / 10 == 1)) {
                    // 如果当前位置没有box但是上一步有，需要添加
                    GridComponent grid = view.getGridComponent(i, j);
                    grid.setBoxInGrid(new Box(40, 40));
                }
            }
        }

        // 恢复hero位置
        int previousHeroRow = previousState.getHeroRow();
        int previousHeroCol = previousState.getHeroCol();
        GridComponent previousHeroGrid = view.getGridComponent(previousHeroRow, previousHeroCol);
        previousHeroGrid.setHeroInGrid(h);
        h.setRow(previousHeroRow);
        h.setCol(previousHeroCol);

        // 恢复地图数据
        for (int i = 0; i < model.getHeight(); i++) {
            System.arraycopy(previousMap[i], 0, currentMap[i], 0, model.getWidth());
        }

        view.decreaseStep();  // 减少步数
        return true;
    }

    public void restartGame() {
        if (level.getSaveFile() == null) {
            level.setMap(Level.DEFAULT_MAPS().get(level.getId()));
        } else {
            // 读入数据
            StringBuilder string = new StringBuilder();
            File file=level.getSaveFile();
            System.out.println(file);
            try (BufferedReader reader = new BufferedReader(new FileReader(level.getSaveFile()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    string.append(line);
                    System.out.println(line);
                }
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(view, "文件可能已被移动或删除，请重新定向");
                level.setMap(Level.DEFAULT_MAPS().get(level.getId()));
            } catch (IOException e) {
                System.out.println("文件无法写入");
                level.setMap(Level.DEFAULT_MAPS().get(level.getId()));
            } catch (NullPointerException e) {
                level.setMap(Level.DEFAULT_MAPS().get(level.getId()));
            }

            // 转换为List
            int[][] matrix = null;
            try {
                matrix = JSON.parseObject(string.toString(), int[][].class);
                if (matrix == null) {
                    throw new NullPointerException();
                }
                for (int[] array : matrix) {
                    if (array.length != matrix[0].length) {
                        throw new JSONException();
                    }
                }
                System.out.println(Arrays.toString(matrix));
                level.setMap(new MapMatrix(matrix));
            } catch (JSONException | ClassCastException e) {
                JOptionPane.showMessageDialog(view, "文件格式错误，请检查是否为二维数组");
                level.setMap(Level.DEFAULT_MAPS().get(level.getId()));
            } catch (NullPointerException e) {
                level.setMap(Level.DEFAULT_MAPS().get(level.getId()));
            }

        }
        view.initialGame();
        view.getStepLabel().setText(String.format("Step: %d", 0));
        System.out.println("Do restart game here");
    }

    public boolean doMove(int row, int col, Direction direction) {
        saveState();
        GridComponent currentGrid = view.getGridComponent(row, col);
        int tRow = row + direction.getRow();
        int tCol = col + direction.getCol();
        GridComponent targetGrid = view.getGridComponent(tRow, tCol);
        int[][] map = model.getMatrix();
        if (map[tRow][tCol] == 0 || map[tRow][tCol] == 2) {
            model.getMatrix()[row][col] -= 20;
            model.getMatrix()[tRow][tCol] += 20;
            Hero h = currentGrid.removeHeroFromGrid();
            targetGrid.setHeroInGrid(h);
            h.setRow(tRow);
            h.setCol(tCol);
            return true;
        } else if (map[tRow][tCol] / 10 == 1) {
            int nextRow = tRow + direction.getRow();
            int nextCol = tCol + direction.getCol();

            if (nextRow >= 0 && nextRow < map.length && nextCol >= 0 && nextCol < map[0].length) {
                GridComponent nextGrid = view.getGridComponent(nextRow, nextCol);
                // 如果箱子的下一个位置是空地或目标点
                if (map[nextRow][nextCol] == 0 || map[nextRow][nextCol] == 2) {
                    // 移动box
                    model.getMatrix()[tRow][tCol] -= 10;
                    model.getMatrix()[nextRow][nextCol] += 10;
                    Box box = targetGrid.removeBoxFromGrid();
                    nextGrid.setBoxInGrid(box);
                    box.setRow(nextRow);
                    box.setCol(nextCol);
                    // 移动hero
                    model.getMatrix()[row][col] -= 20;
                    model.getMatrix()[tRow][tCol] += 20;
                    Hero h = view.getHero();
                    h.startPushAnimation(direction);
                    targetGrid.setHeroInGrid(h);
                    h.setRow(tRow);
                    h.setCol(tCol);

                    checkGameVictory();
                    checkGameFail();
                    return true;
                }
            }
        }
        return false;
    }

    private void checkGameVictory() {
        boolean isComplete = true;
        int[][] map = model.getMatrix();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] % 10 == 2 && map[i][j] / 10 != 1) {
                    isComplete = false;
                    break;
                }
            }
        }
        if (isComplete) {
            view.stopTimer();
            String finalTime = view.getTimeLabel().getText();
            JOptionPane.showMessageDialog(
                    view,
                    "Victory！\nYour time: " + finalTime.substring(6),
                    "Victory",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void checkGameFail() {
        boolean flag = true;
        int[][] map = model.getMatrix();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (i - 1 >= 0 && j - 1 >= 0) {
                    if (map[i - 1][j] == 1 && map[i][j - 1] == 1 && map[i][j] == 10) {
                        flag = false;
                        break;
                    }
                }

                if (i + 1 < map.length && j - 1 >= 0) {
                    if (map[i + 1][j] == 1 && map[i][j - 1] == 1 && map[i][j] == 10) {
                        flag = false;
                        break;
                    }
                }

                if (i + 1 < map.length && j + 1 < map[0].length) {
                    if (map[i + 1][j] == 1 && map[i][j + 1] == 1 && map[i][j] == 10) {
                        flag = false;
                        break;
                    }
                }

                if (i - 1 >= 0 && j + 1 < map[0].length) {
                    if (map[i - 1][j] == 1 && map[i][j + 1] == 1 && map[i][j] == 10) {
                        flag = false;
                        break;
                    }
                }
            }
        }

        if (!flag) {
            view.stopTimer();
            String finalTime = view.getTimeLabel().getText();
            JOptionPane.showMessageDialog(
                    view,
                    "Soft Fail！\nYour time: " + finalTime.substring(6),
                    "Soft Fail",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
    // TODO: add other methods such as loadGame,

    public GamePanel getView() {
        return view;
    }

    public void setView(GamePanel view) {
        this.view = view;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public MapMatrix getModel() {
        return model;
    }

    public void setModel(MapMatrix model) {
        this.model = model;
    }

}
