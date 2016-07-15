package opticallearning.learnoptics;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
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
 * Asks users to select the correct distance from the lens to the laser
 *
 */
public class Distances extends Activity {

    final int LENS = 10; //Constant lens index
    final int ADJUST_FACTOR = 25; //Variable adjustment for lens distance

    final float ENVIRONMENT_WIDTH = 100;
    final float ENVIRONMENT_HEIGHT = 100;

    //These three constant are used to determine where the
    //lasers will be drawn
    final int LASER_COUNT = 4;            //Number of lasers to be drawn
    final int LASER_APERTURE_BOTTOM = 44; //The height at which the laser aperture starts (in px)
    final int LASER_APERTURE_TOP = 78;    //The height at which the laser aperture stops (in px)
    final int ORIGINAL_SIZE = 107;        //The original height of the measured image

    private Button spinner;             //Button which opens prompt for user selection of lens
    private PointF lensCenterPoint;     //Center point of the lens
    private int answerIndex;            //The index of the correct answer
    private int userHeight;
    private boolean processStopped;     //keeps track of the activity's life cycle and responds accordingly
    private User user;                  //Reference to user object

    private ArrayList<Laser> lasers;    //Array of lasers
    private ImageView[] views;          //Array of references to photodetector views

    private Lens lens;                  //Concave or convex lens
    private Boolean answered;           //Tracks whether the user has already answered

