package ua.tankv2.tanks;

import ua.tankv2.action.ActionField;
import ua.tankv2.field.BattleField;
import ua.tankv2.managment.Action;
import ua.tankv2.managment.Direction;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Color;
import java.io.File;
import java.io.IOException;

public class Tiger extends AbstractTank {

    private int armor = 1;

    public Tiger(BattleField bf) {
        super(bf);
        this.armor = armor;
        tankColor = new Color(255,0,0);
        towerColor = new Color(0,255,0);
        setImages();
    }

    public Tiger(BattleField bf, int x, int y, Direction direction, int armor) {
        super(bf, x, y, direction);
        setArmor(armor);
        tankColor = new Color(255,0,0);
        towerColor = new Color(0,255,0);
        setImages();
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    private void setImages(){

//        try {
//            image = ImageIO.read(new File("images/aitank.png").getAbsoluteFile());
//        } catch (IOException e) {
//            throw new IllegalStateException("Can't find tank Tiger images.");
//        }

        java.net.URL imageURL = this.getClass().getResource("images/aitank.png");
        if (imageURL != null){
            imIcon = new ImageIcon(imageURL);
            image = imIcon.getImage();
        } else {
            throw new IllegalStateException("Can't find tank T34 images.");
        }

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
