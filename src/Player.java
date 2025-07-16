import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class Player extends Character implements Movable {
    private int dx, dy;
    private double speed;
    private double originalSpeed;
    private int score;
    private int lives;
    private boolean invisible;
    private boolean doublePoints;
    private static volatile boolean running = true;
    private Thread animationThread;
    private int currentFrame;
    private double lastUpdateTime;
    private static final int ANIMATIONDELAY = 100;

    private Map<String, ImageIcon[]> animations = new HashMap<>();

    public Player(int x, int y, int cellSize, int[][] map) {
        super(x, y, cellSize, map);
        this.lives = 3;
        this.invisible = false;
        this.doublePoints = false;
        this.score = 0;
        this.currentFrame = 0;
        this.originalSpeed = speed;
        loadAnimations();
        animationThread();
    }

    private void loadAnimations() {
        animations.put("walkUp", new ImageIcon[]{
                loadImage("up"),
                loadImage("sUp"),
                loadImage("openUp")
        });

        animations.put("walkDown", new ImageIcon[]{
                loadImage("down"),
                loadImage("sDown"),
                loadImage("openDown")
        });

        animations.put("walkLeft", new ImageIcon[]{
                loadImage("left"),
                loadImage("sLeft"),
                loadImage("openLeft")
        });

        animations.put("walkRight", new ImageIcon[]{
                loadImage("right"),
                loadImage("sRight"),
                loadImage("openRight")
        });
    }

    private ImageIcon loadImage(String name) {
        return new ImageIcon("src/images/animation/" + name + ".png");
    }


    private void updateAnimation() {
        double currentTime = System.currentTimeMillis();

        if (currentTime - lastUpdateTime > ANIMATIONDELAY) {
            String currentAnimationKey = getCurrentAnimationKey();
            ImageIcon[] currentAnimationFrames = animations.get(currentAnimationKey);
            currentFrame = (currentFrame + 1) % currentAnimationFrames.length;
            lastUpdateTime = currentTime;
        }
    }

    private String getCurrentAnimationKey() {
        if (dx == 0 && dy == -1) {
            return "walkUp";
        } else if (dx == 0 && dy == 1) {
            return "walkDown";
        } else if (dx == -1 && dy == 0) {
            return "walkLeft";
        } else {
            return "walkRight";
        }
    }
    public ImageIcon getCurrentAnimationFrame() {
        String currentAnimationKey = getCurrentAnimationKey();
        ImageIcon[] currentAnimationFrames = animations.get(currentAnimationKey);
        return currentAnimationFrames[currentFrame];
    }
    private void animationThread() {
        Thread animationThread = new Thread(() -> {
            while (running) {
                updateAnimation();
                try {
                    Thread.sleep(ANIMATIONDELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        });
        animationThread.start();
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }

    public double getOriginalSpeed() {
        return originalSpeed;
    }

    public void setDirection(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void increaseScore(int value) {
        score += doublePoints ? value * 2 : value;
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public void gainLife() {
        lives++;
    }

    public void loseLife() {
        lives--;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public void setDoublePoints(boolean doublePoints) {
        this.doublePoints = doublePoints;
    }

    public void respawn(int x, int y) {
        setX(x);
        setY(y);
    }

    @Override
    public void move() {
        int nextX = getX() + dx;
        int nextY = getY() + dy;

        if (nextX < 0) {
            nextX = getMap().length - 1;
        } else if (nextX >= getMap().length) {
            nextX = 0;
        }

        if (canMove(nextX, nextY)) {
            setX(nextX);
            setY(nextY);
        }
    }

    @Override
    public boolean isEnemy() {
        return false;
    }


}
