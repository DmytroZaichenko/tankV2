import java.util.Arrays;
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

        coordinatesTank = new int[y*x][2];

        int idx = 0;

        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++){
                if (!bf.isBrick(i, j) && j != super.getX() && i != super.getY() ){
                    coordinatesTank[idx][0] = i;
                    coordinatesTank[idx][1] = j;
                    idx ++;
                }
            }
        }

        //System.out.println(Arrays.deepToString(coordinatesTank));
        Random r = new Random();
        int rand = r.nextInt(idx - 1);
        return coordinatesTank[rand];

    }


}
