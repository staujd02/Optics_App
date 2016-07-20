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

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Joel on 7/1/2016.
 *
 * This is a branching activity of Lens Crafter menu (LensCraftMenu.java)
 *
 * This module asks the user to select the correct material for the lens
 *
 */
public class NIndex extends Activity {

    final int NINDEX_ONE = 3;   //First lens with an acetone composition
    final int NINDEX_TWO = 4;   //Second lens choice with a crown glass composition
    final int NINDEX_THREE = 5; //Third lens with flint glass composition

    final float ENVIRONMENT_WIDTH = 100;
    final float ENVIRONMENT_HEIGHT = 100;

    //These three constant are used to determine where the
    //lasers will be drawn
    final int LASER_COUNT = 4;            //Number of lasers to be drawn
    final int LASER_APERTURE_BOTTOM = 44; //The height at which the laser aperture starts (in px)
    final int LASER_APERTURE_TOP = 78;    //The height at which the laser aperture stops (in px)
    final int ORIGINAL_SIZE = 107;        //The original height of the measured image

    private Button spinner;         //Button which opens prompt for user selection of lens
    private boolean processStopped; //keeps track of the activity's life cycle and responds accordingly
    private int answerIndex;        //The index of the correct answer
    private User user;              //Reference to user object
    private PointF lensCenterPoint; //Center point of the lens
    private ArrayList<Laser> lasers;//Array of lasers
    private ArrayList<Integer> answerGroup; //List of available index answers
    private Lens lens;              //Concave or convex lens
    private Boolean answered;       //Tracks whether the user has already answered


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //Calls super constructor
        setContentView(R.layout.n_index);   //sets the view
        setTitle("Index of Refraction");    //assigns descriptive title

        user = LensCraftMenu.user; //Grab user reference from menu

        //Creates spinner object and assigns it to spinMaterial in n_index.xml
        spinner = (Button) findViewById(R.id.spinMaterial);
        spinner.setText(R.string.spinMaterialText);

