package ua.tankv2.field;

import ua.tankv2.managment.Destroyable;

import java.awt.Color;

public class ObjectBattleField implements Destroyable{

    protected Color colorBlock;
    protected int y;
    protected int x;
    protected int strength = 0;
    protected BattleField bf;


    public Color getColorBlock() {
        return colorBlock;
    }

    public void setColorBlock(Color colorBlock) {
        this.colorBlock = colorBlock;
    }

    public BattleField getBf() {
        return bf;
    }

    public void setBf(BattleField bf) {
        this.bf = bf;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    @Override
    public void destroy() throws Exception {
        bf.updateQuadrant(y, x, " ");
    }
}
