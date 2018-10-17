package sample;

import javafx.scene.image.ImageView;

public class Brick extends ObjectParent {
    public static final int BRICK_WIDTH = 50;
    public static final int BRICK_HEIGHT = 20;
    public static final String[] BRICK_COLORS = {"red", "orange", "green", "blue", "indigo", "violet", "white", "black"};
    public static final String BRICK_IMAGE = "resources/brick_graphics/brick-";
    public static final double DROP_RATE = .1;
    private ImageView curBrick;

    Brick(double xPos, double yPos, int colorPos){
        super(BRICK_IMAGE, BRICK_COLORS, xPos, yPos, colorPos);
        curBrick = myObject;
    }

    public void changeColor(int colorPos){
        changeType(BRICK_IMAGE, BRICK_COLORS, colorPos);
    }

    public boolean dropPowerUp(){
        return Math.random() <= DROP_RATE;
    }

    public void trackMouse(double cursorX, double cursorY, double screenWidth, double screenHeight){
        if(cursorX <= screenWidth - BRICK_WIDTH) {
            curBrick.setX(cursorX);
        }
        if(cursorY <= screenHeight - BRICK_WIDTH) {
            curBrick.setY(cursorY);
        }
    }
}
