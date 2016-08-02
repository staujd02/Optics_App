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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Joel on 7/1/2016.
 *
 * This is a branching activity of Lens Crafter menu (LensCraftMenu.java)
 *
 * Asks the user to adjust the focal length to the correct value for the laser
 * to fall upon the photodetectors.
 *
 */
public class ConcaveConvex extends Activity {

    final int CONVEX_LENS = 1;  //Lens index of the convex lens (in LensCraftMenu.lensArrayList)
    final int CONCAVE_LENS = 6; //Index of the concave lens (in LensCraftMenu.lensArrayList)

    //These units are used to dimension the laser environment's simulated height and width
    //These units are used in all conversions from pixels to units and units to pixels
    final float ENVIRONMENT_WIDTH = 100;
    final float ENVIRONMENT_HEIGHT = 100;

    final int ProgressMax = 6;      //The progress bar constant for max value; setup onCreate()
    final int ProgressDefault = 0;  //The default or starting progress for the seekbar; setup onCreate()

    final float MULTIPLIER = 7; //Unit multiplier used to translate slider value to unit value
    final int OFF_SET = 7;     //The slider value's offset from 0

    //These three constant are used to determine where the
    //lasers will be drawn. They correspond to the image representation of the laser box.
    final int LASER_COUNT = 4;                //Number of lasers to be drawn
    final int LASER_APERTURE_BOTTOM = 44; //The height at which the laser aperture starts (in px)
    final int LASER_APERTURE_TOP = 78;    //The height at which the laser aperture stops (in px)
    final int ORIGINAL_SIZE = 107;        //The original height of the measured image (for scaling)

    private int answerIndex;        //The index of the correct answer
    private User user;              //Reference to loaded user object
    private PointF lensCenterPoint; //Center point of the lens
    private boolean processStopped; //keeps track of the activity's life cycle and responds accordingly
    private ArrayList<Laser> lasers;//Array of lasers
    private Lens lens;          //Concave or convex lens
    private boolean answered;   //Tracks whether the user has already answered

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     //calls super constructor
        setContentView(R.layout.lenscraft); //sets the view
        setTitle("Concave vs. Convex");         //Assigns title

        user = LensCraftMenu.user; //Grabs user object reference from LensCraft menu

        //Sets the onTouch() Listener for the lens holder
        //Creates a dialog box indicating the lens's center position
        DrawingView ccLens = (DrawingView) findViewById(R.id.orginalLens);
        ccLens.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AlertDialog alertDialog = new AlertDialog.Builder(ConcaveConvex.this).create();
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

        //OnTouch() Listener that displays where the laser are emanating from
        ImageView laserBox = (ImageView) findViewById(R.id.imgLaser);
        laserBox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                DrawingView graph = (DrawingView) findViewById(R.id.view);

                //Determines where lasers emanate from
                int yBottom;
                int yTop;
                int ySegment;

                //Use the midpoint of laserbox x for laser x
                int x = (int) (v.getX() * 2 + v.getWidth()) / 2;

                //Creates Ratio
                yBottom = (LASER_APERTURE_BOTTOM * v.getMeasuredHeight())
                        / ORIGINAL_SIZE;
                yTop = (LASER_APERTURE_TOP * v.getMeasuredHeight())
                        / ORIGINAL_SIZE;

                //Divides equally between lasers
                ySegment = (yTop - yBottom)
                        / (LASER_COUNT + 1);

                //Create empty message and Point
                String msg = "";
                PointF p;

                for (int i = 1; i <= LASER_COUNT; i++) {
                    //Calculates laser point
                    p = new PointF(x, v.getY() + yBottom + ySegment * i);

                    //Converts point to standard graph and units
                    p = convertToGraph(p, graph);

                    //Rounds point
                    p.x = Math.round(p.x); p.y = Math.round(p.y);

                    //This prevents the last point from placing a newLine character
                    if(i != LASER_COUNT){
                        msg = msg + "Point (" + p.x +","+p.y+") Exit angle: 0 degrees\n";
                    }
                    else {
                        msg = msg + "Point (" + p.x +","+p.y+") Exit angle: 0 degrees";
                    }
                }

