package ua.tankv2.tanks;

import ua.tankv2.action.ActionField;
import ua.tankv2.action.Bullet;

import ua.tankv2.managment.Direction;
import ua.tankv2.managment.Destroyable;
import ua.tankv2.managment.Drawable;

import ua.tankv2.field.BattleField;

import java.awt.Graphics;
import java.awt.Color;

public abstract class AbstractTank implements Drawable, Destroyable {

    protected int speed = 10;

    protected int x;
    protected int y;

    protected Direction direction;
    protected ActionField af;
    protected BattleField bf;
    protected Bullet bullet;


    protected Color tankColor;
    protected Color towerColor;

    public AbstractTank(ActionField af, BattleField bf) {
        this(af, bf, 0, 512, Direction.UP);
    }

    public AbstractTank(ActionField af, BattleField bf, int x, int y, Direction direction) {
        this.af = af;
        this.bf = bf;
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public void turn(Direction direction) throws Exception {
        this.direction = direction;
        af.processTurn(this);
    }

    public void move() throws Exception {
        af.processMove(this);
    }

    public void fire() throws Exception {
        bullet = new Bullet((getX() + 25), (getY() + 25), direction, this);
        af.processFire(bullet);
    }

    public int getSpeed() {
        return speed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Bullet getBullet(){
        return bullet;
    }

    public Direction getDirection() {
        return direction;
    }

    public void updateX(int x) {
        this.x += x;
    }

    public void updateY(int y) {
        this.y += y;
    }

    private void moveToQuadrant(AbstractTank tank, int v, int h) throws Exception {

        String coordinates = bf.getQuadrantXY(v, h);

        int y = Integer.parseInt(coordinates.split("_")[0]);
        int x = Integer.parseInt(coordinates.split("_")[1]);

        boolean key = true;

        while (key) {

            if (x != tank.getX() && x >= 0 && x <= bf.getDimentionX()) {
                if (x > tank.getX()) {
                    tank.turn(Direction.RIGHT);
                } else {
                    tank.turn(Direction.LEFT);
                }
                tank.move();
            } else {
                break;
            }
        }

        key = true;

        int tankY = tank.getY();

        while (key) {

            if (y != tankY && y >= 0 && y <= bf.getDimentionY()) {
                if (y > tank.getY()) {
                    tank.turn(Direction.BOTTOM);
                } else {
                    tank.turn(Direction.UP);
                }
                tank.move();
            } else {
                break;
            }
        }
    }


    public void destroy() throws Exception {
        updateX(-100);
        updateY(-100);
    }

    public void draw(Graphics g){

        g.setColor(tankColor);
        g.fillRect(this.getX(), this.getY(), bf.SIZE_QUADRANT, bf.SIZE_QUADRANT);

        g.setColor(towerColor);

        if (this.getDirection() == Direction.UP) {
            g.fillRect(this.getX() + 20, this.getY(), 24, 34);
        } else if (this.getDirection() == Direction.BOTTOM) {
            g.fillRect(this.getX() + 20, this.getY() + 30, 24, 34);
        } else if (this.getDirection() == Direction.LEFT) {
            g.fillRect(this.getX(), this.getY() + 20, 34, 24);
        } else {
            g.fillRect(this.getX() + 30, this.getY() + 20, 34, 24);
        }
    }


}
