package tanks;

import java.awt.Color;
import action.ActionField;
import field.BattleField;
import enums.Direction;

public class Tiger extends AbstractTank {

    private int armor = 1;

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public Tiger(ActionField af, BattleField bf, int x, int y, Direction direction, int armor) {
        super(af, bf, x, y, direction);
        setArmor(armor);
        tankColor = new Color(255,0,0);
        towerColor = new Color(0,255,0);
    }

    @Override
    public void destroy() throws Exception {
        if (armor > 0){
            armor --;
        }else {
            super.destroy();
        }
    }


}
