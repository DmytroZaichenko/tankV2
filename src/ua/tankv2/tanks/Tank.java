package ua.tankv2.tanks;

import ua.tankv2.action.Bullet;
import ua.tankv2.managment.*;

public interface Tank extends Drawable, Destroyable {

    public Action setUp();

    public void move();

    public Bullet fire();

    public int getX();

    public int getY();

    public Direction getDirection();

    public void updateX(int x);

    public void updateY(int y);

    public int getSpeed();

    public int getMovePath();


}
