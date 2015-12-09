package ua.tankv2.field;

import ua.tankv2.managment.*;
import java.awt.Graphics;
import java.util.ArrayList;

public class BattleField implements Drawable, Constant  {

    private String[][] battleFieldTmp = {
            { "B", "B", " ", "B", " ", "B", " ", "B", "B" },
            { "B", " ", " ", " ", " ", " ", " ", " ", "B" },
            { "B", "B", " ", " ", "B", " ", "B", "B", "B" },
            { "W","W", "B", " ", " ", " ", "B", "B", " " },
            { "W", " ", " ", "B", "B", " ", "B", "B", "B" },
            { "R", "R", "B", "B", "B", "B", "B", "B", " " },
            { " ", "B", " ", " ", " ", " ", " ", "B", "B" },
            { "B", " ", " ", "B", "B", "B", " ", " ", "B" },
            { " ", " ", "B", "B", "H", "B", "B", " ", " " } };

    private SimpleBFObject[][] battleField;
    private int bfWidth;
    private int bfHeight;
    private ArrayList<SimpleBFObject> arrayListHQ = new ArrayList<SimpleBFObject>();

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

    public SimpleBFObject scanQuadrant(int v, int h){
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
        return "64_128";
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
