/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package physicballs;

import com.sun.javafx.geom.Vec2d;
import items.Ball;

/**
 *
 * @author Liam-Portatil
 */
public class Physics {
    
    public static Vec2d calculo2Vec(Vec2d d, double d_mod, Vec2d v_ini) {
        double v_mod, delta, beta, bounce_angle;
        Vec2d ud, uv_ini, v_fin, uv_fin;
        // Si el modulo del vector de distancia es menor a la suma de los radios
        ud = new Vec2d(d.x / d_mod, d.y / d_mod); // Vector unitario de distancia
        v_mod = Math.hypot(v_ini.x, v_ini.y); // Modulo del vector de velocidad inicial
        uv_ini = new Vec2d(v_ini.x / v_mod, v_ini.y / v_mod); // Vector unitario de la velocidad inicial
        delta = Math.acos(ud.x); // Angulo del vector de distancia respecto al eje X
        beta = Math.acos(uv_ini.x); // Angulo del vector de velocidad incial respecto al eje X
        bounce_angle = beta + (2 * ((Math.PI / 2) - (beta - delta))); // Angulo de rebote
        uv_fin = new Vec2d(Math.cos(bounce_angle), Math.sin(bounce_angle)); // Vector unitario de velocidad final
        return new Vec2d(uv_fin.x * v_mod, uv_fin.y * v_mod); // Vector de velocidad final
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
        b1.setSpeedx((float) (Math.sin(collAngle) * fin_xSpeed_b1 + Math.cos(collAngle) * fin_ySpeed_b1));
        b2.setSpeedy((float) (Math.cos(collAngle) * fin_xSpeed_b2 - Math.sin(collAngle) * fin_ySpeed_b2));
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
}
