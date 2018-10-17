package sample;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;


public class FileReader {
    public static final String LEVEL_PATH = "resources/levels/level";
    private Scanner myScanner;
    private String mypath;
    public ArrayList<Brick> levelBricks;
    public Boolean levelExists = true;


    // File structure: Each line represents a row in the game. The first value on each line indicated the lines Y value.
    // The next value represents the x position of the next block. The next value represents the block type.
    FileReader(int levelNum){
        mypath = LEVEL_PATH + levelNum + ".txt";
        File file = new File(mypath);
        levelBricks = new ArrayList<Brick>();
        try{
            myScanner= new Scanner(file);

            while (myScanner.hasNextLine()){
                String tempLine = myScanner.nextLine();
                String[] curLine = tempLine.split(" ");
                double lineHeight = Double.parseDouble(curLine[0]);
                for(int i = 1; i < curLine.length; i += 2){
                    Brick tempBrick = new Brick(Double.parseDouble(curLine[i]), lineHeight, Integer.parseInt(curLine[i+1]));
                    levelBricks.add(tempBrick);
                }
            }
        }
        catch (IOException e){
            levelExists = false;
        }
    }

    public Boolean getLevelExists(){
        return levelExists;
    }
}
