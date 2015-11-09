/**
 * Created by DmitryZ on 28.10.2015.
 */
public enum Direction {

    UP(1),BOTTOM(2),LEFT(3),RIGHT(4);

    private int value;

    private Direction(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
