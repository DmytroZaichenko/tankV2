package ua.tankv2.field;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Image;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class SimpleBFObject implements BFObject{

    protected Color color;

    private int y;
    private int x;

    private boolean isDestroyed = false;

    protected Image image;

    public SimpleBFObject(int y, int x) {
        this.y = y;
        this.x = x;
        image = setImage();
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

    private Image setImage(){

//        try {
//            return ImageIO.read(new File("images/tileset1.png").getAbsoluteFile());
//        } catch (IOException e) {
//            System.err.println("Can't find battle field object images.");
//            return null;
//        }

        java.net.URL imageURL = this.getClass().getResource("images/tileset1.png");
        if (imageURL != null){
            ImageIcon imIcon = new ImageIcon(imageURL);
            return imIcon.getImage();

        } else {
            throw new IllegalStateException("Can't find battle field object images.");


        }

    }

    private void putToHM(HashMap<String, Integer> hm, int sx1, int sy1, int sx2, int sy2){
        hm.put("sx1",sx1);
        hm.put("sy1",sy1);
        hm.put("sx2",sx2);
        hm.put("sy2",sy2);
    }

    private HashMap<String, Integer> getCoordinatesOnBFObject(){

        HashMap<String, Integer> hashMap = new HashMap<>();
        putToHM(hashMap, 0, 32, 32, 64); //default blank

        if (this instanceof Brick){
            putToHM(hashMap, 128, 0, 160, 32);
        }else if(this instanceof Eagle){
            putToHM(hashMap, 0, 96, 32, 128);
        }else if(this instanceof Blank){
            putToHM(hashMap, 0, 32, 32, 64);
        }else if (this instanceof Rock){
            putToHM(hashMap, 128, 32, 160, 64);
        }else if (this instanceof Water){
            putToHM(hashMap, 96, 32, 128, 64);
        }
        return hashMap;
    }

    @Override
    public void destroy(){
        isDestroyed = true;
    }

    @Override
    public void draw(Graphics g) {
        if(!isDestroyed){
            if (image != null){
                HashMap<String, Integer> hm = getCoordinatesOnBFObject();
                g.drawImage(image, getX(), getY(), getX()+SIZE_QUADRANT, getY()+SIZE_QUADRANT,
                        hm.get("sx1"), hm.get("sy1"), hm.get("sx2"), hm.get("sy2"),
                        new ImageObserver() {
                            @Override
                            public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                                return false;
                            }
                        });
            }else{
                g.setColor(this.color);
                g.fillRect(this.getX(), this.getY(),SIZE_QUADRANT,SIZE_QUADRANT);
            }
        }else if (image != null){
            g.drawImage(image, getX(), getY(), getX()+SIZE_QUADRANT, getY()+SIZE_QUADRANT,
                    0, 32, 32, 64,
                    new ImageObserver() {
                        @Override
                        public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                            return false;
                        }
                    });
        }
    }

}
