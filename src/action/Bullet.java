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
    private AbstractTank tank;

    public Bullet(int x, int y, Direction direction){
        this(x, y, direction, null);
    }

    public Bullet(int x, int y, Direction direction, AbstractTank tank){
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.tank = tank;
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

    public AbstractTank getTank() {
        return tank;
    }

    public void setTank(AbstractTank tank) {
        this.tank = tank;
    }

    public void destroy(){
        x = -100;
        y = -100;
    }

    public void draw(Graphics g) {

        g.setColor(new Color(255, 255, 0));
        g.fillRect(getX(), getY(), 14, 14);
    }


}
