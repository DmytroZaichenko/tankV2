package ua.tankv2.tanks;

import ua.tankv2.Demo;
import ua.tankv2.field.BattleField;
import ua.tankv2.field.Eagle;
import ua.tankv2.field.SimpleBFObject;
import ua.tankv2.managment.Action;
import ua.tankv2.managment.Destroyable;
import ua.tankv2.managment.Direction;

import java.awt.Color;

import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

import java.util.*;


public class BT7 extends AbstractTank {


    public BT7(BattleField bf, int x, int y, Direction direction) {
        super(bf, x, y, direction);
        tankColor = new Color(255,0,0);
        towerColor = new Color(0,255,0);
        speed = 5;
        setImages(this.getClass().getName(), "redtank.png");
    }


//    @Override
//    public Action setUp() {
//
//        int step;
//        for (HashMap.Entry<Destroyable, ArrayList<Object>> entry : listOfActions.entrySet()) {
//            Destroyable objDest = entry.getKey();
//            ArrayList<Object> act = entry.getValue();
//            step = hmStep.get(objDest);
//
//            if (objDest.isDestroyed() && act.size() == step){
//                continue;
//            }
//
//            if (step == act.size()) {
//                step = 0;
//            }
//
//            Object obj = act.get(step++);
//
//            hmStep.put(objDest,step);
//            if (obj instanceof Direction) {
//                if (obj != direction) {
//                    turn((Direction) obj);
//                }
//                return Action.NONE;
//            }
//            return (Action) obj;
//
//        }
//
//        return Action.NONE;
//    }


}
