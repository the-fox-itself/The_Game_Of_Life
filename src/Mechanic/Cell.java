package Mechanic;

public class Cell {
    public Cell(int x, int y, int t) {
        this.x = x;
        this.y = y;
        type = t;
    }

    public int x;
    public int y;

    public int type;
    public static final int TYPE_COMMON = 1;
    public static final int TYPE_CONSTANT = 2;
}
