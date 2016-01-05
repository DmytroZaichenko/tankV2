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
            Map<Destroyable,LinkedList<Direction>> track = new LinkedHashMap<>();

            Direction tmpDirection = direction;

            while (!listBlock.contains(eagle)){

                tmpXTank = xTank;
                tmpYTank = yTank;

                ArrayList<Direction> listOfPermittedDirection = getListPermittedDirections(tmpYTank, tmpXTank, track);


                if (listOfPermittedDirection.size() == 0){
                    break;
                }

                if (yEagle > yTank) {
                    tmpDirection = Direction.DOWN;
                    if (!listOfPermittedDirection.contains(tmpDirection)) {
                        tmpDirection = getBestDirection(yTank, xTank, yEagle, xEagle, listOfPermittedDirection);
                    }
                } else {
                    if (yEagle < yTank) {
                        tmpDirection = Direction.UP;
                        if (!listOfPermittedDirection.contains(tmpDirection)) {
                            tmpDirection = getBestDirection(yTank, xTank, yEagle, xEagle, listOfPermittedDirection);
                        }
                    }
                }

                if (yEagle != tmpYTank){
                    //add tmp direction in the list
                    listAction.add(tmpDirection);

                    if (bf.isShooting(tmpXTank, tmpYTank, tmpDirection, listBlock)) {
                        listAction.add(Action.FIRE);
                    }
                    listAction.add(Action.MOVE);
                    //add to track. need for the see where tank at the moment
                    putToTrack(track, tmpYTank, tmpXTank, tmpDirection);

                    //change coordinates
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
                //end for Y, need refresh direction list if yTank != tmpYTank

                //work on the coordinates X
                tmpXTank = xTank;
                tmpYTank = yTank;

                listOfPermittedDirection = getListPermittedDirections(tmpYTank, tmpXTank, track);

                if (listOfPermittedDirection.size() == 0){
                    System.out.println("I can not to move: BT7");
                    break;
                }

                if (xEagle > xTank) {
                    tmpDirection = Direction.RIGHT;
                    if (!listOfPermittedDirection.contains(tmpDirection)){
                        tmpDirection = getBestDirection(yTank, xTank, yEagle, xEagle, listOfPermittedDirection);
                    }

                } else if (xEagle < xTank) {
                    tmpDirection = Direction.LEFT;
                    if (! listOfPermittedDirection.contains(tmpDirection)){
                        tmpDirection = getBestDirection(yTank, xTank, yEagle, xEagle, listOfPermittedDirection);
                    }
                }
                if (xEagle != tmpXTank){

                    listAction.add(tmpDirection);

                    if (bf.isShooting(tmpXTank, tmpYTank,tmpDirection,listBlock)){
                        listAction.add(Action.FIRE);
                    }
                    listAction.add(Action.MOVE);

                    putToTrack(track, tmpYTank, tmpXTank, tmpDirection);

                    //change coordinates
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

            listOfActions.put((Eagle) eagle, listAction);
            hmStep.put((Eagle) eagle,0);
        }

    }

    private LinkedList<Direction> getDirectionFromTrack(int y, int x, Map<Destroyable, LinkedList<Direction>> track){

        LinkedList<Direction> oldDirection = new LinkedList<>();
        HashMap<String, Integer> coordinates = bf.getQuadrant(x, y);
        Destroyable obj = bf.scanQuadrant(coordinates.get("y"),coordinates.get("x"));
        LinkedList<Direction> hs = track.get(obj);
        if (hs != null){
            oldDirection = hs;
        }
        return oldDirection;
    }

    private ArrayList<Direction> getListPermittedDirections(int y, int x, Map<Destroyable, LinkedList<Direction>> track) {

        LinkedList oldDirection = getDirectionFromTrack(y, x, track);
        ArrayList<Direction> listOfPermittedDirection =  new ArrayList<>();

        for (Direction d : Direction.values()) {
            if (d == Direction.NONE) {
                continue;
            }

            if (!oldDirection.contains(d) && wasNotHere(y, x, d, track)) {
                if (!canMove(x, y, d)) {
                    if (bf.isShooting(x, y, d)) {
                        listOfPermittedDirection.add(d);
                    }
                } else {
                    listOfPermittedDirection.add(d);
                }
            }
        }
        return listOfPermittedDirection;
    }

    private boolean wasNotHere(int y, int x, Direction direction, Map<Destroyable, LinkedList<Direction>> track){

        int limitX = (bf.getDimentionX()-1) * SIZE_QUADRANT;
        int limitY = (bf.getDimentionY()-1) * SIZE_QUADRANT;

        int tmpY = y;
        int tmpX = x;

        if (direction == Direction.UP){
            tmpY -= SIZE_QUADRANT;
        }else if(direction == Direction.DOWN){
            tmpY += SIZE_QUADRANT;
        }else if (direction == Direction.LEFT){
            tmpX -= SIZE_QUADRANT;
        }else if (direction == Direction.RIGHT){
            tmpX += SIZE_QUADRANT;
        }

        if (tmpX < 0 || tmpY < 0 || tmpX > limitX || tmpY > limitY){
            return false;
        }else {
            LinkedList<Direction> linkedList =  getDirectionFromTrack(tmpY, tmpX, track);
            return linkedList.size()  <= 1;
        }

    }

    public boolean canMove(int x, int y, Direction direction){

        int limitX = (bf.getDimentionX()-1) * SIZE_QUADRANT;
        int limitY = (bf.getDimentionY()-1) * SIZE_QUADRANT;

        int tmpY = y;
        int tmpX = x;

        if (direction == Direction.UP){
            tmpY -= SIZE_QUADRANT;
        }else if(direction == Direction.DOWN){
            tmpY += SIZE_QUADRANT;
        }else if (direction == Direction.LEFT){
            tmpX -= SIZE_QUADRANT;
        }else if (direction == Direction.RIGHT){
            tmpX += SIZE_QUADRANT;
        }


        if ((direction == Direction.UP && tmpY <= 0)
                || (direction == Direction.DOWN && tmpY >= limitY)
                || (direction == Direction.LEFT && tmpX <= 0)
                || (direction == Direction.RIGHT && tmpX >= limitX)
                || (!(bf.nextQuadrantBlankDestoyed(x, y, direction)))
                ){
            return false;
        }

        return true;

    }

    private Direction getBestDirection(int yTank, int xTank, int yEagle, int xEagle, ArrayList<Direction> listOfPermittedDirection) {

        Direction tmpDirection;
        Direction yBestDirection = Direction.NONE;
        Direction xBestDirection = Direction.NONE;
        if (yEagle > yTank) {
            yBestDirection = Direction.DOWN;
        } else if (yEagle < yTank) {
            yBestDirection = Direction.UP;
        }

        if (xEagle > xTank) {
            xBestDirection = Direction.RIGHT;
        } else if (xEagle < xTank) {
            xBestDirection = Direction.LEFT;
        }

        if (listOfPermittedDirection.contains(yBestDirection)) {
            tmpDirection = yBestDirection;
        }else if (listOfPermittedDirection.contains(xBestDirection)) {
            tmpDirection = xBestDirection;
        } else {
            tmpDirection = listOfPermittedDirection.get(0);
        }
        return tmpDirection;
    }

    private void putToTrack(Map<Destroyable, LinkedList<Direction>> track, int tmpYTank, int tmpXTank, Direction tmpDirection) {

        HashMap<String,Integer> hm = bf.getQuadrant(tmpXTank,tmpYTank);
        Destroyable obj = bf.scanQuadrant(hm.get("y"),hm.get("x"));

        LinkedList<Direction> al = new LinkedList<>();
        if (track.get(obj) != null) {
            al = track.get(obj);
        }
        al.add(tmpDirection);
        track.put(obj,al);
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
