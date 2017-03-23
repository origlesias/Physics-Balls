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
public class Ball extends Thread {

    protected float x;
    protected float y;

    protected float speedx;
    protected float speedy;

    protected float radius;
    protected float mass;
    
    protected boolean active = true;
    
    protected Color color;

    protected Space parent;

    /**
     * Main constructor
     *
     * @param x
     * @param y
     * @param speedx
     * @param speedy
     * @param radius
     * @param parent
     */
    public Ball(float x, float y, float speed, float radius, float mass, float angle, Space parent) {
        this.x = x;
        this.y = y;
        this.speedx = speedx;
        this.speedy = speedy;
        this.speedx = (float) (speed * Math.cos(Math.toRadians(angle)));
        this.speedy = (float) (-speed * Math.sin(Math.toRadians(angle)));
        this.radius = radius;
        this.parent = parent;
        this.mass = mass;
        this.color = Color.BLUE;
    }

    public void movement() {
        x += speedx;
        y += speedy;
    }

    /**
     * Draw the ball in the graphics context g. Note: The drawing color in g is
     * changed to the color of the ball.
     *
     */
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval((int) (x - radius), (int) (y - radius), (int) radius * 2, (int) radius * 2);
    }

    /**
     * Main ball life cicle
     */
    @Override
    public void run() {
        while (active) {
            try {
                parent.checkCollision(this);
                movement();
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Ball.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Getters and Setters
     */
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

    public float getSpeedx() {
        return speedx;
    }

    public void setSpeedx(float speedx) {
        this.speedx = speedx;
    }

    public float getSpeedy() {
        return speedy;
    }

    public void setSpeedy(float speedy) {
        this.speedy = speedy;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public Space getParent() {
        return parent;
    }

    public void setParent(Space parent) {
        this.parent = parent;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
    public void stopBall(){
        active = false;
    } 
    

}
