/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map_generator;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author Mokiductions
 */
public class MapDesigner extends JFrame {
    
    public MapDesigner() throws IOException {
        super("PhysicsBalls - Map designer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new Designer());
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            new MapDesigner();
        } catch (IOException ex) {
            Logger.getLogger(MapDesigner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
