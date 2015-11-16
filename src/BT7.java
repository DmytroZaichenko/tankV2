public class BT7 extends AbstractTank {


    private int speed = super.getSpeed() * 2;

    public BT7(ActionField af, BattleField bf) {
        super(af, bf);
        speed = 5;
    }

    public BT7(ActionField af, BattleField bf, int x, int y, Direction direction) {
        super(af, bf, x, y, direction);
        speed = 5;
    }

    public void turn(Direction direction) throws Exception {
        this.direction = direction;
        af.processTurn(this);
    }

    public void fire() throws Exception {
        bullet = new Bullet((getX() + 25), (getY() + 25), direction);
        af.processFire(bullet);
    }


}
