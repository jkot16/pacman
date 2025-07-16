import java.awt.*;

public class Upgrade {
    private final int x;
    private final int y;
    private final UpgradeType type;
    private boolean isActive;
    private static final int UPGRADEDURATION = 8000;

    public Upgrade(int x, int y, UpgradeType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.isActive = true;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public UpgradeType getType() {
        return type;
    }

    public boolean isActive() {
        return isActive;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public Color getUpgradeColor() {
        switch (type) {
            case SPEEDBOOST:
                return Color.ORANGE;
            case EXTRALIFE:
                return Color.RED;
            case DOUBLEPOINTS:
                return Color.YELLOW;
            case INVISIBILITY:
                return Color.WHITE;
            case SLOWENEMIES:
                return Color.GREEN;
            default:
                return Color.BLACK;
        }
    }

    public void applyUpgrade(Player player, Map gameMap) {
        System.out.println("Applying upgrade: " + type);
        switch (type) {
            case SPEEDBOOST:
                player.setSpeed(player.getSpeed() * 1.5);
                break;
            case EXTRALIFE:
                player.gainLife();
                break;
            case DOUBLEPOINTS:
                player.setDoublePoints(true);
                break;
            case INVISIBILITY:
                player.setInvisible(true);
                break;
            case SLOWENEMIES:
                gameMap.slowEnemies();
                break;
        }
        Thread applyUpgradeThread = new Thread(() -> {
            try {
                Thread.sleep(UPGRADEDURATION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            removeUpgrade(player, gameMap);
        });
        applyUpgradeThread.start();
    }

    private void removeUpgrade(Player player, Map gameMap) {
        System.out.println("Removing upgrade: " + type);
        switch (type) {
            case SPEEDBOOST:
                player.setSpeed(player.getOriginalSpeed());
                break;
            case DOUBLEPOINTS:
                player.setDoublePoints(false);
                break;
            case INVISIBILITY:
                player.setInvisible(false);
                break;
            case SLOWENEMIES:
                gameMap.resetEnemySpeed();
                break;
        }
        deactivate();
    }
}
