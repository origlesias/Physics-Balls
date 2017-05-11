/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package physicballs;

import com.sun.javafx.geom.Vec2d;
import items.Ball;
import items.Obstacle;
import items.StopItem;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import rules.SpaceRules;

/**
 *
 * @author Liam-Portatil
 */
public class Physics {
    
    
    public static void ballMovement(Ball ball, Space parent){
        checkCollision(ball, parent);
        move(ball, parent);
    }
    
    public static synchronized void move(Ball ball, Space parent){
        float dtime =2*(System.nanoTime()- ball.getTime())/(float) (Math.pow(10, 8));
        float angle= ball.getAngle();
        float accelx= (float) (ball.getAccel()* Math.cos(Math.toRadians(angle)));
        float accely= (float) (ball.getAccel()* Math.sin(Math.toRadians(angle)));
        if(ball.getSpeed()<ball.getMaxspeed()){
        ball.setSpeedx(ball.getSpeedx()+(accelx/2)*dtime*dtime);
        ball.setSpeedy(ball.getSpeedy()+(accely/2)*dtime*dtime);
        }else{
        //ball.setSpeedx((float) (ball.getMaxspeed()* Math.cos(Math.toRadians(angle))));
        //ball.setSpeedy((float) (ball.getMaxspeed()* Math.sin(Math.toRadians(angle))));
        ball.setSpeed(ball.getMaxspeed(), angle);
        }
        if(SpaceRules.gravity){
        ball.setSpeedx(ball.getSpeedx()+(parent.getGravityX()/2)*dtime*dtime);
        ball.setSpeedy(ball.getSpeedy()+(parent.getGravityY()/2)*dtime*dtime);
        }
        ball.setX(ball.getX()+ball.getSpeedx()*dtime);
        ball.setY(ball.getY()+ball.getSpeedy()*dtime);
        ball.currentTime();
    }
    
    public static void checkCollision(Ball ball, Space parent){
            Physics.ballStopItemCollision(ball, parent.getStopItems());
            Physics.ballObstaculoCollision(ball, parent.getObstaculo());
            Physics.ballWallCollision(ball, parent.getD());
            Physics.ballBallCollission(ball, parent.getBalls(), parent);
    }
    
    /**
     * Wall collision
     *
     * @param b
     * @param d
     */
    public synchronized static void ballWallCollision(Ball b, Dimension d) {
        if (b.getRadius() + b.getX() >= d.width) {
            b.setSpeedx(-Math.abs(b.getSpeedx()));
        }
        if (b.getX() - b.getRadius() <= 0) {
            b.setSpeedx(Math.abs(b.getSpeedx()));
        }
        if (b.getRadius() + b.getY() >= d.height) {
            b.setSpeedy(-Math.abs(b.getSpeedy()));
        }
        if (b.getY() - b.getRadius() <= 0) {
            b.setSpeedy(Math.abs(b.getSpeedy()));
        }
        
        if(b.getY()+b.getRadius()>d.height) b.setY(d.height-b.getRadius());
        if(b.getX()+b.getRadius()>d.width) b.setX(d.width-b.getRadius());
        if(b.getY()-b.getRadius()<0) b.setY(b.getRadius());
        if(b.getX()-b.getRadius()<0) b.setX(b.getRadius());
    }
    
    public synchronized static void ballObstaculoCollision(Ball b, Obstacle o) {
        if (o.inRange(b)) {
            if(b.getY()-b.getRadius()>o.getY()+o.getHeight()) b.setY(o.getY()+o.getHeight()+b.getRadius());
            if(b.getX()-b.getRadius()>o.getX()+o.getWidth()) b.setX(o.getX()+o.getWidth()+b.getRadius());
            if(b.getY()+b.getRadius()<o.getY()) b.setY(o.getY()-b.getRadius());
            if(b.getX()+b.getRadius()<o.getX()) b.setX(o.getX()-b.getRadius());
            if (b.getRadius() + b.getX() >= o.getX() + o.getWidth()) {
                b.setSpeedx(Math.abs(b.getSpeedx()));
            }
            if (b.getX() - b.getRadius() <= o.getX()) {
                b.setSpeedx(-Math.abs(b.getSpeedx()));
            }
            if (b.getRadius() + b.getY() >= o.getY() + o.getWidth()) {
                b.setSpeedy(Math.abs(b.getSpeedy()));
            }
            if (b.getY() - b.getRadius() <= o.getY()) {
                b.setSpeedy(-Math.abs(b.getSpeedy()));
            }
        }

    }
    
