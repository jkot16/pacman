public interface Movable {
    void move();
    boolean isEnemy();
    int getX();
    int getY();
    double getOriginalSpeed();
    void setSpeed(double speed);
}
