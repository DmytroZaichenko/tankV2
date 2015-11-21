package ua.tankv2.tanks;

import ua.tankv2.action.ActionField;
import ua.tankv2.field.BattleField;
import ua.tankv2.managment.Direction;

import java.awt.Color;

public class BT7 extends AbstractTank {


    private int speed = super.getSpeed() * 2;

    public BT7(ActionField af, BattleField bf) {
        super(af, bf);
        tankColor = new Color(255,0,0);
        towerColor = new Color(0,255,0);
        speed = 5;
    }

    public BT7(ActionField af, BattleField bf, int x, int y, Direction direction) {
        super(af, bf, x, y, direction);
        tankColor = new Color(255,0,0);
        towerColor = new Color(0,255,0);
        speed = 5;
    }


}
