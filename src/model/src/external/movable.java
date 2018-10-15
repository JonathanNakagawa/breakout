package external;

import javafx.geometry.Bounds;

public interface movable {

    public void setXY(double xPos, double yPos);

    public void checkCollision(double screenWidth, Bounds paddleBounds, double paddleCenter);
}
