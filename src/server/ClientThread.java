/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import items.Ball;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import physicballs.Space;

/**
 *
 * @author Liam-Portatil
 */
public class ClientThread extends Thread {

    private Socket clientSock;
    private String cliAddr;
    private Space parent;

    public ClientThread(Socket s, String cliAddr, Space parent) {
        clientSock = s;
        this.cliAddr = cliAddr;
        System.out.println("Client connection from " + cliAddr);
        this.parent = parent;
        //player = parent.getPlayer();
    }

    public void run() {
        try {
            // Get I/O streams from the socket
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
            PrintWriter out = new PrintWriter(clientSock.getOutputStream(), true);  // autoflush
            boolean done = false;

            // interact with a client
            processClient(in, out);

//            // Close client connection
            clientSock.close();
//            System.out.println("Client (" + cliAddr + ") connection closed\n");
        } catch (Exception e) {
            System.out.println(e);
        }
    }  // end of run()

    private void processClient(BufferedReader in, PrintWriter out) {
        try {
            //Si no hay un bucle infinito, al acabar este proceso, muere el cliente
            while (true) {
                String str = "";
                switch (in.readLine()) {
                    case "up":
                        //player.moveUp();
                        break;
                    case "down":
                        //player.moveDown();
                        break;
                    case "left":
                       // player.moveLeft();
                        break;
                    case "right":
                       // player.moveRight();
                        break;
                    default:
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
