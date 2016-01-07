package ua.tankv2.field;

import java.awt.Color;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;

public class Eagle extends SimpleBFObject {

    public Eagle(int y, int x) {
        super(y, x);
        color = new Color(255,255,0);
    }

    private void setImages(){

        try {
            image = ImageIO.read(new File("tileset1.png").getAbsoluteFile());
        } catch (IOException e) {
            throw new IllegalStateException("Can't find brick images.");
        }

    }
}
