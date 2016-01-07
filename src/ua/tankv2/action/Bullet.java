package ua.tankv2.action;

import ua.tankv2.managment.Direction;
import ua.tankv2.managment.Destroyable;
import ua.tankv2.managment.Drawable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Bullet implements Drawable, Destroyable {

    private int speed = 5;

    private int x;
    private int y;

    private Direction direction;

    private boolean destroyed;
    Image image;

    public Bullet(int x, int y, Direction direction){
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.destroyed = false;
        setImages();
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

    private void putToHM(HashMap<String, Integer> hm, int sx1, int sy1, int sx2, int sy2){
        hm.put("sx1",sx1);
        hm.put("sy1",sy1);
        hm.put("sx2",sx2);
        hm.put("sy2",sy2);
    }

    private HashMap<String, Integer> getCoordinatesOnDirection(){

        HashMap<String, Integer> hashMap = new HashMap<>(4);
        putToHM(hashMap, 0, 0, 32, 32);

        if (direction == Direction.UP){
            putToHM(hashMap, 0, 0, 32, 32);
        }else if (direction == Direction.DOWN){
            putToHM(hashMap, 64, 0, 96, 32);
        }else if(direction == Direction.LEFT){
            putToHM(hashMap, 32, 0, 64, 32);
        }else if (direction == Direction.RIGHT){
            putToHM(hashMap, 96, 0, 128, 32);
        }

        return hashMap;
    }

    public void draw(Graphics g) {

        if (!destroyed){
            if (image != null){
                HashMap<String, Integer> hm = getCoordinatesOnDirection();
                g.drawImage(image, getX(), getY(), getX()+40, getY()+40,
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

        try {
            image = ImageIO.read(new File("bullet.png").getAbsoluteFile());
        } catch (IOException e) {
            throw new IllegalStateException("Can't find bullet images.");
        }

    }
}
