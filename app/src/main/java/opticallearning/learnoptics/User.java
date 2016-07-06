package opticallearning.learnoptics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Joel on 7/6/2016.
 */
public class User implements Serializable{

    String name;    //User's name
    //String school;  //String corresponding to user's school
    //int icon_id;  //Resource id integer corresponding to user's icon
    //int viewCount //Tracks how many questions the user has viewed (not necessarily answered)
    int score;      //User's score
    int attempts;   //Question attempts
    int correct;    //Questions answered correctly
    int incorrect;  //Questions answered incorrectly

    int lensLVL;    //Number of unlocked levels in Lens Crafter sub menu
    int specLVL;    // "                        "  Spectrum matcher sub menu
    int medLVL;     // "                        "  Rank the Medium

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
    final public static User loadUser(String filename){
        ObjectInputStream ois;          //Create the input stream
        File file = new File(filename); //Create file object
        User load;         //User object reference

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
        } catch (ClassNotFoundException e) {
            //Return null if cast failed
            e.printStackTrace();
            load = null;
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
    public boolean saveUser(String filename){
        ObjectOutputStream oos; //Used for writing objects to a file
        boolean success;    //boolean indicating success of save

        //Create file object using filename
        File file = new File(filename);

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

        //Return result of the save attempt
        return success;
    }

    /**
     * Calculates the user's score
     */
    private void calcScore(){
        score = (int) ((correct * 1.35) - (incorrect * .25));
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
    public void incIncorrect(){
        incorrect++;
        incAttempt();
    }

    /**
     * Increments the user's correct value by 1
     * indicating they answered a question
     * correctly
     *
     * The method then increments attempts
     */
    public void incCorrect(){
        correct++;
        incAttempt();
    }

    //Getters and Setters

    //Name: get and set
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public void setSpecLVL(int specLVL) {
        this.specLVL = specLVL;
    }

    //MedLVL: get and set
    public int getMedLVL() {
        return medLVL;
    }

    public void setMedLVL(int medLVL) {
        this.medLVL = medLVL;
    }

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

}
