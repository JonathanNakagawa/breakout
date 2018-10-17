package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Bounds;

import java.io.File;


public class Bouncer {
    private ImageView myBouncer;
    public static final String BOUNCER_IMAGE = "resources/bouncer_graphics/bouncer-";
    public static final String[] BOUNCER_COLORS = {"red", "orange", "yellow", "green", "blue", "indigo", "violet", "yellow"};
    private int directionX = 1;
    private int directionY = -1;
    private int speed = 400;
    private double xSpeed = 0;
    private double ySpeed = 400;
    private int height = 25;
    private int width = 25;
    private int myScale = 1;

    Bouncer(double xPos, double yPos, int colorPos) {
        File imgFile = new File(BOUNCER_IMAGE + BOUNCER_COLORS[colorPos] + ".png");
        Image image = new Image(imgFile.toURI().toString());
        myBouncer = new ImageView(image);

        myBouncer.setX(xPos);
        myBouncer.setY(yPos - height);
    }

    public void updatePos(double elapsedTime){
        myBouncer.setX(myBouncer.getX() + xSpeed * elapsedTime * directionX);
        myBouncer.setY(myBouncer.getY() + ySpeed * elapsedTime * directionY);
    }

    public void checkCollision(double screenWidth, Bounds paddleBounds, double paddleCenter){
        Bounds ballBounds = myBouncer.localToScene(myBouncer.getBoundsInLocal());
        if (ballBounds.getMinX() <= 0 || ballBounds.getMaxX() >= screenWidth){
            directionX = directionX * -1;
        }
        if (ballBounds.getMinY() <= 0){
            directionY = directionY * -1;
        }
        Bounds ballPBounds = myBouncer.getBoundsInParent();
        if(ballPBounds.intersects(paddleBounds)){
            directionY = -1;
            if(ballPBounds.getMinX() + width/2 < paddleCenter){
                double rad = getAngle(getXCenter() - paddleCenter);
                ySpeed = Math.abs(speed * Math.sin(rad));
                xSpeed = Math.abs(speed * Math.cos(rad));
                directionX = -1;
            }
            if(ballPBounds.getMinX() + width/2 > paddleCenter){
                double rad = getAngle(getXCenter() - paddleCenter);
                ySpeed = Math.abs(speed * Math.sin(rad));
                xSpeed = Math.abs(speed * Math.cos(rad));
                directionX = 1;
            }
        }
    }

    public boolean hitFloor(double screenHeight){
        return myBouncer.localToScene(myBouncer.getBoundsInLocal()).getMaxY() >= screenHeight;
    }

    public boolean hitBrick(Bounds brickBounds){
        Bounds ballBounds = myBouncer.getBoundsInParent();
        if(ballBounds.intersects(brickBounds)){
            if(ballBounds.getMinX() < brickBounds.getMinX() && brickBounds.getMinX() < ballBounds.getMaxX()){
                directionX = -1;
            }
            if(brickBounds.getMaxX() > ballBounds.getMinX() && brickBounds.getMaxX() < ballBounds.getMaxX()){
                directionX = 1;
            }
            if(ballBounds.getMinY() < brickBounds.getMinY() && brickBounds.getMinY() < ballBounds.getMaxY()){
                directionY = -1;
            }
            if(brickBounds.getMaxY() > ballBounds.getMinY() && brickBounds.getMaxY() < ballBounds.getMaxY()){
                directionY = 1;
            }
            return true;
        }
        return false;
    }

    public double getAngle(double distance){
        return Math.PI/2 - (distance/50);
    }

    public double getXCenter(){
        return myBouncer.getBoundsInParent().getMinX() + (width * myScale) /2;
    }

    public void setScale(int scale){
        myScale = scale;
        myBouncer.setFitWidth(width * myScale);
        myBouncer.setFitHeight(height * myScale);
    }

    public int getSpeed(){
        return speed;
    }

    public void setSpeed(int nwSpeed){
        speed = nwSpeed;
    }

    public ImageView getBouncer(){
        return myBouncer;
    }

}