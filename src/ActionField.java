import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class ActionField extends JPanel{

    private boolean COLORED_MODE = false;

    private BattleField battleField;

    private Tank defender;
    private Tiger aggressor;

    private Bullet bullet;


    public ActionField() throws  Exception{

        battleField = new BattleField();
        defender = new BT7(this, battleField);

        createAggressor();

        bullet = new Bullet(-100, -100, Direction.BOTTOM);

        JFrame frame = new JFrame("BATTLE FIELD, DAY 4");
        frame.setLocation(750, 150);
        frame.setMinimumSize(new Dimension(battleField.getBfWidth() + 17, battleField.getBfHeight() + 35));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.pack();
        frame.setVisible(true);

    }

    public void createAggressor() {

//        Tank[] tanks = {defender};
//        int[] locAggressor = battleField.getAggressorLocation(tanks);
//
//        if (locAggressor != null) {
//            aggressor = new Tiger(this, battleField, locAggressor[1], locAggressor[0], Direction.UP);
//        }
        aggressor = new Tiger(this, battleField, 0, 3*64, Direction.UP, 1);
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
                //if (battleField.scanQuadrant(j,k).equals("B")) {
                if (battleField.isBrick(j,k)) {
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

        //defender
        g.setColor(new Color(0, 255, 0));
        g.fillRect(defender.getX(), defender.getY(), sizeQuadrant, sizeQuadrant);

        g.setColor(new Color(255, 0, 0));

        if (defender.getDirection() == Direction.UP) {
            g.fillRect(defender.getX() + 20, defender.getY(), 24, 34);
        } else if (defender.getDirection() == Direction.BOTTOM) {
            g.fillRect(defender.getX() + 20, defender.getY() + 30, 24, 34);
        } else if (defender.getDirection() == Direction.LEFT) {
            g.fillRect(defender.getX(), defender.getY() + 20, 34, 24);
        } else {
            g.fillRect(defender.getX() + 30, defender.getY() + 20, 34, 24);
        }

        //aggressor
        g.setColor(new Color(255, 0, 0));
        g.fillRect(aggressor.getX(), aggressor.getY(), sizeQuadrant, sizeQuadrant);

        g.setColor(new Color(0, 255, 0));

        if (aggressor.getDirection() == Direction.UP) {
            g.fillRect(aggressor.getX() + 20, aggressor.getY(), 24, 34);
        } else if (aggressor.getDirection() == Direction.BOTTOM) {
            g.fillRect(aggressor.getX() + 20, aggressor.getY() + 30, 24, 34);
        } else if (aggressor.getDirection() == Direction.LEFT) {
            g.fillRect(aggressor.getX(), aggressor.getY() + 20, 34, 24);
        } else {
            g.fillRect(aggressor.getX() + 30, aggressor.getY() + 20, 34, 24);
        }


        g.setColor(new Color(255, 255, 0));
        g.fillRect(bullet.getX(), bullet.getY(), 14, 14);

    }

    public void runTheGame() throws Exception{

        defender.fire();
        defender.fire();
        defender.fire();
        defender.fire();
        defender.fire();
        defender.fire();

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
                || (nextQuadrantBrik(tank, direction))
                ) {
            return true;
        }
        return false;
    }

    private boolean nextQuadrantBrik(Tank tank, Direction direction) {

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

    private boolean processInterception() throws Exception {

        String coordinates = getQuadrant(bullet.getX(), bullet.getY());
        int y = Integer.parseInt(coordinates.split("_")[0]);
        int x = Integer.parseInt(coordinates.split("_")[1]);


        if (y >= 0 && y < battleField.getDimentionY() && x >= 0 && x < battleField.getDimentionX()) {
            if (battleField.isBrick(y,x)) {
                battleField.updateQuadrant(y, x, "");
                battleField.setCountOfBriks(battleField.getCountOfBriks() - 1);
                return true;
            }

            if (defender.bullet != bullet && battleField.isCoordinatesTank(defender,y,x)){
                defender.destroy();
                return true;
            }

            if (aggressor.bullet != bullet && battleField.isCoordinatesTank(aggressor,y,x)){
                aggressor.destroy();
                return true;
            }
        }

        return false;
    }

    public int howManyBricksInDirection(Tank tank, Direction direction) {

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

    public void shootingBrick(Tank tank, Direction direction) throws Exception {

        int howShots = howManyBricksInDirection(tank, direction);
        while (howShots > 0) {
            tank.fire();
            howShots--;
            if (howShots > 0) {
                tank.turn(direction);
                tank.move();
            }
        }

    }


    public void moveToQuadrant(Tank tank, int v, int h) throws Exception {

        String coordinates = getQuadrantXY(v, h);

        int y = Integer.parseInt(coordinates.split("_")[0]);
        int x = Integer.parseInt(coordinates.split("_")[1]);

        boolean key = true;

        while (key) {

            if (x != tank.getX() && x >= 0 && x <= battleField.getDimentionX()) {
                if (x > tank.getX()) {
                    tank.turn(Direction.RIGHT);
                } else {
                    tank.turn(Direction.LEFT);
                }
                tank.move();
            } else {
                break;
            }
        }

        key = true;

        int tankY = tank.getY();

        while (key) {

            if (y != tankY && y >= 0 && y <= battleField.getDimentionY()) {
                if (y > tank.getY()) {
                    tank.turn(Direction.BOTTOM);
                } else {
                    tank.turn(Direction.UP);
                }
                tank.move();
            } else {
                break;
            }
        }
    }

    public void moveRandom(Tank tank) throws Exception {

        while (true) {

            long time = System.currentTimeMillis();
            String s = String.valueOf(time);
            String lastChar = s.substring(s.length() - 5);

            char[] ch = lastChar.toCharArray();

            for (char lt : ch) {

                int direction = Integer.parseInt(String.valueOf(lt));

                if (direction > 0 && direction < 5) {
                    tank.turn(Direction.values()[direction]);
                    tank.move();
                }
            }

        }

    }

    public void clean(Tank tank) throws Exception {

        int rBrick = 10;
        Random rand = new Random();
        int countBricks = battleField.getCountOfBriks();

        while (countBricks > 0) {

            spinningAroundAndShoot(tank);

            int r;

            if (battleField.getCountOfBriks() > rBrick) {
                r = rand.nextInt(4);
                r = r == 4 ? 1 : r;
                tank.turn(Direction.values()[r]);
                tank.move();
            } else {
                spinningAroundAndShoot(tank);
                cleanRemainderBricks(tank);
                break;
            }

        }
    }

    private void cleanRemainderBricks(Tank tank) throws Exception {

        int tankX = tank.getX();
        int tankY = tank.getY();

        int[][]coordinatsBrik = new int[battleField.getCountOfBriks()][2];
        int idy = 0;
        for (int y = 0; y < battleField.getDimentionY(); y++) {
            for (int x = 0; x < battleField.getDimentionX(); x++) {
                if (battleField.isBrick(y, x)) {
                    coordinatsBrik[idy][0] = y ;
                    coordinatsBrik[idy][1] = x ;
                    idy++;
                }
            }
        }

        String coordinates = getQuadrant(tankX, tankY);
        int y = Integer.parseInt(coordinates.split("_")[0]);
        int x = Integer.parseInt(coordinates.split("_")[1]);

        int yMin = coordinatsBrik[0][0];
        int yMax = coordinatsBrik[coordinatsBrik.length - 1][0];

        Direction directionMove = Direction.UP;
        int board = 0;

        if (y > yMin && y < yMax || y < yMin) {
            directionMove = Direction.BOTTOM;
            board = yMax * battleField.SIZE_QUADRANT;
            tank.turn(Direction.UP);
            while (tankY != yMin * battleField.SIZE_QUADRANT) {
                tank.move();
            }
        } else if (y > yMax) {
            directionMove = Direction.UP;
            board = yMin * battleField.SIZE_QUADRANT;
            tank.turn(Direction.UP);
            while (tankY != yMax * battleField.SIZE_QUADRANT) {
                tank.move();
            }
        }

        while (board != tankY) {
            spinningAroundAndShoot(tank);
            tank.turn(directionMove);
            tank.move();
        }

        spinningAroundAndShoot(tank);

    }

    private void spinningAroundAndShoot(Tank tank) throws Exception {

        for (Direction direction : Direction.values()) {

            if (direction == Direction.NONE){
                continue;
            }

            tank.turn(direction);
            shootingBrick(tank,direction);
        }
    }



}
