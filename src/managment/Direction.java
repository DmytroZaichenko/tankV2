package ua.tankv2.managment;

public enum Direction {

    UP(1),BOTTOM(2),LEFT(3),RIGHT(4),NONE(5);

    private int value;

    private Direction(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
