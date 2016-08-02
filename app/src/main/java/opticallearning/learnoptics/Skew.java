package opticallearning.learnoptics;

import android.app.*;
import android.content.*;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.*;
import android.view.*;
import android.view.animation.TranslateAnimation;
import android.widget.*;
import java.text.DecimalFormat;
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

    final int LENS = 8; //Constant lens index
    int adjustment; //Constant adjustment for lens height

    final float ENVIRONMENT_WIDTH = 100;
    final float ENVIRONMENT_HEIGHT = 100;

    final int ProgressMax = 4;
    final int ProgressDefault = 2;

    final int MULTIPLIER = 5; //Unit multiplier used to translate slider value to unit value
    final int OFF_SET = -10;     //The slider value's offset from 0

    //These three constant are used to determine where the
    //lasers will be drawn
    final int LASER_COUNT = 4;            //Number of lasers to be drawn
    final int LASER_APERTURE_BOTTOM = 44; //The height at which the laser aperture starts (in px)
    final int LASER_APERTURE_TOP = 78;    //The height at which the laser aperture stops (in px)
    final int ORIGINAL_SIZE = 107;        //The original height of the measured image

    private PointF lensCenterPoint;     //Center point of the lens
    private boolean processStopped;     //keeps track of the activity's life cycle and responds accordingly
    private int answerIndex;            //The index of the correct answer
    private User user;                  //Reference to user object
    private ArrayList<Laser> lasers;    //Array of lasers
    private int prev_progress = -1;     //Previous seek bar progress (used in animation)
    private Lens lens;                  //Concave or convex lens
    private Boolean answered;           //Tracks whether the user has already answered

    private DrawingView lensView; //Reference to the lens view
    private DrawingView graph;    //Reference to the background drawing view
    private Button btnLaser;      //Reference to the button to activate the laser
    private SeekBar bar;          //References the seekBar
    private ImageView[] photoDects;    //References of the photodectectors

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //call super constructor
        setContentView(R.layout.lenscraft);      //sets the view
        setTitle("Lens Height");            //assigns descriptive title

        user = LensCraftMenu.user; //Grab user reference from menu

        graph = (DrawingView) findViewById(R.id.view);

        photoDects = new ImageView[4];
        photoDects[0] = (ImageView) findViewById(R.id.dectOne);
        photoDects[1] = (ImageView) findViewById(R.id.dectTwo);
        photoDects[2] = (ImageView) findViewById(R.id.dectThree);
        photoDects[3] = (ImageView) findViewById(R.id.dectFour);

        DrawingView skewLens = (DrawingView) findViewById(R.id.orginalLens);
        lensView = skewLens;
        skewLens.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                lensCenterPoint = lensCenterPoint();

                AlertDialog alertDialog = new AlertDialog.Builder(Skew.this).create();
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
                AlertDialog alertDialog = new AlertDialog.Builder(Skew.this).create();
                alertDialog.setTitle("Laser Origin Points");
                alertDialog.setMessage(msg);
                alertDialog.show();
                return false;
            }
        });

        SeekBar bar = (SeekBar) findViewById(R.id.seekMove);
        this.bar = bar;
        bar.setVisibility(View.VISIBLE);
        bar.setMax(ProgressMax);
        bar.setProgress(ProgressDefault);
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TranslateAnimation animation;

                //Calculates the distance height adjustment based on seek bar's progress value
                adjustment = progress * MULTIPLIER + OFF_SET;

                //Converts the unit value of the adjustment to a pixel value
                float pixelAdjustment = adjustment * (graph.getHeight()/ENVIRONMENT_HEIGHT);
                float prevAdjust;

                //The default value for prev_progress is -1, assign prev to current if prev = to default
                if(prev_progress == -1){
                    prev_progress = progress;
                }

                //Calculates the adjustment
                prevAdjust = prev_progress * MULTIPLIER + OFF_SET;

                //Converts to pixels from units
                prevAdjust = prevAdjust * (graph.getHeight()/ENVIRONMENT_HEIGHT);

                //Set animation properties (from prev to current)
                animation = new TranslateAnimation(0, 0, (int) prevAdjust, (int) pixelAdjustment);

                //Assign current progress as the new previous progress
                prev_progress = progress;

                //Animation properties
                animation.setDuration(250);
                animation.setFillAfter(true);
                lensView.startAnimation(animation);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        Button laserON = (Button) findViewById(R.id.btnLaserActivate);
        btnLaser = laserON;
        laserON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create lens from user value
                SeekBar bar = (SeekBar) findViewById(R.id.seekMove);

                float value = MULTIPLIER * bar.getProgress() + OFF_SET;

                value = value * (graph.getHeight()/ENVIRONMENT_HEIGHT);

                //Calculate Distance Draw
                lens = new Lens(LensCraftMenu.lensArrayList.get(LENS));

                lens.setLocation((int)lensView.getX(),(int) (lensView.getY() + value),lensView.getHeight(),lensView.getWidth());

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
     * Sets the boolean processStopped according to the current
     * state of the activity
     *
     * User has answered => Call the Unique startup
     * User hasn't answered yet => Call the Super Constructor for onStart() only
     */
    @Override
    protected void onStop() {
        super.onStop();
        if(!answered){
            processStopped = true;
        }
    }

    /**
     * This sub routine is intended to clear the screen of any latent
     * content that persisted from a previous question-answer cycle
     */
    public void reset(){
        //Initialize the laser array
        lasers = new ArrayList<>();

        //Clears the animation
        for(ImageView v: photoDects){
            v.clearAnimation();
        }

        //Resets the drawing view and its respective objects
        lensView.reset();
        graph.reset();

        //Ensure button is clickable and slider can change
        bar.setEnabled(true);
        bar.setProgress(2);
        btnLaser.setClickable(true);

        //Reset Photodetector Image
        LightPhotodetectors(false);
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

        init();
    }

    /**
     * This sub routine runs the majority of the content and is repeated when
     * the user indicates they would like to attempt another question
     */
    public void init(){

        answered = false;   //Set the answered state back to false

        reset(); //Resets all the previous values from the previous run

        setAnswerIndex();   //Chooses the answer index

        //This directions dialog displays until the user opts out of
        //displaying the directions
        if (user != null) {
            if(user.getHints()){
                //Directions Alert Dialogue
                new AlertDialog.Builder(Skew.this)
                        .setTitle("Directions") //Sets the title of the dialogue
                        .setMessage("Select the correct height to shift the lens to focus the light on the photodetectors.") //Sets the Message
                        //Creates OK button for user interaction (Dismisses Dialogue)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //User pressed yes
                                setUpLasers();          //Assigns the laser's their origin point dynamically
                                setUpPhotoDetectors();  //Assigns the correct lens to the lasers for calculation of the
                                //the photodetectors location (does not render lasers)
                                setGrid();
                                updateBar();
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
                                setGrid();
                                updateBar();
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
     */
    public void updateBar(){
        Button shape = (Button) findViewById(R.id.indShape);
        Button nIndex = (Button) findViewById(R.id.indNindex);
        Button length = (Button) findViewById(R.id.indFocalLength);
        Button radius = (Button) findViewById(R.id.indRadius);

        String msg;

        DecimalFormat f = new DecimalFormat("#.###");

        //Set the text of the N Index (Constant in this case)
        msg = getResources().getString(R.string.indPreIndex) + " " +f.format(lens.getNIndex()) ;
        nIndex.setText(msg);

        float width = lens.getWidth();

        //Set Radius
        msg = getResources().getString(R.string.indPreRadius) + " " + width;
        radius.setText(msg);

        //Calculate Focal Length using width
        width = 1/width + 1/width;
        width = (lens.getNIndex() - 1) * width;
        width = 1/width;

        //Set the Shape tag Concave
        msg = getResources().getString(R.string.indPreShape) + " Convex";
        shape.setText(msg);

        //Set the text of the length
        msg = getResources().getString(R.string.indPreFocal) + " " + f.format(width);
        length.setText(msg);
    }

    /**
     * This class is intended to set the answer index based upon the previous answers
     * so no answer is repeated
     *
     */
    private void setAnswerIndex() {
        //Correct Index
        Random rand = new Random(); //Create new random

        //Determines the correct slider position at random
        answerIndex = rand.nextInt(bar.getMax()); //Gets a number from a set determined by the number of slider positions

        //Assign Lens object
        lens = new Lens(LensCraftMenu.lensArrayList.get(LENS));
    }

    /**
     * Assigns the origin point to the lasers and initializes them
     * It also adds the lasers to lasers array
     */
    public void setUpLasers() {
        ImageView laserBox = (ImageView) findViewById(R.id.imgLaser);

        laserBox.measure(laserBox.getWidth(), laserBox.getHeight());
        graph.measure(graph.getWidth(), graph.getHeight());

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
                    new Point(graph.getWidth(), graph.getHeight()));
            lasers.add(laser);
        }
    }

    /**
     * Turn on the grid at runtime to ensure all objects are drawn and
     * dimensioned
     */
    protected void setGrid() {
        //Make all necessary reference calls
        ImageView laserBox = (ImageView) findViewById(R.id.imgLaser);

        //Synchronizes environment constants with drawing view
        //necessary for labels to be drawn
        graph.setEHeight((int)ENVIRONMENT_HEIGHT);
        graph.setEWidth((int)ENVIRONMENT_WIDTH);

        //Activate the grid on the main view, and assign a starting x point
        graph.setDrawGrid(true, laserBox.getX() + laserBox.getWidth(), graph.getHeight());

        graph.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                PointF p  = new PointF(event.getX(),event.getY());
                p.x = p.x - ((DrawingView) v).getStartX();

                //Translate y coordinate from screen plot scheme to standard plot scheme
                p.y = v.getHeight() - p.y;

                //Convert pixels to standard units
                p.x = p.x * (ENVIRONMENT_WIDTH/(v.getWidth()));
                p.y = p.y * (ENVIRONMENT_HEIGHT/(v.getHeight()));

                new AlertDialog.Builder(Skew.this)
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
        lensCenterPoint = viewCenter_toGraph(lensView, graph);
    }

    /**
     * Assigns correct lens to laser, calculates the destination, and then
     * moves the photodetectors at that location
     */
    private void setUpPhotoDetectors() {
        TranslateAnimation animation;
        PointF end;
        Float xDelta;
        Float yDelta;

        //Initialize the lens object
        lens = new Lens(LensCraftMenu.lensArrayList.get(LENS));

        adjustment = answerIndex * MULTIPLIER + OFF_SET;

        float pixelAdjustment = adjustment * (graph.getHeight()/ENVIRONMENT_HEIGHT);

        //Adjust the height based on correct answer
        lens.setLocation((int) lensView.getX(), (int) (lensView.getY() + pixelAdjustment),lensView.getHeight(), lensView.getWidth());

        for(int i = 0; i < photoDects.length; i++) {
            //Adjust the focal length from units to pixels
            float focalLength = (float) (lens.getfLen() * (graph.getWidth()/ENVIRONMENT_WIDTH));

            lasers.get(i).setLens(lens, focalLength); //Grab matching laser
            lasers.get(i).calculate();
            end = new PointF(lasers.get(i).getEnd().x, lasers.get(i).getEnd().y);

            //Compensate for offset of end point and origin of
            //photodetector view
            end.y = end.y - (photoDects[0].getHeight() / 2);
            end.x = end.x - (float) (photoDects[0].getWidth() * .75);

            if (end.x > photoDects[i].getX()) {
                xDelta = end.x - photoDects[i].getX();
            } else {
                xDelta = -(photoDects[i].getX() - end.x);
            }

            if (end.y > photoDects[i].getY()) {
                yDelta = end.y - photoDects[i].getY();
            } else {
                yDelta = -(photoDects[i].getY() - end.y);
            }

            animation = new TranslateAnimation(0, xDelta, 0, yDelta);
            animation.setDuration(500);
            animation.setFillAfter(true);
            photoDects[i].startAnimation(animation);
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
            bar.setEnabled(false);
            btnLaser.setClickable(false);

            //Create handler object to call runables after a delay
            Handler dialogEngine = new Handler();

            //Sends user's score to be recorded
            recordAnswer(user,correct);

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
        //New laser list
        lasers = new ArrayList<>();

        setUpLasers();

        //Add user's choice of lens
        //and calculate
        for(Laser l: lasers){

            //Adjust the focal length from units to pixels
            float focalLength = (float) (lens.getfLen() * (graph.getWidth()/ENVIRONMENT_WIDTH));

            l.setLens(lens, focalLength);
            l.calculate();
        }

        //Request the drawing view to render lasers
        graph.drawLasers(lasers);
    }

    /**
     * This function retrieves the lens holder view and executes the render
     * lens function
     */
    private void DrawLens() {
        lensView.drawLens(lens);
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
        if(lit){
            for(ImageView i: photoDects){
                i.setImageResource(R.drawable.detector_lit);
            }
        }
        else{
            for(ImageView i: photoDects){
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
            new AlertDialog.Builder(Skew.this)
                    .setTitle("Correct!") //Sets the title of the dialogue
                    .setMessage("You chose the correct height. Would you like to " +
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
                            startActivity(new Intent(Skew.this, LensCraftMenu.class));
                        }
                    }).show(); //Shows created dialogue
        }
        //User is not correct
        else{
            //Incorrect Answer Alert Dialogue
            //Ask if the user would like to try again
            new AlertDialog.Builder(Skew.this)
                    .setTitle("Nope") //Sets the title of the dialogue
                    .setMessage("Sorry, that is not the right height. Would you like " +
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
        if (user != null) {
            if (correct) {
                //Increment the user's correct count
                user.incCorrect(User.HEIGHT_QUESTION);
                if (user.getLensLVL() < 4) {
                    user.setLensLVL(4);
                }
            } else {
                //User is wrong
                //Increment user's incorrect count
                user.incIncorrect(User.HEIGHT_QUESTION);
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
     * This class computes where the center of the lens holder is located
     * in reference to the seek bar's progress
     *
     * @return the center point of the lens (in standard coordinates)
     */
    public PointF lensCenterPoint(){
        PointF p = new PointF();
        float adjust;

        adjust = bar.getProgress() * MULTIPLIER + OFF_SET;

        p.x = lensView.getX();
        p.y = lensView.getY();

        p = convertToGraph(p,graph);

        p.y = p.y + adjust;

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
