
public class T34 extends Tank {

    private int speed = super.getSpeed() * 2;

    public T34(ActionField af, BattleField bf) {
        super(af, bf);
        speed = 5;
    }

    public T34(ActionField af, BattleField bf, int x, int y, Direction direction) {
        super(af, bf, x, y, direction);
        speed = 5;
    }


}
