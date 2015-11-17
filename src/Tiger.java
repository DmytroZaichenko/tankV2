import java.awt.Color;
import java.awt.Graphics;

public class Tiger extends AbstractTank {

    private int armor = 1;

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public int getArmor() {
        return armor;
    }

    public Tiger(ActionField af, BattleField bf, int x, int y, Direction direction, int armor) {
        super(af, bf, x, y, direction);
        setArmor(armor);
    }

    @Override
    public void destroy() throws Exception {
        if (armor > 0){
            armor --;
        }else {
            super.destroy();
        }
    }

    @Override
    public void draw(Graphics g) {


        g.setColor(new Color(255, 0, 0));
        g.fillRect(this.getX(), this.getY(), bf.SIZE_QUADRANT, bf.SIZE_QUADRANT);

        g.setColor(new Color(0, 255, 0));

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
