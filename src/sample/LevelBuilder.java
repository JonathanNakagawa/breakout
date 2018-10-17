package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.control.TextField;
import java.util.TreeMap;
import java.util.ArrayList;

public class LevelBuilder {
    public static final int SCREEN_HEIGHT = 800;
    public static final int SCREEN_WIDTH = 600;
    public static final Paint BACKGROUND = Color.GRAY;
    public static final int PADDING = 10;
    public static final int INSET = 200;

    private Scene myScene;
    private Group myGroup;
    private TextField nameInput;
    private Brick traceBrick;
    private TreeMap<Number, ArrayList<Brick>> savedBricks;
    private String fileName;

    LevelBuilder(){
        setUp();
    }

    private void setUp(){
        savedBricks = new TreeMap<Number, ArrayList<Brick>>();

        VBox textContainer = new VBox(PADDING);
        textContainer.setAlignment(Pos.CENTER);
        textContainer.setPadding(new Insets(0, INSET, 0, INSET));

        myScene = new Scene(textContainer,SCREEN_WIDTH, SCREEN_HEIGHT);
        myScene.setFill(BACKGROUND);

        nameInput = new TextField("Level Name");
        nameInput.setAlignment(Pos.CENTER);

        Button btn = new Button("Submit");
        btn.setOnAction(e -> finishLoad());


        textContainer.getChildren().addAll(nameInput, btn);


    }


    public Scene getMyScene(){
        return myScene;
    }

    private void handleKeyInput(KeyEvent e){
        KeyCode code = e.getCode();
        if(code == KeyCode.DIGIT8 || code == KeyCode.DIGIT9){
            return;
        }
        if(code.isDigitKey()){
            traceBrick.changeColor(Integer.parseInt(e.getText()));
        }
        if(code == KeyCode.ENTER){
            FileWriter f = new FileWriter(fileName, savedBricks);
        }
    }

    public void setBrick(MouseEvent e){
        Brick nwBrick = new Brick(e.getX(), e.getY(), traceBrick.getType());
        if(!savedBricks.containsKey(e.getY())){
            ArrayList<Brick> nwList = new ArrayList<Brick>();
            nwList.add(nwBrick);
            savedBricks.put(nwBrick.getY(), nwList);
        }
        else {
            savedBricks.get(e.getY()).add(nwBrick);
        }
        myGroup.getChildren().add(nwBrick.getObject());

    }

    private void finishLoad(){
        fileName = nameInput.getText();
        myGroup = new Group();
        myScene.setRoot(myGroup);
        myScene.setOnMouseMoved(e -> traceBrick.trackMouse(e.getX(), e.getY(), SCREEN_WIDTH, SCREEN_HEIGHT));
        myScene.setOnMouseClicked(e -> setBrick(e));
        myScene.setOnKeyPressed(e -> handleKeyInput(e));
        traceBrick = new Brick(100, 100, 0);
        myGroup.getChildren().add(traceBrick.getObject());

    }


}
