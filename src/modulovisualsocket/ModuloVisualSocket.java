/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modulovisualsocket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.physicballs.items.*;

/**
 * Class client that finds and connects to the PhysicsBalls Server
 * 
 * 
 * @author Liam-Portatil
 */
public class ModuloVisualSocket extends Thread {

    /**
     * Global parameters
     */
    private static final int PORT = 11111;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ArrayList<Walls.wall> walls;
    public boolean live = true, connected =false;

    /**
     * Modulo visual constructor 
     */
    public ModuloVisualSocket() {
        try {
            Socket socketConnection = new Socket(getServerIP().getHostAddress(), 11111);
            out = new ObjectOutputStream(socketConnection.getOutputStream());
            in = new ObjectInputStream(socketConnection.getInputStream());
            Register();
            connected =true;
            //this.start();
        } catch (IOException ex) {
            System.out.println("refused server connection");
        }

    }

    @Override
    public void run() {
        while (live) {
            try {
                Object o = in.readObject();
                if (o instanceof Status) {
                    if (((Status) o).ID >= 500) {
                        //Se ha producido un error
                        System.out.println(((Status) o).description);
                        live = false;
                    }
                } else if (o instanceof Peticion) {
                    switch (((Peticion) o).getAccion()) {
                        case "update_walls":
                            if (((Status) ((Peticion) o).getObject(0)).ID == 1) {
                                walls = ((ArrayList<Walls.wall>) ((Peticion) o).getObject(1));
                            }
                            break;
                        case "update_addWall":
                            walls.add(((Walls.wall) ((Peticion) o).getObject(0)));
                            break;
                        case "update_removeWall":
                            walls.remove(((Walls.wall) ((Peticion) o).getObject(0)));
                            break;
                        case "addBall":
                            if (((Status) ((Peticion) o).getObject(0)).ID == 1) {
                                Ball b = ((Ball) ((Peticion) o).getObject(1));
                                System.out.println(b.getRadius());
                            } else {
                                System.out.println("Couldnt add ball");
                            }
                            break;
                    }
                }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ModuloVisualSocket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void Register() {
        try {
            out.writeObject("modulo_visual");
        } catch (IOException ex) {
            Logger.getLogger(ModuloVisualSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Walls.wall> getWalls() {
        return walls;
    }

    public void sendBall(Ball b, Walls.wall w) {
        try {
            Peticion p = new Peticion("enviar_pelota");
            p.pushData(b);
            p.pushData(w);
            out.writeObject(p);
        } catch (IOException ex) {

        }
    }

    /**
     * Finds the IP of the server using the available port 
     * @return 
     */
    public InetAddress getServerIP() {
        InetAddress ip = null;
        try {
            DatagramSocket c = new DatagramSocket();
            c.setBroadcast(true);
            c.setSoTimeout(5000);
            byte[] sendData = "/ping".getBytes();
            try {
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), PORT);
                c.send(sendPacket);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();
                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue; // Don't want to broadcast to the loopback interface
                }
                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    InetAddress broadcast = interfaceAddress.getBroadcast();
                    if (broadcast == null) {
                        continue;
                    }
                    try {
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, 11111);
                        c.send(sendPacket);
                    } catch (Exception e) {
                        System.err.println("ERROR SENDING BROADCAST PACKET");
                    }
                }
            }
            byte[] recvBuf = new byte[15000];
            DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
            c.receive(receivePacket);

            String message = new String(receivePacket.getData()).trim();
            if (message.equals("/ping")) {
                ip = receivePacket.getAddress();
                System.out.println("DISCOVERED IP: " + ip);
            }
            //Close the port!
            c.close();
        } catch (IOException ex) {
            System.err.println("BROADCAST TIMED OUT");
        }
        return ip;
    }
    
    public void sendStatistics(StatisticsData data){
        try {
            Peticion p = new Peticion("enviar_estadisticas");
            p.pushData(data);
            out.writeObject(p);
        } catch (Exception e) {
            System.out.println("Error enviando las estadisticas \n"+e);
            connected = false;
        }
    }
}
