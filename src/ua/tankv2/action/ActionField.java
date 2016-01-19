package ua.tankv2.action;

import ua.tankv2.field.Blank;
import ua.tankv2.field.Water;
import ua.tankv2.field.Rock;
import ua.tankv2.managment.*;
import ua.tankv2.tanks.*;
import ua.tankv2.field.BattleField;
import ua.tankv2.managment.Action;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;


public class ActionField extends JPanel {


    private BattleField battleField;
    private T34 defender;
    private BT7 aggressor;
    private Bullet bullet;
    private Tiger tiger;



    public ActionField() throws  Exception {

        battleField = new BattleField();

        defender  = new T34(battleField);
        aggressor   = new BT7(battleField,battleField.getAggressorLocation().get("x"),
                              battleField.getAggressorLocation().get("y"),Direction.DOWN);

        tiger  = new Tiger(battleField,battleField.getTigerLocation().get("x"),
                                       battleField.getTigerLocation().get("y"),
                                       Direction.DOWN, 2);
        List<Tank> listOfTank = new ArrayList<Tank>() {
            {
                add(defender);
                add(aggressor);
                add(tiger);
            }
        };

        
        destroyObjUnderTank(listOfTank);
        
        bullet = new Bullet(-100, -100, Direction.NONE);

        JFrame frame = new JFrame("BATTLE FIELD, DAY 7");
        frame.setLocation(750, 150);
        frame.setMinimumSize(new Dimension(battleField.getBfWidth() + 17, battleField.getBfHeight() + 35));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.pack();
        frame.setVisible(true);

    }

    private void destroyObjUnderTank(List<Tank> listOfTank) {

        for (int i = 0; i < listOfTank.size(); i++) {
            Tank t = listOfTank.get(i);
            HashMap<String, Integer> hm = battleField.getQuadrant(t.getX(),t.getY());
            Destroyable obj = battleField.scanQuadrant(hm.get("y"), hm.get("x"));
            if (!(obj instanceof Blank) || !(obj.isDestroyed())) {
                obj.destroy();
            }
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int i = 0;
        Color cc;
        for (int v = 0; v < battleField.getDimentionY(); v++) {
            for (int h = 0; h < battleField.getDimentionX(); h++) {
                if (battleField.COLORED_MODE) {
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
                g.fillRect(h * battleField.SIZE_QUADRANT, v * battleField.SIZE_QUADRANT,
                               battleField.SIZE_QUADRANT, battleField.SIZE_QUADRANT);
            }
        }

        battleField.draw(g);
        defender.draw(g);
        aggressor.draw(g);
        bullet.draw(g);
        tiger.draw(g);

    }

    public void runTheGame() throws Exception{

        processAction(aggressor.setUp(), defender);
//        while (true) {
//            if (!aggressor.isDestroyed() && !defender.isDestroyed()) {
//                processAction(aggressor.setUp(), aggressor);
//            }
//            if (!aggressor.isDestroyed() && !defender.isDestroyed()) {
//                processAction(defender.setUp(), defender);
//            }
//        }
    }

    private void processAction(Action a, Tank tank) throws Exception{

        if (a == Action.MOVE) {
            processMove(tank);
        } else if (a == Action.FIRE) {
            processTurn();
            processFire(tank.fire());
        }

    }

    public void processTurn() throws Exception{
        repaint();
    }

    public void processMove(Tank tank) throws Exception{

        Direction direction = tank.getDirection();
        int step = 1;
        int covered = 0;

        if (battleField.checkLimits(tank.getX(), tank.getY(),direction)){
            return;
        }

        processTurn();

        while (covered < battleField.SIZE_QUADRANT) {

            if (direction == Direction.UP) {
                tank.updateY(-step);
            } else if (direction == Direction.DOWN) {
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

    public void processFire(Bullet bullet) throws Exception{

        this.bullet = bullet;
        int step = 1;
        int incToOut = battleField.OUT_FIELD;
        while ((bullet.getX() > (-1)*incToOut && bullet.getX() < battleField.getBfWidth() + incToOut)
                && (bullet.getY() > (-1) * incToOut && bullet.getY() < battleField.getBfHeight() + incToOut)) {

            if (bullet.getDirection() == Direction.UP) {
                bullet.updateY(-step) ;
            } else if (bullet.getDirection() == Direction.DOWN) {
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


    private boolean processInterception() throws Exception {

        HashMap<String, Integer> coordinates = battleField.getQuadrant(bullet.getX(), bullet.getY());
        int y = coordinates.get("y");
        int x = coordinates.get("x");

        Destroyable obf;
        if (y >= 0 && y < battleField.getDimentionY() && x >= 0 && x < battleField.getDimentionX()) {
            obf = battleField.scanQuadrant(y,x);
            if (!obf.isDestroyed() && !(obf instanceof Blank) && !(obf instanceof Water) && !(obf instanceof Rock)) {
                battleField.destroyObject(y, x);
                return true;
            }

            if (!aggressor.isDestroyed() && checkInterception(battleField.getQuadrant(aggressor.getY(),aggressor.getX()),coordinates)){
                aggressor.destroy();
                return true;
            }

            if (!defender.isDestroyed() && checkInterception(battleField.getQuadrant(defender.getY(), defender.getX()),coordinates)){
                defender.destroy();
                return true;
            }

        }

        return false;
    }

    private boolean checkInterception(HashMap<String,Integer> obj, HashMap<String,Integer> quadrant){

        int oy = obj.get("y");
        int ox = obj.get("x");

        int qy = quadrant.get("y");
        int qx = quadrant.get("x");

        if (oy >= 0 && oy < battleField.getDimentionY() && ox >= 0 && ox < battleField.getDimentionX()) {
            if (oy == qy && ox == qx) {
                return true;
            }
        }
        return false;
    }

}
