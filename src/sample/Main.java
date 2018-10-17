package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Random;
import java.io.File;
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Iterator;


public class Main extends Application {
    public static final String TITLE = "Breakout";
    public static final int SCREEN_HEIGHT = 800;
    public static final int SCREEN_WIDTH = 600;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final Paint BACKGROUND = Color.web("#009AFE");
    public static final String LIFE_MESSAGE = "Lives Remaining: ";
    public static final String LEVEL_MESSAGE = "Current Level: ";
    public static final String SCORE_MESSAGE = "Current Score: ";
    public static final String SPLASHSCRENE_PATH = "resources/splashscrene.png";
    public static final String WINSCRENE_PATH = "resources/winscrene.png";
    public static final String LOSESCRENE_PATH = "resources/losescrene.png";
    public static final double MESSAGE_HEIGHT = 50;
    public static final double LIFE_XPOS = 50;
    public static final double LEVEL_XPOS = 250;
    public static final double SCORE_XPOS = 450;
    public static final int SPEED_INCREMENT = 100;
    public static final int POWERUP_TYPES = 2;
    public static final int PADDLE_COLORS = 3;

    // some things we need to remember during our game
    private Timeline animation;
    private FileReader myFileReader;
    private Stage myStage;
    private Scene myScene;
    private Group myRoot;
    private Paddle myPaddle;
    private Bouncer myBouncer;
    private ArrayList<Powerup> myPowerUps;
    private ArrayList<Brick> myBricks;
    private Text lifeDisplay;
    private Text levelDisplay;
    private Text scoreDisplay;
    private ImageView splashScreen;
    private int lifeCount = 3;
    private int curLevel = 0;
    private int score = 0;



    /**
     * Initialize what will be displayed and how it will be updated.
     */
    @Override
    public void start (Stage stage) {
        myStage = stage;
        setupGame(SCREEN_WIDTH, SCREEN_HEIGHT, BACKGROUND);
        stage.setScene(myScene);
        stage.setTitle(TITLE);
        stage.setResizable(false);
        stage.show();
    }

    private void setupGame (int width, int height, Paint background) {
        myRoot = new Group();
        myScene = new Scene(myRoot, width, height, background);
        setSplashScrene();
        myRoot.getChildren().add(splashScreen);
        myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
    }

