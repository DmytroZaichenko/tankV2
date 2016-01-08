package ua.tankv2.tanks;

import ua.tankv2.field.BattleField;
import ua.tankv2.managment.Action;
import ua.tankv2.managment.Direction;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class T34 extends AbstractTank {


    public T34(BattleField bf) {
        super(bf);
        tankColor  = new Color(0, 255, 0);
        towerColor = new Color(255, 0, 0);
        setImages();
        setActionForDestroyObject(bf.getArrayListEagle());
    }

    public T34(BattleField bf, int x, int y, Direction direction) {
        super(bf, x, y, direction);
        tankColor  = new Color(0, 255, 0);
        towerColor = new Color(255, 0, 0);
        setImages();
        setActionForDestroyObject(bf.getArrayListEagle());
    }

    private void setImages(){

        try {
            image = ImageIO.read(new File("playertank.png").getAbsoluteFile());
        } catch (IOException e) {
            throw new IllegalStateException("Can't find tank T34 images.");
        }

    }


//    @Override
//    public Action setUp() {
//
//        if (step >= actions.length) {
//            step = 0;
//        }
//        if (!(actions[step] instanceof Action)) {
//            turn((Direction) actions[step++]);
//        }
//        if (step >= actions.length) {
//            step = 0;
//        }
//        return (Action) actions[step++];
//    }

}
