package ua.tankv2.tanks;

import ua.tankv2.Demo;
import ua.tankv2.action.Bullet;

import ua.tankv2.managment.*;

import ua.tankv2.field.BattleField;
import ua.tankv2.managment.Action;

import javax.swing.*;
import java.awt.Image;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.util.*;


public abstract class AbstractTank implements Tank {

    protected int speed = 10;
    protected int movePath = 1;

    //current position on BF
    protected int x;
    protected int y;
    protected Direction direction;


    protected BattleField bf;

    private boolean destroyed;

    protected Color tankColor;
    protected Color towerColor;

    protected LinkedHashMap<Destroyable, ArrayList<Object>> listOfActions;
    protected HashMap<Destroyable,Integer> hmStep = new HashMap<>();

    protected Image image;

    public AbstractTank(BattleField bf) {
        this(bf,320 , 0, Direction.UP);
    }

    public AbstractTank(BattleField bf, int x, int y, Direction direction) {
        this.bf = bf;
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.destroyed = false;
    }

    public void turn(Direction direction){
        this.direction = direction;
    }

    public void move(){

    }

    public Bullet fire() {
        int bulletX = -100;
        int bulletY = -100;
        if (direction == Direction.UP) {
//            bulletX = x + 64;
//            bulletY = y - 64;
            bulletX = x;
            bulletY = y;
        } else if (direction == Direction.DOWN) {
            bulletX = x + 25;
            bulletY = y + 64;
        } else if (direction == Direction.LEFT) {
            bulletX = x - 64;
            bulletY = y + 25;
        } else if (direction == Direction.RIGHT) {
            bulletX = x + 64;
            bulletY = y + 25;
        }
        //return new Bullet(bulletX, bulletY, direction);
        return new Bullet(x, y, direction);
    }

    protected void setImages(String nameObj, String nameImage){

        java.net.URL imageURL = Demo.class.getResource("images/" + nameImage);
        if (imageURL != null){
            image = new ImageIcon(imageURL).getImage();
        } else {
            throw new IllegalStateException("Can't find tank "+nameObj+" images.");
        }

    }

    public int getSpeed() {
        return speed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Direction getDirection() {
        return direction;
    }

    public void updateX(int x) {
        this.x += x;
    }

    public void updateY(int y) {
        this.y += y;
    }


    public void destroy(){
        updateX(-100);
        updateY(-100);
    }

    private void putToHM(HashMap<String, Integer> hm, int sx1, int sy1, int sx2, int sy2){
        hm.put("sx1",sx1);
        hm.put("sy1",sy1);
        hm.put("sx2",sx2);
        hm.put("sy2",sy2);
    }

    private HashMap<String, Integer> getCoordinatesOnDirection(){

        HashMap<String, Integer> hashMap = new HashMap<>(4);
        putToHM(hashMap, 0, 0, 32, 32);

        if (direction == Direction.UP){
            putToHM(hashMap, 0, 0, 32, 32);
        }else if (direction == Direction.DOWN){
            putToHM(hashMap, 0, 32, 32, 64);
        }else if(direction == Direction.LEFT){
            putToHM(hashMap, 0, 64, 32, 96);
        }else if (direction == Direction.RIGHT){
            putToHM(hashMap, 0, 96, 32, 128);
        }

        return hashMap;
    }

    public void draw(Graphics g){

        if (!destroyed) {
            if (image != null){
                HashMap<String, Integer> hm = getCoordinatesOnDirection();
                g.drawImage(image, getX(), getY(), getX()+SIZE_QUADRANT, getY()+SIZE_QUADRANT,
                                           hm.get("sx1"), hm.get("sy1"), hm.get("sx2"), hm.get("sy2"),
                        new ImageObserver() {
                    @Override
                    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                        return false;
                    }
                });
            }else{
                g.setColor(tankColor);
                g.fillRect(this.getX(), this.getY(), SIZE_QUADRANT, SIZE_QUADRANT);

                g.setColor(towerColor);

                if (this.getDirection() == Direction.UP) {
                    g.fillRect(this.getX() + 20, this.getY(), 24, 34);
                } else if (this.getDirection() == Direction.DOWN) {
                    g.fillRect(this.getX() + 20, this.getY() + 30, 24, 34);
                } else if (this.getDirection() == Direction.LEFT) {
                    g.fillRect(this.getX(), this.getY() + 20, 34, 24);
                } else {
                    g.fillRect(this.getX() + 30, this.getY() + 20, 34, 24);
                }
            }
        }

    }

