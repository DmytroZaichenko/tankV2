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

    public Tank(ActionField af, BattleField bf){
        this(af, bf, 64, 512, 1);
    }

    public Tank(ActionField af, BattleField bf, int x, int y, int direction){
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

    public void move()throws Exception{
        af.processMove(this);
    }

    public void fire() throws Exception{
        Bullet bullet = new Bullet((x + 25), (y + 25), direction);
        af.processFire(bullet);
    }


    public void updateX(int x){
        this.x += x;
    }

    public void updateY(int y){
        this.y += y;
    }

    public void moveToQuadrant(int v, int h) throws Exception {

//        String coordinates = actionField.getQuadrantXY(v, h);
//
//        int y = Integer.parseInt(coordinates.split("_")[0]);
//        int x = Integer.parseInt(coordinates.split("_")[1]);
//
//        boolean key = true;
//
//        while (key) {
//
//            if (x != this.x && x >= 0 && x <= 576) {
//                if (x > this.x) {
//                    move(RIGHT);
//                } else {
//                    move(LEFT);
//                }
//            } else {
//                break;
//            }
//        }
//
//        key = true;
//
//        while (key) {
//
//            if (y != tankY && y >= 0 && y <= max_limit_y) {
//                if (y > tankY) {
//                    key = move(BOTTOM);
//                } else {
//                    key = move(UP);
//                }
//            } else {
//                break;
//            }
//        }

    }
}
