import java.util.Random;

public class Aggressor extends Tank {

    private int [][] coordinatesTank;

    public Aggressor(ActionField af, BattleField bf) {
        super(af, bf);

    }

    public Aggressor(ActionField af, BattleField bf, int x, int y, Direction direction) {
        super(af, bf, x, y, direction);
    }

    public int[] addCoordinatesWithOutBrickAndTank(){

        int y = bf.getDimentionY();
        int x = bf.getDimentionX();

        int idx = 0;

        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++){
                if (!bf.isBrick(i, j) && j != super.getX() && i != super.getY() ){
                    coordinatesTank[idx][0] = y;
                    coordinatesTank[idx][1] = x;
                }
            }
        }

        Random r = new Random();
        int rand = r.nextInt(3);
        return coordinatesTank[rand];

    }


}
