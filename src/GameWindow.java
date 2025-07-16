import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class GameWindow extends JFrame {
    private Map map;
    private Thread gameThread;
    private Thread timeThread;
    private static volatile boolean running = true;
    private double speed;
    private String mapSize;

    public GameWindow(int mapWidth, int mapHeight, double speed, String mapSize) {
        this.speed = speed;
        this.mapSize = mapSize;
        setTitle("New Game");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        map = new Map(mapWidth, mapHeight, speed, this);
        add(map);

        setupEscapeKey();

        // Main functional threads
        startGameThread();
        startTimeThread();

        setVisible(true);
        map.requestFocusInWindow();
    }


    private void setupEscapeKey() {
        InputMap inputMap = map.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = map.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "exitGame");
        actionMap.put("exitGame", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                running = false;
                showExitConfirmationDialog();
            }
        });
    }

    private void startGameThread() {
        Thread gameThread = new Thread(() -> {
            try {
                while (running) {
                    map.updateGameEvents();
                    map.revalidate();
                    Thread.sleep((long)(1000/speed));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        gameThread.start();
    }

    private void startTimeThread() {
        Thread timeThread = new Thread(() -> {
            try {
                while (running) {
                    Thread.sleep(1000);
                    map.incrementTime();
                    map.revalidate();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        timeThread.start();
    }

    public void gameOver() {
        running = false;
        JTextField nameField = new JTextField();
        Object[] message = {
                "Enter your nickname for High Scores:", nameField
        };
        int option = JOptionPane.showConfirmDialog(this, message, "InputWindow", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            String nick = nameField.getText().trim();
            if (!nick.isEmpty()) {
                HighScore highScore = new HighScore(nick, map.getPlayer().getScore(), mapSize);
                HighScore.saveHighScore(highScore);
            }
        }
        this.dispose();
        SwingUtilities.invokeLater(() -> {
            Window menuWindow = new Window();
            menuWindow.setVisible(true);
        });
    }

    private void showExitConfirmationDialog() {
        JDialog dialog = new JDialog(this, "Exit Game", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);

        JLabel message = new JLabel("Are you sure you want to leave the game?", JLabel.CENTER);
        dialog.add(message, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton yesButton = new JButton("Yes");
        yesButton.setBackground(Color.GREEN);
        yesButton.setOpaque(true);
        yesButton.setBorderPainted(true);
        yesButton.addActionListener(e -> {
            dialog.dispose();
            running = false;
            dispose();
            Window menuWindow = new Window();
            menuWindow.setVisible(true);
        });

        JButton noButton = new JButton("No");
        noButton.setBackground(Color.RED);
        noButton.setOpaque(true);
        noButton.setBorderPainted(true);
        noButton.addActionListener(e -> {
            dialog.dispose();
            running = true;
            startGameThread();
            startTimeThread();
            map.requestFocusInWindow();
        });

        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);

        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
