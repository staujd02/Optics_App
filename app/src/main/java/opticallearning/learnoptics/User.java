package opticallearning.learnoptics;

import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Joel on 7/6/2016.
 *
 * User object intended to store the user's information and tracking info
 *
 * This class tracks the user's performance, provides access to the higher levels,
 * and handles loading and saving the user object
 */
public class User implements Serializable{

    private String name;    //User's name
    private String school;  //String corresponding to user's school
    private int standing;   //Indicates the student's standing (freshman: 0,sophomore: 1,junior: 3, senior: 4)
                                //-1 for this value means unknown or not applicable

    private String uuid;    //Unique identifier for the installed device
    private boolean HS;     //Indicates whether the listed school is a high school
    private String userName; //String used for storing the user's identification
    //private int icon_id;    //Resource id integer corresponding to user's icon
    //private int viewCount //Tracks how many questions the user has viewed (not necessarily answered)
    private int score;      //User's score
    private int attempts;   //Question attempts
    private int correct;    //Questions answered correctly
    private int incorrect;  //Questions answered incorrectly

    //Database Fields - Specific data correlated particular questions
    private int lensShapeAtmp;
    private int lensShapeCor;
    final static protected int SHAPE_QUESTION = 0;

    private int indexAtmp;
    private int indexCor;
    final static protected int INDEX_QUESTION = 1;

    private int widthAtmp;
    private int widthCor;
    final static protected int WIDTH_QUESTION = 2;

    private int fLenAtmp;
    private int fLenCor;
    final static protected int FOCAL_QUESTION = 3;

    private int distanceAtmp;
    private int distanceCor;
    final static protected int DISTANCE_QUESTION = 4;

    private int heightAtmp;
    private int heightCor;
    final static protected int HEIGHT_QUESTION = 5;

    private int moveDetectorAtmp;
    private int moveDetectorCor;
    final static protected int DETECTOR_QUESTION = 6;

    private int lensLVL;    //Number of unlocked levels in Lens Crafter sub menu
    private int specLVL;    // "                        "  Spectrum matcher sub menu
    private int medLVL;     // "                        "  Rank the Medium

    private boolean hints;      //Option for the user to bypass hints
    private boolean background; //Option for the user to bypass the background sections
    private boolean setupComplete; //Indicates whether the user has gone through setup

    public User(){
        name = "anonymous";

        //Set all user data to 0
        score = 0;
        attempts = 0;
        correct = 0;
        incorrect = 0;

        //Unlock the first level for each
        lensLVL = 1;
        specLVL = 1;
        medLVL = 1;

        //Turn on background/hints by default
        hints = true;
        background = true;
        setupComplete = false;
    }

    public User(String name){
        this.name = name;

        //Set all user data to 0
        score = 0;
        attempts = 0;
        correct = 0;
        incorrect = 0;

        //Unlock the first level for each
        lensLVL = 1;
        specLVL = 1;
        medLVL = 1;

        //Turn on background/hints by default
        hints = true;
        background = true;
    }

    /*
    public User(String name, int icon_ic){


    }
     */

    /*
    public User(String name, String school, int icon_ic){


    }
     */

    /**
     * Loads a user from data file
     * *NOTE: this will only load the FIRST user from the file
     *
     * @param filename the name of the file on which the user's info is stored
     */
    public static User loadUser(String filename, Context c){
        ObjectInputStream ois;          //Create the input stream
        File file;          //Create file object
        User load;         //User object reference

        file = c.getFileStreamPath(filename);

        //Check whether file exists
        if(!file.exists()){
            //Return null if no file exist
            return null;
        }

        try {
            //Initialize object input stream
            ois = new ObjectInputStream(new FileInputStream(file));

            //Load user object and cast to (User)
            load = (User) ois.readObject();

            //Close object input stream
            ois.close();
        } catch (IOException e) {
            //Return null if input stream failed
            e.printStackTrace();
            load = null;
            System.out.println("File failed to open");
        } catch (ClassNotFoundException e) {
            //Return null if cast failed
            e.printStackTrace();
            load = null;
            System.out.println("User failed to parse");
        }

        //Return the user (load) to caller
        return load;
    }

