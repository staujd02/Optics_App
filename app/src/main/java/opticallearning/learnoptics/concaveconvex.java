package opticallearning.learnoptics;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
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

    final int CONVEX_LENS = 3;  //Index of the convex lens
    final int CONCAVE_LENS = 0; //Index of the concave lens

    int answerIndex;    //The index of the correct answer
    User user;          //Reference to user object
    Button spinner;     //Button used to select lens

    ArrayList<Laser> lasers;//Array of lasers
    ImageView[] views;      //Array of references to photodetector views

    Lens lens;          //Concave or convex lens
    Boolean answered;   //Tracks whether the user has already answered

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

                                if(which == CONCAVE_LENS){
                                    lens = LensCraftMenu.lensArrayList.get(CONCAVE_LENS);
                                }
                                else{
                                    lens = LensCraftMenu.lensArrayList.get(CONVEX_LENS);
                                }


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
        answered = false;   //Set the answered state back to false

        //Initialize the laser array
        lasers = new ArrayList<>();

        //Ensure button is clickable
        Button spin = (Button) findViewById(R.id.spinCC);
        spin.setClickable(true);

        //Correct Index
        Random rand = new Random(); //Create new random

        options = (int) rand.nextGaussian(); //Gets a number from a set with a
        // Gaussian distribution at 0
        // 50/50 chance of being positive or negative
        if(options > 0){
            //Correct index is 0
            answerIndex = CONCAVE_LENS; //Normal concave lens made of crown glass
        }
        else{
            //Correct index is 1
            answerIndex = CONVEX_LENS; //
        }

        //Get the Lens holder from concaveconvex.xml for measurments
        DrawingView lencc = (DrawingView) findViewById(R.id.ccLen);
        //Assign the lens holder location to lens object
        lens.setLocation((int) lencc.getX(), (int) lencc.getY(),lencc.getHeight(), lencc.getWidth());

        setUpLasers();          //Assigns the laser's their origin point dynamically
        setUpPhotoDetectors();  //Assigns the correct lens to the lasers for calculation of the
                                //the photodetectors location (does not render lasers)

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
     * Assigns the origin point to the lasers and initializes them
     * It also adds the lasers to lasers array
     */
    public void setUpLasers(){
        ImageView laserBox = (ImageView) findViewById(R.id.imgLaser);
        DrawingView drawingview = (DrawingView) findViewById(R.id.view);

        //Highest laser
        Laser laser1 = new Laser(new PointF(
                laserBox.getX() + laserBox.getWidth() - 20,
                (laserBox.getHeight() + 2*laserBox.getY() - 50) / 2
        ),new Point(drawingview.getWidth(),drawingview.getHeight()));

        Laser laser2 = new Laser(new PointF(
                laserBox.getX() + laserBox.getWidth() - 15,
                (laserBox.getHeight() + 2*laserBox.getY() - 25) / 2
        ),new Point(drawingview.getWidth(),drawingview.getHeight()));

        Laser laser3 = new Laser(new PointF(
                laserBox.getX() + laserBox.getWidth() - 15,
                (laserBox.getHeight() + 2*laserBox.getY() + 25) / 2
        ),new Point(drawingview.getWidth(),drawingview.getHeight()));

        Laser laser4 = new Laser(new PointF(
                laserBox.getX() + laserBox.getWidth() - 20,
                (laserBox.getHeight() + 2*laserBox.getY() + 50) / 2
        ),new Point(drawingview.getWidth(),drawingview.getHeight()));

        //Add lasers to laser array list
        lasers.add(laser1); lasers.add(laser2); lasers.add(laser3); lasers.add(laser4);
    }

    /**
     * Assigns correct lens to laser, calculates the destination, and then
     * moves the photodetectors at that location
     */
    private void setUpPhotoDetectors() {

        views = new ImageView[4];
        TranslateAnimation animation;
        MyAnimationListener listener;
        PointF end;
        Float xDelta;
        Float yDelta;

        views[0] = (ImageView) findViewById(R.id.ccDet1);
        views[1] = (ImageView) findViewById(R.id.ccDet2);
        views[2] = (ImageView) findViewById(R.id.ccDet3);
        views[3] = (ImageView) findViewById(R.id.ccDet4);

        for(int i = 0; i < views.length; i++){

            lasers.get(i).addLens(LensCraftMenu.lensArrayList.get(answerIndex)); //Grab matching laser
            lasers.get(i).calculate();
            end = lasers.get(i).getEnd();

            if(end.x > views[i].getX()){
                xDelta = end.x - views[i].getX();
            }
            else{
                xDelta = -(views[i].getX() - end.x);
            }

            if(end.y > views[i].getY()){
                yDelta = end.y - views[i].getY();
            }
            else{
                yDelta = -(views[i].getY() - end.y);
            }

            animation = new TranslateAnimation(0, xDelta, 0, yDelta);
            animation.setDuration(500);
            animation.setFillAfter(false);
            listener = new MyAnimationListener();
            listener.setImage(views[i]);
            animation.setAnimationListener(listener);

            views[i].startAnimation(animation);
        }
    }

    public class MyAnimationListener implements Animation.AnimationListener {
        ImageView view;

        public void setImage(ImageView view) {
            this.view = view;
        }

        public void onAnimationEnd(Animation animation) {
            for(int i = 0; i < views.length; i++){
                view.clearAnimation();
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(view.getWidth(), view.getHeight());
                view.setLayoutParams(lp);
            }
        }
        public void onAnimationRepeat(Animation animation) {
        }
        public void onAnimationStart(Animation animation) {
        }
    }

    /**
     * Handles the action of the user's choice
     *
     * @param correct indicates if the user's answer was wrong or right
     */
    private void ClickHandler(boolean correct){
        if(!answered){
            //Recorded that the user has answered
            answered = false;

            //Disable further interaction with button
            Button spin = (Button) findViewById(R.id.spinCC);
            spin.setClickable(false);

            //Create handler object to call runables after a delay
            Handler dialogEngine = new Handler();

            //Sends user's score to be recorded
            recordAnswer(user,correct);

            DrawLens();
            DrawLasers();
            //LightPhotodetectors()... or not

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
    }

    private void DrawLasers() {
        //Capture drawing view
        DrawingView dv = (DrawingView) findViewById(R.id.view);

        //Add user's choice of lens
        //and calculate
        for(Laser l: lasers){
            l.addLens(lens);
            l.calculate();
        }

        //Request the drawing view to render lasers
        dv.drawLasers(lasers);
    }

    private void DrawLens() {
        DrawingView view = (DrawingView) findViewById(R.id.ccLen);
        view.drawLens(lens);
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