        //Sets the lens holder onTouch() event to display the center location of the lens holder
        DrawingView materialLens = (DrawingView) findViewById(R.id.materialLen);
        materialLens.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AlertDialog alertDialog = new AlertDialog.Builder(NIndex.this).create();
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
                AlertDialog alertDialog = new AlertDialog.Builder(NIndex.this).create();
                alertDialog.setTitle("Laser Origin Points");
                alertDialog.setMessage(msg);
                alertDialog.show();
                return false;
            }
        });

        //User's options created dynamically by pulling information from data base
        String[] array = {
                "Convex: N Index " + LensCraftMenu.lensArrayList.get(NINDEX_ONE).getNIndex() + " (Radius " +
                                        LensCraftMenu.lensArrayList.get(NINDEX_ONE).getRadius() + ")",
                "Convex: N Index " + LensCraftMenu.lensArrayList.get(NINDEX_TWO).getNIndex() + " (Radius " +
                        LensCraftMenu.lensArrayList.get(NINDEX_TWO).getRadius() + ")",
                "Convex: N Index " + LensCraftMenu.lensArrayList.get(NINDEX_THREE).getNIndex() + " (Radius " +
                        LensCraftMenu.lensArrayList.get(NINDEX_THREE).getRadius() + ")"
        };

        //Creates Adapter for reading the array materials into spinMaterials
        //spinMaterial > {"Acetone Low N","Crown Glass Med N","Flint Glass High N"}
        final ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, array);

        //Assigns dropdown behaviour
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Create onClickListener
        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create Dialog for the user to pick a lens
                new AlertDialog.Builder(NIndex.this)
                        //Set title and the adapter created above
                        .setTitle("Pick Lens Material")
                        .setAdapter(adapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Grabs array of choices
                                String[] s = getResources().getStringArray(R.array.materials);
                                //Sets the user's choice as the button's text
                                spinner.setText(s[which]);

                                //This sets the lens according to the user's selection
                                if(which == 0){
                                    lens = new Lens(LensCraftMenu.lensArrayList.get(NINDEX_ONE));
                                }
                                else if(which == 1){
                                    lens = new Lens(LensCraftMenu.lensArrayList.get(NINDEX_TWO));
                                }
                                else{
                                    lens = new Lens(LensCraftMenu.lensArrayList.get(NINDEX_THREE));
                                }

                                //Determines User Correctness
                                //User's choice => Correct Choice
                                switch (which){
                                    case 0:
                                        if(answerIndex == NINDEX_ONE)
                                            ClickHandler(true); //Runs a correct answer dialog
                                        else
                                            ClickHandler(false);//Runs an incorrect answer dialog
                                        break;
                                    case 1:
                                        if(answerIndex == NINDEX_TWO)
                                            ClickHandler(true);
                                        else
                                            ClickHandler(false);
                                        break;
                                    case 2:
                                        if(answerIndex == NINDEX_THREE)
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
        views[0] = (ImageView) findViewById(R.id.rxDect1);
        views[1] = (ImageView) findViewById(R.id.rxDect2);
        views[2] = (ImageView) findViewById(R.id.rxDect3);
        views[3] = (ImageView) findViewById(R.id.rxDect4);

        //Clears the animation
        for(ImageView v: views){
            v.clearAnimation();
        }

        //Resets the drawing view and its respective objects
        DrawingView graph = (DrawingView) findViewById(R.id.view);
        DrawingView lens = (DrawingView) findViewById(R.id.materialLen);
        lens.reset();
        graph.reset();

        //Ensure button is clickable
        Button spin = (Button) findViewById(R.id.spinMaterial);
        spin.setClickable(true);
        spin.setText(R.string.spinMaterialText);

        //Reset Photodetector Image
        LightPhotodetectors(false);
    }

    public void init(){
        int options;    //Picks the correct answer > later is translated to index
        answered = false;   //Set the answered state back to false

        reset();//Clear any latent content and prep for another run

        //Sets the answer index from the answer group
        setAnswerIndex();

        //Correct Index
        Random rand = new Random(); //Create new random

        options = rand.nextInt(3); //gets an integer 0-2 with equal chances for each

        if(options == 0){
            //Correct index corresponds to first material
            answerIndex = NINDEX_ONE;
        }
        else if(options == 1){
            answerIndex = NINDEX_TWO;
        }
        else{
            //Correct index corresponds to last material
            answerIndex = NINDEX_THREE;
        }

        //This directions dialog displays until the user opts out of
        //displaying the directions
        if (user != null) {
            if(user.getHints()){
                //Directions Alert Dialogue
                new AlertDialog.Builder(NIndex.this)
                        .setTitle("Directions") //Sets the title of the dialogue
                        .setMessage("Select the correct N index to focus the light on the photodetectors.") //Sets the Message
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
                new AlertDialog.Builder(NIndex.this)
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
     * This class is intended to set the answer index based upon the previous answers
     * so no answer is repeated
     *
     */
    private void setAnswerIndex() {

        //Check if answerGroup has been initialized
        if(answerGroup == null){
            //Initialize
            answerGroup = new ArrayList<>();
        }

        //Check if answer pool is empty/exhausted
        if(answerGroup.size() == 0){
            answerGroup.add(NINDEX_ONE);answerGroup.add(NINDEX_TWO);answerGroup.add(NINDEX_THREE);
            System.out.println("ANSWER GROUP RESET");
        }

        System.out.println("ANSWER GROUP CONTAINS:" + answerGroup.toString());

        //Create random object
        Random rand = new Random(); //Creates new random

        //Sets chosenAnswer to an index of the available pool of answers
        int chosenAnswer = rand.nextInt(answerGroup.size()); //gets an integer 0-2 with equal chances for each

        System.out.println("THE CHOSEN INDEX IS " + chosenAnswer);
        System.out.println("WHICH CORRESPONDS TO " + answerGroup.get(chosenAnswer));

        //Set answer index equal to the chosen answer
        answerIndex = answerGroup.get(chosenAnswer);

        System.out.println("REMOVING " + answerGroup.get(chosenAnswer));

        //Remove that answer from the next pool
        answerGroup.remove(chosenAnswer);

        System.out.println("THE ANSWER GROUP IS NOW " +  answerGroup.toString());
        System.out.println("THE ANSWER GROUP SIZE IS NOW " + answerGroup.size());
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
        DrawingView view = (DrawingView) findViewById(R.id.materialLen);
        DrawingView canvas = (DrawingView) findViewById(R.id.view);
        ImageView laserBox = (ImageView) findViewById(R.id.imgLaser);

        //Synchronizes environment constants with drawing view
        //necessary for labels to be drawn
        canvas.setEHeight((int)ENVIRONMENT_HEIGHT);
        canvas.setEWidth((int)ENVIRONMENT_WIDTH);

        //Activate the grid on the main view, and assign a starting x point
        canvas.setDrawGrid(true, laserBox.getX() + laserBox.getWidth(), canvas.getHeight());

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

                new AlertDialog.Builder(NIndex.this)
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

        ImageView[] views;      //Array of references to photodetector views

        views = new ImageView[4];
        TranslateAnimation animation;
        PointF end;
        Float xDelta;
        Float yDelta;

        views[0] = (ImageView) findViewById(R.id.rxDect1);
        views[1] = (ImageView) findViewById(R.id.rxDect2);
        views[2] = (ImageView) findViewById(R.id.rxDect3);
        views[3] = (ImageView) findViewById(R.id.rxDect4);

        //Initialize the lens object
        lens = new Lens(LensCraftMenu.lensArrayList.get(answerIndex));

        //Get the Lens holder from n_index.xml for measurments
        DrawingView materialLen = (DrawingView) findViewById(R.id.materialLen);
        DrawingView dv = (DrawingView) findViewById(R.id.view);

        //Assign the lens holder location to lens object
        lens.setLocation((int) materialLen.getX(), (int) materialLen.getY(),materialLen.getHeight(), materialLen.getWidth());

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
            answered = true;

            //Disable further interaction with button
            Button spin = (Button) findViewById(R.id.spinMaterial);
            spin.setClickable(false);

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
        //Capture drawing view
        DrawingView dv = (DrawingView) findViewById(R.id.view);
        DrawingView dl = (DrawingView) findViewById(R.id.materialLen);

        lens.setLocation((int)dl.getX(),(int) dl.getY(),dl.getHeight(),dl.getWidth());

        //New laser list
        lasers = new ArrayList<>();

        setUpLasers();

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
        DrawingView view = (DrawingView) findViewById(R.id.materialLen);
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
        views[0] = (ImageView) findViewById(R.id.rxDect1);
        views[1] = (ImageView) findViewById(R.id.rxDect2);
        views[2] = (ImageView) findViewById(R.id.rxDect3);
        views[3] = (ImageView) findViewById(R.id.rxDect4);

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
            new AlertDialog.Builder(NIndex.this)
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
                            startActivity(new Intent(NIndex.this, LensCraftMenu.class));
                        }
                    }).show(); //Shows created dialogue
        }
        //User is not correct
        else{
            //Incorrect Answer Alert Dialogue
            //Ask if the user would like to try again
            new AlertDialog.Builder(NIndex.this)
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
                            startActivity(new Intent(NIndex.this, LensCraftMenu.class));
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
                user.incCorrect();
                if(user.getLensLVL() < 3) {
                    user.setLensLVL(3);
                }
            }
            else{
                //User is wrong
                //Increment user's incorrect count
                user.incIncorrect();
            }

            user.saveUser("default.dat",getApplicationContext());
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
