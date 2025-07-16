import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighScore implements Serializable, Comparable<HighScore> {

    private String nick;
    private int score;
    private String mapSize;


    public HighScore(String nick, int score, String mapSize) {
        this.nick = nick;
        this.score = score;
        this.mapSize = mapSize;
    }


    public String getNick() {
        return nick;
    }


    public int getScore() {
        return score;
    }

    public String getMapSize() {
        return mapSize;
    }

    public static void saveHighScore(HighScore highScore) {
        List<HighScore> highScores = loadHighScores(highScore.getMapSize());
        highScores.add(highScore);
        String fileName = "src/highscores_" + highScore.getMapSize() + ".dat";
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(highScores);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }


    public static List<HighScore> loadHighScores(String mapSize) {
        String fileName = "src/highscores_" + mapSize + ".dat";
        List<HighScore> highScores = new ArrayList<>();

        File file = new File(fileName);
        if (!file.exists()) {
            return highScores;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            highScores = (List<HighScore>)in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return highScores;
    }

    public static List<HighScore> getHighScoresByMapSize(String mapSize) {
        List<HighScore> allScores = loadHighScores(mapSize);
        Collections.sort(allScores);
        return allScores;
    }


    @Override
    public int compareTo(HighScore other) {
        return Integer.compare(other.getScore(), this.score);
    }

}
