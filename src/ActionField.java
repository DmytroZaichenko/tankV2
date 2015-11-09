import javax.swing.*;
import java.awt.*;

public class ActionField extends JPanel{

    private boolean COLORED_MODE = false;

    private BattleField battleField;
    private Tank tank;
    private Bullet bullet;


    public ActionField() throws  Exception{

        battleField = new BattleField();
        tank = new Tank(this, battleField);
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

        int sizeQuadrant = battleField.SIZE_QUADRANT;

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

                g.fillRect(h * sizeQuadrant, v * sizeQuadrant, sizeQuadrant, sizeQuadrant);
            }
        }



        for (int j = 0; j < battleField.getDimentionY(); j++) {
            for (int k = 0; k < battleField.getDimentionX(); k++) {
                if (battleField.scanQuadrant(j,k).equals("B")) {
                    String coordinates = getQuadrantXY(j + 1, k + 1);
                    int separator = coordinates.indexOf("_");
                    int y = Integer.parseInt(coordinates
                            .substring(0, separator));
                    int x = Integer.parseInt(coordinates
                            .substring(separator + 1));
                    g.setColor(new Color(0, 0, 255));
                    g.fillRect(x, y, sizeQuadrant, sizeQuadrant);
                }
            }
        }


        g.setColor(new Color(255, 0, 0));
        g.fillRect(tank.getX(), tank.getY(), sizeQuadrant, sizeQuadrant);

        g.setColor(new Color(0, 255, 0));

        if (tank.getDirection() == Direction.UP) {
            g.fillRect(tank.getX() + 20, tank.getY(), 24, 34);
        } else if (tank.getDirection() == Direction.BOTTOM) {
            g.fillRect(tank.getX() + 20, tank.getY() + 30, 24, 34);
        } else if (tank.getDirection() == Direction.LEFT) {
            g.fillRect(tank.getX(), tank.getY() + 20, 34, 24);
        } else {
            g.fillRect(tank.getX() + 30, tank.getY() + 20, 34, 24);
        }

        g.setColor(new Color(255, 255, 0));
        g.fillRect(bullet.getX(), bullet.getY(), 14, 14);
    }

    public void runTheGame() throws Exception{

        tank.clean();


    }

    public String getQuadrant(int x, int y){
        return y / battleField.SIZE_QUADRANT + "_" + x / battleField.SIZE_QUADRANT;
    }

    public String getQuadrantXY(int v, int h){
        return (v - 1) * battleField.SIZE_QUADRANT + "_" + (h - 1) * battleField.SIZE_QUADRANT;
    }

    public void processTurn(Tank tank) throws Exception{
        repaint();
    }

    public void processDestroy(Tank tank) throws Exception{
        repaint();
    }

    public void processMove(Tank tank) throws Exception{

        this.tank = tank;
        Direction direction = tank.getDirection();
        int step = 1;
        int covered = 0;

        if (checkLimits(tank)){return;}

        tank.turn(direction);

        while (covered < battleField.SIZE_QUADRANT) {

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

    public boolean checkLimits(Tank tank) {

        Direction direction = tank.getDirection();

        if ((direction == Direction.UP && tank.getY() == 0)
                || (direction == Direction.BOTTOM && tank.getY() >= battleField.getBfHeight())
                || (direction == Direction.LEFT && tank.getX() == 0)
                || (direction == Direction.RIGHT && tank.getX() >= battleField.getBfWidth())
                || (nextQuadrantBrik(direction))
                ) {
            return true;
        }
        return false;
    }

    private boolean nextQuadrantBrik(Direction direction) {

        int tmpTankX = tank.getX();
        int tmpTankY = tank.getY();

        if (direction == Direction.UP) {
            tmpTankY -= battleField.SIZE_QUADRANT;
        } else if (direction == Direction.BOTTOM) {
            tmpTankY += battleField.SIZE_QUADRANT;
        } else if (direction == Direction.LEFT) {
            tmpTankX -= battleField.SIZE_QUADRANT;
        } else if (direction == Direction.RIGHT){
            tmpTankX += battleField.SIZE_QUADRANT;
        }

        String coordinates = getQuadrant(tmpTankX, tmpTankY);
        int y = Integer.parseInt(coordinates.split("_")[0]);
        int x = Integer.parseInt(coordinates.split("_")[1]);

        if (y >= 0 && y < battleField.getDimentionY() && x >= 0 && x < battleField.getDimentionX()) {
            if (!battleField.scanQuadrant(y,x).trim().isEmpty()) {
                return true;
            }
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

    private boolean processInterception() {

        String coordinates = getQuadrant(bullet.getX(), bullet.getY());
        int y = Integer.parseInt(coordinates.split("_")[0]);
        int x = Integer.parseInt(coordinates.split("_")[1]);


        if (y >= 0 && y < battleField.getDimentionY() && x >= 0 && x < battleField.getDimentionX()) {
            if (battleField.isBrick(y,x)) {
                battleField.updateQuadrant(y, x, "");
                battleField.setCountOfBriks(battleField.getCountOfBriks() - 1);
                return true;
            }
        }

        return false;
    }

    public int howManyBricksInDirection(Direction direction) {

        int result = 0;

        String coordinates = getQuadrant(tank.getX(), tank.getY());
        int y = Integer.parseInt(coordinates.split("_")[0]);
        int x = Integer.parseInt(coordinates.split("_")[1]);

        if (direction == Direction.UP) {
            for (int j = 0; j <= y; j++) {
                if (battleField.isBrick(j, x)) {
                    result++;
                }
            }

        } else if (direction == Direction.BOTTOM) {
            for (int j = y; j <= battleField.getDimentionY() - 1; j++) {
                if (battleField.isBrick(j, x)) {
                    result++;
                }
            }
        } else if (direction == Direction.LEFT) {
            for (int j = x; j >= 0; j--) {
                if (battleField.isBrick(y, j)) {
                    result++;
                }
            }

        } else if (direction == Direction.RIGHT) {
            for (int j = x; j <= battleField.getDimentionX() - 1; j++) {
                if (battleField.isBrick(y, j)) {
                    result++;
                }
            }
        }

        return result;
    }



}
