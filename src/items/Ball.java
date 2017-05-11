/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package items;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.logging.Level;
import java.util.logging.Logger;
import physicballs.Physics;
import physicballs.Space;
import rules.SpaceRules;

/**
 *
 * @author Liam-Portatil
 */
public class Ball extends Item {

    
    private float speedx;
    private float speedy;
    private float maxspeed=20;

    private float accel=0;
    
    private boolean stoped= false;
    
    private float radius;
    
    public enum ballType{
        NORMAL, EXPLOSIVE, BULLET;
    }
    
    ballType type;
    
    private long time;
    
    public boolean active = true;


    /**
     * Main constructor
     *
     * @param x
     * @param y
     * @param speed
     * @param radius
     * @param parent
     */
    public Ball(float x, float y, float speed, float accel, float radius, float angle, String type) {
        super(x,y,radius,Color.BLUE);
        speedx = (float) (speed * Math.cos(Math.toRadians(angle)));
        speedy = (float) (-speed * Math.sin(Math.toRadians(angle)));
        this.accel= accel;
        this.radius = radius;
        setType(type);
        color();
    }
    
    public Ball(float x, float y, float speedx, float speedy, float radius, String type){
        super(x,y,radius,Color.BLUE);
        this.speedx= speedx;
        this.speedy= speedy;
        this.radius= radius;
        setType(type);
        color();
    }
    
    public Ball(){}
    
    public void color(){
        switch(type){
            case NORMAL:
                this.setColor(Color.BLUE);
                break;
            case EXPLOSIVE:
                this.setColor(Color.RED);
                break;
            case BULLET:
                this.setColor(Color.ORANGE);
                break;
        }
    }
    
    public ballType getType(){
        return type;
    }
    
    public void setType(String type){
        switch(type){
            case "N":
                this.type= ballType.NORMAL;
                break;
            case "E":
                this.type= ballType.EXPLOSIVE;
                break;
            case "B":
                this.type= ballType.BULLET;
                break;
        }
    }
    
    

    /**
     * Draw the ball in the graphics context g. Note: The drawing color in g is
     * changed to the color of the ball.
     *
     */
    public void draw(Graphics g) {
        Graphics2D gg= (Graphics2D) g;
        gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gg.setColor(color);
        gg.fillOval((int) (posX - radius), (int) (posY - radius), (int) radius * 2, (int) radius * 2);
        }

    /**
     * Main ball life cicle
     */
//    @Override
//    public void run() {
//        time = System.nanoTime();
//        while (true) {
//            Physics.ballMovement(this,parent);
//            do {
//                try {
//                    Thread.sleep(15);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(Ball.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            } while (!active);
//        }
//    }

    /**
     * Getters and Setters
     */

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
    
    public void stopBall(){
        active = false;
    } 
    
    public void currentTime(){
        time= System.nanoTime();
    }

    public long getTime() {
        return time;
    }

    public float getAccel() {
        return accel;
    }

    public void setAccel(float accel) {
        this.accel = accel;
    }

    public float getMaxspeed() {
        return maxspeed;
    }

    public void setMaxspeed(float maxspeed) {
        this.maxspeed = maxspeed;
    }
    
    
    
    public double getSpeed(){
        return Math.hypot(speedx, speedy);
    }
    
    public void setSpeed(float speed, float angle){
        speedx = (float) (speed * Math.cos(Math.toRadians(angle)));
        speedy = (float) (speed * Math.sin(Math.toRadians(angle)));
    }
    
    public float getAngle(){
        return (float) Math.toDegrees(Math.atan2(speedy, speedx));
    }

    public boolean isStoped() {
        return stoped;
    }

    public void setStoped(boolean stoped) {
        this.stoped = stoped;
    }
    
    
    
}
