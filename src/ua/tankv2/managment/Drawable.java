package ua.tankv2.managment;

import java.awt.Graphics;


public interface Drawable {

    String BRICK = "B";
    String ROCK  = "R";
    String EAGLE = "E";
    String WATER = "W";
    String HQ    = "H";

    void draw(Graphics g);

    int SIZE_QUADRANT = 64;
    boolean COLORED_MODE = false;
    int OUT_FIELD = 14;

}
