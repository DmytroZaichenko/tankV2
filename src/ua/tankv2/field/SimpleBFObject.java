package ua.tankv2.field;

import java.awt.Color;
import java.awt.Graphics;

public class SimpleBFObject implements BFObject{

    protected Color color;

    private int y;
    private int x;

    private boolean isDestroyed = false;

    public SimpleBFObject(int y, int x) {
        this.y = y;
        this.x = x;
    }

    public boolean isDestroyed(){
        return isDestroyed;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    @Override
    public void destroy(){
        isDestroyed = true;
    }

    @Override
    public void draw(Graphics g) {
        if(!isDestroyed){
            g.setColor(this.color);
            g.fillRect(this.getX(), this.getY(),SIZE_QUADRANT,SIZE_QUADRANT);
        }
    }

}
