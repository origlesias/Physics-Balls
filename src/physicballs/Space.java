/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package physicballs;

import com.sun.javafx.geom.Vec2d;
import items.Ball;
import items.Obstaculo;
import items.Player;
import items.StopItem;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import rules.SpaceRules;

/**
 *
 * @author Liam-Portatil
 */
public class Space extends Canvas implements Runnable {

    /**
     * Global parameters
     */
    private int spaceWidth;
    private int spaceHeight;

    private int ballLimit = 3;
    private int stopItemsLimit = 1;

    private ArrayList<Ball> balls;
    private ArrayList<StopItem> stopItems;

    private Obstaculo obstaculo;

    private Player player;

    /**
     * Main constructor
     *
     * @param spaceWidth
     * @param spaceHeigth
     * @param ballLimit
     */
    public Space(int spaceWidth, int spaceHeigth, int ballLimit) {
        this.spaceWidth = spaceWidth;
        this.spaceHeight = spaceHeigth;
        this.ballLimit = ballLimit;

        //init
        init();

    }

    /**
     * Init
     */
    private void init() {
        //JPanel parameters
        setPreferredSize(new Dimension(spaceWidth, spaceHeight));

        //Player
        player = new Player(30, 300, 10, 10, 10, 1, this);

        //Ball parameters
        balls = new ArrayList<Ball>();
        stopItems = new ArrayList<StopItem>();

        for (int con = 0; con < ballLimit; con++) {
            if (SpaceRules.sizes) {
                balls.add(new Ball(con * 55 + 20, con * 40 + 20, 2, 10, 10, 325, this));
            } else {
                balls.add(new Ball(con * 55 + 20, con * 40 + 20, 2, 10, 10, 325, this));

            }
        }

        stopItems.add(new StopItem(350, 50, 50, this));
        stopItems.add(new StopItem(150, 250, 50, this));

        obstaculo = new Obstaculo(400, 250, 30);

        player.start();

        for (int con = 0; con < balls.size(); con++) {
            balls.get(con).start();
        }

        stopItems.get(0).start();
        stopItems.get(1).start();

    }

    //Space painter
    public synchronized void paint() {
        BufferStrategy bs;

        bs = this.getBufferStrategy();
        if (bs == null) {
            return; // =======================================================>>
        }

        Graphics gg = bs.getDrawGraphics();

        gg.setColor(Color.black);
        gg.fillRect(0, 0, spaceWidth, spaceHeight);

        stopItems.get(0).draw(gg);
        stopItems.get(1).draw(gg);

        obstaculo.draw(gg);

        for (int con = 0; con < balls.size(); con++) {
            balls.get(con).draw(gg);
        }

        player.draw(gg);

        bs.show();

        gg.dispose();

    }

    /**
     * Check collisions
     *
     * @param b
     */
    public void checkCollision(Ball b) throws InterruptedException {
        try {
            ballPlayerCollision(b, player);
            ballBallCollission(b, balls);
            ballStopItemCollision(b, stopItems);
            ballObstaculoCollision(b, obstaculo);
            ballWallCollision(b, new Dimension(spaceWidth, spaceHeight));
        } catch (Exception e) {

        }

    }

    /**
     *
     * @param p
     * @throws InterruptedException
     */
    public void checkCollision(Player p) throws InterruptedException {
        ballPlayerCollision(p, player);
        playerBallCollission(p, balls);
//        ballStopItemCollision(p, stopItems);
        ballWallCollision(p, new Dimension(spaceWidth, spaceHeight));
    }

    /**
     * Wall collision
     *
     * @param b
     * @param d
     */
    public void ballWallCollision(Ball b, Dimension d) {
        if (b.getRadius() + b.getX() >= d.width) {
            b.setSpeedx(-Math.abs(b.getSpeedx()));
        }
        if (b.getX() - b.getRadius() <= 0) {
            b.setSpeedx(Math.abs(b.getSpeedx()));
        }
        if (b.getRadius() + b.getY() >= d.height) {
            b.setSpeedy(-Math.abs(b.getSpeedx()));
        }
        if (b.getY() - b.getRadius() <= 0) {
            b.setSpeedy(Math.abs(b.getSpeedx()));
        }
        
        if(b.getY()+b.getRadius()>d.height) b.setY(d.height-b.getRadius());
        if(b.getX()+b.getRadius()>d.width) b.setX(d.width-b.getRadius());
        if(b.getY()+b.getRadius()<0) b.setY(b.getRadius());
        if(b.getX()+b.getRadius()<0) b.setX(b.getRadius());
    }

