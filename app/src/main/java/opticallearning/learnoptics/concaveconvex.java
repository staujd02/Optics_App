package opticallearning.learnoptics;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.Random;
import java.util.logging.LogRecord;

/**
 * Created by Joel on 7/1/2016.
 *
 * This is a branching activity of Lens Crafter menu (LensCraftMenu.java)
 *
 * Asks the user to choose between a concave lens and a convex lens
 *
 */
public class ConcaveConvex extends Activity {

    int answerIndex;    //The index of the correct answer
    User user;          //Reference to user object
    Button spinner;     //Button used to select lens

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //call super constructor
        setContentView(R.layout.concaveconvex); //set the view
        setTitle("Concave vs. Convex"); //Assign title
        user = MainActivity.user;

        //Create button object and connect to spinCC in concaveconvex.xml
        spinner = (Button) findViewById(R.id.spinCC);
        spinner.setText("Pick Lens");

        //Assign to string array lensTypes {"Concave", "Convex"}
        //using an adapter
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.lensTypes, android.R.layout.simple_spinner_item);

        //Sets the drop down view type
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Create onClickListener
        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create Dialog for the user to pick a lens
                new AlertDialog.Builder(ConcaveConvex.this)
                        //Set title and the adapter created above
                        .setTitle("Pick Lens Type")
                        .setAdapter(adapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Grabs array of choices
                                String[] s = getResources().getStringArray(R.array.lensTypes);
                                //Sets the user's choice as the button's text
                                spinner.setText(s[which]);

                                //Determine Correctness
                                if(which == answerIndex){
                                    //Run Handler with correct answer given
                                    ClickHandler(true);
                                }
                                else{
                                    //Run Handler with incorrect answer given
                                    ClickHandler(false);
                                }

                                //Ends the dialog
                                dialog.dismiss();
                            }
                            //creates dialog object and displays it
                        }).create().show();
            }
        });
    }

    /**
     * This is the android onStart() method called
     * every time the activity is restarted
     *
     * This includes all of the necessary operations to start
     * the same activity over again
     *
     * *Note: anything that does not need to be rerun for a new operation
     * should be placed in the onCreate() method
     */
    @Override
    protected void onStart() {
        super.onStart(); //Always start with the super constructor

        int options;    //Picks the correct answer > later is translated to index

        //Correct Index
        Random rand = new Random(); //Create new random

        options = (int) rand.nextGaussian(); //Gets a number from a set with a
        // Gaussian distribution at 0
        // 50/50 chance of being positive or negative
        if(options > 0){
            //Correct index is 0
            answerIndex = 0;
        }
        else{
            //Correct index is 1
            answerIndex = 1;
        }

        //todo Calculate Laser's Path
        //todo Move Photodetectors

        //This directions dialog displays until the user opts out of
        //displaying the directions
        if(user.getHints()){
            //Directions Alert Dialogue
            new AlertDialog.Builder(ConcaveConvex.this)
                    .setTitle("Directions") //Sets the title of the dialogue
                    .setMessage("Pick the lens type, concave or convex, to direct the laser to the photodetectors.") //Sets the Message
                    //Creates OK button for user interaction (Dismisses Dialogue)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //User pressed yes
                        }
                    })
                    .show(); //Shows created dialogue
        }
    }

    /**
     * Handles the action of the user's choice
     *
     * @param correct indicates if the user's answer was wrong or right
     */
    private void ClickHandler(boolean correct){
        //Create handler object to call runables after a delay
        Handler dialogEngine = new Handler();

        //Sends user's score to be recorded
        recordAnswer(user,correct);

        //DrawLens()
        //DrawLasers()
        //LightPhotodectors()... or not

        //Check correctness and display appropriate dialogs
        if(correct){
            //User is correct
            dialogEngine.postDelayed(correctDelay, 2500);
        }
        else{
            //User is incorrect
            dialogEngine.postDelayed(incorrectDelay, 2500);
        }
    }

    /**
     * The user is correct
     *
     * Using a post-delayed handler with this runnable
     * is much better than a timer delayed action in
     * this circumstance
     */
    private Runnable correctDelay = new Runnable() {
        @Override
        public void run() {
            //Run the dialog (correct answer)
            runResultsDialog(true);
        }
    };

    /**
     * The user is incorrect
     *
     * Using a post-delayed handler with this runnable
     * is much better than a timer delayed action in
     * this circumstance
     */
    private Runnable incorrectDelay = new Runnable() {
        @Override
        public void run() {
            //Run the dialog (incorrect answer)
            runResultsDialog(false);
        }
    };

    /**
     * This class displays a dialog informing the user if their answer was
     * right or wrong
     *
     * @param right boolean representing user's correctness in their answer
     */
    private void runResultsDialog(boolean right){
        //User is correct
        if(right){
            //Correct Answer Alert Dialogue
            //Ask if the user would like to play again
            new AlertDialog.Builder(ConcaveConvex.this)
                    .setTitle("Correct!") //Sets the title of the dialogue
                    .setMessage("You chose the correct lens. Would you like to " +
                            "play again?") //Sets the Message
                    //Creates OK button for user interaction (Dismisses Dialogue)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //User pressed yes
                            recreate();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which){
                            //User pressed NO?
                            startActivity(new Intent(ConcaveConvex.this, LensCraftMenu.class));
                        }
                    }).show(); //Shows created dialogue
        }
        //User is not correct
        else{
            //Incorrect Answer Alert Dialogue
            //Ask if the user would like to play again
            new AlertDialog.Builder(ConcaveConvex.this)
                    .setTitle("Nope") //Sets the title of the dialogue
                    .setMessage("Sorry, that is not the right lens. Would you like " +
                            "to play again?") //Sets the Message
                    //Creates OK button for user interaction (Dismisses Dialogue)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //User pressed yes
                            recreate();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which){
                            //User pressed NO?
                            startActivity(new Intent(ConcaveConvex.this, LensCraftMenu.class));
                        }
                    })
                    .show(); //Shows created dialogue
        }

    }

    /**
     * This function records the user's score
     *
     * @param user      reference variable to the user who gave the answer
     * @param correct   boolean indicating the user's correctness
     */
    private void recordAnswer(User user, boolean correct){

        //This is a good place to double check for a bad reference

        //if the user's answer == correct index
        if(correct){
            //Increment the user's correct count
            user.incCorrect();
        }
        else{
            //User is wrong
            //Increment user's incorrect count
            user.incCorrect();
        }
    }
}
