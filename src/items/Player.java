/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package items;

import java.awt.Color;
import java.awt.Dimension;
import java.util.logging.Level;
import java.util.logging.Logger;
import physicballs.Physics;
import physicballs.Space;

/**
 *
 * @author Liam-Portatil
 */
public class Player extends Ball {

    public Player(float x, float y, float speed, float accel, float radius, float mass, float angle, Space parent) {
        super(x, y, speed, accel, radius, mass, angle, parent, "N");
        color = Color.red;
    }

    public void moveUp() {
        posY -= speedy;
    }

    public void moveDown() {
        posY += speedy;
    }

    public void moveLeft() {
        posX += speedx;
    }

    public void moveRight() {
        posX -= speedx;
    }
    
   
    public void checkCollision(){
        Physics.ballPlayerCollision(this, parent.getPlayer(),parent);
        Physics.playerBallCollission(this, parent.getBalls());
//        ballStopItemCollision(p, stopItems);
        Physics.ballWallCollision(this, parent.getD());
    }

    @Override
    public void run() {

        while (true) {
            try {
                checkCollision();
//                movement();
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Ball.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
