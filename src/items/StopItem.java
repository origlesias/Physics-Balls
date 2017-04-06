/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package items;

import java.awt.Color;
import java.awt.Graphics;
import java.util.logging.Level;
import java.util.logging.Logger;
import physicballs.Space;
import rules.SpaceRules;

/**
 *
 * @author Liam-Portatil
 */
public class StopItem extends Thread {

    /**
     * Global parameters
     */
    private float x;
    private float y;

    private float width;

    private boolean occupied;

    Ball b = null;

    private Space parent;

    /**
     * Main constructor
     *
     * @param x
     * @param y
     * @param width
     * @param parent
     */
    public StopItem(float x, float y, float width, Space parent) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.parent = parent;
        this.occupied = false;
    }

    public synchronized void insert(Ball b){
        while (occupied && this.b != b) {
            try {
                wait();
            } catch (InterruptedException ex) {
            }
        }
        if (this.b == null) {
            this.b = b;
            occupied = true;
        }
        if (!inRange(this.b)) {
            this.b = null;
            occupied = false;
            notifyAll();
        }

    }

    /**
     * Draw the ball in the graphics context g. Note: The drawing color in g is
     * changed to the color of the ball.
     *
     */
    public void draw(Graphics g) {
        if (this.occupied) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.MAGENTA);
        }

        g.fillRect((int) x, (int) y, (int) width, (int) width);
    }

    /**
     * Main ball life cicle
     */
    @Override
    public void run() {
        while (true) {
            try {
//                if (b != null) {
//                    if (!(b.getY() - b.getRadius() < y + width
//                            && b.getY() + b.getRadius() > y
//                            && b.getX() - b.getRadius() < x + width
//                            && b.getX() + b.getRadius() > x)) {
//                        b = null;
////                        parent.notifyAll();
//                    }
//                }
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Ball.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean ocuped) {
        this.occupied = ocuped;
    }

    public void setOccupied(boolean ocuped, Ball b) {
        this.occupied = ocuped;
        this.b = b;
    }

    public void setBall(Ball b) {
        this.b = b;
    }

    public Ball getBall() {
        return this.b;
    }

    public synchronized boolean inRange(Ball b) {
        return b.getY() - b.getRadius() < y + width
                && b.getY() + b.getRadius() > y
                && b.getX() - b.getRadius() < x + width
                && b.getX() + b.getRadius() > x;
    }
}
