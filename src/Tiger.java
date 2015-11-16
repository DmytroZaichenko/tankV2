
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

}
