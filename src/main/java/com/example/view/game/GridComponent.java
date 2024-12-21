package com.example.view.game;

import com.example.view.game.Hero;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class GridComponent extends JComponent {
    private int row;
    private int col;
    private final int id;
    private Hero hero;
    private Box box;
    static Color color = new Color(246, 246, 229);

    private static final Color WALL_COLOR = new Color(90, 77, 65);
    private static final Color FLOOR_COLOR = new Color(230, 220, 210);
    private static final Color DESTINATION_COLOR = new Color(255, 215, 0, 100);

    private static final Color DIAMOND_COLOR = new Color(0, 191, 255);    // 深蓝色钻石
    private static final Color DIAMOND_LIGHT = new Color(135, 206, 250);  // 浅蓝色高光
    private static final Color DIAMOND_SHADOW = new Color(0, 127, 255);   // 阴影色

    public GridComponent(int row, int col, int id, int gridSize) {
        this.setSize(gridSize, gridSize);
        this.row = row;
        this.col = col;
        this.id = id;
        this.setLocation(8, 8);
        setLayout(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color borderColor = color;
        switch (id % 10) {
            case 1: // 墙
                g2d.setColor(WALL_COLOR);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.setColor(WALL_COLOR.darker());
                g2d.fillRect(getWidth()-3, 0, 3, getHeight());
                g2d.fillRect(0, getHeight()-3, getWidth(), 3);
                borderColor = WALL_COLOR.darker();
                break;

            case 0: // 地板
                g2d.setColor(FLOOR_COLOR);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                break;

            case 2: // 目标点
                // 绘制地板背景
                g2d.setColor(FLOOR_COLOR);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // 计算钻石大小和位置
                int diamondSize = getWidth() / 2;
                int margin = (getWidth() - diamondSize) / 2;
                // 创建钻石的点
                int[] xPoints = {
                        margin + diamondSize/2,           // 顶点
                        margin + diamondSize,             // 右点
                        margin + diamondSize/2,           // 底点
                        margin                            // 左点
                };
                int[] yPoints = {
                        margin,                           // 顶点
                        margin + diamondSize/2,           // 右点
                        margin + diamondSize,             // 底点
                        margin + diamondSize/2            // 左点
                };
                // 绘制钻石主体
                g2d.setColor(DIAMOND_COLOR);
                g2d.fillPolygon(xPoints, yPoints, 4);
                // 添加高光效果
                int[] lightXPoints = {
                        xPoints[0],
                        xPoints[1],
                        xPoints[0]
                };
                int[] lightYPoints = {
                        yPoints[0],
                        yPoints[1],
                        yPoints[1]
                };
                g2d.setColor(DIAMOND_LIGHT);
                g2d.fillPolygon(lightXPoints, lightYPoints, 3);

                // 添加阴影效果
                int[] shadowXPoints = {
                        xPoints[0],
                        xPoints[2],
                        xPoints[3]
                };
                int[] shadowYPoints = {
                        yPoints[1],
                        yPoints[2],
                        yPoints[3]
                };
                g2d.setColor(DIAMOND_SHADOW);
                g2d.fillPolygon(shadowXPoints, shadowYPoints, 3);

                // 添加边框
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(1));
                g2d.drawPolygon(xPoints, yPoints, 4);
                break;
        }

        Border border = BorderFactory.createLineBorder(borderColor, 1);
        this.setBorder(border);
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getId() {
        return id;
    }

    public void setHeroInGrid(Hero hero) {
        this.hero = hero;
        this.add(hero);
    }

    public void setBoxInGrid(Box box) {
        this.box = box;
        this.add(box);
    }

    public Hero removeHeroFromGrid() {
        this.remove(this.hero);
        Hero h = this.hero;
        this.hero = null;
        this.revalidate();
        this.repaint();
        return h;
    }

    public Box removeBoxFromGrid() {
        this.remove(this.box);
        Box b = this.box;
        this.box = null;
        this.revalidate();
        this.repaint();
        return b;
    }
}