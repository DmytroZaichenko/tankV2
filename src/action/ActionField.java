package ua.tankv2.action;

import ua.tankv2.field.Blank;
import ua.tankv2.field.SimpleBFObject;
import ua.tankv2.managment.*;
import ua.tankv2.managment.Action;
import ua.tankv2.tanks.BT7;
import ua.tankv2.tanks.T34;
import ua.tankv2.field.BattleField;
import ua.tankv2.tanks.Tank;


import javax.swing.*;
import java.awt.*;


public class ActionField extends JPanel implements Constant{


    private BattleField battleField;
    private T34 defender;
    private BT7 aggressor;
    private Bullet bullet;



    public ActionField() throws  Exception {

        battleField = new BattleField();

        defender  = new T34(battleField);
        String location = battleField.getAggressorLocation();
        aggressor   = new BT7(battleField,
                Integer.parseInt(location.split("_")[1]), Integer.parseInt(location.split("_")[0]),Direction.RIGHT);


        bullet = new Bullet(-100, -100, Direction.NONE);

        JFrame frame = new JFrame("BATTLE FIELD, DAY 7");
        frame.setLocation(750, 150);
        frame.setMinimumSize(new Dimension(battleField.getBfWidth() + 17, battleField.getBfHeight() + 35));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.pack();
        frame.setVisible(true);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int i = 0;
        Color cc;
        for (int v = 0; v < 9; v++) {
            for (int h = 0; h < 9; h++) {
                if (COLORED_MODE) {
                    if (i % 2 == 0) {
                        cc = new Color(252, 241, 177);
                    } else {
                        cc = new Color(233, 243, 255);
                    }
                } else {
                    cc = new Color(180, 180, 180);
                }
                i++;
                g.setColor(cc);
                g.fillRect(h * SIZE_QUADRANT, v * SIZE_QUADRANT, SIZE_QUADRANT, SIZE_QUADRANT);
            }
        }

        battleField.draw(g);
        defender.draw(g);
        aggressor.draw(g);
        bullet.draw(g);

    }

    public void runTheGame() throws Exception{

        while (true) {
            if (!aggressor.isDestroyed() && !defender.isDestroyed()) {
                processAction(aggressor.setUp(), aggressor);
            }
            if (!aggressor.isDestroyed() && !defender.isDestroyed()) {
                processAction(defender.setUp(), defender);
            }
        }
    }

    private void processAction(Action a, Tank tank) throws Exception{

        if (a == Action.MOVE) {
            processMove(tank);
        } else if (a == Action.FIRE) {
            processTurn(tank);
            processFire(tank.fire());
        }

    }

    public void processTurn(Tank tank) throws Exception{
        repaint();
    }

    public void processMove(Tank tank) throws Exception{

        Direction direction = tank.getDirection();
        int step = 1;
        int covered = 0;

        if (checkLimits(tank,direction)){
            return;
        }

        processTurn(tank);

        while (covered < SIZE_QUADRANT) {

            if (direction == Direction.UP) {
                tank.updateY(-step);
            } else if (direction == Direction.BOTTOM) {
                tank.updateY(step);
            } else if (direction == Direction.LEFT) {
               tank.updateX(-step);
            } else if (direction == Direction.RIGHT) {
                tank.updateX(step);
            }

            covered += step;

            repaint();
            Thread.sleep(tank.getSpeed());
        }
    }

    public boolean checkLimits(Tank tank, Direction direction) {

        if ((direction == Direction.UP && tank.getY() == 0)
                || (direction == Direction.BOTTOM && tank.getY() >= battleField.getBfHeight())
                || (direction == Direction.LEFT && tank.getX() == 0)
                || (direction == Direction.RIGHT && tank.getX() >= battleField.getBfWidth())
                || (nextQuadrantBlankDestoyed(tank, direction))
           ){
            return true;
        }
        return false;
    }

    private boolean nextQuadrantBlankDestoyed(Tank tank, Direction direction){

        String coordinates = getQuadrant(tank.getX(), tank.getY());
        int y = Integer.parseInt(coordinates.split("_")[0]);
        int x = Integer.parseInt(coordinates.split("_")[1]);

        int tmpX = x;
        int tmpY = y;

        if (direction == Direction.UP){
            tmpY = --y;
        }else if(direction == Direction.BOTTOM){
            tmpY = --y;
        }else if (direction == Direction.LEFT){
            tmpX = --x;
        }else if (direction == Direction.RIGHT){
            tmpX = ++x;
        }

        try {
            SimpleBFObject obj = battleField.scanQuadrant(tmpY, tmpX);
            return obj.isDestroyed() || (!(obj instanceof Blank));
        }catch (ArrayIndexOutOfBoundsException e){
            return true;
        }

    }

    public void processFire(Bullet bullet) throws Exception{

        this.bullet = bullet;
        int step = 1;

        while ((bullet.getX() > -14 && bullet.getX() < 590)
                && (bullet.getY() > -14 && bullet.getY() < 590)) {

            if (bullet.getDirection() == Direction.UP) {
                bullet.updateY(-step) ;
            } else if (bullet.getDirection() == Direction.BOTTOM) {
                bullet.updateY(step) ;
            } else if (bullet.getDirection() == Direction.LEFT) {
                bullet.updateX(-step) ;
            } else if (bullet.getDirection() == Direction.RIGHT) {
                bullet.updateX(step);
            }

            if (processInterception()){
                bullet.destroy();
            }

            repaint();
            Thread.sleep(bullet.getSpeed());
            if (bullet.isDestroyed()){
                break;
            }

        }

    }

    public String getQuadrant(int x, int y){
        return y / SIZE_QUADRANT + "_" + x / SIZE_QUADRANT;
    }

    private boolean processInterception() throws Exception {

        String coordinates = getQuadrant(bullet.getX(), bullet.getY());
        int y = Integer.parseInt(coordinates.split("_")[0]);
        int x = Integer.parseInt(coordinates.split("_")[1]);

        SimpleBFObject obf;
        if (y >= 0 && y < battleField.getDimentionY() && x >= 0 && x < battleField.getDimentionX()) {
            obf = battleField.scanQuadrant(y,x);
            if (!obf.isDestroyed() && !(obf instanceof Blank)) {
                battleField.destroyObject(y, x);
                return true;
            }

//            if (!aggressor.isDestroyed() && checkInterception(getQuadrant(aggressor.getY(), aggressor.getX()),coordinates)){
//                aggressor.destroy();
//                return true;
//            }

            if (!defender.isDestroyed() && checkInterception(getQuadrant(defender.getY(), defender.getX()),coordinates)){
                defender.destroy();
                return true;
            }

        }

        return false;
    }

    private boolean checkInterception(String obj, String quadrant){

        int oy = Integer.parseInt(obj.split("_")[0]);
        int ox = Integer.parseInt(obj.split("_")[1]);

        int qy = Integer.parseInt(quadrant.split("_")[0]);
        int qx = Integer.parseInt(quadrant.split("_")[1]);

        if (oy >= 0 && oy < 9 && ox >= 0 && ox < 9) {
            if (oy == qy && ox == qx) {
                return true;
            }
        }
        return false;
    }

}
