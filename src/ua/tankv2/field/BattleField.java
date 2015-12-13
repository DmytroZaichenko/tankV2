package ua.tankv2.field;

import ua.tankv2.managment.*;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class BattleField implements Drawable, Constant  {

    private String[][] battleFieldTmp = {
            { "B", "B", " ", "B", " ", "B", " ", "B", "B" },
            { "B", " ", " ", " ", " ", " ", " ", " ", "B" },
            { "B", "B", " ", " ", "B", " ", "B", "B", "B" },
            { "W","W", "B", " ", " ", " ", "B", "B", " " },
            { "W", "H", " ", "B", "B", " ", "B", "B", "B" },
            { "R", "R", "B", "B", "B", "B", "B", "B", " " },
            { " ", "B", " ", " ", " ", " ", " ", "B", "B" },
            { "B", " ", " ", "B", "B", "B", " ", " ", "B" },
            { " ", " ", "B", "B", "H", "B", "B", " ", " " } };

    private SimpleBFObject[][] battleField;
    private int bfWidth;
    private int bfHeight;
    private ArrayList<SimpleBFObject> arrayListHQ = new ArrayList<>();

    public BattleField(){

        fillBattleField();
        setBfHeight(SIZE_QUADRANT * battleField.length);
        setBfWidth(SIZE_QUADRANT * battleField.length);

    }

    public ArrayList<SimpleBFObject> getArrayListHQ() {
        return arrayListHQ;
    }

    private void setBfWidth(int bfWidth) {
        this.bfWidth = bfWidth;
    }

    private void setBfHeight(int bfHeight) {
        this.bfHeight = bfHeight;
    }

    public int getBfWidth() {
        return bfWidth;
    }

    public int getBfHeight() {
        return bfHeight;
    }

    public int getDimentionX(){
        return battleField.length;
    }

    public int getDimentionY(){
        return battleField.length;
    }

    private void createBattleField(){

        battleField = new SimpleBFObject[battleFieldTmp.length][];
        for (int y = 0; y < battleFieldTmp.length ; y++) {
            battleField[y] = new SimpleBFObject[battleFieldTmp[y].length];
        }
    }

    private void fillBattleField(){

        createBattleField();
        SimpleBFObject obf;

        for (int j = 0; j < getDimentionY(); j++) {
            for (int k = 0; k < getDimentionX(); k++) {

                String coordinates = getQuadrantXY(j + 1, k + 1);
                int separator = coordinates.indexOf("_");
                int y = Integer.parseInt(coordinates
                        .substring(0, separator));
                int x = Integer.parseInt(coordinates
                        .substring(separator + 1));

                String stateField = battleFieldTmp[j][k];

                if (stateField.equals(ROCK)) {
                    obf = new Rock(y,x);
                } else if (stateField.equals(EAGLE)) {
                    obf = new Eagle(y,x);
                } else if (stateField.equals(BRICK)) {
                    obf = new Brick(y,x);
                } else if (stateField.equals(WATER)) {
                    obf = new Water(y,x);
                } else if (stateField.equals(HQ)) {
                    obf = new Hq(y,x);
                    arrayListHQ.add(obf);
                } else {
                    obf = new Blank(y,x);
                }
                updateQuadrant(j, k, obf);
            }
        }
    }

    public Destroyable scanQuadrant(int v, int h){
        return battleField[v][h];
    }

    public void updateQuadrant(int v, int h, SimpleBFObject object){
        battleField[v][h] = object;
    }


    public String getQuadrantXY(int v, int h){
        return (v - 1) * SIZE_QUADRANT + "_" + (h - 1) * SIZE_QUADRANT;
    }

    public void destroyObject(int v, int h) throws Exception{
        battleField[v][h].destroy();
    }

    public String getAggressorLocation() {
        return "64_512";
    }

    public boolean checkLimits(int v, int h, Direction direction) {

        if ((direction == Direction.UP && v == 0)
                || (direction == Direction.DOWN && v >= getDimentionY())
                || (direction == Direction.LEFT && h == 0)
                || (direction == Direction.RIGHT && h >= getDimentionX())
                ) {
            return true;
        }
        return false;
    }

    private boolean isBlock(Destroyable obj, HashSet<Destroyable> list){

        if (list == null){
            list = new HashSet<>();
        };

        if (!(obj.isDestroyed()) && !(obj instanceof Blank) && !(list.contains(obj)) ){
            list.add(obj);
            return true;
        }
        return false;

    }

    public boolean isBlockOnDirection(int y, int x, Direction direction, HashSet<Destroyable> list){

        int firstV = y / SIZE_QUADRANT;
        int firstH = x / SIZE_QUADRANT;

        if (checkLimits(firstV, firstH, direction)) {
            return false;
        }

        Destroyable obj;

        if (direction == Direction.UP){

            for (int tmpV = firstV; tmpV >= 0 ; tmpV --) {
                obj = scanQuadrant(tmpV, firstH);
                if (isBlock(obj,list)){
                    return true;
                }
            }

        }else if (direction == Direction.DOWN){

            for (int tmpV = firstV; tmpV < getDimentionY() ; tmpV ++) {
                obj = scanQuadrant(tmpV, firstH);
                if (isBlock(obj,list)){
                    return true;
                }
            }

        }else if (direction == Direction.RIGHT){

            for (int tmpH = firstH; tmpH < getDimentionX() ; tmpH++) {
                obj = scanQuadrant(firstV, tmpH);
                if (isBlock(obj,list)){
                    return true;
                }
            }

        }else if (direction == Direction.LEFT){

            for (int tmpH = firstH; tmpH >= 0 ; tmpH--) {
                obj = scanQuadrant(firstV, tmpH);
                if (isBlock(obj,list)){
                    return true;
                }
            }
        }

        return false;
    }


    @Override
    public void draw(Graphics g) {

        for (int j = 0; j < battleField.length; j++) {
            for (int k = 0; k < battleField.length; k++) {
                battleField[j][k].draw(g);
            }
        }
    }
}