    /**
     *
     * Creates the Activity and loads initial objects
     *
     * @param savedInstanceState used by the super constructor
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //Calls super constructor
        setContentView(R.layout.distance);  //sets the content view
        setTitle("Lens Distance");          //Assigns specific title over default

        user = LensCraftMenu.user; //Grabs user reference from menu

        //Creates spinner object and sets the reference to spinner spinDistance in distance.xml
        spinner = (Button) findViewById(R.id.spinDistance);
        spinner.setText(R.string.spinDistanceText);

        DrawingView dLens = (DrawingView) findViewById(R.id.dLen);
        dLens.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AlertDialog alertDialog = new AlertDialog.Builder(Distances.this).create();
                alertDialog.setTitle("Lens Center-point");
                alertDialog.setMessage("(" +
                        Math.round(lensCenterPoint.x)
                        +","+
                        Math.round(lensCenterPoint.y)+
                        ")");
                alertDialog.show();
                return false;
            }
        });

        String[] array = {
                ADJUST_FACTOR + " units closer to laser",
                "Do not move lens",
                ADJUST_FACTOR + " units farther from laser",
        };

        final ArrayAdapter<CharSequence> adapter =  new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, array);

        //Assign drop down behaviour
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Create onClickListener
        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create Dialog for the user to pick a lens
                new AlertDialog.Builder(Distances.this)
                        //Set title and the adapter created above
                        .setTitle("Pick Distance")
                        .setAdapter(adapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Grabs array of choices
                                String[] s = getResources().getStringArray(R.array.distances);
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
        if(processStopped){
            processStopped = false;
            return;
        }

        answered = false;   //Set the answered state back to false

        //Initialize the laser array
        lasers = new ArrayList<>();

        //Ensure button is clickable
        Button spin = (Button) findViewById(R.id.spinDistance);
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
                new AlertDialog.Builder(Distances.this)
                        .setTitle("Directions") //Sets the title of the dialogue
                        .setMessage("Select the correct distance to place the lens from the laser to focus the light on the photodetectors. " +
                                "The lens has a focal length of " + LensCraftMenu.lensArrayList.get(LENS).getfLen() + ".") //Sets the Message
                        //Creates OK button for user interaction (Dismisses Dialogue)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //User pressed yes
                                setUpLasers();          //Assigns the laser's their origin point dynamically
                                setUpPhotoDetectors();  //Assigns the correct lens to the lasers for calculation of the
                                //the photodetectors location (does not render lasers)
                                setGrid();
                            }
                        })
                        .setCancelable(false)
                        .show(); //Shows created dialogue
            }
            else{
                //Start Alert Dialogue
                new AlertDialog.Builder(Distances.this)
                        .setTitle("Start?") //Sets the title of the dialogue
                        .setMessage("Press OK to start.") //Sets the Message
                        //Creates OK button for user interaction (Dismisses Dialogue)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //User pressed yes
                                setUpLasers();          //Assigns the laser's their origin point dynamically
                                setUpPhotoDetectors();  //Assigns the correct lens to the lasers for calculation of the
                                //the photodetectors location (does not render lasers)
                                setGrid();
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
        for (int i = 1; i <= LASER_COUNT; i++) {
            laser = new Laser(new PointF(x,
                    laserBox.getY() + yBottom + ySegment * i),
                    new Point(drawingview.getWidth(), drawingview.getHeight()));
            lasers.add(laser);
        }
    }

    /**
     * Turn on the grid at runtime to ensure all objects are drawn and
     * dimensioned
     */
    protected void setGrid() {
        //Make all necessary reference calls
        DrawingView view = (DrawingView) findViewById(R.id.dLen);
        DrawingView canvas = (DrawingView) findViewById(R.id.view);
        ImageView laserBox = (ImageView) findViewById(R.id.imgLaser);

        //Activate the grid on the main view, and assign a starting x point
        canvas.setDrawGrid(true, laserBox.getX() + laserBox.getWidth(), canvas.getHeight());

        System.out.println(laserBox.getX() + laserBox.getWidth());

        canvas.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                PointF p  = new PointF(event.getX(),event.getY());

                p.x = p.x - ((DrawingView) v).getStartX();

                //Translate y coordinate from screen plot scheme to standard plot scheme
                p.y = v.getHeight() - p.y;

                //Convert pixels to standard units
                p.x = p.x * (ENVIRONMENT_WIDTH/(v.getWidth()));
                p.y = p.y * (ENVIRONMENT_HEIGHT/(v.getHeight()));

                new AlertDialog.Builder(Distances.this)
                        .setTitle("Location") //Title of the dialogue
                        .setMessage("("+
                                Math.round(p.x)+
                                ","+
                                Math.round(p.y)+")") //Where the user touched
                        //Creates OK button for user interaction (Dismisses Dialogue)
                        .show(); //Shows created dialogue
                return false;
            }
        });

        //Find the len's center point
        lensCenterPoint = viewCenter_toGraph(view, canvas);
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

        views[0] = (ImageView) findViewById(R.id.dDect1);
        views[1] = (ImageView) findViewById(R.id.dDect2);
        views[2] = (ImageView) findViewById(R.id.dDect3);
        views[3] = (ImageView) findViewById(R.id.dDect4);

        DrawingView dv = (DrawingView) findViewById(R.id.view);

        //Initialize the lens object
        lens = new Lens(LensCraftMenu.lensArrayList.get(LENS));

        //Get the Lens holder from distance.xml for measurements
        DrawingView dLens = (DrawingView) findViewById(R.id.dLen);

        //Adjust standard units into a pixel value
        float pixelFactor = ADJUST_FACTOR * (dv.getWidth()/ENVIRONMENT_WIDTH);

        //Adjust the distance based on correct answer
        if(answerIndex == 0){
            lens.setLocation((int) (dLens.getX() - pixelFactor), ((int) dLens.getY()),dLens.getHeight(), dLens.getWidth());
        }
        else if(answerIndex == 1){
            lens.setLocation((int) dLens.getX(), (int) dLens.getY(),dLens.getHeight(), dLens.getWidth());
        }
        else{
            lens.setLocation((int) (dLens.getX() + pixelFactor), (int) dLens.getY(),dLens.getHeight(), dLens.getWidth());
        }

        for(int i = 0; i < views.length; i++) {
            //Adjust the focal length from units to pixels
            float focalLength = (float) (lens.getfLen() * (dv.getWidth()/ENVIRONMENT_WIDTH));

            lasers.get(i).setLens(lens, focalLength); //Grab matching laser
            lasers.get(i).calculate();
            end = new PointF(lasers.get(i).getEnd().x, lasers.get(i).getEnd().y);

            //Compensate for offset of end point and origin of
            //photodetector view
            end.y = end.y - (views[0].getHeight() / 2);
            end.x = end.x - (float) (views[0].getWidth() * .75);

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
            Button spin = (Button) findViewById(R.id.spinDistance);
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
        DrawingView dl = (DrawingView) findViewById(R.id.dLen);
        TranslateAnimation animation;

        //Adjust standard units into a pixel value
        float pixelFactor = ADJUST_FACTOR * (dv.getWidth()/ENVIRONMENT_WIDTH);

        //Translate the lens location according to the user's preference
        switch (userHeight){
            case 0:
                //Move lens backwards
                lens.setLocation((int)(dl.getX() - pixelFactor),(int) dl.getY(),dl.getHeight(),dl.getWidth());
                animation = new TranslateAnimation(0,(int) -pixelFactor,0,0);
                break;
            case 1:
                //Don't move the lens at all
                lens.setLocation((int)dl.getX(),(int) dl.getY(),dl.getHeight(),dl.getWidth());
                animation = new TranslateAnimation(0, 0, 0, 0);
                break;
            case 2:
                //Move the lens forward
                lens.setLocation((int)(dl.getX() + pixelFactor),(int) dl.getY(),dl.getHeight(),dl.getWidth());
                animation = new TranslateAnimation(0, (int) pixelFactor, 0, 0);
                break;
            default:
                //Don't move the lens as default
                lens.setLocation((int)dl.getX(),(int) dl.getY(),dl.getHeight(),dl.getWidth());
                animation = new TranslateAnimation(0, 0, 0, dl.getY());
                break;
        }

        //Set animation prefences
        animation.setDuration(100);
        animation.setFillAfter(true);   //Lens remains where drawing finished
        dl.startAnimation(animation);   //Start the animation

        //New laser list
        lasers = new ArrayList<>();

        setUpLasers();  //Call a laser setup

        //Add user's choice of lens
        //and calculate
        for(Laser l: lasers){

            //Adjust the focal length from units to pixels
            float focalLength = (float) (lens.getfLen() * (dv.getWidth()/ENVIRONMENT_WIDTH));

            l.setLens(lens, focalLength);
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
        DrawingView view = (DrawingView) findViewById(R.id.dLen);
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
        views[0] = (ImageView) findViewById(R.id.dDect1);
        views[1] = (ImageView) findViewById(R.id.dDect2);
        views[2] = (ImageView) findViewById(R.id.dDect3);
        views[3] = (ImageView) findViewById(R.id.dDect4);

        if(lit){
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
            new AlertDialog.Builder(Distances.this)
                    .setTitle("Correct!") //Sets the title of the dialogue
                    .setMessage("You chose the correct distance. Would you like to " +
                            "try again?") //Sets the Message
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
                            startActivity(new Intent(Distances.this, LensCraftMenu.class));
                        }
                    }).show(); //Shows created dialogue
        }
        //User is not correct
        else{
            //Incorrect Answer Alert Dialogue
            //Ask if the user would like to play again
            new AlertDialog.Builder(Distances.this)
                    .setTitle("Nope") //Sets the title of the dialogue
                    .setMessage("Sorry, that is not the right distance. Would you like " +
                            "to try again?") //Sets the Message
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
                            startActivity(new Intent(Distances.this, LensCraftMenu.class));
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

        //if the user's answer == correct index
        System.out.print(user);
        if (user != null) {
            if (correct) {
                //Increment the user's correct count
                user.incCorrect();
                if (user.getLensLVL() < 5) {
                    user.setLensLVL(5);
                    System.out.println("Lens Level was set to Five");
                }
            } else {
                //User is wrong
                //Increment user's incorrect count
                user.incIncorrect();
            }

            user.saveUser("default.dat", getApplicationContext());
        }
    }

    public PointF viewCenter_toGraph(View v, View graph){
        //Create a new PointF located at the center of the view
        PointF p = new PointF((v.getX()*2 + v.getWidth())/2f, (v.getY()*2 + v.getHeight())/2f );

        p.x = p.x - ((DrawingView) graph).getStartX();

        //Invert y to measure from the bottom up (like a typical graph)
        p.y = graph.getHeight() - p.y;

        //Then convert pixels to standard units
        p.x = p.x * (ENVIRONMENT_WIDTH/graph.getWidth());
        p.y = p.y * (ENVIRONMENT_HEIGHT/graph.getHeight());

        return p;
    }

}