                //Displays results of compiled message
                AlertDialog alertDialog = new AlertDialog.Builder(ConcaveConvex.this).create();
                alertDialog.setTitle("Laser Origin Points");
                alertDialog.setMessage(msg);
                alertDialog.show();
                return false;
            }
        });

        SeekBar bar = (SeekBar) findViewById(R.id.seekLength);
        bar.setVisibility(View.VISIBLE);
        bar.setProgress(ProgressDefault);
        bar.setMax(ProgressMax);
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateBar(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar){}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        Button laserON = (Button) findViewById(R.id.btnLaserActivate);

        laserON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create lens from user value
                SeekBar bar = (SeekBar) findViewById(R.id.seekLength);

                float value = MULTIPLIER * bar.getProgress();

                //Concave lens has negative fLength, Convex Lens has positive fLength
                if(lens.isConcave()){
                    value = (-1)*value - OFF_SET;
                }
                else{
                    value = value + OFF_SET;
                }

                //Assign length to lens
                //String id, String material, Rect graphic_Reference, double fLen, boolean concave, float radius, float nIndex
                lens = new Lens(lens.getId(),lens.getMaterial(),lens.getGraphic_Reference(),value,lens.isConcave(),lens.getRadius(),lens.getNIndex());

                //Determine Correctness
                if(bar.getProgress() == answerIndex){
                    //Run Handler with correct answer given
                    ClickHandler(true);
                }
                else{
                    //Run Handler with incorrect answer given
                    ClickHandler(false);
                }
            }
        });
    }

    /**
     * This is a main function of the android life cycle
     *
     * Trackers whether the user has answered before navigating away
     * from the activity
     *
     * If unanswered, the user has (yoda logic frame)
     *    onStart() will only call the super constructor
     *              (doesn't go through laser/lens setup)
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
     */
    @Override
    protected void onStart() {
        super.onStart(); //Always start with the super constructor

        //If the processed was stopped during mid run
        //don't re-run laser/lens setup processes
        if(processStopped){
            processStopped = false;
            return;
        }

        init();
    }

    /**
     * This sub routine is intended to clear the screen of any latent
     * content that persisted from a previous question-answer cycle
     */
    public void reset(){
        //Initialize the laser array
        lasers = new ArrayList<>();

        //Array of image views
        ImageView[] views = new ImageView[4];

        //Assign view references
        views[0] = (ImageView) findViewById(R.id.dectOne);
        views[1] = (ImageView) findViewById(R.id.dectTwo);
        views[2] = (ImageView) findViewById(R.id.dectThree);
        views[3] = (ImageView) findViewById(R.id.dectFour);

        //Clears the animation
        for(ImageView v: views){
            v.clearAnimation();
        }

        //Resets the drawing view and its respective objects
        DrawingView graph = (DrawingView) findViewById(R.id.view);
        DrawingView lens = (DrawingView) findViewById(R.id.orginalLens);
        lens.drawLens(null);
        lens.reset();
        graph.reset();

        //Ensure button is clickable
        Button ON = (Button) findViewById(R.id.btnLaserActivate);
        SeekBar bar = (SeekBar) findViewById(R.id.seekLength);
        bar.setEnabled(true);
        ON.setClickable(true);

        //Reset Photodetector Image
        LightPhotodetectors(false);
    }

    /**
     * This class manages the startup procedure for preparing
     * a new problem for the user to solve.
     */
    public void init(){
        answered = false;   //Set the answered state back to false

        //Removes latent content from previous answer
        reset();

        SeekBar bar = (SeekBar) findViewById(R.id.seekLength);

        //Correct Index
        Random rand = new Random(); //Create new random

        //Determines the correct slider position at random
        answerIndex = rand.nextInt(bar.getMax()); //Gets a number from a set determined by the number of slider positions

        //Next it chooses whether the lens is concave or convex
        if(rand.nextInt(2) == 0){
            lens = new Lens(LensCraftMenu.lensArrayList.get(CONVEX_LENS));
        }
        else{
            lens = new Lens(LensCraftMenu.lensArrayList.get(CONCAVE_LENS));
        }

        //This directions dialog displays until the user opts out of
        //displaying the directions
        if (user != null) {
            if(user.getHints()){
                //Directions Alert Dialogue
                new AlertDialog.Builder(ConcaveConvex.this)
                        .setTitle("Directions") //Sets the title of the dialogue
                        .setMessage("Use the lens's focal length to calculate which lens will refract the laser on" +
                                "to the photodetectors.") //Sets the Message
                        //Creates OK button for user interaction (Dismisses Dialogue)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //User pressed yes
                                setUpLasers();          //Assigns the laser's their origin point dynamically
                                setUpPhotoDetectors();  //Assigns the correct lens to the lasers for calculation of the
                                //the photodetectors location (does not render lasers)
                                setGrid();
                                updateBar( ((SeekBar) findViewById(R.id.seekLength)).getProgress());
                            }
                        })
                        .setCancelable(false)
                        .show(); //Shows created dialogue
            }
            else{
                //Start Alert Dialogue
                new AlertDialog.Builder(ConcaveConvex.this)
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
                                updateBar( ((SeekBar) findViewById(R.id.seekLength)).getProgress());
                            }
                        })
                        .setCancelable(false)
                        .show(); //Shows created dialogue
            }
        }
    }

    /**
     * This function updates the button displays to the correct values set by the user
     *
     * @param value progress of the seekBar
     */
    public void updateBar(int value){
        Button shape = (Button) findViewById(R.id.indShape);
        Button nIndex = (Button) findViewById(R.id.indNindex);
        Button length = (Button) findViewById(R.id.indFocalLength);
        Button radius = (Button) findViewById(R.id.indRadius);

        String msg;

        DecimalFormat f = new DecimalFormat("#.###");

        float fLen = value * MULTIPLIER;

        if(lens.isConcave()){
            //Set the Shape tag Concave
            msg = getResources().getString(R.string.indPreShape) + " Concave";
            shape.setText(msg);

            //Sets the focal length
            fLen = fLen*(-1) - OFF_SET;
            msg = getResources().getString(R.string.indPreFocal) + " " + f.format(fLen);
            fLen *= -1;
        }
        else{
            //Set the Shape tag Concave
            msg = getResources().getString(R.string.indPreShape) + " Convex";
            shape.setText(msg);

            fLen = fLen + OFF_SET;
            msg = getResources().getString(R.string.indPreFocal) + " " + f.format(fLen);
        }

        //Set the text of the length
        length.setText(msg);

        //Set the text of the N Index (Constant in this case)
        msg =getResources().getString(R.string.indPreIndex) + " " +f.format(lens.getNIndex()) ;
        nIndex.setText(msg);

        //Calculate radius
        fLen = 1/fLen;
        fLen = fLen/(lens.getNIndex() - 1);
        fLen = 1/fLen;

        //Set Radius
        msg = getResources().getString(R.string.indPreRadius) + " " + f.format(fLen);
        radius.setText(msg);
    }

    /**
     * Assigns the origin point to the lasers and initializes them
     * It also adds the lasers to lasers array
     */
    public void setUpLasers(){
        //Grabs appropriate view references
        ImageView laserBox = (ImageView) findViewById(R.id.imgLaser);
        DrawingView drawingview = (DrawingView) findViewById(R.id.view);

        //Measures the views
        laserBox.measure(laserBox.getWidth(), laserBox.getHeight());
        drawingview.measure(drawingview.getWidth(), drawingview.getHeight());

        //Determine where to draw lasers
        int yBottom; int yTop; int ySegment;

        //Use the midpoint of laserbox x for laser x
        int x = (int) (laserBox.getX()*2 + laserBox.getWidth()) / 2;

        yBottom = (LASER_APERTURE_BOTTOM * laserBox.getMeasuredHeight())
                                / ORIGINAL_SIZE;
        yTop = (LASER_APERTURE_TOP * laserBox.getMeasuredHeight())
                                / ORIGINAL_SIZE;

        //Divide equally between lasers
        ySegment = (yTop - yBottom)
                        / (LASER_COUNT + 1);

        Laser laser; //Generic laser object

        //Draws lasers's Laser objects
        for(int i = 1; i <= LASER_COUNT; i++){
            //Creates the laser objects (start Point, Area's maximum point)
            laser = new Laser(new PointF(x,
                    laserBox.getY() + yBottom + ySegment*i),
                    new Point(drawingview.getWidth(),drawingview.getHeight()));
            lasers.add(laser);  //Add new laser to the list
        }
    }


    /**
     * Turn on the grid at runtime to ensure all objects are drawn and
     * dimensioned
     */
    protected void setGrid() {
        //Grabs the necessary view references
        DrawingView view = (DrawingView) findViewById(R.id.orginalLens);
        DrawingView canvas = (DrawingView) findViewById(R.id.view);
        ImageView laserBox = (ImageView) findViewById(R.id.imgLaser);

        //Synchronizes environment constants with drawing view
        //necessary for labels to be drawn
        canvas.setEHeight((int)ENVIRONMENT_HEIGHT);
        canvas.setEWidth((int)ENVIRONMENT_WIDTH);

        //Activates the grid on the main view, and assign a starting x point
        canvas.setDrawGrid(true, laserBox.getX() + laserBox.getWidth(), canvas.getHeight());

        //Sets the canvas onTouch() event to display where the user touched
        canvas.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //Create a point using the user's x and y touch location
                PointF p  = new PointF(event.getX(),event.getY());

                //Subtracts the starting x value from the user's x location
                p.x = p.x - ((DrawingView) v).getStartX();

                //Translate y coordinate from screen plot scheme to standard plot scheme
                //y = 0 is at the bottom of the screen this way
                p.y = v.getHeight() - p.y;

                //Convert pixels to standard units
                p.x = p.x * (ENVIRONMENT_WIDTH/(v.getWidth()));
                p.y = p.y * (ENVIRONMENT_HEIGHT/(v.getHeight()));

                //Create dialog with information and display
                new AlertDialog.Builder(ConcaveConvex.this)
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

        //Array of image views
        ImageView[] views = new ImageView[4];

        TranslateAnimation animation;   //Animation that moves the photodetectors
        PointF end;                     //End point of the animation
        float xDelta;                   //Change in x to go from current location to end point x
        float yDelta;                   //Change in y to go from current location to end point y
        float value;                    //Calculated value using slider index and multiplier

        //Assign view references
        views[0] = (ImageView) findViewById(R.id.dectOne);
        views[1] = (ImageView) findViewById(R.id.dectTwo);
        views[2] = (ImageView) findViewById(R.id.dectThree);
        views[3] = (ImageView) findViewById(R.id.dectFour);

        //Get correct focal length
        value = MULTIPLIER * answerIndex;

        System.out.println("I KNOW THE ANSWER !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("Its position " + answerIndex);

        //Concave lens has negative fLength, Convex Lens has positive fLength
        if(lens.isConcave()){
            value = (-1)*value - OFF_SET;
        }
        else{
            value = value + OFF_SET;
        }

        //Assign length to lens
        //String id, String material, Rect graphic_Reference, double fLen, boolean concave, float radius, float nIndex
        lens = new Lens(lens.getId(),lens.getMaterial(),lens.getGraphic_Reference(),value,lens.isConcave(),lens.getRadius(),lens.getNIndex());

        //Get the Lens holder from the layout for measurments
        DrawingView lencc = (DrawingView) findViewById(R.id.orginalLens);
        DrawingView graph = (DrawingView) findViewById(R.id.view);

        //Assign the lens holder location to lens object
        lens.setLocation((int) lencc.getX(), (int) lencc.getY(),lencc.getHeight(), lencc.getWidth());

        //Loop through views
        for(int i = 0; i < views.length; i++){

            //Adjust the focal length from units to pixels
            float focalLength = (float) (lens.getfLen() * (graph.getWidth()/ENVIRONMENT_WIDTH));

            lasers.get(i).setLens(lens, focalLength); //Grab matching laser
            lasers.get(i).calculate();
            end = new PointF(lasers.get(i).getEnd().x, lasers.get(i).getEnd().y);

            //Compensate for offset of end point and origin of
            //photodetector view
            end.y = end.y - (views[0].getHeight() / 2);
            end.x = end.x - (float) (views[0].getWidth() * .75);

            //Calculate x Delta
            if(end.x > views[i].getX()){
                xDelta = end.x - views[i].getX();
            }
            else{
                xDelta = -(views[i].getX() - end.x);
            }

            //Calculate y delta
            if(end.y > views[i].getY()){
                yDelta = end.y - views[i].getY();
            }
            else{
                yDelta = -(views[i].getY() - end.y);
            }

            //Create animation
            animation = new TranslateAnimation(0, xDelta, 0, yDelta);
            animation.setDuration(500); //Set duration
            animation.setFillAfter(true);   //Tell the view to remain after translation
            views[i].startAnimation(animation); //Run animation
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
            answered = true;

            //Disable further interaction with button
            Button ON = (Button) findViewById(R.id.btnLaserActivate);
            SeekBar bar = (SeekBar) findViewById(R.id.seekLength);
            bar.setEnabled(false);
            ON.setClickable(false);

            //Create handler object to call runables after a delay
            Handler dialogEngine = new Handler();

            //Sends user's score to be recorded
            recordAnswer(user,correct);

            //Function calls
            //----------------------
            DrawLens();                     //Draw the lens
            DrawLasers();                   //Draw the lasers
            LightPhotodetectors(correct);   //Light detectors (or not...)
            //----------------------

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
        DrawingView dl = (DrawingView) findViewById(R.id.orginalLens);

        //Set the lens location in the laser's space based on lens holder view
        lens.setLocation((int)dl.getX(),(int) dl.getY(),dl.getHeight(),dl.getWidth());

        //New laser list
        lasers = new ArrayList<>();

        //Populates laser array
        setUpLasers();

        //Add user's choice of lens
        //and calculate
        for(Laser l: lasers){

            //Adjust the focal length from units to pixels
            float focalLength = (float) (lens.getfLen() * (dv.getWidth()/ENVIRONMENT_WIDTH));

            //Sends the lens to the laser for interaction
            l.setLens(lens,focalLength);
            //Tell the laser information is set and ready for calculation
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
        DrawingView view = (DrawingView) findViewById(R.id.orginalLens);
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
     * Assigns the appropriate images to the photodetectors based on lit parameter
     *
     * @param lit boolean whether to assign lit image or unlit image
     */
    private void LightPhotodetectors(boolean lit){
        //Create view array
        ImageView[] views = new ImageView[4];

        //Populate with view references
        views[0] = (ImageView) findViewById(R.id.dectOne);
        views[1] = (ImageView) findViewById(R.id.dectTwo);
        views[2] = (ImageView) findViewById(R.id.dectThree);
        views[3] = (ImageView) findViewById(R.id.dectFour);

        //Sets image to all ImageViews
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
            //Ask if the user would like to try again
            new AlertDialog.Builder(ConcaveConvex.this)
                    .setTitle("Correct!") //Sets the title of the dialogue
                    .setMessage("You chose the correct lens. Would you like to " +
                            "try again?") //Sets the Message
                    //Creates OK button for user interaction (Dismisses Dialogue)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //User pressed yes
                            init();
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
            //Ask if the user would like to try again
            new AlertDialog.Builder(ConcaveConvex.this)
                    .setTitle("Nope") //Sets the title of the dialogue
                    .setMessage("Sorry, that is not the right lens. Would you like " +
                            "to try again?") //Sets the Message
                    //Creates OK button for user interaction (Dismisses Dialogue)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //User pressed yes
                            init();
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
        if (user != null) {
            if(correct){
                //Increment the user's correct count
                user.incCorrect(User.SHAPE_QUESTION);
                if(user.getLensLVL() < 2){
                    user.setLensLVL(2);
                }
            }
            else{
                //User is wrong
                //Increment user's incorrect count
                user.incIncorrect(User.SHAPE_QUESTION);
            }

            user.saveUser("default.dat", getApplicationContext());
        }
    }

    /**
     * This class calculates the center point of a view and returns its center
     * in standard units
     *
     * @param v     the view of which the centerpoint will be found
     * @param graph the Drawing view containing the view
     * @return      returns center point of view (standard units)
     */
    public PointF viewCenter_toGraph(View v, DrawingView graph){
        //Create a new PointF located at the center of the view
        PointF p = new PointF((v.getX()*2 + v.getWidth())/2f, (v.getY()*2 + v.getHeight())/2f );

        //Convert Point
        p = convertToGraph(p,graph);

        return p;
    }

    /**
     * Converts a point in pixels to standard units and changes the y to represent a standard
     * graph layout (y starts at the bottom instead of the top)
     *
     * @param point point to be converted
     * @param graph view representative of the graph
     * @return the converted point
     */
    public PointF convertToGraph(PointF point, DrawingView graph){
        PointF p = new PointF(point.x,point.y);

        p.x = p.x - graph.getStartX();

        //Invert y to measure from the bottom up (like a typical graph)
        p.y = graph.getHeight() - p.y;

        //Then convert pixels to standard units
        p.x = p.x * (ENVIRONMENT_WIDTH/graph.getWidth());
        p.y = p.y * (ENVIRONMENT_HEIGHT/graph.getHeight());

        return p;
    }
}
