package ua.tankv2.tanks;

import ua.tankv2.field.BattleField;
import ua.tankv2.field.SimpleBFObject;
import ua.tankv2.managment.Action;
import ua.tankv2.managment.Destroyable;
import ua.tankv2.managment.Direction;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;

public class BT7 extends AbstractTank {

    private ArrayList<Object> listOfActions = new ArrayList<>();

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

        boolean isBlockOnDirection = false;
        HashSet<Destroyable> listBlock = new HashSet<>();

        for (SimpleBFObject hq : arrHQ ) {

            int yHQ = hq.getY();
            int xHQ = hq.getX();

            int yTank = getY();
            int xTank = getX();

            while (yHQ != yTank) {

                isBlockOnDirection = bf.isBlockOnDirection(yTank, xTank, direction, listBlock);
                if (yHQ < yTank) {
                    listOfActions.add(Direction.UP);
                    yTank -= SIZE_QUADRANT;
                } else if (yHQ > yTank) {
                    listOfActions.add(Direction.DOWN);
                    yTank += SIZE_QUADRANT;
                }

                if (isBlockOnDirection) {
                    listOfActions.add(Action.FIRE);
                }
                listOfActions.add(Action.MOVE);
            }

            while (xHQ != xTank){

                isBlockOnDirection = bf.isBlockOnDirection(yTank, xTank, direction, listBlock);
                if (xHQ < xTank){
                    listOfActions.add(Direction.LEFT);
                    xTank -= SIZE_QUADRANT;
                }else if (xHQ > xTank){
                    listOfActions.add(Direction.RIGHT);
                     xTank += SIZE_QUADRANT;
                }

                if (isBlockOnDirection) {
                    listOfActions.add(Action.FIRE);
                }

                listOfActions.add(Action.MOVE);

            }
            System.out.println();
        }

    }

    private int step = 0;

    @Override
    public Action setUp() {

        if (step == listOfActions.size() && !(isHQDestroyed())){
            step = 0;
        }

        Object obj = listOfActions.get(step ++);
        if (obj instanceof Direction){
            if (obj != direction) {
                turn((Direction) obj);
            }
            return Action.NONE;
        }

        return (Action) obj;
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
