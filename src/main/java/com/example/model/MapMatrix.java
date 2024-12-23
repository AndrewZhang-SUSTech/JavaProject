package com.example.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

/**
 * This class is to record the map of one game. For example:
 * matrix =
 * {1, 1, 1, 1, 1, 1},
 * {1, 0, 0, 0, 0, 1},
 * {1, 0, 20, 12, 0, 1},
 * {1, 0, 10, 2, 0, 1},
 * {1, 1, 1, 1, 1, 1}
 * The Unit digit number cannot be changed during one game.
 * 1 represents the wall
 * 0 represents the free space
 * 2 represents the target location
 * The Then digit number can be changed during one game.
 * Ten digit 1 represents the box
 * Ten digit 2 represents the hero/player
 * So that 12 represents a box on the target location and 22 represents the
 * player on the target location.
 */
public class MapMatrix implements Serializable {
    private static final long serialVersionUID = 1L;
    int[][] matrix;

    public MapMatrix(int[][] matrix) {
        this.matrix = matrix;

    }

    public int getWidth() {
        return this.matrix[0].length;
    }

    public int getHeight() {
        return this.matrix.length;
    }

    public int getId(int row, int col) {
        return matrix[row][col];
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public static boolean isValidMatrix(int[][] matrix) {
        if (matrix == null || matrix.length == 0) {
            return false;
        }
        int cols = matrix[0].length;
        for (int[] row : matrix) {
            if (row == null || row.length != cols) {
                return false;
            }
        }
        // check if there is only one hero
        int heroCount = 0;
        int boxCount = 0;
        int targetCount = 0;
        for(int[] row : matrix) {
            for(int cell : row) {
                if (cell / 10 == 2) {
                    heroCount++;
                }
                if(cell / 10 == 1) {
                    boxCount++;
                }
                if(cell % 10 == 2) {
                    targetCount++;
                }
                if(heroCount > 1) {
                    return false;
                }
            }
        }
        if(heroCount == 0 || boxCount == 0) {
            return false;
        }
        // check if the number of boxes equals the number of targets
        if(boxCount != targetCount) {
            return false;
        }

        for(int[] row : matrix) {
            for(int cell : row) {
                if(cell % 10 == 0) {
                    continue;
                }
                if (cell % 10 == 1) {
                    continue;
                }
                if (cell / 10 == 1) {
                    continue;
                }
                if (cell / 10 == 2) {
                    continue;
                }
                if (cell % 10 == 2) {
                    continue;
                }
                if (cell / 10 == 0) {
                    continue;
                }
                return false;
            }
        }
        return true;
    }

    private int[][] deepCopy(int[][] original) {
        return Arrays.stream(original)
                .map(int[]::clone)
                .toArray(int[][]::new);
    }
    
    private void writeObject(ObjectOutputStream oos) throws IOException {
        if (!isValidMatrix(matrix)) {
            throw new IOException("矩阵数据无效，无法序列化！");
        }
        oos.defaultWriteObject();
    }
    
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        if (!isValidMatrix(matrix)) {
            throw new IOException("反序列化后，矩阵数据无效！");
        }
    }
}
