package com.example.model;

public class GameState {
    private final int[][] map;
    private final int heroRow;
    private final int heroCol;

    public GameState(int[][] map, int heroRow, int heroCol) {
        this.map = new int[map.length][map[0].length];
        for (int i = 0; i < map.length; i++) {
            this.map[i] = map[i].clone();
        }
        this.heroRow = heroRow;
        this.heroCol = heroCol;
    }

    public int[][] getMap() {
        return map;
    }

    public int getHeroRow() {
        return heroRow;
    }

    public int getHeroCol() {
        return heroCol;
    }
}
