package ua.tankv2.field;

import ua.tankv2.tanks.AbstractTank;
import ua.tankv2.managment.*;

import java.awt.Graphics;
import java.awt.Color;
import java.util.Random;

public class BattleField implements Drawable {

    public final boolean COLORED_MODE = false;
    public final int SIZE_QUADRANT = 64;

    private String[][] battleField = {
            { "B", "B", " ", "B", " ", "B", " ", "B", "B" },
            { "B", " ", " ", " ", " ", " ", " ", " ", "B" },
            { "B", "B", " ", " ", "B", " ", "B", "B", "B" },
            { "W ","W", "B", " ", " ", " ", "B", "B", " " },
            { "W", " ", " ", "B", "B", " ", "B", "B", "B" },
            { "R", "R", "B", "B", "B", "B", "B", "B", " " },
            { " ", "B", " ", " ", " ", " ", " ", "B", "B" },
            { "E", " ", " ", "B", "B", "B", " ", " ", "B" },
            { " ", " ", "B", " ", " ", " ", "B", " ", " " } };

    private ObjectBattleField [][] battleField1:
    private int bfWidth;
    private int bfHeight;
    private int countOfBlocks;

    public BattleField(){

        createBattleField();
        setCountOfBlocks(howManyBlocksInField());
        setBfHeight(SIZE_QUADRANT * battleField.length);
        setBfWidth(SIZE_QUADRANT * battleField.length);

    }

    public BattleField(String[][] battleField){
        this.battleField = battleField;
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

    public String[][] getBattleField() {
        return battleField;
    }

    public void setBattleField(String[][] battleField) {
        this.battleField = battleField;
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

        String fString = "";

        for (int j = 0; j < getDimentionY(); j++) {
            for (int k = 0; k < getDimentionX(); k++) {
                String thatIsBlock = scanQuadrant(j, k);

                if (!whatABlock.equals(" ")) {

                    String coordinates = getQuadrantXY(j + 1, k + 1);
                    int separator = coordinates.indexOf("_");
                    int y = Integer.parseInt(coordinates
                            .substring(0, separator));
                    int x = Integer.parseInt(coordinates
                            .substring(separator + 1));

                    fString = whatABlock.substring(0, 1);

                    if (fString == "R") {

                    } else if (fString == "E") {

                    } else if (fString == "B") {

                    } else if (fString == "W") {

                    }else {

                    }


                }
            }
        }
    }

    public String scanQuadrant(int v, int h){
        return battleField[v][h];
    }

    public void updateQuadrant(int v, int h, String object){
        battleField[v][h] = object;
    }

    public int howManyBlocksInField() {

        int result = 0;

        for (int y = 0; y < getDimentionY(); y++) {
            for (int x = 0; x < getDimentionX(); x++) {
                if (isBrick(y,x)){
                    result ++;
                }
            }
        }

        return result;
    }

    public boolean isBrick(int y, int x){
        return !scanQuadrant(y,x).equals(" ");
    }

    public int[] getAggressorLocation(AbstractTank[] tanks){

        int maxY = getDimentionY();
        int maxX = getDimentionX();

        int[][] coordinatesTank = new int[maxY * maxX][2];
        int idx = 0;

        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++){
                if (!isBrick(y, x) && !isCoordinatesTank(tanks, y, x)){
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

        String whatABlock = " ";
        Color colorBlock = Color.black;
        String fString = "";

        for (int j = 0; j < getDimentionY(); j++) {
            for (int k = 0; k < getDimentionX(); k++) {
                whatABlock = scanQuadrant(j, k);

                if (!whatABlock.equals(" ")) {
                    String coordinates = getQuadrantXY(j + 1, k + 1);
                    int separator = coordinates.indexOf("_");
                    int y = Integer.parseInt(coordinates
                            .substring(0, separator));
                    int x = Integer.parseInt(coordinates
                            .substring(separator + 1));

                    fString = whatABlock.substring(0, 1);

                    if (fString == "R") {
                        colorBlock = Color.gray;
                    } else if (fString == "E") {
                        colorBlock = Color.black;
                    } else if (fString == "B") {
                        colorBlock = new Color(0,153,0);
                    } else if (fString == "W") {
                        colorBlock = Color.PINK;
                    }else {
                        colorBlock = new Color(0, 0, 255);
                    }

                    //g.setColor(new Color(0, 0, 255));
                    g.setColor(colorBlock);
                    g.fillRect(x, y, SIZE_QUADRANT, SIZE_QUADRANT);
                }
            }
        }
    }
}
