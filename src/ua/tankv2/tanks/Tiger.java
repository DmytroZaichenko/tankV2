package ua.tankv2.tanks;

import ua.tankv2.action.ActionField;
import ua.tankv2.field.BattleField;
import ua.tankv2.managment.Action;
import ua.tankv2.managment.Direction;

import java.awt.Color;

public class Tiger extends AbstractTank {

    private int armor = 1;

    public Tiger(BattleField bf) {
        super(bf);
        this.armor = armor;
        tankColor = new Color(255,0,0);
        towerColor = new Color(0,255,0);
    }

    public Tiger(BattleField bf, int x, int y, Direction direction, int armor) {
        super(bf, x, y, direction);
        setArmor(armor);
        tankColor = new Color(255,0,0);
        towerColor = new Color(0,255,0);
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    @Override
    public void destroy() {
        if (armor > 0){
            armor --;
        }else {
            super.destroy();
        }
    }

    @Override
    public Action setUp() {
        return Action.FIRE;
    }
}
