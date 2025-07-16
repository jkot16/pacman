import java.util.Random;

public class Ghost extends Character implements Movable {
    private Player player;
    private Map gameMap;
    private Random random = new Random();
    private double lastUpgradeTime = System.currentTimeMillis();
    private double speed;
    private double originalSpeed;

    public Ghost(int x, int y, int cellSize, int[][] map, Player player, Map gameMap) {
        super(x, y, cellSize, map);
        this.player = player;
        this.gameMap = gameMap;
        this.originalSpeed = this.speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getOriginalSpeed() {
        return originalSpeed;
    }

    @Override
    public void move() {
        int nextX = getX();
        int nextY = getY();

        int direction = random.nextInt(4);
        switch (direction) {
            case 0:
                nextX = getX() - 1;
                break;
            case 1:
                nextX = getX() + 1;
                break;
            case 2:
                nextY = getY() - 1;
                break;
            case 3:
                nextY = getY() + 1;
                break;
        }

        if (canMove(nextX, nextY) && !gameMap.isEnemyAt(nextX, nextY)) {
            setX(nextX);
            setY(nextY);
        }

        if (System.currentTimeMillis() - lastUpgradeTime > 5000 && random.nextInt(4) == 0) {
            lastUpgradeTime = System.currentTimeMillis();
            gameMap.createUpgrade(getX(), getY());
        }
    }


    @Override
    public boolean isEnemy() {
        return true;
    }
}
