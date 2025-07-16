public abstract class Character implements Movable {
    private int x, y;
    private int cellSize;
    private int[][] map;

    public Character(int x, int y, int cellSize, int[][] map) {
        this.x = x;
        this.y = y;
        this.cellSize = cellSize;
        this.map = map;
    }

    protected boolean canMove(int x, int y) {
        return x >= 0 && x < map.length && y >= 0 && y < map[0].length && map[x][y] == 0;
    }

    @Override
    public abstract void move();

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getCellSize() {
        return cellSize;
    }


    public int[][] getMap() {
        return map;
    }

}
