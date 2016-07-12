package opticallearning.learnoptics;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Joel on 7/1/2016.
 *
 * This is a branching activity of Lens Crafter menu (LensCraftMenu.java)
 *
 * The user should select how to 'shew' the lens up, down, or in the middle to refract the laser
 * towards the photodetectors
 *
 */
public class Skew extends Activity {

    final int LENS = 6; //Constant lens index
    final int ADJUSTMENT = 30; //Constant adjustment for lens height

    //These three constant are used to determine where the
    //lasers will be drawn
    final int LASER_COUNT = 4;                //Number of lasers to be drawn
    final int LASER_APERTURE_BOTTOM = 44; //The height at which the laser aperture starts (in px)
    final int LASER_APERTURE_TOP = 78;    //The height at which the laser aperture stops (in px)
    final int ORIGINAL_SIZE = 107;        //The original height of the measured image

    Button spinner; //Button which opens prompt for user selection of lens

    boolean processStopped; //keeps track of the activity's life cycle and responds accordingly

    int answerIndex;    //The index of the correct answer

    int userHeight;

    User user;          //Reference to user object

    ArrayList<Laser> lasers;//Array of lasers
    ImageView[] views;      //Array of references to photodetector views

    Lens lens;          //Concave or convex lens
    Boolean answered;   //Tracks whether the user has already answered


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //call super constructor
        setContentView(R.layout.skew);  //sets the view
        setTitle("Lens Height");        //assigns descriptive title

        user = LensCraftMenu.user; //Grab user reference from menu

        //Creates spinner object and assigns it to spinSkew in skew.xml
        spinner = (Button) findViewById(R.id.spinSkew);
        spinner.setText("Pick Lens Height");