    /**
     * Ball with ball collision
     *
     * @param b
     * @param balls
     */
    public synchronized static void ballBallCollission(Ball b, CopyOnWriteArrayList<Ball> balls, Space space) {
        // Variables usadas en las comrobaciones
        double r, d_mod;
        Vec2d d;
        
        for (Ball ball : balls) { // Comprueba respecto a todas las bolas del espacio
            if (ball != b) {     // exceptuando la propia bola

                r = b.getRadius() + ball.getRadius(); // Suma de los radios de las bolas, para compro
                d = new Vec2d(ball.getX() - b.getX(), ball.getY() - b.getY()); // Vector de distancia entre centros
                d_mod = Math.hypot(d.x, d.y); // Modulo del vector de distancia

                //Checks if in range
                if (d_mod <= r) {
                    if(b.getType()==Ball.ballType.NORMAL&&ball.getType()==Ball.ballType.NORMAL){
                        bounce(b, ball);
                    } else {
                        specialCollision(b,ball, space);
                    }
                }
            }
        }
    }
    
    
    
    public static void ballStopItemCollision(Ball b, ArrayList<StopItem> items){
        for (StopItem item : items) {
            if (item.inRange(b) || item.getBall() == b) {
                item.insert(b);
            }
        }
    }
    
    public static boolean ballStopItemInRange(Ball b, StopItem item) {
        return b.getY() - b.getRadius() < item.getY() + item.getWidth()
                && b.getY() + b.getRadius() > item.getY()
                && b.getX() - b.getRadius() < item.getX() + item.getWidth()
                && b.getX() + b.getRadius() > item.getX();
    }
    
    public static void bounce(Ball b1, Ball b2){
        if(b1.isStoped()){
            calcBounceStopedBall(b2, b1);
        }else if(b2.isStoped()){
            calcBounceStopedBall(b1, b2);
    }else{
            calcBounce(b1,b2);
        }
    }
    
    

