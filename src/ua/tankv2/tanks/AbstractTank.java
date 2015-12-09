package ua.tankv2.tanks;

import ua.tankv2.action.ActionField;
import ua.tankv2.action.Bullet;

import ua.tankv2.managment.*;

import ua.tankv2.field.BattleField;

import java.awt.Graphics;
import java.awt.Color;

public abstract class AbstractTank implements Tank, Constant {

    protected int speed = 10;
    protected int movePath = 1;

    //current position on BF
    protected int x;
    protected int y;

    protected Direction direction;
    protected BattleField bf;

    private boolean destroyed;

    protected Color tankColor;
    protected Color towerColor;

    public AbstractTank(BattleField bf) {
        this(bf, 0, 512, Direction.UP);
    }

    public AbstractTank(BattleField bf, int x, int y, Direction direction) {
        this.bf = bf;
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.destroyed = false;
    }

    public void turn(Direction direction){
        this.direction = direction;
    }

    public void move(){

    }

    public Bullet fire() {

        int bulletX = -100;
        int bulletY = -100;
        if (direction == Direction.UP) {
            bulletX = x + 25;
            bulletY = y - 25;
        } else if (direction == Direction.BOTTOM) {
            bulletX = x + 25;
            bulletY = y + 25;
        } else if (direction == Direction.LEFT) {
            bulletX = x - 25;
            bulletY = y + 25;
        } else if (direction == Direction.RIGHT) {
            bulletX = x + SIZE_QUADRANT;
            bulletY = y + 25;
        }
        return new Bullet(bulletX, bulletY, direction);
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

        if (!destroyed) {
            g.setColor(tankColor);
            g.fillRect(this.getX(), this.getY(), SIZE_QUADRANT, SIZE_QUADRANT);

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

    @Override
    public Action setUp() {
        return null;
    }

    @Override
    public int getMovePath() {
        return movePath;
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }
}