    private void ballObstaculoCollision(Ball b, Obstaculo o) {
        if (o.inRange(b)) {
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
    public void ballBallCollission(Ball b, ArrayList<Ball> balls) {
        // Variables usadas en las comrobaciones
        double r, d_mod;
        Vec2d d, v_ini;

        for (Ball ball : balls) { // Comprueba respecto a todas las bolas del espacio
            if (ball != b) {     // exceptuando la propia bola

                r = b.getRadius() + ball.getRadius(); // Suma de los radios de las bolas, para compro
                d = new Vec2d(ball.getX() - b.getX(), ball.getY() - b.getY()); // Vector de distancia entre centros
                d_mod = Math.hypot(d.x, d.y); // Modulo del vector de distancia
                v_ini = new Vec2d(b.getSpeedx(), b.getSpeedy()); // Vector de velocidad inicial

                //Checks if in range
                if (d_mod <= r) {
                    if (SpaceRules.appliedPhysics) {
                        Physics.calcBounce(b, ball);
                    } else {
                        Vec2d v = Physics.calculo2Vec(d, d_mod, v_ini);
                        b.setSpeedx((float) v.x); // Asigna la descomposicion X del vector de velocidad final
                        b.setSpeedy((float) v.y);
                    }

                }
            }
        }
    }

    public synchronized void playerBallCollission(Player p, ArrayList<Ball> balls) {
        // Variables usadas en las comrobaciones
        double r, d_mod;
        Vec2d d, v_ini;

        for (Ball ball : balls) { // Comprueba respecto a todas las bolas del espacio
            // exceptuando la propia bola

            r = p.getRadius() + ball.getRadius(); // Suma de los radios de las bolas, para compro
            d = new Vec2d(ball.getX() - p.getX(), ball.getY() - p.getY()); // Vector de distancia entre centros
            d_mod = Math.hypot(d.x, d.y); // Modulo del vector de distancia
            v_ini = new Vec2d(p.getSpeedx(), p.getSpeedy()); // Vector de velocidad inicial

            //Checks if in range
            if (d_mod <= r) {
                Vec2d v = Physics.calculo2Vec(d, d_mod, v_ini);
                p.setSpeedx((float) v.x); // Asigna la descomposicion X del vector de velocidad final
                p.setSpeedy((float) v.y);
            }

        }
    }

    public synchronized void ballPlayerCollision(Ball b, Ball p) {
        // Variables usadas en las comrobaciones
        double r, d_mod;
        Vec2d d, v_ini;

        r = b.getRadius() + p.getRadius(); // Suma de los radios de las bolas, para compro
        d = new Vec2d(p.getX() - b.getX(), p.getY() - b.getY()); // Vector de distancia entre centros
        d_mod = Math.hypot(d.x, d.y); // Modulo del vector de distancia
        v_ini = new Vec2d(b.getSpeedx(), b.getSpeedy()); // Vector de velocidad inicial

        //Checks if in range
        if (d_mod <= r) {
            Vec2d v = Physics.calculo2Vec(d, d_mod, v_ini);
            for (int con = 0; con < balls.size(); con++) {
                if (b == balls.get(con)) {
                    delete(b, con);
                }
            }
        }

    }

    public synchronized void delete(Ball b, int con) {
        balls.get(con).stopBall();
        balls.remove(con);
    }

    public void ballStopItemCollision(Ball b, ArrayList<StopItem> items) throws InterruptedException {
        for (StopItem item : items) {
            if (item.inRange(b) || item.getBall() == b) {
                item.insert(b);
            }
        }
    }

    public synchronized boolean ballStopItemInRange(Ball b, StopItem item) {
        return b.getY() - b.getRadius() < item.getY() + item.getWidth()
                && b.getY() + b.getRadius() > item.getY()
                && b.getX() - b.getRadius() < item.getX() + item.getWidth()
                && b.getX() + b.getRadius() > item.getX();
    }

    /**
     *
     * @return
     */
    public Player getPlayer() {
        return player;
    }

    public void addBall() {
        Ball b = new Ball(240, 240, 2, 20, 50, 325, this);
        b.setColor(Color.yellow);
        b.start();
        balls.add(b);
    }

    /**
     * Main life cicle
     */
    @Override
    public void run() {
        this.createBufferStrategy(2);
        while (true) {
            this.paint();
            try {
                Thread.sleep(15); // nano -> ms
            } catch (InterruptedException ex) {
            }
        }
    }

}
