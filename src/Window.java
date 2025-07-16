import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Window extends JFrame {
    private Map map;

    public Window() {
        setTitle("Pacman");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        initializeMenu();
    }

    private void initializeMenu() {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(getWidth(), getHeight()));
        setContentPane(layeredPane);

        try {
            addBackgroundImage(layeredPane);
            addPacmanImage(layeredPane);
            addJavaImage(layeredPane);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);
    }

    private void addBackgroundImage(JLayeredPane layeredPane) {
        ImageIcon backgroundImage = new ImageIcon(getClass().getResource("images/background.png"));
        Image backgroundImg = backgroundImage.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        ImageIcon scaledBackgroundImage = new ImageIcon(backgroundImg);
        JLabel backgroundLabel = new JLabel(scaledBackgroundImage);
        backgroundLabel.setBounds(0, 0, getWidth(), getHeight());
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);
    }

    private void addPacmanImage(JLayeredPane layeredPane)  {
        ImageIcon image1 = new ImageIcon(getClass().getResource("/images/pacmann.png"));
        Image img1 = image1.getImage().getScaledInstance(800, 800, Image.SCALE_SMOOTH);
        ImageIcon scaledImage1 = new ImageIcon(img1);
        JLabel label1 = new JLabel(scaledImage1);
        label1.setBounds(0, -80, scaledImage1.getIconWidth(), scaledImage1.getIconHeight());
        layeredPane.add(label1, JLayeredPane.PALETTE_LAYER);
    }

    private void addJavaImage(JLayeredPane layeredPane)  {
        ImageIcon image2 = new ImageIcon(getClass().getResource("/images/javaa.png"));
        Image img2 = image2.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        ImageIcon scaledImage2 = new ImageIcon(img2);
        JLabel label2 = new JLabel(scaledImage2);
        label2.setBounds(450, 220, scaledImage2.getIconWidth(), scaledImage2.getIconHeight());
        layeredPane.add(label2, JLayeredPane.PALETTE_LAYER);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");

        JMenuItem newGame = new JMenuItem("New Game");
        newGame.addActionListener(e -> startNewGame());
        menu.add(newGame);

        JMenu highScoresMenu = new JMenu("High Scores");
        addHighScoresMenuItems(highScoresMenu);
        menu.add(highScoresMenu);

        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> exitGame());
        menu.add(exit);

        menuBar.add(menu);
        return menuBar;
    }

    private void addHighScoresMenuItems(JMenu highScoresMenu) {
        String[] sizes = {"Mini", "Small", "Medium", "Large", "Extra Large"};
        for (String size : sizes) {
            JMenuItem menuItem = new JMenuItem(size);
            menuItem.addActionListener(e -> showHighScores(size));
            highScoresMenu.add(menuItem);
        }
    }

    private void startNewGame() {
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);

        String[] options = {"Mini", "Small", "Medium", "Large", "Extra Large"};
        int response = JOptionPane.showOptionDialog(this, "Choose board size:", "New Game", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        int mapWidth = 25;
        int mapHeight = 17;
        double speed = 3.5;
        String mapSize = "Medium";
        switch (response) {
            case 0:
                mapWidth = 13;
                mapHeight = 9;
                speed = 2.5;
                mapSize = "Mini";
                break;
            case 1:
                mapWidth = 15;
                mapHeight = 11;
                speed = 3;
                mapSize = "Small";
                break;
            case 2:
                mapWidth = 25;
                mapHeight = 17;
                speed = 3.5;
                mapSize = "Medium";
                break;
            case 3:
                mapWidth = 35;
                mapHeight = 23;
                speed = 6;
                mapSize = "Large";
                break;
            case 4:
                mapWidth = 45;
                mapHeight = 29;
                speed = 7;
                mapSize = "Extra Large";
                break;
        }

        new GameWindow(mapWidth, mapHeight, speed, mapSize);
        dispose();
    }

    private void showHighScores(String mapSize) {
        List<HighScore> highScores = HighScore.getHighScoresByMapSize(mapSize);

        String[] highScoresArray = new String[highScores.size()];
        for (int i = 0; i < highScores.size(); i++) {
            HighScore score = highScores.get(i);
            highScoresArray[i] = score.getNick() + ": " + score.getScore();
        }
        JList<String> highScoresList = new JList<>(highScoresArray);

        JScrollPane scrollPane = new JScrollPane(highScoresList);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        JOptionPane.showMessageDialog(this, scrollPane, "High Scores - " + mapSize, JOptionPane.PLAIN_MESSAGE);
    }

    private void exitGame() {
        System.exit(1);
    }
}
