package ua.tankv2.tanks;

import ua.tankv2.field.BattleField;
import ua.tankv2.field.SimpleBFObject;
import ua.tankv2.field.Hq;
import ua.tankv2.managment.Action;
import ua.tankv2.managment.Destroyable;
import ua.tankv2.managment.Direction;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class BT7 extends AbstractTank {

    private HashMap<Hq, ArrayList<Object>> listOfActions = new HashMap<>();

    private int speed = super.getSpeed() * 2;

    public BT7(BattleField bf) {
        super(bf);
        tankColor = new Color(255,0,0);
        towerColor = new Color(0,255,0);
        speed = 5;
        setActionForDestroyHQ();
    }

    public BT7(BattleField bf, int x, int y, Direction direction) {
        super(bf, x, y, direction);
        tankColor = new Color(255,0,0);
        towerColor = new Color(0,255,0);
        speed = 5;
        setActionForDestroyHQ();
    }

    private void setActionForDestroyHQ(){

        ArrayList<SimpleBFObject> arrHQ = bf.getArrayListHQ();

        if (arrHQ.size() == 0){
            return;
        }

        int tmpYTank = 0;
        int tmpXTank = 0;

        int yTank = getY();
        int xTank = getX();

        HashSet<Destroyable> listBlock = new HashSet<>();

        for (SimpleBFObject hq : arrHQ ) {

            int yHQ = hq.getY();
            int xHQ = hq.getX();

            listOfActions.put((Hq) hq,new ArrayList<>());

            Direction tmpDirection = direction;

            while (yHQ != yTank) {

                tmpYTank = yTank;
                tmpXTank = xTank;

                if (yHQ < yTank) {
                    tmpDirection = Direction.UP;
                    listOfActions.get(hq).add(tmpDirection);
                    yTank -= SIZE_QUADRANT;
                } else if (yHQ > yTank) {
                    tmpDirection = Direction.DOWN;
                    listOfActions.get((Hq)hq).add(tmpDirection);
                    yTank += SIZE_QUADRANT;
                }

                if (bf.isBlockOnDirection(tmpYTank, tmpXTank, tmpDirection, listBlock)) {
                    listOfActions.get(hq).add(Action.FIRE);
                }
                if (listBlock.contains(hq)){
                    break;
                }else {
                    listOfActions.get(hq).add(Action.MOVE);
                }

            }

            while (xHQ != xTank && !(listBlock.contains(hq))){

                tmpYTank = yTank;
                tmpXTank = xTank;

                if (xHQ < xTank){
                    tmpDirection = Direction.LEFT;
                    listOfActions.get(hq).add(tmpDirection);
                    xTank -= SIZE_QUADRANT;
                }else if (xHQ > xTank){
                    tmpDirection = Direction.RIGHT;
                    listOfActions.get(hq).add(tmpDirection);
                     xTank += SIZE_QUADRANT;
                }

                if (bf.isBlockOnDirection(tmpYTank, tmpXTank, tmpDirection, listBlock)) {
                    listOfActions.get(hq).add(Action.FIRE);
                }

                if (listBlock.contains(hq)){
                    break;
                }else {
                    listOfActions.get(hq).add(Action.MOVE);
                }

            }

        }
    }

    private int step = 0;

    @Override
    public Action setUp() {

        for (HashMap.Entry<Hq, ArrayList<Object>> entry : listOfActions.entrySet()) {
            Hq h = entry.getKey();
            ArrayList<Object> act = entry.getValue();
            while (!(h.isDestroyed())) {
                Object obj = act.get(step++);
                if (obj instanceof Direction) {
                    if (obj != direction) {
                        turn((Direction) obj);
                    }
                    return Action.NONE;
                }

                return (Action) obj;
            }
        }
//        if (step == listOfActions.size()) {
//            if (!(isHQDestroyed())) {
//                step = 0;
//            } else {
//                return Action.NONE;
//            }
//        }
//
//        Object obj = listOfActions.get(step++);
//        if (obj instanceof Direction) {
//            if (obj != direction) {
//                turn((Direction) obj);
//            }
//            return Action.NONE;
//        }
//
//        return (Action) obj;
        return Action.NONE;
    }

    private boolean isHQDestroyed(){

        for (SimpleBFObject hq:  bf.getArrayListHQ()) {
            if (!(hq.isDestroyed())) {
                return false;
            }
        }
        return true;
    }

}
