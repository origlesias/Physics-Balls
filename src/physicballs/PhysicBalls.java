/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package physicballs;

import java.awt.Container;
import java.awt.GridLayout;
import javax.swing.JFrame;
import rules.SpaceRules;

/**
 *
 * @author Liam-Portatil
 */
public class PhysicBalls extends JFrame {

    /**
     * Global parameters
     */
    private Container content;
    private Space space = new Space(1000, 600, 10);

    /**
     * Constructor
     */
    public PhysicBalls() {
        init();
        new Thread(space).start();

    }

    public void init() {
        //initial values
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(0, 0);
        setLayout(new GridLayout(1, 1));

        //Main panel
        content = getContentPane();
        content.add(space);

        pack();
        setVisible(true);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new PhysicBalls();
    }

}
