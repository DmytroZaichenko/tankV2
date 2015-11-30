package ua.tankv2.action;

import ua.tankv2.managment.Direction;
import ua.tankv2.managment.Destroyable;
import ua.tankv2.managment.Drawable;
import ua.tankv2.tanks.AbstractTank;

import java.awt.Graphics;
import java.awt.Color;

public class Bullet implements Drawable, Destroyable {

    private int speed = 10;

    private int x;
    private int y;

    private Direction direction;

    private boolean destroyed;

    public Bullet(int x, int y, Direction direction){
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.destroyed = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }

    public Direction getDirection() {
        return direction;
    }

    public void updateX(int x){
        this.x += x;
    }

    public void updateY(int y){
        this.y += y;
    }

    public void destroy(){
        destroyed = true;
    }

    public void draw(Graphics g) {

        if (!destroyed){
            g.setColor(new Color(255, 255, 0));
            g.fillRect(this.x, this.y, 14, 14);
        }

    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }
}
