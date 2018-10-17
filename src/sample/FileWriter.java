package sample;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map;

public class FileWriter {
    public static final String DIRECTORY_PATH = "resources/levels/level";

    public String filePath;


    FileWriter(String name, TreeMap<Number, ArrayList<Brick>> map){
        filePath = DIRECTORY_PATH + name + ".txt";
        try{
            PrintWriter writer = new PrintWriter(DIRECTORY_PATH + name + ".txt", "UTF-8");
            for(Map.Entry<Number, ArrayList<Brick>> entry: map.entrySet()){
                String height = String.valueOf(entry.getKey());
                String positions = "";
                for(Brick hldBrk: entry.getValue()){
                    positions = positions + " " + hldBrk.getX() + " " + hldBrk.getType();
                }
                writer.println(height + positions);
            }
            writer.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }


    }

}
