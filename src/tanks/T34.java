package tanks;

import java.awt.Color;

public class T34 extends AbstractTank {


    public T34(ActionField af, BattleField bf) {
        super(af, bf);
        tankColor  = new Color(0, 255, 0);
        towerColor = new Color(255, 0, 0);
    }

    public T34(ActionField af, BattleField bf, int x, int y, Direction direction) {
        super(af, bf, x, y, direction);
        tankColor  = new Color(0, 255, 0);
        towerColor = new Color(255, 0, 0);
    }


}
