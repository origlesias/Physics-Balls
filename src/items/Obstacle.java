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
public class Obstacle extends Item{

    /**
     * Global parameters
     */

    protected float width;
    protected float height;
    
//    private Space parent;
    /**
     *
     * @param x
     * @param y
     * @param width
     */
    public Obstacle(float x, float y, float mass, float width, float height, Space parent) {
        super(x/100*parent.getD().width,y/100*parent.getD().height,mass,Color.LIGHT_GRAY);
        this.width = width;
        this.height= height;
    }
    
    /**
     * Draw the ball in the graphics context g. Note: The drawing color in g is
     * changed to the color of the ball.
     *
     */
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect((int) posX, (int) posY, (int) width, (int) height);
    }
    
    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
    
    

    public synchronized boolean inRange(Ball b) {
        return b.getY() - b.getRadius() < posY + height
                && b.getY() + b.getRadius() > posY
                && b.getX() - b.getRadius() < posX + width
                && b.getX() + b.getRadius() > posX;
    }
}
