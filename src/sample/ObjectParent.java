package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;

abstract public class ObjectParent {
    protected int myType;
    protected ImageView myObject;

    ObjectParent(String Path, String[] types, double xPos, double yPos, int typePos){
        myType = typePos;
        File imgFile = new File(Path + types[myType] + ".png");
        Image image = new Image(imgFile.toURI().toString());
        myObject = new ImageView(image);
        myObject.setX(xPos);
        myObject.setY(yPos);
    }

    public void changeType(String Path, String[] types, int newType){
        myType = newType;
        File imgFile = new File(Path + types[myType] + ".png");
        Image image = new Image(imgFile.toURI().toString());
        myObject.setImage(image);
    }

    public int getType(){
        return myType;
    }

    public double getX(){
        return myObject.getX();
    }

    public double getY(){
        return myObject.getY();
    }

    public ImageView getObject(){
        return myObject;
    }
}
