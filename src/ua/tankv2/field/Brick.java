package ua.tankv2.field;

import java.awt.*;
import java.awt.image.ImageObserver;

public class Brick extends SimpleBFObject {

    public Brick(int y, int x) {
        super(y, x);
        color = new Color(0,0,255);
    }

}
