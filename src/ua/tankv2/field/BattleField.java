package ua.tankv2.field;

import ua.tankv2.managment.*;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class BattleField implements Drawable {

    private String[][] battleFieldTmp = {
            { "E", "B", "W", "W", "E", "B", "B", "B", "B" },
            { "B", "W", " ", " ", "W", " ", " ", " ", "B" },
            { "B", "B", " ", " ", "B", " ", "B", "B", "B" },
            { "W","W", "B", " ", " ", " ", "B", "B",  "E" },
            { "W", " ", " ", "B", "B", " ", "B", "B", "B" },
            { "R", "W", "B", "B", "B", "B", "B", "B", " " },
            { "E", "W", "B", " ", " ", " ", " ", "B", "B" },
            { "B", " ", " ", "B", "B", "B", " ", "W", "B" },
            { " ", " ", "B", "B", " ", "B", "B", "W", "B" } };

    private SimpleBFObject[][] battleField;
    private int bfWidth;
    private int bfHeight;
    private ArrayList<Eagle> arrayListEagle = new ArrayList<>();

    public BattleField(){

        fillBattleField();
        setBfHeight(SIZE_QUADRANT * battleField.length);
        setBfWidth(SIZE_QUADRANT * battleField.length);

    }

    public ArrayList<Eagle> getArrayListEagle() {
        return arrayListEagle;
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
                    arrayListEagle.add((Eagle)obf);
                } else if (stateField.equals(BRICK)) {
                    obf = new Brick(y,x);
                } else if (stateField.equals(WATER)) {
                    obf = new Water(y,x);
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

    public HashMap<String, Integer> getAggressorLocation() {

        HashMap<String,Integer> loc = new HashMap<String, Integer>(){
            {
                put("y",384);
                put("x",192);
            }
        };

        return loc;
    }

    public boolean checkLimits(int x, int y, Direction direction) {

        int limitX = (getDimentionX()-1) * SIZE_QUADRANT;
        int limitY = (getDimentionY()-1) * SIZE_QUADRANT;

        if ((direction == Direction.UP && x == 0)
                || (direction == Direction.DOWN && y >= limitY)
                || (direction == Direction.LEFT && x == 0)
                || (direction == Direction.RIGHT && x >= limitX)
                || (!(nextQuadrantBlankDestoyed(x, y, direction)))
                ){
            return true;
        }
        return false;
    }


    private boolean isBlock(Destroyable obj, HashSet<Destroyable> list){

        if (list == null){
            list = new HashSet<>();
        }

        if (!(obj.isDestroyed()) && !(obj instanceof Blank) && !(list.contains(obj)) ){
            list.add(obj);
            return true;
        }
        return false;

    }

    private Integer getRoundValue(int value){
        return (int) Math.round((double) value / (double) SIZE_QUADRANT);
    }

    public HashMap<String, Integer> getQuadrant(int x, int y){

        HashMap<String, Integer>  result = new HashMap<>(2);
        result.put("x",getRoundValue(x));
        result.put("y",getRoundValue(y));
        return result;

    }

    public boolean nextQuadrantBlankDestoyed(int x, int y, Direction direction){

        HashMap<String, Integer> coordinates = getQuadrant(x, y);
        int v = coordinates.get("y");
        int h = coordinates.get("x");

        int tmpX = h;
        int tmpY = v;

        if (direction == Direction.UP){
            tmpY = --v;
        }else if(direction == Direction.DOWN){
            tmpY = ++v;
        }else if (direction == Direction.LEFT){
            tmpX = --h;
        }else if (direction == Direction.RIGHT){
            tmpX = ++h;
        }

        try {
            Destroyable obj = scanQuadrant(tmpY, tmpX);
            return obj.isDestroyed() || obj instanceof Blank;
        }catch (ArrayIndexOutOfBoundsException e){
            return true;
        }
    }

    public boolean isShooting(int x, int y, Direction direction){

        HashMap<String, Integer> coordinates = getQuadrant(x, y);
        int v = coordinates.get("y");
        int h = coordinates.get("x");

        int tmpH = h;
        int tmpV = v;

        if (direction == Direction.UP){
            tmpV = --v;
        }else if(direction == Direction.DOWN){
            tmpV = ++v;
        }else if (direction == Direction.LEFT){
            tmpH = --h;
        }else if (direction == Direction.RIGHT){
            tmpH = ++h;
        }

        try {
            Destroyable obj = scanQuadrant(tmpV, tmpH);
            boolean result;

            if (obj instanceof Blank || obj instanceof Water){
                result =  false;
            }else if (obj.isDestroyed() ){
                result =  false;
            }else{
                result =  true;
            }


            return result;

        }catch (ArrayIndexOutOfBoundsException e){
            return false;
        }
    }

    public boolean isShooting(int x, int y, Direction direction, HashSet<Destroyable> listObj){

        HashMap<String, Integer> coordinates = getQuadrant(x, y);
        int v = coordinates.get("y");
        int h = coordinates.get("x");

        int tmpH = h;
        int tmpV = v;

        if (direction == Direction.UP){
            tmpV = --v;
        }else if(direction == Direction.DOWN){
            tmpV = ++v;
        }else if (direction == Direction.LEFT){
            tmpH = --h;
        }else if (direction == Direction.RIGHT){
            tmpH = ++h;
        }

        try {
            Destroyable obj = scanQuadrant(tmpV, tmpH);
            boolean result;
            
            if (obj instanceof Blank || obj instanceof Water){
                result =  false;
            }else if (obj.isDestroyed() ){
                result =  false;
            }else if (listObj.contains(obj)){
                result =  false;
            }else{
                result =  true;
            }

            if (result) {
                listObj.add(obj);
            }
            
            return result;
            
        }catch (ArrayIndexOutOfBoundsException e){
            return false;
        }
    }

    public boolean isBlockOnDirection(int y, int x, Direction direction, HashSet<Destroyable> list){

        HashMap<String, Integer> hm = getQuadrant(x,y);
        int firstV = hm.get("y");
        int firstH = hm.get("x");

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
