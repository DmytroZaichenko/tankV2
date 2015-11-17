import sun.security.krb5.internal.crypto.Des;

import java.awt.Graphics;
import java.awt.Color;

public abstract class AbstractTank implements Drawable,Destroyable {

    protected int speed = 10;

    protected int x;
    protected int y;

    protected Direction direction;
    protected ActionField af;
    protected BattleField bf;
    protected Bullet bullet;

    protected Color tankColor;
    protected Color towerColor;

    public AbstractTank(ActionField af, BattleField bf) {
        this(af, bf, 0, 512, Direction.UP);
    }

    public AbstractTank(ActionField af, BattleField bf, int x, int y, Direction direction) {
        this.af = af;
        this.bf = bf;
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public void turn(Direction direction) throws Exception {
        this.direction = direction;
        af.processTurn(this);
    }

    public void move() throws Exception {
        af.processMove(this);
    }

    public void fire() throws Exception {
        bullet = new Bullet((getX() + 25), (getY() + 25), direction);
        af.processFire(bullet);
    }

    public int getSpeed() {
        return speed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Direction getDirection() {
        return direction;
    }

    public void updateX(int x) {
        this.x += x;
    }

    public void updateY(int y) {
        this.y += y;
    }


    public void destroy() throws Exception {
        updateX(-100);
        updateY(-100);
    }

    public void draw(Graphics g){

        g.setColor(tankColor);
        g.fillRect(this.getX(), this.getY(), bf.SIZE_QUADRANT, bf.SIZE_QUADRANT);

        g.setColor(towerColor);

        if (this.getDirection() == Direction.UP) {
            g.fillRect(this.getX() + 20, this.getY(), 24, 34);
        } else if (this.getDirection() == Direction.BOTTOM) {
            g.fillRect(this.getX() + 20, this.getY() + 30, 24, 34);
        } else if (this.getDirection() == Direction.LEFT) {
            g.fillRect(this.getX(), this.getY() + 20, 34, 24);
        } else {
            g.fillRect(this.getX() + 30, this.getY() + 20, 34, 24);
        }
    }


}
