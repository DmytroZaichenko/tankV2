import java.util.Random;

/**
 * Created by DmitryZ on 02.11.2015.
 */
public class Tank {

    private int speed = 10;

    private int x;
    private int y;

    private int direction;

    ActionField af;
    BattleField bf;

    public Tank(ActionField af, BattleField bf) {
        this(af, bf, 0, 512, 1);
    }

    public Tank(ActionField af, BattleField bf, int x, int y, int direction) {
        this.af = af;
        this.bf = bf;
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public int getSpeed() {
        return speed;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDirection() {
        return direction;
    }

    public void turn(int direction) throws Exception {
        this.direction = direction;
        af.processTurn(this);
    }

    public void move() throws Exception {
        af.processMove(this);
    }

    public void fire() throws Exception {
        Bullet bullet = new Bullet((x + 25), (y + 25), direction);
        af.processFire(bullet);
    }


    public void updateX(int x) {
        this.x += x;
    }

    public void updateY(int y) {
        this.y += y;
    }

    public void shootingBrick(TankDirection direction) throws Exception {

        int howShots = af.howManyBricksInDirection(direction);
        while (howShots > 0) {
            fire();
            howShots--;
            if (howShots > 0) {
                turn(direction.getValue());
                move();
            }
        }

    }


    public void moveToQuadrant(int v, int h) throws Exception {

        String coordinates = af.getQuadrantXY(v, h);

        int y = Integer.parseInt(coordinates.split("_")[0]);
        int x = Integer.parseInt(coordinates.split("_")[1]);

        boolean key = true;

        while (key) {

            if (x != this.x && x >= 0 && x <= 576) {
                if (x > this.x) {
                    turn(TankDirection.RIGHT.getValue());
                } else {
                    turn(TankDirection.LEFT.getValue());
                }
                move();
            } else {
                break;
            }
        }

        key = true;

        while (key) {

            if (y != this.y && y >= 0 && y <= 576) {
                if (y > this.y) {
                    turn(TankDirection.BOTTOM.getValue());
                } else {
                    turn(TankDirection.UP.getValue());
                }
                move();
            } else {
                break;
            }
        }
    }

    public void moveRandom() throws Exception {

        while (true) {

            long time = System.currentTimeMillis();
            String s = String.valueOf(time);
            String lastChar = s.substring(s.length() - 5);

            char[] ch = lastChar.toCharArray();

            for (char lt : ch) {

                int direction = Integer.parseInt(String.valueOf(lt));

                if (direction > 0 && direction < 5) {
                    turn(direction);
                    move();
                }
            }

        }

    }

    public void clean() throws Exception {

        int rBrick = 10;
        Random rand = new Random();
        int countBricks = bf.getCountOfBriks();

        while (countBricks > 0) {

            spinningAroundAndShoot();

            int r;

            if (bf.getCountOfBriks() > rBrick) {
                r = rand.nextInt(5);
                r = r == 4 ? 1 : r;
                turn(TankDirection.values()[r].getValue());
                move();
            } else {
                spinningAroundAndShoot();
                cleanRemainderBricks(rBrick);
                break;
            }

        }
    }

    private void cleanRemainderBricks(int countRtaminderBricks) throws Exception {

        int[][]coordinatsBrik = new int[bf.getCountOfBriks()][2];
        int idy = 0;
        for (int y = 0; y < bf.getDimentionY(); y++) {
            for (int x = 0; x < bf.getDimentionX(); x++) {
                if (bf.isBrick(y, x)) {
                    coordinatsBrik[idy][0] = y ;
                    coordinatsBrik[idy][1] = x ;
                    idy++;
                }
            }
        }

        String coordinates = af.getQuadrant(this.x, this.y);
        int y = Integer.parseInt(coordinates.split("_")[0]);
        int x = Integer.parseInt(coordinates.split("_")[1]);

        int yMin = coordinatsBrik[0][0];
        int yMax = coordinatsBrik[coordinatsBrik.length - 1][0];

        TankDirection directionMove = TankDirection.UP;
        int board = 0;

        if (y > yMin && y < yMax || y < yMin) {
            directionMove = TankDirection.BOTTOM;
            board = yMax * 64;
            turn(TankDirection.UP.getValue());
            while (this.y != yMin * 64) {
                move();
            }
        } else if (y > yMax) {
            directionMove = TankDirection.UP;
            board = yMin * 64;
            turn(TankDirection.UP.getValue());
            while (this.y != yMax * 64) {
                move();
            }
        }

        while (board != this.y) {
            spinningAroundAndShoot();
            turn(directionMove.getValue());
            move();
        }

        spinningAroundAndShoot();

    }

    private void spinningAroundAndShoot() throws Exception {

        for (TankDirection direction : TankDirection.values()) {

            turn(direction.getValue());
            shootingBrick(direction);
        }
    }

}
