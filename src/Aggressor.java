
public class Aggressor extends Tank {

    private int [][] coordinatsTank = {{0,64},{},{}};
    //new int[3][2];

    public Aggressor(ActionField af, BattleField bf) {
        super(af, bf);

    }

    public Aggressor(ActionField af, BattleField bf, int x, int y, Direction direction) {
        super(af, bf, x, y, direction);
    }

    public void addCoordinat(){

        coordinatsTank[0]
    }


}
