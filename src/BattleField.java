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


}
