package ua.tankv2.action;

import ua.tankv2.field.SimpleBFObject;
import ua.tankv2.managment.Constant;
import ua.tankv2.managment.Direction;
import ua.tankv2.tanks.AbstractTank;
import ua.tankv2.tanks.T34;
import ua.tankv2.tanks.Tiger;
import ua.tankv2.field.BattleField;


import javax.swing.*;
import java.awt.*;


public class ActionField extends JPanel implements Constant{


    private BattleField battleField;
    private T34 defender;
    private Tiger aggressor;
    private Bullet bullet;

    private AbstractTank[] tanksInGame;


    public ActionField() throws  Exception {

        battleField = new BattleField();
        aggressor   = new Tiger(this, battleField);
        //defender  = new T34(this, battleField);
        //aggressor = new Tiger(this, battleField, 0, 3*battleField.SIZE_QUADRANT, Direction.UP, 1);

        tanksInGame = new AbstractTank[] {aggressor};

        bullet = new Bullet(-100, -100, Direction.BOTTOM);

        JFrame frame = new JFrame("BATTLE FIELD, DAY 4");
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
                if (COLORDED_MODE) {
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
                g.fillRect(h * 64, v * 64, 64, 64);
            }
        }

        battleField.draw(g);
        //defender.draw(g);
        aggressor.draw(g);
        bullet.draw(g);

    }

    public void runTheGame() throws Exception{

          //clean(defender);
        aggressor.fire();
        aggressor.fire();
        aggressor.fire();
        aggressor.fire();
        aggressor.fire();
        aggressor.fire();

    }

    public void processTurn(AbstractTank tank) throws Exception{
        repaint();
    }

    public void processMove(AbstractTank tank) throws Exception{

        Direction direction = tank.getDirection();
        int step = 1;
        int covered = 0;

        if (checkLimits(tank)){
            return;
        }

        tank.turn(direction);

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

    public boolean checkLimits(AbstractTank tank) {

        Direction direction = tank.getDirection();

        if ((direction == Direction.UP && tank.getY() == 0)
                || (direction == Direction.BOTTOM && tank.getY() >= battleField.getBfHeight())
                || (direction == Direction.LEFT && tank.getX() == 0)
                || (direction == Direction.RIGHT && tank.getX() >= battleField.getBfWidth())
                || (nextQuadrantBlock(tank, direction))
                ) {
            return true;
        }
        return false;
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

        }

    }

    private boolean processInterception() throws Exception {

        String coordinates = battleField.getQuadrant(bullet.getX(), bullet.getY());
        int y = Integer.parseInt(coordinates.split("_")[0]);
        int x = Integer.parseInt(coordinates.split("_")[1]);

        SimpleBFObject obf = null;
        if (y >= 0 && y < battleField.getDimentionY() && x >= 0 && x < battleField.getDimentionX()) {
            obf = battleField.scanQuadrant(y,x);
            if (obf != null) {
                obf.setBullet(bullet);
                obf.destroy();
                return true;
            }

            for (AbstractTank tank: tanksInGame) {

                if (tank.getBullet() != bullet){
                    tank.destroy();
                    return true;
                }

            }

        }

        return false;
    }

}
