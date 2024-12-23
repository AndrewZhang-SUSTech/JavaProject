package com.example.view.game;

import java.awt.AWTEvent;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

/**
 * This class is only to enable key events.
 */
public abstract class ListenerPanel extends JPanel {
    public ListenerPanel() {
        enableEvents(AWTEvent.KEY_EVENT_MASK);
        this.setFocusable(true);
    }

    @Override
    protected void processKeyEvent(KeyEvent e) {
        super.processKeyEvent(e);
        if (e.getID() == KeyEvent.KEY_PRESSED) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_RIGHT -> doMoveRight();
                case KeyEvent.VK_LEFT -> doMoveLeft();
                case KeyEvent.VK_UP -> doMoveUp();
                case KeyEvent.VK_DOWN -> doMoveDown();
                case KeyEvent.VK_SPACE -> doPause();
                case KeyEvent.VK_BACK_SPACE -> doUndo();
            }
        }
    }


    public abstract void doMoveRight();
    public abstract void doMoveLeft();
    public abstract void doMoveUp();
    public abstract void doMoveDown();
    public abstract void doPause();
    public abstract void doUndo();
}