    public static void calcBounce(Ball b1, Ball b2) {
        // Ángulo de colision entre las bolas
        double collAngle = Math.atan2((b2.getY() - b1.getY()), (b2.getX() - b1.getX()));
        // Velocidad de la bola 1
        Vec2d v_b1 = new Vec2d(b1.getSpeedx(), b1.getSpeedy());
        double mod_v_b1 = Math.hypot(v_b1.x, v_b1.y);
        // Velocidad de la bola 2
        Vec2d v_b2 = new Vec2d(b2.getSpeedx(), b2.getSpeedy());
        double mod_v_b2 = Math.hypot(v_b2.x, v_b2.y);
        // Calcula direcciones
        double d1 = Math.atan2(v_b1.y, v_b1.x);
        double d2 = Math.atan2(v_b2.y, v_b2.x);
        // Calcula las nuevas velocidades relativas
        double new_xSpeed_b1 = mod_v_b1 * Math.cos(d1 - collAngle);
        double new_ySpeed_b1 = mod_v_b1 * Math.sin(d1 - collAngle);
        double new_xSpeed_b2 = mod_v_b2 * Math.cos(d2 - collAngle);
        double new_ySpeed_b2 = mod_v_b2 * Math.sin(d2 - collAngle);
        // Calcula las nuevas velocidades finales
        double fin_xSpeed_b1 = ((b1.getMass() - b2.getMass()) * new_xSpeed_b1 + (2 * b2.getMass()) * new_xSpeed_b2) / (b1.getMass() + b2.getMass());
        double fin_xSpeed_b2 = ((2 * b1.getMass()) * new_xSpeed_b1 + (b2.getMass() - b1.getMass()) * new_xSpeed_b2) / (b1.getMass() + b2.getMass());
        double fin_ySpeed_b1 = new_ySpeed_b1;
        double fin_ySpeed_b2 = new_ySpeed_b2;
        // Aplica las velocidades finales al ángulo de posicion
        b1.setSpeedx((float) (Math.cos(collAngle) * fin_xSpeed_b1 - Math.sin(collAngle) * fin_ySpeed_b1));
        b2.setSpeedx((float) (Math.sin(collAngle) * fin_xSpeed_b1 + Math.cos(collAngle) * fin_ySpeed_b1));
        b1.setSpeedy((float) (Math.cos(collAngle) * fin_xSpeed_b2 - Math.sin(collAngle) * fin_ySpeed_b2));
        b2.setSpeedy((float) (Math.sin(collAngle) * fin_xSpeed_b2 + Math.cos(collAngle) * fin_ySpeed_b2));
        
        // Pone las posiciones de las bolas como vectores para facilitar el cálculo
        Vec2d pos_b1 = new Vec2d(b1.getX(), b1.getY());
        Vec2d pos_b2 = new Vec2d(b2.getX(), b2.getY());
        // Calcula las diferencias entre las posiciones de las bolas
        Vec2d posDiff = new Vec2d(pos_b1.x - pos_b2.x, pos_b1.y - pos_b2.y);
        double mod_posDiff = Math.hypot(posDiff.x, posDiff.y);
        double scale = (((b1.getRadius() + b2.getRadius()) - mod_posDiff) / mod_posDiff);
        Vec2d mtd = new Vec2d(posDiff.x * scale, posDiff.y * scale);
        // Calcula las inversas de las masas de las bolas
        double inv_mass_b1 = 1 / b1.getMass();
        double inv_mass_b2 = 1 / b2.getMass();
        
        // Calcula las nuevas posiciones para evitar bugs de solapamiento de las bolas
        pos_b1 = new Vec2d(pos_b1.x + (mtd.x * (inv_mass_b1 / (inv_mass_b1 + inv_mass_b2))),
                pos_b1.y + (mtd.y * (inv_mass_b1 / (inv_mass_b1 + inv_mass_b2))));
        
        pos_b2 = new Vec2d(pos_b2.x - (mtd.x * (inv_mass_b2 / (inv_mass_b1 + inv_mass_b2))),
                pos_b2.y - (mtd.y * (inv_mass_b2 / (inv_mass_b1 + inv_mass_b2))));
        
        // Establece las nuevas posiciones
        b1.setX((float) pos_b1.x);
        b1.setY((float) pos_b1.y);
        b2.setX((float) pos_b2.x);
        b2.setY((float) pos_b2.y);
        
    }
    
