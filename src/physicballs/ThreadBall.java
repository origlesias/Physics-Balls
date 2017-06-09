/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package physicballs;

import items.Ball;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Oriol
 */
public class ThreadBall implements Runnable{

    Ball b;

    public ThreadBall(Ball b) {
        this.b = b;
    }
    
    
    
    @Override
    public void run() {
        b.currentTime();
        while (true) {
            Physics.ballMovement(b);
            do {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Ball.class.getName()).log(Level.SEVERE, null, ex);
                }
            } while (!b.active);
    }
    }
    
    
    
}
