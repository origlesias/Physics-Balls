/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package items;

import java.awt.Color;
import java.awt.Graphics;
import physicballs.Space;

/**
 *
 * @author Liam-Portatil
 */
public class StopItem extends Obstacle{

    /**
     * Global parameters
     */


    private boolean occupied;

    Ball b = null;

    private Space parent;

    /**
     * Main constructor
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param parent
     */
    public StopItem(float x, float y, float width, float height, Space parent) {
        super(x, y, width, height, parent);
        this.parent = parent;
        this.occupied = false;
    }
    
    public StopItem(){}

    public synchronized void insert(Ball b){
        if(occupied && this.b != b) {
            try {
                b.setStoped(true);
                wait();
                b.currentTime();
                b.setStoped(false);
            } catch (InterruptedException ex) {
            }
        }
        if (this.b == null) {
            this.b = b;
            occupied = true;
        }
        if (!intersects(this.b)||!parent.getBalls().contains(this.b)) {
            notifyBalls();
        }
    }

    public synchronized void notifyBalls(){
            this.b = null;
            occupied = false;
            notifyAll();
    }
    
    /**
     * Draw the ball in the graphics context g. Note: The drawing color in g is
     * changed to the color of the ball.
     *
     */
    @Override
    public void draw(Graphics g) {
        if (this.occupied) {
            g.setColor(Color.green);
        } else {
            g.setColor(Color.MAGENTA);
        }
        g.fillRect((int) posX, (int) posY, (int) width, (int) height);
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
}
