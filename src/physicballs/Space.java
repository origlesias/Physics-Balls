/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package physicballs;

import items.Ball;
import items.Obstacle;
import items.StopItem;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
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
    private static int spaceWidth=1280;
    private static int spaceHeight=720;
    private Dimension d;
    private String name;

    private int ballLimit = 2;
    private int stopItemsLimit = 1;

    private CopyOnWriteArrayList<Ball> balls;
    private ArrayList<StopItem> stopItems;

    private Obstacle obstaculo;
    
    private final float gravityX = -3f;
    private final float gravityY = 6f;


    /**
     * Main constructor
     *
     * @param spaceWidth
     * @param spaceHeigth
     * @param ballLimit
     */
    public Space(int spaceWidth, int spaceHeigth, int ballLimit) {
        this.ballLimit = ballLimit;
        d= new Dimension(spaceWidth, spaceHeight);
        //init
        init();

    }
    
    public Space(){}

    /**
     * Init
     */
    private void init() {
        //JPanel parameters
        setPreferredSize(d);

        //Player
        //player = new Player(30, 300, 10, 10, 10, 1, this);

        //Ball parameters
        balls = new CopyOnWriteArrayList<>();
        stopItems = new ArrayList<>();

        Ball b;
        for (int con = 0; con < ballLimit; con++) {
            if (SpaceRules.sizes) {
                b=new Ball((float )Math.random()*100, (float )Math.random()*100, 0.5f, 1, 10+con*2, 80,  "N");
                balls.add(b);
            } else {
                if(con<8){
                    b=new Ball((float )Math.random()*100, (float )Math.random()*100, 0.5f, 1, 10+con*2, 80,  "N");
                }else{
                    b=new Ball((float )Math.random()*100, (float )Math.random()*100, 0.5f, 1, 10+con*2, 80,  "E");
                }
                
                balls.add(b);

            }
        }

        stopItems.add(new StopItem(400, 200, 50, 50, this));
        stopItems.add(new StopItem(500, 500, 200, 200, this));

        obstaculo = new Obstacle(300, 200, 30, 60, this);

        //new Thread(player).start();

        for (int con = 0; con < balls.size(); con++) {
//            new Thread(balls.get(con)).start();
              new Thread(new ThreadBall(balls.get(con), this)).start();
        }


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

        //player.draw(gg);

        bs.show();

        gg.dispose();

    }
    
    public synchronized void delete(Ball b, int con) {
        balls.get(con).stopBall();
        balls.remove(con);
    }
    
    public synchronized void delete(int con) {
            balls.get(con).stopBall();
            balls.remove(con);
    }

    public CopyOnWriteArrayList<Ball> getBalls() {
        return balls;
    }

    public ArrayList<StopItem> getStopItems() {
        return stopItems;
    }

    public Obstacle getObstaculo() {
        return obstaculo;
    }

    public float getGravityX() {
        return gravityX;
    }
    
    public float getGravityY() {
        return gravityY;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBallLimit() {
        return ballLimit;
    }

    public void setBallLimit(int ballLimit) {
        this.ballLimit = ballLimit;
    }

    public static int getSpaceWidth() {
        return spaceWidth;
    }

    public static int getSpaceHeight() {
        return spaceHeight;
    }

    public static void setSpaceWidth(int spaceWidth) {
        Space.spaceWidth = spaceWidth;
    }

    public static void setSpaceHeight(int spaceHeight) {
        Space.spaceHeight = spaceHeight;
    }

    public Dimension getD() {
        return d;
    }
    

    public void addBall() {
        Ball b = new Ball(240, 240, 2, 1, 20, 325,  "N");
        b.setColor(Color.yellow);
        //new Thread(b).start();
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
            //this.checkHoles();
            try {
                Thread.sleep(15); // nano -> ms
            } catch (InterruptedException ex) {
            }
        }
    }

}
