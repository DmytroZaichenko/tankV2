package ua.tankv2.action;

import ua.tankv2.Demo;
import ua.tankv2.managment.Direction;
import ua.tankv2.managment.Destroyable;
import ua.tankv2.managment.Drawable;
import ua.tankv2.tanks.AbstractTank;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.HashMap;

public class Bullet implements Drawable, Destroyable {

    private int speed = 5;

    private int x;
    private int y;
    private Direction direction;

    private AbstractTank tank;

    private boolean destroyed;
    Image image;

    public Bullet(int x, int y, Direction direction, AbstractTank tank){
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.destroyed = false;
        this.tank = tank;
        setImages();
    }

    public AbstractTank getTank() {
        return tank;
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

    private void putToHM(HashMap<String, Integer> hm, int dx1, int dy1, int dx2, int dy2,
                                                      int sx1, int sy1, int sx2, int sy2){

        hm.put("sx1",sx1);
        hm.put("sy1",sy1);
        hm.put("sx2",sx2);
        hm.put("sy2",sy2);

        hm.put("dx1",dx1);
        hm.put("dy1",dy1);
        hm.put("dx2",dx2);
        hm.put("dy2",dy2);
    }

    private HashMap<String, Integer> getCoordinatesOnDirection(){

        int sq = SIZE_QUADRANT;
        int hs = sq / 2;

        HashMap<String, Integer> hashMap = new HashMap<>(8);
        putToHM(hashMap, x, y, x + sq, y + sq, 0, 0, 32, 32);

        if (direction == Direction.UP){
            putToHM(hashMap, x, y - hs, x + sq, y + sq - hs,  0, 0, 32, 32);
        }else if (direction == Direction.DOWN){
            putToHM(hashMap, x , y + hs , x + sq , y + sq + hs , 64, 0, 96, 32);
        }else if(direction == Direction.LEFT){
            putToHM(hashMap, x - hs, y , x + sq - hs , y + sq , 32, 0, 64, 32);
        }else if (direction == Direction.RIGHT){
            putToHM(hashMap, x + hs , y , x + sq + hs , y + sq , 96, 0, 128, 32);
        }

        return hashMap;
    }

    public void draw(Graphics g) {

        if (!destroyed){
            if (image != null){
                HashMap<String, Integer> hm = getCoordinatesOnDirection();
                g.drawImage(image,
                        hm.get("dx1"), hm.get("dy1"), hm.get("dx2"), hm.get("dy2"),
                        hm.get("sx1"), hm.get("sy1"), hm.get("sx2"), hm.get("sy2"),
                        new ImageObserver() {
                            @Override
                            public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                                return false;
                            }
                        });
            }else{
                g.setColor(new Color(255, 255, 0));
                g.fillRect(this.x, this.y, 14, 14);
            }
        }

    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    private void setImages(){

        java.net.URL imageURL = Demo.class.getResource("images/bullet.png");
        if (imageURL != null){
            image = new ImageIcon(imageURL).getImage();
        } else {
            throw new IllegalStateException("Can't find bullet images.");
        }

    }
}