    protected void setActionForDestroyObject(ArrayList<Destroyable> arrObject){


        if (arrObject.size() == 0){
            return;
        }

        listOfActions = new LinkedHashMap<>();

        int countInQuadrant = 1;
        int tmpYTank;
        int tmpXTank;

        int yTank = getY();
        int xTank = getX();

        HashSet<Destroyable> listBlock = new HashSet<>();

        for (Destroyable objDestroyed : arrObject ) {


            int yEagle = objDestroyed.getY();
            int xEagle = objDestroyed.getX();

            ArrayList<Object> listAction = new ArrayList<>();
            Map<Destroyable,LinkedList<Direction>> track = new LinkedHashMap<>();
            Direction tmpDirection = direction;

            while (!listBlock.contains(objDestroyed)){

                tmpXTank = xTank;
                tmpYTank = yTank;

                ArrayList<Direction> listOfPermittedDirection = getListPermittedDirections(tmpYTank, tmpXTank, track, countInQuadrant);

                if (listOfPermittedDirection.size() == 0){
                    listOfPermittedDirection = getListPermittedDirections(tmpYTank, tmpXTank, track, 2);
                    if (listOfPermittedDirection.size() == 0) {
                        break;
                    }
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

                listOfPermittedDirection = getListPermittedDirections(tmpYTank, tmpXTank, track,countInQuadrant);

                if (listOfPermittedDirection.size() == 0){
                    listOfPermittedDirection = getListPermittedDirections(tmpYTank, tmpXTank, track, 2);
                    if (listOfPermittedDirection.size() == 0) {
                        break;
                    }
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

            listOfActions.put( objDestroyed, listAction);
            hmStep.put(objDestroyed,0);
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

    private ArrayList<Direction> getListPermittedDirections(int y, int x, Map<Destroyable, LinkedList<Direction>> track, int countHere) {

        LinkedList oldDirection = getDirectionFromTrack(y, x, track);
        ArrayList<Direction> listOfPermittedDirection =  new ArrayList<>();

        for (Direction d : Direction.values()) {
            if (d == Direction.NONE) {
                continue;
            }

            if (!oldDirection.contains(d) && wasNotHere(y, x, d, track, countHere)) {
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

    private boolean wasNotHere(int y, int x, Direction direction, Map<Destroyable, LinkedList<Direction>> track, int countHere){

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
            return linkedList.size()  < countHere;
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


        if ((direction == Direction.UP && tmpY < 0)
                || (direction == Direction.DOWN && tmpY > limitY)
                || (direction == Direction.LEFT && tmpX < 0)
                || (direction == Direction.RIGHT && tmpX > limitX)
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
            if (listOfPermittedDirection.size() == 1){
                tmpDirection = listOfPermittedDirection.get(0);
            }else{

                tmpDirection = listOfPermittedDirection.get(0);
                if (yBestDirection  == Direction.NONE){
                    if (listOfPermittedDirection.contains(Direction.UP)){
                        tmpDirection =  Direction.UP;
                    }else if (listOfPermittedDirection.contains(Direction.DOWN)){
                        tmpDirection =  Direction.DOWN;
                    }
                }else if(xBestDirection == Direction.NONE){

                    if (listOfPermittedDirection.contains(Direction.RIGHT)){
                        tmpDirection =  Direction.RIGHT;
                    }else if (listOfPermittedDirection.contains(Direction.LEFT)){
                        tmpDirection =  Direction.LEFT;
                    }
                }
            }

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

    @Override
    public Action setUp() {

        int step;
        for (HashMap.Entry<Destroyable, ArrayList<Object>> entry : listOfActions.entrySet()) {
            Destroyable objDest = entry.getKey();
            ArrayList<Object> act = entry.getValue();
            step = hmStep.get(objDest);

            if (objDest.isDestroyed() && act.size() == step){
                continue;
            }

            if (step == act.size()) {
                step = 0;
            }

            Object obj = act.get(step++);

            hmStep.put(objDest,step);
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

    @Override
    public int getMovePath() {
        return movePath;
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }
}
