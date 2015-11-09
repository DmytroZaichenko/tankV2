import java.util.Random;

/**
 * Created by DmitryZ on 02.11.2015.
 */
public class BattleField {

    public final boolean COLORED_MODE = false;
    public final int SIZE_QUADRANT = 64;

    private String[][] battleField = {
            { "B", "B", " ", "B", " ", "B", " ", "B", "B" },
            { "B", " ", " ", " ", " ", " ", " ", " ", "B" },
            { "B", "B", " ", " ", "B", " ", "B", "B", "B" },
            { " ", "B", "B", " ", " ", " ", "B", "B", " " },
            { "B", " ", " ", "B", "B", " ", "B", "B", "B" },
            { "B", "B", "B", "B", "B", "B", "B", "B", " " },
            { " ", "B", " ", " ", " ", " ", " ", "B", "B" },
            { "B", " ", " ", "B", "B", "B", " ", " ", "B" },
            { " ", " ", "B", " ", " ", " ", "B", " ", " " } };


    private int bfWidth;
    private int bfHeight;
    private int countOfBriks;

    public BattleField(){

        setCountOfBriks(howManyBricksInField());
        setBfHeight(SIZE_QUADRANT * battleField.length);
        setBfWidth(SIZE_QUADRANT * battleField.length);

    }

    private void setBfWidth(int bfWidth) {
        this.bfWidth = bfWidth;
    }

    private void setBfHeight(int bfHeight) {
        this.bfHeight = bfHeight;
    }

    public int getCountOfBriks() {
        return countOfBriks;
    }

    public void setCountOfBriks(int countOfBriks) {
        this.countOfBriks = countOfBriks;
    }

    public BattleField(String[][] battleField){
        this.battleField = battleField;
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

    public String scanQuadrant(int v, int h){
        return battleField[v][h];
    }

    public void updateQuadrant(int v, int h, String object){
        battleField[v][h] = object;
    }

    public int getDimentionX(){
        return battleField.length;
    }

    public int getDimentionY(){
        return battleField.length;
    }

    public int howManyBricksInField() {

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
        return scanQuadrant(y,x).equals("B");
    }

    public int[] getAggressorLocation(Tank[] tanks){

        int maxY = getDimentionY();
        int maxX = getDimentionX();

        int[][] coordinatesTank = new int[maxY * maxX][2];
        int idx = 0;

        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++){
                if (!isBrick(y, x) && !isCoordinatesOtherTank(tanks, y, x)){
                    coordinatesTank[idx][0] = y;
                    coordinatesTank[idx][1] = x;
                    idx ++;
                }
            }
        }

        Random r = new Random();
        int rand = r.nextInt(idx - 1);
        return coordinatesTank[rand];

    }

    private boolean isCoordinatesOtherTank( Tank[] tanks, int y, int x) {

        boolean result = false;

        x *= SIZE_QUADRANT;
        y *= SIZE_QUADRANT;

        for (Tank tank : tanks){
            if(tank.getX() == x && tank.getY() == y){
                return true;
            }
        }

        return result;
    }


}