    private void finishLoad(){
        myScene.setOnMouseMoved(e -> myPaddle.trackMouse(e.getX(), SCREEN_WIDTH));
        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    private void step (double elapsedTime) {
        if(lifeCount == 0){
            winGame(false);
        }
        if(myBricks.size() == 0){
            curLevel++;
            clearLevel();
            loadLevel();
        }
        if(myBouncer != null){
            myBouncer.updatePos(elapsedTime);
            myBouncer.checkCollision(myScene.getWidth(), myPaddle.getObject().getBoundsInParent(), myPaddle.getX());
            breakBricks();
            if(myBouncer.hitFloor(SCREEN_HEIGHT)){
                loseBouncer(myBouncer);
            }
        }
        if(myPowerUps != null && !myPowerUps.isEmpty()){
            for(Powerup powUp: myPowerUps){
                powUp.updatePos(elapsedTime);
            }
            checkPowerUP();
        }
        updateDisplayText();
    }

    private void handleKeyInput(KeyCode code){
        if (code == KeyCode.SPACE && myBouncer == null){
            myBouncer = new Bouncer(myPaddle.getX(), myPaddle.getY(), 0);
            myBouncer.setSpeed(myBouncer.getSpeed() + SPEED_INCREMENT * curLevel);
            myRoot.getChildren().add(myBouncer.getBouncer());
        }
        if (code == KeyCode.ENTER){
            launchLevelBuilder();
        }
        if (code == KeyCode.T){
            lifeCount += 1;
        }
        if (code == KeyCode.S){
            curLevel++;
            clearLevel();
            loadLevel();
        }
        if (code == KeyCode.R && myBouncer != null){
            myRoot.getChildren().remove(myBouncer.getBouncer());
            myBouncer = null;

        }
        if (code == KeyCode.G && splashScreen != null){
            finishLoad();
            loadLevel();
            myRoot.getChildren().remove(splashScreen);
            splashScreen = null;
        }
        if (code == KeyCode.C && myPaddle != null){
            myPaddle.changeColor((myPaddle.getType() + 1) % PADDLE_COLORS);
        }
    }

    private void setSplashScrene(){
        File imgFile = new File(SPLASHSCRENE_PATH);
        Image image = new Image(imgFile.toURI().toString());
        splashScreen = new ImageView(image);
        splashScreen.setFitWidth(SCREEN_WIDTH);
        splashScreen.setPreserveRatio(true);
    }

    private void winGame(Boolean endState){
        String path = new String();
        animation.stop();
        if(endState){
            path = WINSCRENE_PATH;
        }
        else {
            path = LOSESCRENE_PATH;
        }
        File imgFile = new File(path);
        Image image = new Image(imgFile.toURI().toString());
        ImageView endScreen = new ImageView(image);
        endScreen.setFitWidth(SCREEN_WIDTH);
        endScreen.setPreserveRatio(true);
        myRoot.getChildren().clear();
        myRoot.getChildren().add(endScreen);
    }

    private void loadLevel(){
        myPowerUps = new ArrayList<Powerup>();
        myPaddle = new Paddle(SCREEN_HEIGHT, SCREEN_WIDTH, 0);
        myRoot.getChildren().addAll(myPaddle.getObject());
        updateDisplayText();

        myFileReader = new FileReader(curLevel);
        if(!myFileReader.getLevelExists()){
            winGame(true);
        }

        myBricks = myFileReader.levelBricks;
        for(Brick brick: myBricks){
            myRoot.getChildren().add(brick.getObject());
        }
    }

    public void clearLevel(){
        myRoot.getChildren().clear();
        myBouncer = null;
        lifeDisplay = null;
        levelDisplay = null;
        scoreDisplay = null;
    }

    public void updateDisplayText(){
        if(lifeDisplay == null){
            lifeDisplay = new Text(LIFE_XPOS, MESSAGE_HEIGHT, LIFE_MESSAGE + lifeCount);
            myRoot.getChildren().add(lifeDisplay);
        }
        if(levelDisplay == null){
            levelDisplay = new Text(LEVEL_XPOS, MESSAGE_HEIGHT, LEVEL_MESSAGE + curLevel);
            myRoot.getChildren().add(levelDisplay);
        }
        if(scoreDisplay == null){
            scoreDisplay = new Text(SCORE_XPOS, MESSAGE_HEIGHT, SCORE_MESSAGE + score);
            myRoot.getChildren().add(scoreDisplay);
        }
        lifeDisplay.setText(LIFE_MESSAGE + lifeCount);
        levelDisplay.setText(LEVEL_MESSAGE + curLevel);
        scoreDisplay.setText(SCORE_MESSAGE + score);
    }

    private void breakBricks(){
        for(Iterator<Brick> iter = myBricks.iterator(); iter.hasNext();){
            Brick hldBrick = iter.next();
            if(myBouncer.hitBrick(hldBrick.getObject().getBoundsInParent())){
                score += hldBrick.getType() + 1;
                if(hldBrick.getType() == 0){
                    iter.remove();
                    if(hldBrick.dropPowerUp()){
                        Powerup tempPowerUp = new Powerup(hldBrick.getX(), hldBrick.getY(), getRandomNumberInRange(0, POWERUP_TYPES));
                        myPowerUps.add(tempPowerUp);
                        myRoot.getChildren().add(tempPowerUp.getObject());
                    }
                    myRoot.getChildren().remove(hldBrick.getObject());
                }
                else {
                    hldBrick.changeColor(hldBrick.getType() - 1);
                }
            }
        }
    }

    private void loseBouncer(Bouncer lostBouncer){
        lifeCount -= 1;
        myRoot.getChildren().remove(lostBouncer.getBouncer());
        myBouncer = null;
    }

    private void checkPowerUP(){
        for(Iterator<Powerup> iter = myPowerUps.iterator(); iter.hasNext();){
            Powerup hldPowerup = iter.next();
            if(hldPowerup.caught(myPaddle.getObject().getBoundsInParent())){
                if(hldPowerup.getType() == 0){
                    lifeCount++;
                }
                else if(hldPowerup.getType() == 1){
                    if(myBouncer != null){
                        int curSpeed = myBouncer.getSpeed();
                        myBouncer.setSpeed(curSpeed + SPEED_INCREMENT);
                    }
                }
                else if(hldPowerup.getType() == 2){
                    if(myBouncer != null){
                        myBouncer.setScale(2);
                    }
                }
                iter.remove();
                myRoot.getChildren().remove(hldPowerup.getObject());
            }
            else if(hldPowerup.hitFloor(SCREEN_HEIGHT)){
                iter.remove();
                myRoot.getChildren().remove(hldPowerup.getObject());
            }
        }
    }

    private void launchLevelBuilder(){
        LevelBuilder lvlBld = new LevelBuilder();
        myStage.setScene(lvlBld.getMyScene());

    }

    private static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    /**
     * Start the program.
     */
    public static void main (String[] args) {
        launch(args);
    }
}
