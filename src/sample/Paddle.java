package sample;

import javafx.scene.image.ImageView;
import javafx.geometry.Bounds;


public class Paddle extends ObjectParent{
    public static final String PADDLE_IMAGE = "resources/paddle_graphics/paddle-";
    public static final String[] PADDLE_COLORS = {"red", "green", "pink"};
    public static final int MY_LENGTH = 100;
    private ImageView curPaddle;

    Paddle(double screenHeight, double screenWidth, int colorPos){
        super(PADDLE_IMAGE, PADDLE_COLORS, screenWidth/2, screenHeight - 10, colorPos);
        curPaddle = myObject;
    }

    public void trackMouse(double cursorX, double screenWidth){
        if(cursorX <= screenWidth - MY_LENGTH) {
            curPaddle.setX(cursorX);
        }
    }

    public void changeColor(int nwColorPos){
        changeType(PADDLE_IMAGE, PADDLE_COLORS, nwColorPos);
    }

    @Override
    public double getX(){
        return curPaddle.getBoundsInParent().getMinX() + MY_LENGTH/2;
    }

    @Override
    public double getY(){
        Bounds paddleBounds = curPaddle.localToScene(curPaddle.getBoundsInLocal());
        return paddleBounds.getMinY();
    }
}
