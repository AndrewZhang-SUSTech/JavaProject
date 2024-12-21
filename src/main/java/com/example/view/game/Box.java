package com.example.view.game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;

public class Box extends JComponent {

    private int row;
    private int col;

    private final int value = 10;

    public Box(int width, int height) {
        this.setSize(width, height);
        this.setLocation(5, 5);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        // 启用抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        Color woodColor = new Color(193, 154, 107);
        Color woodDarkColor = new Color(160, 120, 80);
        Color woodLightColor = new Color(210, 180, 140);

        g2d.setColor(woodColor);
        g2d.fillRect(2, 2, w-4, h-4);

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(2, 2, w-4, h-4);

        g2d.setColor(woodDarkColor);
        for (int i = 5; i < h-5; i += 8) {
            g2d.setColor(new Color(160, 120, 80, 100));
            g2d.drawLine(4, i, w-4, i);
        }

        g2d.setColor(woodLightColor);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawLine(4, 4, w-4, 4);
        g2d.drawLine(4, 4, 4, h-4);

        g2d.setColor(woodDarkColor);
        g2d.drawLine(w-4, 4, w-4, h-4);
        g2d.drawLine(4, h-4, w-4, h-4);

        drawMetalClasps(g2d, w, h);
    }

    private void drawMetalClasps(Graphics2D g2d, int w, int h) {
        Color metalColor = new Color(192, 192, 192);
        Color metalDarkColor = new Color(128, 128, 128);

        int claspSize = 8;
        g2d.setColor(metalColor);

        drawClasp(g2d, 6, 6, claspSize, metalColor, metalDarkColor);
        drawClasp(g2d, w-6-claspSize, 6, claspSize, metalColor, metalDarkColor);
        drawClasp(g2d, 6, h-6-claspSize, claspSize, metalColor, metalDarkColor);
        drawClasp(g2d, w-6-claspSize, h-6-claspSize, claspSize, metalColor, metalDarkColor);
    }

    private void drawClasp(Graphics2D g2d, int x, int y, int size, Color metalColor, Color darkColor) {
        g2d.setColor(metalColor);
        g2d.fillRect(x, y, size, size);

        g2d.setColor(darkColor);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRect(x, y, size, size);

        g2d.drawLine(x+2, y+size/2, x+size-2, y+size/2);
        g2d.drawLine(x+size/2, y+2, x+size/2, y+size-2);
    }


    public int getValue() {
        return value;
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
}
