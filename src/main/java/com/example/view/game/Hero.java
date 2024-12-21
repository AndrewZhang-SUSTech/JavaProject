package com.example.view.game;

import com.example.model.Direction;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class Hero extends JComponent {
    private int row;
    private int col;
    private final int value = 20;
    private static final Color SKIN_COLOR = new Color(255, 218, 185);
    private static final Color CLOTH_COLOR = new Color(70, 130, 180);
    private static final Color SHOE_COLOR = new Color(139, 69, 19);

    // 动作状态
    private Direction pushDirection = null;
    private boolean isPushing = false;
    private Timer animationTimer;
    private static final int ANIMATION_DURATION = 200; // 动画持续时间（毫秒）

    public Hero(int width, int height, int row, int col) {
        this.row = row;
        this.col = col;
        this.setSize(width, height);
        this.setLocation(8, 8);

        // 初始化动画计时器
        animationTimer = new Timer(ANIMATION_DURATION, e -> {
            isPushing = false;
            pushDirection = null;
            repaint();
            ((Timer)e.getSource()).stop();
        });
        animationTimer.setRepeats(false);
    }

    // 推动动作方法
    public void startPushAnimation(Direction direction) {
        isPushing = true;
        pushDirection = direction;
        // 重置并启动动画计时器
        animationTimer.restart();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // 计算位移
        int offsetX = 0;
        int offsetY = 0;
        if (isPushing && pushDirection != null) {
            switch (pushDirection) {
                case LEFT -> offsetX = -3;
                case RIGHT -> offsetX = 3;
                case UP -> offsetY = -3;
                case DOWN -> offsetY = 3;
            }
        }

        // 调整关键尺寸
        int headSize = w / 3;
        int bodyWidth = w / 2;
        int bodyHeight = h / 2;
        int legLength = h / 4;
        int armWidth = w/7;
        int armLength = h/2;

        // 绘制头部
        g2d.setColor(SKIN_COLOR);
        g2d.fillOval((w - headSize) / 2 + offsetX, 2 + offsetY, headSize, headSize);

        // 绘制身体
        int[] bodyX = {
                w/2 - bodyWidth/2 + offsetX,
                w/2 + bodyWidth/2 + offsetX,
                w/2 + bodyWidth/3 + offsetX,
                w/2 - bodyWidth/3 + offsetX
        };
        int[] bodyY = {
                headSize + 2 + offsetY,
                headSize + 2 + offsetY,
                headSize + bodyHeight + offsetY,
                headSize + bodyHeight + offsetY
        };
        g2d.setColor(CLOTH_COLOR);
        g2d.fillPolygon(bodyX, bodyY, 4);

        // 设置手臂角度
        double defaultArmAngle = Math.PI / 30;
        double pushArmAngle = Math.PI / 6;

        // 绘制手臂
        drawArm(g2d, true, w, headSize, bodyWidth, armWidth, armLength, offsetX, offsetY, defaultArmAngle, pushArmAngle);
        drawArm(g2d, false, w, headSize, bodyWidth, armWidth, armLength, offsetX, offsetY, defaultArmAngle, pushArmAngle);

        // 绘制腿
        int legWidth = w/6;
        // 左腿
        g2d.setColor(CLOTH_COLOR);
        g2d.fillRoundRect(
                w/2 - bodyWidth/4 - legWidth/2 + offsetX,
                headSize + bodyHeight + offsetY,
                legWidth,
                legLength,
                legWidth/2,
                legWidth/2
        );
        // 右腿
        g2d.fillRoundRect(
                w/2 + bodyWidth/4 - legWidth/2 + offsetX,
                headSize + bodyHeight + offsetY,
                legWidth,
                legLength,
                legWidth/2,
                legWidth/2
        );

        // 绘制鞋子
        g2d.setColor(SHOE_COLOR);
        // 左鞋
        g2d.fillRoundRect(
                w/2 - bodyWidth/4 - legWidth/2 - 2 + offsetX,
                headSize + bodyHeight + legLength - 4 + offsetY,
                legWidth + 4,
                8,
                4,
                4
        );
        // 右鞋
        g2d.fillRoundRect(
                w/2 + bodyWidth/4 - legWidth/2 - 2 + offsetX,
                headSize + bodyHeight + legLength - 4 + offsetY,
                legWidth + 4,
                8,
                4,
                4
        );

        // 绘制面部特征
        g2d.setColor(Color.BLACK);
        // 左眼
        g2d.fillOval(w/2 - headSize/4 + offsetX, headSize/2 + offsetY, 3, 3);
        // 右眼
        g2d.fillOval(w/2 + headSize/4 - 3 + offsetX, headSize/2 + offsetY, 3, 3);
        // 嘴巴（微笑）
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawArc(
                w/2 - headSize/4 + offsetX,
                headSize/2 + offsetY,
                headSize/2,
                headSize/4,
                0,
                -180
        );
    }

    private void drawArm(Graphics2D g2d, boolean isLeft, int w, int headSize,
                         int bodyWidth, int armWidth, int armLength,
                         int offsetX, int offsetY, double defaultAngle, double pushAngle) {
        int baseX = isLeft ?
                w/2 - bodyWidth/2 - armWidth/2 :
                w/2 + bodyWidth/2 - armWidth/2;
        int baseY = headSize + 2;

        // 计算手臂的旋转角度和长度
        double rotation = isLeft ? -defaultAngle : defaultAngle;
        int currentArmLength = armLength;

        if (isPushing && pushDirection != null) {
            switch (pushDirection) {
                case LEFT:
                    if (isLeft) {
                        rotation = pushAngle;
                        currentArmLength = (int)(armLength * 1.2);
                    }
                    break;
                case RIGHT:
                    if (!isLeft) {
                        rotation = -pushAngle;
                        currentArmLength = (int)(armLength * 1.5);
                        baseX -= armWidth/2;
                    }
                    break;
                case UP:
                    // 向上移动时，两只手臂都举过头顶
                    rotation = isLeft ? -Math.PI/2.2 : Math.PI/2.2; // 接近垂直向上的角度
                    currentArmLength = (int)(armLength * 1.3); // 增加手臂长度
                    // 调整手臂位置
                    if (isLeft) {
                        baseX += armWidth * 1.5;
                        baseY -= armWidth;
                    } else {
                        baseX -= armWidth * 1.5;
                        baseY -= armWidth;
                    }
                    break;
                case DOWN:
                    // 向下移动时，两只手臂都向下伸展
                    rotation = 0;
                    currentArmLength = (int)(armLength * 1.3);
                    if (isLeft) {
                        baseX += armWidth;
                    } else {
                        baseX -= armWidth;
                    }
                    break;
            }
        }

        // 保存当前变换状态
        AffineTransform oldTransform = g2d.getTransform();

        // 设置旋转中心点
        g2d.translate(baseX + offsetX + armWidth/2, baseY + offsetY);
        g2d.rotate(rotation);
        g2d.translate(-(baseX + offsetX + armWidth/2), -(baseY + offsetY));

        // 绘制手臂
        g2d.setColor(CLOTH_COLOR);

        // 向右推动时调整手臂宽度
        int currentArmWidth = armWidth;
        if (isPushing && pushDirection == Direction.RIGHT && !isLeft) {
            currentArmWidth = (int)(armWidth * 0.8); // 使手臂稍细一些
        }

        currentArmWidth = armWidth;
        if (isPushing && pushDirection == Direction.DOWN) {
            currentArmWidth = (int)(armWidth * 0.9); // 使手臂稍细一些
        }

        g2d.fillRoundRect(
                baseX + offsetX,
                baseY + offsetY,
                currentArmWidth,
                currentArmLength,
                currentArmWidth/2,
                currentArmWidth/2
        );

        // 绘制手掌
        int handSize = currentArmWidth + 2;
        g2d.setColor(SKIN_COLOR);
        g2d.fillOval(
                baseX + offsetX - 1,
                baseY + offsetY + currentArmLength - handSize/2,
                handSize,
                handSize
        );

        // 恢复变换状态
        g2d.setTransform(oldTransform);
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