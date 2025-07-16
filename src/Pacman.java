import javax.swing.*;

public class Pacman {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Window standardWindow = new Window();
            standardWindow.setVisible(true);
        });
    }
}