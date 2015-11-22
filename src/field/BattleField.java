package ua.tankv2.field;

import ua.tankv2.tanks.AbstractTank;
import ua.tankv2.managment.*;

import java.awt.Graphics;
import java.awt.Color;
import java.util.Random;

public class BattleField implements Drawable {

    public final boolean COLORED_MODE = false;
    public final int SIZE_QUADRANT = 64;

    private String[][] battleFieldTmp = {
            { "B", "B", " ", "B", " ", "B", " ", "B", "B" },
            { "B", " ", " ", " ", " ", " ", " ", " ", "B" },
            { "B", "B", " ", " ", "B", " ", "B", "B", "B" },
            { "W","W", "B", " ", " ", " ", "B", "B", " " },
            { "W", " ", " ", "B", "B", " ", "B", "B", "B" },
            { "R", "R", "B", "B", "B", "B", "B", "B", " " },
            { " ", "B", " ", " ", " ", " ", " ", "B", "B" },
            { "W", " ", " ", "B", "B", "B", " ", " ", "B" },
            { " ", " ", "B", " ", " ", " ", "B", " ", " " } };

    private ObjectBattleField [][] battleField;
    private int bfWidth;
    private int bfHeight;
    private int countOfBlocks;

    public BattleField(){

        fillBattleField();
        setCountOfBlocks(howManyBlocksInField());
        setBfHeight(SIZE_QUADRANT * battleField.length);
        setBfWidth(SIZE_QUADRANT * battleField.length);

    }

    private void setBfWidth(int bfWidth) {
        this.bfWidth = bfWidth;
    }

    private void setBfHeight(int bfHeight) {
        this.bfHeight = bfHeight;
    }

    public int getCountOfBlocks() {
        return countOfBlocks;
    }

    public void setCountOfBlocks(int countOfBlocks) {
        this.countOfBlocks = countOfBlocks;
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

        battleField = new ObjectBattleField[battleFieldTmp.length][];
        for (int y = 0; y < battleFieldTmp.length ; y++) {
            battleField[y] = new ObjectBattleField[battleFieldTmp[y].length];
        }
    }

    private void fillBattleField(){

        createBattleField();
        String firstCh = "";
        ObjectBattleField obf = null;

        for (int j = 0; j < getDimentionY(); j++) {
            for (int k = 0; k < getDimentionX(); k++) {

                String stateField = battleFieldTmp[j][k];
                if (!stateField.equals(" ")) {

                    String coordinates = getQuadrantXY(j + 1, k + 1);
                    int separator = coordinates.indexOf("_");
                    int y = Integer.parseInt(coordinates
                            .substring(0, separator));
                    int x = Integer.parseInt(coordinates
                            .substring(separator + 1));

                    firstCh = stateField.substring(0, 1);

                    if (firstCh == "R") {
                        obf = new Rock();
                        obf.setColorBlock(Color.darkGray);
                    } else if (firstCh == "E") {
                        obf = new Eagle();
                        obf.setColorBlock(Color.BLACK);
                    } else if (firstCh == "B") {
                        obf = new Brick();
                        obf.setColorBlock(Color.CYAN);
                    } else if (firstCh == "W") {
                        obf = new Water();
                        obf.setColorBlock(Color.blue);
                    }else {

                    }

                    obf.setX(x);
                    obf.setY(y);
                    obf.setBf(this);
                    obf.setStrength(0);

                    updateQuadrant(j,k,obf);

                }
            }
        }
    }

    public ObjectBattleField scanQuadrant(int v, int h){
        return battleField[v][h];
    }

    public void updateQuadrant(int v, int h, ObjectBattleField object){
        battleField[v][h] = object;
    }

    public int howManyBlocksInField() {

        int result = 0;

        for (int y = 0; y < getDimentionY(); y++) {
            for (int x = 0; x < getDimentionX(); x++) {
                if (isBlock(y,x)){
                    result ++;
                }
            }
        }

        return result;
    }

    public boolean isBlock(int y, int x){
        return !(scanQuadrant(y,x) == null);
    }

    public int[] getAggressorLocation(AbstractTank[] tanks){

        int maxY = getDimentionY();
        int maxX = getDimentionX();

        int[][] coordinatesTank = new int[maxY * maxX][2];
        int idx = 0;

        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++){
                if (!isBlock(y, x) && !isCoordinatesTank(tanks, y, x)){
                    coordinatesTank[idx][0] = y * SIZE_QUADRANT;
                    coordinatesTank[idx][1] = x * SIZE_QUADRANT;
                    idx ++;
                }
            }
        }

        if (idx != 0){
            Random r = new Random();
            int rand = r.nextInt(idx - 1);
            return coordinatesTank[rand];
        }

        return null;
    }

    public boolean isCoordinatesTank(AbstractTank[] tanks, int y, int x) {

        for (AbstractTank tank : tanks){
            return isCoordinatesTank(tank, y, x);
        }

        return false;
    }

    public boolean isCoordinatesTank(AbstractTank tank, int y, int x) {

        if (tank.getX() == x * SIZE_QUADRANT && tank.getY() == y * SIZE_QUADRANT) {
            return true;
        }
        return false;
    }

    public String getQuadrant(int x, int y){
        return y / SIZE_QUADRANT + "_" + x / SIZE_QUADRANT;
    }

    public String getQuadrantXY(int v, int h){
        return (v - 1) * SIZE_QUADRANT + "_" + (h - 1) * SIZE_QUADRANT;
    }

    @Override
    public void draw(Graphics g) {

        Color cc;
        int i = 0;

        for (int v = 0; v < getDimentionY(); v++) {
            for (int h = 0; h < getDimentionX(); h++) {
                if (COLORED_MODE) {
                    if (i % 2 == 0) {
                        cc = new Color(252, 241, 177);
                    } else {
                        cc = new Color(233, 243, 255);
                    }
                } else {
                    cc = new Color(180, 180, 180);
                    //cc = Color.WHITE;
                }
                i++;
                g.setColor(cc);

                g.fillRect(h * SIZE_QUADRANT, v * SIZE_QUADRANT, SIZE_QUADRANT, SIZE_QUADRANT);
            }
        }

        Color colorBlock = Color.black;
        ObjectBattleField obf = null;

        for (int j = 0; j < getDimentionY(); j++) {

            for (int k = 0; k < getDimentionX(); k++) {
                obf = scanQuadrant(j,k);

                if ( obf != null) {
                    colorBlock = obf.getColorBlock();
                    g.setColor(colorBlock);
                    g.fillRect(obf.getX(), obf.getY(), SIZE_QUADRANT, SIZE_QUADRANT);
                }
            }
        }
    }
}