    /**
     * This method saves the user's information to a specific file
     *
     * @param filename name of the file to which the user will be save
     *                 *NOTE: the user own's the entire file -> ONE User per File
     * @return returns a boolean indicating save was successful
     */
    public boolean saveUser(String filename, Context c){
        ObjectOutputStream oos; //Used for writing objects to a file
        boolean success;    //boolean indicating success of save

        //Create file object using filename
        File file = c.getFileStreamPath(filename);

        //Save user information locally
        try {
            //Initialize object output stream, boolean for append is false
            oos = new ObjectOutputStream(new FileOutputStream(file, false));

            //Write user object to the file
            oos.writeObject(this);

            //Flush buffer
            oos.flush();
            //Close stream
            oos.close();
            //Record Success
            success = true;
        } catch (IOException e) {
            //If catch is trigger the save failed
            success = false;
            e.printStackTrace();
        }

        //Create .json file
        File json = c.getFileStreamPath("temp.json");

        //Save User data to .json for server upload
        try{

            //Create filewriter object
            FileWriter f = new FileWriter(json);

            //Create json object to hold information
            JSONObject obj = new JSONObject();
            obj.put("UserID", uuid);
            obj.put("School", school);
            obj.put("High School", HS);
            obj.put("Standing",standing);
            obj.put("Total Cor", correct);
            obj.put("Total InCor", incorrect);
            obj.put("Lens Shape Atmp",lensShapeAtmp);
            obj.put("Lens Shape Cor", lensShapeCor);
            obj.put("N Index Atmp", indexAtmp);
            obj.put("N Index Cor", indexCor);
            obj.put("Lens Widths Atmp", widthAtmp);
            obj.put("Lens Widths Cor", widthCor);
            obj.put("Focal Length Atmp", fLenAtmp);
            obj.put("Focal Length Cor", fLenCor);
            obj.put("Distance Atmp", distanceAtmp);
            obj.put("Distance Cor", distanceCor);
            obj.put("Height Atmp", heightAtmp);
            obj.put("Height Cor", heightCor);
            obj.put("Move Dect Atmp", moveDetectorAtmp);
            obj.put("Move Dect Cor", moveDetectorCor);

            //Writes the json to the file
            f.write(obj.toString());

            System.out.println(obj.toString());

            //Close the file
            f.flush();
            f.close();

            //Rename to original after successful write
            File original = c.getFileStreamPath("userDat.json");

            //Ensure both files exist
            if(json.exists()) {
                if (original.exists()){
                    //Rename the temp json to overwrite original file
                    json.renameTo(original);
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Json Exception");
        }

        //Return result of the save attempt
        return success;
    }

    /**
     * Calculates the user's score
     */
    private void calcScore(){
        score = (int) (100 * (correct / (attempts * 1.0)));
        if(score < 0){
            score = 0;
        }
    }

    /**
     * Increments the user's attempts and
     * calls for a score recalculation
     *
     */
    private void incAttempt(){
        attempts++;
        calcScore();
    }

    /**
     * Increments the user's incorrect value by 1
     * indicating they answered a question
     * incorrectly
     *
     * This method then increments attempts
     */
    public void incIncorrect(int question_type){
        incorrect++;
        incAttempt();

        switch (question_type){
            case SHAPE_QUESTION:
                lensShapeAtmp++;
                break;
            case INDEX_QUESTION:
                indexAtmp++;
                break;
            case WIDTH_QUESTION:
                widthAtmp++;
                break;
            case FOCAL_QUESTION:
                fLenAtmp++;
                break;
            case DISTANCE_QUESTION:
                distanceAtmp++;
                break;
            case HEIGHT_QUESTION:
                heightAtmp++;
                break;
            case DETECTOR_QUESTION:
                moveDetectorAtmp++;
                break;
        }
    }

    /**
     * Increments the user's correct value by 1
     * indicating they answered a question
     * correctly
     *
     * The method then increments attempts
     */
    public void incCorrect(int question_type){
        correct++;
        incAttempt();

        switch (question_type){
            case SHAPE_QUESTION:
                lensShapeAtmp++;
                lensShapeCor++;
                break;
            case INDEX_QUESTION:
                indexAtmp++;
                indexCor++;
                break;
            case WIDTH_QUESTION:
                widthAtmp++;
                widthCor++;
                break;
            case FOCAL_QUESTION:
                fLenAtmp++;
                fLenCor++;
                break;
            case DISTANCE_QUESTION:
                distanceAtmp++;
                distanceCor++;
                break;
            case HEIGHT_QUESTION:
                heightAtmp++;
                heightCor++;
                break;
            case DETECTOR_QUESTION:
                moveDetectorAtmp++;
                moveDetectorCor++;
                break;
        }

    }

    //Getters and Setters

    //School: get and set
    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    //Standing: get and set
    public int getStanding() {return standing;}

    public void setStanding(int standing) {this.standing = standing;}

    //High School: get and set
    public boolean isHS() {return HS;}

    public void setHS(boolean HS) {this.HS = HS;}

    //Setup Indicator: Get and set
    public boolean isSetupComplete() {
        return setupComplete;
    }

    public void setSetupComplete(boolean setupComplete) {
        this.setupComplete = setupComplete;
    }

    //Username: get and set
    public String getUserName() {return userName;}

    public void setUserName(String userName) {this.userName = userName;}

    //Name: get and set
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //Hints: get and set
    public boolean getHints() {return hints;}

    public void setHints(boolean view){hints = view;}

    //Background: get and set
    public boolean getBackground() {return background;}

    public void setBackground(boolean view) {background = view;}

    //LensLVL: get and set
    public int getLensLVL() {
        return lensLVL;
    }

    public void setLensLVL(int lensLVL) {
        this.lensLVL = lensLVL;
    }

    //SpecLVL: get and set
    public int getSpecLVL() {
        return specLVL;
    }

    //public void setSpecLVL(int specLVL) {this.specLVL = specLVL;}

    //MedLVL: get and set
    public int getMedLVL() {
        return medLVL;
    }

    //public void setMedLVL(int medLVL) {this.medLVL = medLVL;}

    //Attempts: get
    public int getAttempts() {
        return attempts;
    }

    //Correct: get
    public int getCorrect() {
        return correct;
    }

    //Incorrect: get
    public int getIncorrect() {
        return incorrect;
    }

    //Score: get
    public int getScore() {
        return score;
    }

    protected void setUUID(Context c){
        DeviceUuidFactory id = new DeviceUuidFactory(c);
        uuid = id.getDeviceUuid().toString();
    }

}
