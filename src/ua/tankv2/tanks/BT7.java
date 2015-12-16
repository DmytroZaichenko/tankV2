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
import java.util.LinkedHashMap;


public class BT7 extends AbstractTank {

    private LinkedHashMap<Hq, ArrayList<Object>> listOfActions;

    public BT7(BattleField bf, int x, int y, Direction direction) {
        super(bf, x, y, direction);
        tankColor = new Color(255,0,0);
        towerColor = new Color(0,255,0);
        speed = 5;
        setActionForDestroyHQ();
    }

    private void setActionForDestroyHQ(){

        ArrayList<Hq> arrHQ = bf.getArrayListHQ();

        if (arrHQ.size() == 0){
            return;
        }

        listOfActions = new LinkedHashMap<>();

        int tmpYTank;
        int tmpXTank;

        int yTank = getY();
        int xTank = getX();

        HashSet<Destroyable> listBlock = new HashSet<>();

        for (SimpleBFObject hq : arrHQ ) {

            int yHQ = hq.getY();
            int xHQ = hq.getX();
            ArrayList<Object> listAction = new ArrayList<>();

            Direction tmpDirection = direction;

            while (!listBlock.contains(hq)){

                tmpXTank = xTank;
                tmpYTank = yTank;

                if (yHQ > yTank) {
                    tmpDirection = Direction.DOWN;
                    listAction.add(tmpDirection);
                    yTank += SIZE_QUADRANT;
                } else if (yHQ < yTank) {
                    tmpDirection = Direction.UP;
                    listAction.add(tmpDirection);
                    yTank -= SIZE_QUADRANT;
                }

                if (yHQ != tmpYTank){
                    if (bf.isShooting(tmpXTank, tmpYTank, tmpDirection, listBlock)) {
                        listAction.add(Action.FIRE);
                    }
                    listAction.add(Action.MOVE);
                }


                if (xHQ > xTank) {
                    tmpDirection = Direction.RIGHT;
                    listAction.add(tmpDirection);
                    xTank += SIZE_QUADRANT;

                } else if (xHQ < xTank) {
                    tmpDirection = Direction.LEFT;
                    listAction.add(tmpDirection);
                    xTank -= SIZE_QUADRANT;
                }
                if (xHQ != tmpXTank){
                    if (bf.isShooting(tmpXTank, yTank,tmpDirection,listBlock)){
                        listAction.add(Action.FIRE);
                    }
                    listAction.add(Action.MOVE);
                }
            }
            listOfActions.put((Hq) hq, listAction);
            hmStep.put((Hq)hq,0);
        }

    }

    private HashMap<Hq,Integer> hmStep = new HashMap<>();

    @Override
    public Action setUp() {

        int step;
        for (HashMap.Entry<Hq, ArrayList<Object>> entry : listOfActions.entrySet()) {
            Hq h = entry.getKey();
            ArrayList<Object> act = entry.getValue();
            step = hmStep.get(h);

            if (h.isDestroyed() && act.size() == step){
                continue;
            }

            if (step == act.size()) {
                step = 0;
            }

            Object obj = act.get(step++);
            //System.out.println("hq [x: "+h.getX()+" y: "+h.getY()+"] tank [x: "+ x + " y: " + y+"]
            // action: "+obj.toString());
            hmStep.put(h,step);
            if (obj instanceof Direction) {
                if (obj != direction) {
                    turn((Direction) obj);
                }
                return Action.NONE;
            }
            return (Action) obj;

        }

        return Action.NONE;
    }


}
