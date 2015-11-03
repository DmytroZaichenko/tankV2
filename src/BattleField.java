/**
 * Created by DmitryZ on 02.11.2015.
 */
public class BattleField {

    public final boolean COLORED_MODE = false;
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

    private int bfWidth = 576;
    private int bfHeight = 576;

    public BattleField(){}

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


}
