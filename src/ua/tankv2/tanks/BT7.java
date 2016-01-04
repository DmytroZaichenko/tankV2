package ua.tankv2.tanks;

import ua.tankv2.field.BattleField;
import ua.tankv2.field.Eagle;
import ua.tankv2.field.SimpleBFObject;
import ua.tankv2.managment.Action;
import ua.tankv2.managment.Destroyable;
import ua.tankv2.managment.Direction;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;


public class BT7 extends AbstractTank {

    private LinkedHashMap<Eagle, ArrayList<Object>> listOfActions;

    public BT7(BattleField bf, int x, int y, Direction direction) {
        super(bf, x, y, direction);
        tankColor = new Color(255,0,0);
        towerColor = new Color(0,255,0);
        speed = 5;
        setActionForDestroyEagls();
    }

    private void setActionForDestroyEagls(){

        ArrayList<Eagle> arrEagle = bf.getArrayListEagle();

        if (arrEagle.size() == 0){
            return;
        }

        listOfActions = new LinkedHashMap<>();

        int tmpYTank;
        int tmpXTank;

        int yTank = getY();
        int xTank = getX();

        HashSet<Destroyable> listBlock = new HashSet<>();

        for (SimpleBFObject eagle : arrEagle ) {

            int yEagle = eagle.getY();
            int xEagle = eagle.getX();
            ArrayList<Object> listAction = new ArrayList<>();

            Direction tmpDirection = direction;

            while (!listBlock.contains(eagle)){

                tmpXTank = xTank;
                tmpYTank = yTank;

                if (yEagle > yTank) {
                    tmpDirection = Direction.DOWN;
                    listAction.add(tmpDirection);
                    yTank += SIZE_QUADRANT;
                } else if (yEagle < yTank) {
                    tmpDirection = Direction.UP;
                    listAction.add(tmpDirection);
                    yTank -= SIZE_QUADRANT;
                }

                if (yEagle != tmpYTank){
                    if (bf.isShooting(tmpXTank, tmpYTank, tmpDirection, listBlock)) {
                        listAction.add(Action.FIRE);
                    }
                    listAction.add(Action.MOVE);
                }


                if (xEagle > xTank) {
                    tmpDirection = Direction.RIGHT;
                    listAction.add(tmpDirection);
                    xTank += SIZE_QUADRANT;

                } else if (xEagle < xTank) {
                    tmpDirection = Direction.LEFT;
                    listAction.add(tmpDirection);
                    xTank -= SIZE_QUADRANT;
                }
                if (xEagle != tmpXTank){
                    if (bf.isShooting(tmpXTank, yTank,tmpDirection,listBlock)){
                        listAction.add(Action.FIRE);
                    }
                    listAction.add(Action.MOVE);
                }
            }
            listOfActions.put((Eagle) eagle, listAction);
            hmStep.put((Eagle) eagle,0);
        }

    }

    private HashMap<Eagle,Integer> hmStep = new HashMap<>();

    @Override
    public Action setUp() {

        int step;
        for (HashMap.Entry<Eagle, ArrayList<Object>> entry : listOfActions.entrySet()) {
            Eagle eagle = entry.getKey();
            ArrayList<Object> act = entry.getValue();
            step = hmStep.get(eagle);

            if (eagle.isDestroyed() && act.size() == step){
                continue;
            }

            if (step == act.size()) {
                step = 0;
            }

            Object obj = act.get(step++);

            hmStep.put(eagle,step);
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