        //Creates array adapter for reading skew[] into spinSkew
        //skew > {"Above Median", "At Median", "Below Median"}
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.skew, android.R.layout.simple_spinner_item);
        //Assigns dropdown behaviour of adapter
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Create onClickListener
        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create Dialog for the user to pick a lens
                new AlertDialog.Builder(Skew.this)
                        //Set title and the adapter created above
                        .setTitle("Pick Lens Height")
                        .setAdapter(adapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Grabs array of choices
                                String[] s = getResources().getStringArray(R.array.skew);
                                //Sets the user's choice as the button's text
                                spinner.setText(s[which]);

                                //Set the lens
                                lens = new Lens(LensCraftMenu.lensArrayList.get(LENS));

                                //Save the user's height choice
                                userHeight = which;

                                //Determines User Correctness
                                //User's choice => Correct Choice
                                switch (which){
                                    case 0:
                                        if(answerIndex == 0)
                                            ClickHandler(true); //Runs a correct answer dialog
                                        else
                                            ClickHandler(false);//Runs an incorrect answer dialog
                                        break;
                                    case 1:
                                        if(answerIndex == 1)
                                            ClickHandler(true);
                                        else
                                            ClickHandler(false);
                                        break;
                                    case 2:
                                        if(answerIndex == 2)
                                            ClickHandler(true);
                                        else
                                            ClickHandler(false);
                                        break;
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
     * Sets the boolean processStopped according to the current
     * state of the activity
     *
     * User has answered => Call the Unique startup
     * User hasn't answered yet => Call the Super Constructor for onStart() only
     */
    @Override
    protected void onStop() {
        super.onStop();
        if(!answered)processStopped = true;
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
     *
     */
    @Override
    protected void onStart() {
        super.onStart(); //Always start with the super constructor

        //If process was stopped, skip setup and allow
        //super constructor to resume the activity
        //---Also reset flag
        if(processStopped == true){
            processStopped = false;
            return;
        }

        int options;    //Picks the correct answer > later is translated to index
        answered = false;   //Set the answered state back to false

        //Initialize the laser array
        lasers = new ArrayList<>();

        //Ensure button is clickable
        Button spin = (Button) findViewById(R.id.spinSkew);
        spin.setClickable(true);

        //Reset Photodetector Image
        LightPhotodetectors(false);

        //Correct Index
        Random rand = new Random(); //Create new random

        answerIndex = rand.nextInt(3); //gets an integer 0-2 with equal chances for each

        //This directions dialog displays until the user opts out of
        //displaying the directions
        if (user != null) {
            if(user.getHints()){
                //Directions Alert Dialogue
                new AlertDialog.Builder(Skew.this)
                        .setTitle("Directions") //Sets the title of the dialogue
                        .setMessage("Select the correct height of the lens to focus the light on the photodetectors.") //Sets the Message
                        //Creates OK button for user interaction (Dismisses Dialogue)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //User pressed yes
                                setUpLasers();          //Assigns the laser's their origin point dynamically
                                setUpPhotoDetectors();  //Assigns the correct lens to the lasers for calculation of the
                                //the photodetectors location (does not render lasers)
                            }
                        })
                        .setCancelable(false)
                        .show(); //Shows created dialogue
            }
            else{
                //Start Alert Dialogue
                new AlertDialog.Builder(Skew.this)
                        .setTitle("Start?") //Sets the title of the dialogue
                        .setMessage("Press OK to start.") //Sets the Message
                        //Creates OK button for user interaction (Dismisses Dialogue)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //User pressed yes
                                setUpLasers();          //Assigns the laser's their origin point dynamically
                                setUpPhotoDetectors();  //Assigns the correct lens to the lasers for calculation of the
                                //the photodetectors location (does not render lasers)
                            }
                        })
                        .setCancelable(false)
                        .show(); //Shows created dialogue
            }
        }
    }


    /**
     * Assigns the origin point to the lasers and initializes them
     * It also adds the lasers to lasers array
     */
    public void setUpLasers() {
        ImageView laserBox = (ImageView) findViewById(R.id.imgLaser);
        DrawingView drawingview = (DrawingView) findViewById(R.id.view);

        laserBox.measure(laserBox.getWidth(), laserBox.getHeight());
        drawingview.measure(drawingview.getWidth(), drawingview.getHeight());

        //Determine where to draw lasers
        int yBottom;
        int yTop;
        int ySegment;

        //Use the midpoint of laserbox x for laser x
        int x = (int) (laserBox.getX() * 2 + laserBox.getWidth()) / 2;

        yBottom = (LASER_APERTURE_BOTTOM * laserBox.getMeasuredHeight())
                / ORIGINAL_SIZE;
        yTop = (LASER_APERTURE_TOP * laserBox.getMeasuredHeight())
                / ORIGINAL_SIZE;

        //Divide equally between lasers
        ySegment = (yTop - yBottom)
                / (LASER_COUNT + 1);

        Laser laser;

        //Draw lasers Lasers
        if (LASER_COUNT > 0)
            for (int i = 1; i <= LASER_COUNT; i++) {
                laser = new Laser(new PointF(x,
                        laserBox.getY() + yBottom + ySegment * i),
                        new Point(drawingview.getWidth(), drawingview.getHeight()));
                lasers.add(laser);
            }
    }

    /**
     * Assigns correct lens to laser, calculates the destination, and then
     * moves the photodetectors at that location
     */
    private void setUpPhotoDetectors() {

        views = new ImageView[4];
        TranslateAnimation animation;
        PointF end;
        Float xDelta;
        Float yDelta;

        views[0] = (ImageView) findViewById(R.id.skDect1);
        views[1] = (ImageView) findViewById(R.id.skDect2);
        views[2] = (ImageView) findViewById(R.id.skDect3);
        views[3] = (ImageView) findViewById(R.id.skDect4);

        //Initialize the lens object
        lens = new Lens(LensCraftMenu.lensArrayList.get(LENS));

        //Get the Lens holder from n_index.xml for measurments
        DrawingView skewLen = (DrawingView) findViewById(R.id.skewLen);
        ImageView photoTemplate = (ImageView) findViewById(R.id.skDect1);

        //Adjust the height based on correct answer
        if(answerIndex == 0){
            lens.setLocation((int) skewLen.getX(), ((int) skewLen.getY() - ADJUSTMENT),skewLen.getHeight(), skewLen.getWidth());
        }
        else if(answerIndex == 1){
            lens.setLocation((int) skewLen.getX(), (int) skewLen.getY(),skewLen.getHeight(), skewLen.getWidth());
        }
        else{
            lens.setLocation((int) skewLen.getX(), (int) skewLen.getY() + ADJUSTMENT,skewLen.getHeight(), skewLen.getWidth());
        }

        for(int i = 0; i < views.length; i++) {
            lasers.get(i).setLens(lens); //Grab matching laser
            lasers.get(i).calculate();
            end = new PointF(lasers.get(i).getEnd().x, lasers.get(i).getEnd().y);

            //Compensate for offset of end point and origin of
            //photodetector view
            end.y = end.y - (photoTemplate.getHeight() / 2);
            end.x = end.x - (float) (photoTemplate.getWidth() * .75);

            if (end.x > views[i].getX()) {
                xDelta = end.x - views[i].getX();
            } else {
                xDelta = -(views[i].getX() - end.x);
            }

            if (end.y > views[i].getY()) {
                yDelta = end.y - views[i].getY();
            } else {
                yDelta = -(views[i].getY() - end.y);
            }

            animation = new TranslateAnimation(0, xDelta, 0, yDelta);
            animation.setDuration(500);
            animation.setFillAfter(true);
            views[i].startAnimation(animation);
        }
    }

    /**
     * Handles the action of the user's choice
     *
     * @param correct indicates if the user's answer was wrong or right
     */
    private void ClickHandler(boolean correct){
        if(!answered){
            //Records that the user has answered
            answered = false;

            //Disable further interaction with button
            Button spin = (Button) findViewById(R.id.spinSkew);
            spin.setClickable(false);

            //Create handler object to call runables after a delay
            Handler dialogEngine = new Handler();

            //Sends user's score to be recorded
            recordAnswer(user,correct);
            System.out.println("Sent " + correct + " to be logged.");

            DrawLens();
            DrawLasers();
            LightPhotodetectors(correct);

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

    /**
     * Sets up the lasers and sends them to Drawing view to be
     * rendered
     */
    private void DrawLasers() {
        //Capture drawing view
        DrawingView dv = (DrawingView) findViewById(R.id.view);
        DrawingView dl = (DrawingView) findViewById(R.id.skewLen);
        TranslateAnimation animation;

        switch (userHeight){
            case 0:
                lens.setLocation((int)dl.getX(),(int) dl.getY() - ADJUSTMENT,dl.getHeight(),dl.getWidth());
                animation = new TranslateAnimation(0, 0, 0,- ADJUSTMENT);
                break;
            case 1:
                lens.setLocation((int)dl.getX(),(int) dl.getY(),dl.getHeight(),dl.getWidth());
                animation = new TranslateAnimation(0, 0, 0, 0);
                break;
            case 2:
                lens.setLocation((int)dl.getX(),(int) dl.getY() + ADJUSTMENT,dl.getHeight(),dl.getWidth());
                animation = new TranslateAnimation(0, 0, 0, ADJUSTMENT);
                break;
            default:
                lens.setLocation((int)dl.getX(),(int) dl.getY(),dl.getHeight(),dl.getWidth());
                animation = new TranslateAnimation(0, 0, 0, dl.getY());
                break;
        }

        animation.setDuration(100);
        animation.setFillAfter(true);
        dl.startAnimation(animation);

        //New laser list
        lasers = new ArrayList<>();

        setUpLasers();

        //Add user's choice of lens
        //and calculate
        for(Laser l: lasers){
            l.setLens(lens);
            l.calculate();
        }

        //Request the drawing view to render lasers
        dv.drawLasers(lasers);
    }

    /**
     * This function retrieves the lens holder view and executes the render
     * lens function
     */
    private void DrawLens() {
        DrawingView view = (DrawingView) findViewById(R.id.skewLen);
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
     * Sets the photodetector images depending on user correctness
     *
     * @param lit whether or not the photodectors should be lit
     */
    private void LightPhotodetectors(boolean lit){
        ImageView[] views = new ImageView[4];
        views[0] = (ImageView) findViewById(R.id.skDect1);
        views[1] = (ImageView) findViewById(R.id.skDect2);
        views[2] = (ImageView) findViewById(R.id.skDect3);
        views[3] = (ImageView) findViewById(R.id.skDect4);

        if(lit == true){
            for(ImageView i: views){
                i.setImageResource(R.drawable.detector_lit);
            }
        }
        else{
            for(ImageView i: views){
                i.setImageResource(R.drawable.detector);
            }
        }
    }

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
            new AlertDialog.Builder(Skew.this)
                    .setTitle("Correct!") //Sets the title of the dialogue
                    .setMessage("You chose the correct height. Would you like to " +
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
                            startActivity(new Intent(Skew.this, LensCraftMenu.class));
                        }
                    }).show(); //Shows created dialogue
        }
        //User is not correct
        else{
            //Incorrect Answer Alert Dialogue
            //Ask if the user would like to play again
            new AlertDialog.Builder(Skew.this)
                    .setTitle("Nope") //Sets the title of the dialogue
                    .setMessage("Sorry, that is not the right height. Would you like " +
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
                            startActivity(new Intent(Skew.this, LensCraftMenu.class));
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
    private void recordAnswer(User user, boolean correct) {

        //This is a good place to double check for a bad reference

        //if the user's answer == correct index
        System.out.print(user);
        if (user != null) {
            if (correct) {
                //Increment the user's correct count
                user.incCorrect();
                if (user.getLensLVL() < 4) {
                    user.setLensLVL(4);
                    System.out.println("Lens Level was set to Four");
                }
            } else {
                //User is wrong
                //Increment user's incorrect count
                user.incIncorrect();
            }

            user.saveUser("default.dat", getApplicationContext());
        }
    }
}
