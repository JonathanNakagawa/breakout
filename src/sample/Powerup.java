package sample;

import javafx.scene.image.ImageView;
import javafx.geometry.Bounds;

public class Powerup extends ObjectParent{
    public static final int POWERUP_WIDTH = 50;
    public static final int POWERUP_HEIGHT = 50;
    public static final String[] POWERUP_NAME = {"life", "speed", "big"};
    public static final int DROP_SPEED = 100;
    public static final String POWERUP_IMAGE = "resources/powerup_graphics/";
    private ImageView curPowerUp;

    Powerup(double xPos, double yPos, int namePos){
        super(POWERUP_IMAGE, POWERUP_NAME, xPos, yPos, namePos);
        curPowerUp = myObject;

        curPowerUp.setFitHeight(POWERUP_HEIGHT);
        curPowerUp.setFitWidth(POWERUP_WIDTH);
    }

    public void updatePos(double elapsedTime){
        curPowerUp.setY(curPowerUp.getY() + DROP_SPEED * elapsedTime);
    }

    public boolean hitFloor(double screenHeight){
        return curPowerUp.localToScene(curPowerUp.getBoundsInLocal()).getMaxY() >= screenHeight;
    }

    public boolean caught(Bounds paddleBounds){
        return curPowerUp.getBoundsInParent().intersects(paddleBounds);
    }
}