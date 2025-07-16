import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Map extends JPanel implements KeyListener {
    private List<Movable> movables = new ArrayList<>();
    private List<Upgrade> upgrades = new ArrayList<>();
    private Player player;
    private int mapWidth;
    private int mapHeight;
    private int[][] map;
    private JPanel[][] cellGrid;
    private boolean[][] points;
    private Random random = new Random();
    private int timeElapsed = 0;
    private GameWindow gameWindow;
    private JPanel gridPanel;
    private JLabel livesLabel;
    private JLabel scoreLabel;
    private JLabel timeLabel;

    public Map(int mapWidth, int mapHeight, double speed, GameWindow gameWindow) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.gameWindow = gameWindow;
        setLayout(new BorderLayout());
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(mapHeight, mapWidth));
        add(gridPanel, BorderLayout.CENTER);

        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(3, 1));
        livesLabel = new JLabel("Lives: 0");
        scoreLabel = new JLabel("Score: 0");
        timeLabel = new JLabel("Time: 0s");
        statsPanel.add(livesLabel);
        statsPanel.add(scoreLabel);
        statsPanel.add(timeLabel);
        add(statsPanel, BorderLayout.NORTH);

        setupMap(true, speed);
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
    }

    public void updateGameEvents() {
        for (Movable movable : movables) {
            movable.move();
        }
        checkFoodAndUpgrades();
        checkCollisions();
        if (allFoodCollected()) {
            gameWindow.gameOver();
        }
        setupMap(false, 0);
    }

    public Player getPlayer() {
        return player;
    }

    private boolean allFoodCollected() {
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                if (points[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }


    private void checkCollisions() {
        for (Movable movable : movables) {
            if (movable != player && movable.getX() == player.getX() && movable.getY() == player.getY()) {
                if (!player.isInvisible()) {
                    player.loseLife();
                    if (player.getLives() > 0) {
                        respawnPlayer();
                    } else {
                        gameWindow.gameOver();
                    }
                }
                break;
            }
        }
    }

    private void respawnPlayer() {
        int playerSpawnX;
        int playerSpawnY;
        do {
            playerSpawnX = random.nextInt(mapWidth);
            playerSpawnY = random.nextInt(mapHeight);
        } while (map[playerSpawnX][playerSpawnY] == 1 || isEnemyAt(playerSpawnX, playerSpawnY));
        player.respawn(playerSpawnX, playerSpawnY);
    }

    public boolean isEnemyAt(int x, int y) {
        for (Movable movable : movables) {
            if (movable.isEnemy() && movable.getX() == x && movable.getY() == y) {
                return true;
            }
        }
        return false;
    }

    private void initializeEnemies(int numberOfEnemies) {
        for (int i = 0; i < numberOfEnemies; i++) {
            int enemyStartX;
            int enemyStartY;
            do {
                enemyStartX = random.nextInt(mapWidth);
                enemyStartY = random.nextInt(mapHeight);
            } while (map[enemyStartX][enemyStartY] == 1 || (enemyStartX == player.getX() && enemyStartY == player.getY()));

            Ghost ghost = new Ghost(enemyStartX, enemyStartY, player.getCellSize(), map, player, this);
            movables.add(ghost);
        }
    }


    private void initializeMiniMap() {
        int[][] miniMap = {
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
                {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1},
                {1, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        };

        for (int i = 0; i < miniMap.length; i++) {
            for (int j = 0; j < miniMap[i].length; j++) {
                map[j][i] = miniMap[i][j];
                points[j][i] = (map[j][i] == 0);
            }
        }
    }


    private void initializeSmallMap() {
        int[][] smallMap = {
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1},
                {1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1},
                {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1},
                {1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1},
                {1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };

        for (int i = 0; i < smallMap.length; i++) {
            for (int j = 0; j < smallMap[i].length; j++) {
                map[j][i] = smallMap[i][j];
                points[j][i] = (map[j][i] == 0);
            }
        }
    }

    private void initializeMediumMap() {
        int[][] mediumMap = {
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1},
                {1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 1, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1},
                {1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1},
                {1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1},
                {1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1},
                {1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 1, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1},
                {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };

        for (int i = 0; i < mediumMap.length; i++) {
            for (int j = 0; j < mediumMap[i].length; j++) {
                map[j][i] = mediumMap[i][j];
                points[j][i] = (map[j][i] == 0);
            }
        }
    }

    private void initializeLargeMap() {
        int[][] largeMap = {
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 0, 1},
                {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1},
                {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
                {1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1},
                {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 1},
                {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };

        for (int i = 0; i < largeMap.length; i++) {
            for (int j = 0; j < largeMap[i].length; j++) {
                map[j][i] = largeMap[i][j];
                points[j][i] = (map[j][i] == 0);
            }
        }
    }


    private void initializeExtraLargeMap() {
        int[][] extraLargeMap = {
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1},
                {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };

        for (int i = 0; i < extraLargeMap.length; i++) {
            for (int j = 0; j < extraLargeMap[i].length; j++) {
                map[j][i] = extraLargeMap[i][j];
                points[j][i] = (map[j][i] == 0);
            }
        }
    }


    private int getEnemyCount(int mapWidth, int mapHeight) {
        if (mapWidth == 13 && mapHeight == 9) {
            return 1;
        } else if (mapWidth == 15 && mapHeight == 11) {
            return 2;
        } else if (mapWidth == 25 && mapHeight == 17) {
            return 4;
        } else if (mapWidth == 35 && mapHeight == 23) {
            return 6;
        } else {
            return 10;
        }
    }

    public void incrementTime() {
        timeElapsed++;
    }

    private void checkFoodAndUpgrades() {
        if (points[player.getX()][player.getY()]) {
            points[player.getX()][player.getY()] = false;
            player.increaseScore(10);
        }

        for (int i = 0; i < upgrades.size(); i++) {
            Upgrade upgrade = upgrades.get(i);
            if (upgrade.isActive() && upgrade.getX() == player.getX() && upgrade.getY() == player.getY()) {
                upgrade.applyUpgrade(player, this);
                upgrades.remove(i);
                break;
            }
        }
    }

    public void resetEnemySpeed() {
        for (Movable movable : movables) {
            if (movable.isEnemy()) {
                movable.setSpeed(movable.getOriginalSpeed());
            }
        }
    }

    public void slowEnemies() {
        for (Movable movable : movables) {
            if (movable.isEnemy()) {
                movable.setSpeed(movable.getOriginalSpeed() * 0.5);
            }
        }
    }

    public void createUpgrade(int x, int y) {
        UpgradeType[] types = UpgradeType.values();
        for (UpgradeType type : types) {
            boolean exists = false;
            for (Upgrade upgrade : upgrades) {
                if (upgrade.isActive() && upgrade.getType() == type) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                Upgrade newUpgrade = new Upgrade(x, y, type);
                upgrades.add(newUpgrade);
                break;
            }
        }
    }

    private void setupMap(boolean initialize, double speed) {
        if (initialize) {
            map = new int[mapWidth][mapHeight];
            points = new boolean[mapWidth][mapHeight];
            cellGrid = new JPanel[mapWidth][mapHeight];

            int playerStartX = mapWidth / 2;
            int playerStartY = mapHeight / 2;

            player = new Player(playerStartX, playerStartY, 40, map);
            movables.add(player);
            player.setSpeed(speed);

            initializeEnemies(getEnemyCount(mapWidth, mapHeight));

            switch (mapWidth) {
                case 13:
                    if (mapHeight == 9) {
                        initializeMiniMap();
                    }
                    break;
                case 15:
                    if (mapHeight == 11) {
                        initializeSmallMap();
                    }
                    break;
                case 25:
                    if (mapHeight == 17) {
                        initializeMediumMap();
                    }
                    break;
                case 35:
                    if (mapHeight == 23) {
                        initializeLargeMap();
                    }
                    break;
                case 45:
                    if (mapHeight == 29) {
                        initializeExtraLargeMap();
                    }
                    break;
            }

            gridPanel.removeAll();
            gridPanel.setLayout(new GridLayout(mapHeight, mapWidth));

            for (int j = 0; j < mapHeight; j++) {
                for (int i = 0; i < mapWidth; i++) {
                    JPanel cell = new JPanel();
                    cell.setLayout(new GridBagLayout());
                    if (map[i][j] == 1) {
                        cell.setBackground(Color.BLUE);
                    } else {
                        cell.setBackground(Color.BLACK);
                        if (points[i][j]) {
                            JPanel point = new JPanel();
                            point.setBackground(Color.WHITE);
                            int foodSize = 10;
                            point.setPreferredSize(new Dimension(foodSize, foodSize));
                            cell.add(point, new GridBagConstraints());
                        }
                    }
                    gridPanel.add(cell);
                    cellGrid[i][j] = cell;
                }
            }

            gridPanel.revalidate();

        } else {
            int cellSize = player.getCellSize();
            int foodSize = cellSize / 4;

            for (int j = 0; j < mapHeight; j++) {
                for (int i = 0; i < mapWidth; i++) {
                    JPanel cell = cellGrid[i][j];
                    cell.removeAll();
                    cell.setLayout(new GridBagLayout());

                    GridBagConstraints gbc = new GridBagConstraints();
                    gbc.gridx = 0;
                    gbc.gridy = 0;
                    gbc.anchor = GridBagConstraints.CENTER;

                    if (map[i][j] == 1) {
                        cell.setBackground(Color.BLUE);
                    } else {
                        cell.setBackground(Color.BLACK);
                        if (points[i][j]) {
                            JPanel point = new JPanel();
                            point.setBackground(Color.WHITE);
                            point.setPreferredSize(new Dimension(foodSize, foodSize));
                            cell.add(point, gbc);
                        }
                    }
                    cell.revalidate();
                    cell.repaint();
                }
            }

            for (Upgrade upgrade : upgrades) {
                JPanel cell = cellGrid[upgrade.getX()][upgrade.getY()];
                cell.removeAll();
                cell.setLayout(new GridBagLayout());

                JPanel upgradePanel = new JPanel();
                upgradePanel.setBackground(upgrade.getUpgradeColor());
                int upgradeSize = Math.min(cellSize, cellSize) / 2;
                upgradePanel.setPreferredSize(new Dimension(upgradeSize, upgradeSize));

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.anchor = GridBagConstraints.CENTER;

                cell.add(upgradePanel, gbc);
                cell.revalidate();
            }

            for (Movable movable : movables) {
                JPanel cell = cellGrid[movable.getX()][movable.getY()];
                cell.removeAll();
                cell.setLayout(new GridBagLayout());

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.anchor = GridBagConstraints.CENTER;

                double entitySize = Math.min(cell.getWidth() * 1.3, cell.getHeight() * 1.3);

                if (movable == player) {
                    ImageIcon playerIcon = player.getCurrentAnimationFrame();
                    Image playerImage = playerIcon.getImage().getScaledInstance((int)entitySize, (int)entitySize, Image.SCALE_SMOOTH);
                    ImageIcon resizedPlayerIcon = new ImageIcon(playerImage);
                    JLabel playerLabel = new JLabel(resizedPlayerIcon);
                    playerLabel.setPreferredSize(new Dimension((int) entitySize, (int) entitySize));
                    cell.add(playerLabel, gbc);

                } else if (movable.isEnemy()) {
                    ImageIcon ghostIcon = new ImageIcon("src/images/ghost.png");
                    Image ghostImage = ghostIcon.getImage();
                    Image resizedGhostImage = ghostImage.getScaledInstance((int) entitySize, (int) entitySize, Image.SCALE_SMOOTH);
                    ImageIcon resizedGhostIcon = new ImageIcon(resizedGhostImage);
                    JLabel ghostLabel = new JLabel(resizedGhostIcon);
                    ghostLabel.setPreferredSize(new Dimension((int) entitySize, (int) entitySize));

                    cell.add(ghostLabel, gbc);
                }
                cell.revalidate();
                cell.repaint();
            }

            livesLabel.setText("Lives: " + player.getLives());
            scoreLabel.setText("Score: " + player.getScore());
            timeLabel.setText("Time: " + timeElapsed + "s");
            gridPanel.revalidate();
        }
    }


    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                player.setDirection(-1, 0);
                break;
            case KeyEvent.VK_RIGHT:
                player.setDirection(1, 0);
                break;
            case KeyEvent.VK_UP:
                player.setDirection(0, -1);
                break;
            case KeyEvent.VK_DOWN:
                player.setDirection(0, 1);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {}
}