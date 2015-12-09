package ua.tankv2.field;

import ua.tankv2.managment.Constant;
import java.awt.Color;
import java.awt.Graphics;

public class SimpleBFObject implements BFObject,Constant{

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
    public void destroy() throws Exception {
        isDestroyed = true;
    }

    @Override
    public void draw(Graphics g) {
        if(!isDestroyed){
            g.setColor(this.color);
            g.fillRect(this.getX(), this.getY(),SIZE_QUADRANT,SIZE_QUADRANT);
        }
    }

    @Override
    public int hashCode() {

        int result = 23;
        result += 37 * new Integer(y).hashCode();
        result += 37 * new Integer(x).hashCode();
        result += 37 * new Boolean(isDestroyed).hashCode();

        return result;
    }
}
