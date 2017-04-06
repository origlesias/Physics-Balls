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
    private int spaceWidth;
    private int spaceHeight;
    private Dimension d;

    private int ballLimit = 3;
    private int stopItemsLimit = 1;

    private CopyOnWriteArrayList<Ball> balls;
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
        d= new Dimension(spaceWidth, spaceHeight);
        //init
        init();

    }

    /**
     * Init
     */
    private void init() {
        //JPanel parameters
        setPreferredSize(d);

        //Player
        //player = new Player(30, 300, 10, 10, 10, 1, this);

        //Ball parameters
        balls = new CopyOnWriteArrayList<Ball>();
        //stopItems = new ArrayList<StopItem>();

        Ball b;
        for (int con = 0; con < ballLimit; con++) {
            if (SpaceRules.sizes) {
                b=new Ball(con * 55 + 20, con * 40 + 20, 2, 1, 10+con*2, 10+con*2, con*20, this, "N");
                balls.add(b);
            } else {
                if(con<8){
                    b=new Ball(con * 55 + 20, con * 40 + 20, 2, 1, 10+con*2, 10+con*2, con*20, this, "N");
                }else{
                    b=new Ball(con * 55 + 20, con * 40 + 20, 2, 1, 10+con*2, 10+con*2, con*20, this, "E");
                }
                
                balls.add(b);

            }
        }

        //stopItems.add(new StopItem(350, 50, 50, this));
        //stopItems.add(new StopItem(150, 250, 50, this));

        obstaculo = new Obstaculo(400, 250, 30);

        //new Thread(player).start();

        for (int con = 0; con < balls.size(); con++) {
            new Thread(balls.get(con)).start();
        }

        //stopItems.get(0).start();
        //stopItems.get(1).start();

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

        //stopItems.get(0).draw(gg);
        //stopItems.get(1).draw(gg);

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

    /**
     *
     * @return
     */
    public Player getPlayer() {
        return player;
    }

    public CopyOnWriteArrayList<Ball> getBalls() {
        return balls;
    }

    public ArrayList<StopItem> getStopItems() {
        return stopItems;
    }

    public Obstaculo getObstaculo() {
        return obstaculo;
    }
    
    

    public Dimension getD() {
        return d;
    }
    
    

    public void addBall() {
        Ball b = new Ball(240, 240, 2, 1, 20, 50, 325, this, "N");
        b.setColor(Color.yellow);
        new Thread(b).start();
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
