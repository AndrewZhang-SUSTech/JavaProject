package com.example.model;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Level {
    public static MapMatrix levelMap1() {
        return new MapMatrix(new int[][] {
                { 1, 1, 1, 1, 1, 1 },
                { 1, 20, 0, 0, 0, 1 },
                { 1, 0, 0, 10, 2, 1 },
                { 1, 0, 2, 10, 0, 1 },
                { 1, 1, 1, 1, 1, 1 },
        });
    }

    public static MapMatrix levelMap2() {
        return new MapMatrix(new int[][] {
                { 1, 1, 1, 1, 1, 1, 0 },
                { 1, 20, 0, 0, 0, 1, 1 },
                { 1, 0, 10, 10, 0, 1, 1 },
                { 1, 0, 1, 2, 0, 2, 1 },
                { 1, 0, 0, 0, 0, 0, 1 },
                { 1, 1, 1, 1, 1, 1, 1 },
        });
    }

    public static MapMatrix levelMap3() {
        return new MapMatrix(new int[][] {
                { 0, 0, 1, 1, 1, 1, 0 },
                { 1, 1, 1, 0, 0, 1, 0 },
                { 1, 20, 0, 2, 10, 1, 1 },
                { 1, 0, 0, 0, 10, 0, 1 },
                { 1, 0, 1, 2, 0, 0, 1 },
                { 1, 0, 0, 0, 0, 0, 1 },
                { 1, 1, 1, 1, 1, 1, 1 },
        });
    }

    public static MapMatrix levelMap4() {
        return new MapMatrix(new int[][] {
                { 1, 1, 1, 1, 1, 1, 0, 0 },
                { 1, 0, 0, 0, 0, 1, 1, 1 },
                { 1, 0, 0, 0, 2, 2, 0, 1 },
                { 1, 0, 10, 10, 10, 20, 0, 1 },
                { 1, 0, 0, 1, 0, 2, 0, 1 },
                { 1, 1, 1, 1, 1, 1, 1, 1 },
        });
    }

    public static MapMatrix levelMap5() {
        return new MapMatrix(new int[][] {
                { 1, 1, 1, 1, 1, 1, 0, 0 },
                { 1, 0, 0, 0, 0, 1, 1, 1 },
                { 1, 0, 0, 0, 2, 2, 0, 1 },
                { 1, 0, 10, 10, 10, 20, 0, 1 },
                { 1, 0, 0, 1, 0, 2, 0, 1 },
                { 1, 1, 1, 1, 1, 1, 1, 1 },
        });
    }

    public static List<Level> DEFAULT_LEVELS() {
        return Arrays.asList(new Level(0, levelMap1(), 0), new Level(0, levelMap2(), 1), new Level(0, levelMap3(), 2),
                new Level(0, levelMap4(), 3), new Level(0, levelMap5(), 4));
    }

    public static List<MapMatrix> DEFAULT_MAPS() {
        return Arrays.asList(levelMap1(), levelMap2(), levelMap3(), levelMap4(), levelMap5());
    }

    private int step;
    private MapMatrix map;
    private int id;
    private File saveFile;
    private int seconds;

    public Level(int step, MapMatrix map, int id) {
        this.step = step;
        this.map = map;
        this.id = id;
    }

    public Level() {
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public MapMatrix getMap() {
        return map;
    }

    public void setMap(MapMatrix map) {
        this.map = map;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public File getSaveFile() {
        return saveFile;
    }

    public void setSaveFile(File saveFile) {
        this.saveFile = saveFile;
    }

    public Integer getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }
}
