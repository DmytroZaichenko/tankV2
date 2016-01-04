package ua.tankv2.tanks;

import ua.tankv2.field.BattleField;
import ua.tankv2.field.Eagle;
import ua.tankv2.field.SimpleBFObject;
import ua.tankv2.managment.Action;
import ua.tankv2.managment.Destroyable;
import ua.tankv2.managment.Direction;

import java.awt.Color;
import java.util.*;


public class BT7 extends AbstractTank {

    private LinkedHashMap<Eagle, ArrayList<Object>> listOfActions;

    public BT7(BattleField bf, int x, int y, Direction direction) {
        super(bf, x, y, direction);
        tankColor = new Color(255,0,0);
        towerColor = new Color(0,255,0);
        speed = 5;
        setActionForDestroyObject();
    }

    private void setActionForDestroyObject(){

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
            Map<Destroyable,HashSet<Direction>> track = new LinkedHashMap<>();

            Direction tmpDirection = direction;

            while (!listBlock.contains(eagle)){

                tmpXTank = xTank;
                tmpYTank = yTank;

                HashSet oldDirection = new HashSet<Direction>();
                HashMap<String, Integer> coordinats = bf.getQuadrant(tmpXTank, tmpYTank);
                Destroyable obj = bf.scanQuadrant(coordinats.get("y"),coordinats.get("x"));
                HashSet<Direction> hs = track.get(obj);
                if (hs != null){
                    oldDirection = hs;
                }

                ArrayList<Direction> listOfPermittedDirection = getPermittedDirections(tmpYTank, tmpXTank, oldDirection);

                if (listOfPermittedDirection.size() == 0){
                    break;
                }


                if (yEagle > yTank) {
                    tmpDirection = Direction.DOWN;

                    if (listOfPermittedDirection.contains(tmpDirection)){
                        listAction.add(tmpDirection);
                        yTank += SIZE_QUADRANT;
                    }else {

                        tmpDirection = listOfPermittedDirection.get(0);
                        listAction.add(tmpDirection);
                        if (tmpDirection == Direction.UP){
                            yTank -= SIZE_QUADRANT;
                        }else if(tmpDirection == Direction.DOWN){
                            yTank += SIZE_QUADRANT;
                        }else if (tmpDirection == Direction.LEFT){
                            xTank -= SIZE_QUADRANT;
                        }else if (tmpDirection == Direction.RIGHT){
                            xTank += SIZE_QUADRANT;
                        }
                    }
                } else if (yEagle < yTank) {
                    tmpDirection = Direction.UP;
                    if (listOfPermittedDirection.contains(tmpDirection)){
                        listAction.add(tmpDirection);
                        yTank -= SIZE_QUADRANT;
                    }else {

                        tmpDirection = listOfPermittedDirection.get(0);

                        listAction.add(tmpDirection);
                        if (tmpDirection == Direction.UP){
                            yTank -= SIZE_QUADRANT;
                        }else if(tmpDirection == Direction.DOWN){
                            yTank += SIZE_QUADRANT;
                        }else if (tmpDirection == Direction.LEFT){
                            xTank -= SIZE_QUADRANT;
                        }else if (tmpDirection == Direction.RIGHT){
                            xTank += SIZE_QUADRANT;
                        }
                    }
                }

                boolean isAction = false;
                if (yEagle != tmpYTank && listOfPermittedDirection.contains(tmpDirection)){

                    if (bf.isShooting(tmpXTank, tmpYTank, tmpDirection, listBlock)) {
                        listAction.add(Action.FIRE);
                    }
                    listAction.add(Action.MOVE);
                    putToTrack(track, tmpYTank, tmpXTank, tmpDirection);
                    isAction = true;

                }


                if (xEagle > xTank) {
                    tmpDirection = Direction.RIGHT;
                    if (listOfPermittedDirection.contains(tmpDirection)){
                        listAction.add(tmpDirection);
                        xTank += SIZE_QUADRANT;
                    }else {

                        tmpDirection = listOfPermittedDirection.get(0);
                        listAction.add(tmpDirection);
                        if (tmpDirection == Direction.UP){
                            yTank -= SIZE_QUADRANT;
                        }else if(tmpDirection == Direction.DOWN){
                            yTank += SIZE_QUADRANT;
                        }else if (tmpDirection == Direction.LEFT){
                            xTank -= SIZE_QUADRANT;
                        }else if (tmpDirection == Direction.RIGHT){
                            xTank += SIZE_QUADRANT;
                        }
                    }

                } else if (xEagle < xTank) {

                    tmpDirection = Direction.LEFT;

                    if (listOfPermittedDirection.contains(tmpDirection)){
                        listAction.add(tmpDirection);
                        xTank -= SIZE_QUADRANT;

                    }else {

                        tmpDirection = listOfPermittedDirection.get(0);
                        listAction.add(tmpDirection);
                        if (tmpDirection == Direction.UP){
                            yTank -= SIZE_QUADRANT;
                        }else if(tmpDirection == Direction.DOWN){
                            yTank += SIZE_QUADRANT;
                        }else if (tmpDirection == Direction.LEFT){
                            xTank -= SIZE_QUADRANT;
                        }else if (tmpDirection == Direction.RIGHT){
                            xTank += SIZE_QUADRANT;
                        }
                    }
                }
                if (xEagle != tmpXTank && listOfPermittedDirection.contains(tmpDirection)){
                    if (bf.isShooting(tmpXTank, yTank,tmpDirection,listBlock)){
                        listAction.add(Action.FIRE);
                    }
                    listAction.add(Action.MOVE);
                    if (isAction){
                        putToTrack(track, yTank, tmpXTank, tmpDirection);
                    }else {
                        putToTrack(track, tmpYTank, tmpXTank, tmpDirection);
                    }

                }

            }

            listOfActions.put((Eagle) eagle, listAction);
            hmStep.put((Eagle) eagle,0);
        }

    }

    private void putToTrack(Map<Destroyable, HashSet<Direction>> track, int tmpYTank, int tmpXTank, Direction tmpDirection) {

        HashMap<String,Integer> hm = bf.getQuadrant(tmpXTank,tmpYTank);
        Destroyable obj = bf.scanQuadrant(hm.get("y"),hm.get("x"));

        HashSet<Direction> al = new HashSet<>();
        if (track.get(obj) != null) {
            al = track.get(obj);
        }
        al.add(tmpDirection);
        track.put(obj,al);
    }

    private ArrayList<Direction> getPermittedDirections(int y, int x, HashSet<Direction> oldDirection ) {
        ArrayList<Direction> listOfPermittedMotion = new ArrayList<>();
        for (Direction d : Direction.values()) {
            if (d == Direction.NONE) {
                continue;
            }
            if (!bf.canMove(x, y, d)) {
                if (bf.isShooting(x, y, d)) {
                    if (!oldDirection.contains(d)){
                        listOfPermittedMotion.add(d);
                    }
                }
            } else {
                if (!oldDirection.contains(d)){
                    listOfPermittedMotion.add(d);
                }
            }
        }
        return listOfPermittedMotion;
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
