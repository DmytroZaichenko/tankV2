package ua.tankv2.field;

import java.awt.*;
import java.awt.image.ImageObserver;

public class Water extends SimpleBFObject {

    public Water(int x, int y) {
        super(x, y);
        color = new Color(0, 185, 255);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2D = (Graphics2D)g;
        Composite org = g2D.getComposite();
        Composite translucent = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
        g2D.setComposite(translucent);
        g.drawImage(image, getX(), getY(), getX() + SIZE_QUADRANT, getY() + SIZE_QUADRANT, 96, 32, 128, 64, new ImageObserver() {
            @Override
            public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                return false;
            }
        });
        g2D.setComposite(org);
    }
}
