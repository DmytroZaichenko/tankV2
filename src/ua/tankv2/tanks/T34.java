package ua.tankv2.tanks;

import ua.tankv2.Demo;
import ua.tankv2.field.BattleField;
import ua.tankv2.managment.Direction;

import javax.swing.*;
import java.awt.Color;


public class T34 extends AbstractTank {


    public T34(BattleField bf) {
        super(bf);
        tankColor  = new Color(0, 255, 0);
        towerColor = new Color(255, 0, 0);
        setImages(this.getClass().getName(), "playertank.png");
        setActionForDestroyObject(bf.getArrayListEagle());
    }

    public T34(BattleField bf, int x, int y, Direction direction) {
        super(bf, x, y, direction);
        tankColor  = new Color(0, 255, 0);
        towerColor = new Color(255, 0, 0);
        setImages(this.getClass().getName(), "playertank.png");
        setActionForDestroyObject(bf.getArrayListEagle());
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
