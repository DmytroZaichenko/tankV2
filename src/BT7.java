import java.awt.Color;
import java.awt.Graphics;

public class BT7 extends AbstractTank {


    private int speed = super.getSpeed() * 2;

    public BT7(ActionField af, BattleField bf) {
        super(af, bf);
        speed = 5;
    }

    public BT7(ActionField af, BattleField bf, int x, int y, Direction direction) {
        super(af, bf, x, y, direction);
        speed = 5;
    }

    @Override
    public void draw(Graphics g) {

    }


}