    public static void calcBounceStopedBall(Ball b1, Ball b2) {
        // Ángulo de colision entre las bolas
        double collAngle = Math.atan2((b2.getY() - b1.getY()), (b2.getX() - b1.getX()));
        // Velocidad de la bola 1
        Vec2d v_b1 = new Vec2d(b1.getSpeedx(), b1.getSpeedy());
        double mod_v_b1 = Math.hypot(v_b1.x, v_b1.y);
        // Velocidad de la bola 2
        Vec2d v_b2 = new Vec2d(b2.getSpeedx(), b2.getSpeedy());
        double mod_v_b2 = 0;
        // Calcula direcciones
        double d1 = Math.atan2(v_b1.y, v_b1.x);
        double d2 = Math.atan2(v_b2.y, v_b2.x);
        // Calcula las nuevas velocidades relativas
        double new_xSpeed_b1 = mod_v_b1 * Math.cos(d1 - collAngle);
        double new_ySpeed_b1 = mod_v_b1 * Math.sin(d1 - collAngle);
        double new_xSpeed_b2 = mod_v_b2 * Math.cos(d2 - collAngle);
        double new_ySpeed_b2 = mod_v_b2 * Math.sin(d2 - collAngle);
        // Calcula las nuevas velocidades finales
        double fin_xSpeed_b1 = ((b1.getMass() - b2.getMass()) * new_xSpeed_b1 + (2 * b2.getMass()) * new_xSpeed_b2) / (b1.getMass() + b2.getMass());
        double fin_xSpeed_b2 = ((2 * b1.getMass()) * new_xSpeed_b1 + (b2.getMass() - b1.getMass()) * new_xSpeed_b2) / (b1.getMass() + b2.getMass());
        double fin_ySpeed_b1 = new_ySpeed_b1;
        double fin_ySpeed_b2 = new_ySpeed_b2;
        // Aplica las velocidades finales al ángulo de posicion
        b1.setSpeedx((float) (Math.cos(collAngle) * fin_xSpeed_b1 - Math.sin(collAngle) * fin_ySpeed_b1));
        b1.setSpeedy((float) (Math.cos(collAngle) * fin_xSpeed_b2 - Math.sin(collAngle) * fin_ySpeed_b2));
        
        // Pone las posiciones de las bolas como vectores para facilitar el cálculo
        Vec2d pos_b1 = new Vec2d(b1.getX(), b1.getY());
        Vec2d pos_b2 = new Vec2d(b2.getX(), b2.getY());
        // Calcula las diferencias entre las posiciones de las bolas
        Vec2d posDiff = new Vec2d(pos_b1.x - pos_b2.x, pos_b1.y - pos_b2.y);
        double mod_posDiff = Math.hypot(posDiff.x, posDiff.y);
        double scale = (((b1.getRadius() + b2.getRadius()) - mod_posDiff) / mod_posDiff);
        Vec2d mtd = new Vec2d(posDiff.x * scale, posDiff.y * scale);
        // Calcula las inversas de las masas de las bolas
        double inv_mass_b1 = 1 / b1.getMass();
        double inv_mass_b2 = 1 / b2.getMass();
        
        // Calcula las nuevas posiciones para evitar bugs de solapamiento de las bolas
        pos_b1 = new Vec2d(pos_b1.x + (mtd.x * (inv_mass_b1 / (inv_mass_b1 + inv_mass_b2))),
                pos_b1.y + (mtd.y * (inv_mass_b1 / (inv_mass_b1 + inv_mass_b2))));
        
        pos_b2 = new Vec2d(pos_b2.x - (mtd.x * (inv_mass_b2 / (inv_mass_b1 + inv_mass_b2))),
                pos_b2.y - (mtd.y * (inv_mass_b2 / (inv_mass_b1 + inv_mass_b2))));
        
        // Establece las nuevas posiciones
        b1.setX((float) pos_b1.x);
        b1.setY((float) pos_b1.y);
        b2.setX((float) pos_b2.x);
        b2.setY((float) pos_b2.y);
        
        
    }
    
    public synchronized static void specialCollision(Ball b1, Ball b2, Space space){
        if(b1.getType()==Ball.ballType.EXPLOSIVE||b2.getType()==Ball.ballType.EXPLOSIVE){
        if(b1.getType()==Ball.ballType.EXPLOSIVE){
            explode(b1, space);
        }
        if(b2.getType()==Ball.ballType.EXPLOSIVE){
            explode(b2,space);
        }
        }else{
            if(b1.getType()==Ball.ballType.BULLET&&b2.getType()==Ball.ballType.BULLET){
                if(Math.hypot(b1.getSpeedx(), b1.getSpeedy())>8||Math.hypot(b2.getSpeedx(), b2.getSpeedy())>8){
                    impact(b1,b2, space);
                }else{
                    bounce(b1, b2);
                }
        }else if(b1.getType()==Ball.ballType.BULLET){
                if(Math.hypot(b1.getSpeedx(), b1.getSpeedy())>8){
                    impact(b1,b2, space);
                }else{
                    bounce(b1, b2);
                }
            }else{
                if(Math.hypot(b2.getSpeedx(), b2.getSpeedy())>8){
                    impact(b2,b1, space);
                }else{
                    bounce(b1, b2);
                }
            }
        }
    }
    
    public static void explode(Ball b, Space space){
        if(space.getBalls().contains(b)){
        for(int i=0;i!=10;i++){
            float angle= (float) (i*36+(new Random().nextInt(36)));
            Ball bullet=new Ball((float) (b.getX()+b.getRadius()/10*Math.cos(Math.toRadians(angle))),(float) (b.getY()+b.getRadius()/10*Math.sin(Math.toRadians(angle))), 2, 1, b.getRadius()/10, angle, "B");
            space.getBalls().add(bullet);
            new Thread(new ThreadBall(bullet,space)).start();
        }
        space.delete(space.getBalls().indexOf(b));
        }
    }
    
    public static void impact(Ball bullet, Ball ball, Space space){
        if(space.getBalls().contains(bullet)){
        space.delete(space.getBalls().indexOf(bullet));
        ball.setMass(ball.getMass()+bullet.getMass());
        ball.setRadius(ball.getMass()+bullet.getMass()/10);
        }
    }
}
