import java.awt.Color;
import java.awt.Graphics;

public class T34 extends AbstractTank {

    private int speed = super.getSpeed() * 2;

    public T34(ActionField af, BattleField bf) {
        super(af, bf);
        speed = 5;
    }

    public T34(ActionField af, BattleField bf, int x, int y, Direction direction) {
        super(af, bf, x, y, direction);
        speed = 5;
    }

    @Override
    public void draw(Graphics g) {
        
        g.setColor(new Color(0, 255, 0));
        g.fillRect(this.getX(), this.getY(), bf.SIZE_QUADRANT, bf.SIZE_QUADRANT);

        g.setColor(new Color(255, 0, 0));

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
