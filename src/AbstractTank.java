import java.awt.Graphics;

public abstract class AbstractTank implements Drawable {

    protected int speed = 10;

    protected int x;
    protected int y;

    protected Direction direction;
    protected ActionField af;
    protected BattleField bf;
    protected Bullet bullet;

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
        af.processDestroy(this);

    }

    public abstract void draw(Graphics g);


}
