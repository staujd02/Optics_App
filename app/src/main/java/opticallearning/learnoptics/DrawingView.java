package opticallearning.learnoptics;

import android.content.Context;
import android.graphics.*;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;

/**
 * Created by Joel on 6/29/2016.
 *
 * Construction class for custom view DrawingView
 *
 * This view is intended for drawing the lenses bitmaps and the behaviour
 * of lasers interacting with them.
 *
 * The view is dual purpose.
 *
 *      Either it is a lens drawing view
 *          or
 *      It is a background/laser drawing view
 *
 * This view is custom class used for the laser and lens graphics.
 *
 * The lens instances and laser instances of this view could be combined with some work and
 * a few extra routines to create a master view. Rather than individual views of a lens or lasers.
 */
public class DrawingView extends View{

    //Create paint object for drawing graphics
    private Paint paint = new Paint();
    private Paint font_paint;
    private ArrayList<Laser> lasers;//List of lasers to be rendered
    private Lens lens;              //Lens object to be rendered
    private Bitmap bm;              //Loaded bitmap of lenses

    final protected int pixelOFFSET = 6;          //How much padding should be added to printed label
    final protected int LINE_FREQUENCY = 20;

    private int environment_height = -1; //Used for calculating label output
    private int environment_width = -1;  //Used for calculating label output

    private float startX;           //X offset where the grid starts
    private float startY;           //Y offset where the grid starts
    private boolean drawGrid;       //Whether or not to render a grid on this view

    private Rect rect;              //Rect output for drawing lens
    private Rect scr;               //Source rect from lens data base

    /**
     * First constructor and simplest constructor
     * that can be called
     *
     * @param context required for super constructor
     */
    public DrawingView(Context context)
    {
        super(context);
        init();
    }

    /**
     * Second constructor, usually the one called by
     * the android system
     *
     * @param context required for super constructor
     * @param attrs required for super constructor
     */
    public DrawingView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    /**
     * Most specific constructor
     *
     * @param context required for super constructor
     * @param attrs required for super constructor
     * @param defStyle required for super constructor
     */
    public DrawingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * All constructors are directed here to ensure
     * all objects are loaded and all tasks are completed
     */
    private void init(){
        setFocusable(true);             //Must be true for user interaction
        setFocusableInTouchMode(true);  //Must be true for user interaction
        setWillNotDraw(false);          //Must be false to drawn on view
        BitmapFactory.Options opts = new BitmapFactory.Options();

        opts.inScaled = false;

        //Load up bitmap for use later
        bm = BitmapFactory.decodeResource(getResources(), R.drawable.lens, opts);

        //Allocate space for two rectangles
        rect = new Rect();
        scr = new Rect();

        //Intialize the laser list
        lasers = new ArrayList<>();

        //Set the defaults for the paint object
        font_paint = new Paint();
        font_paint.setColor(Color.BLACK);

        //Creates a fuzzier, more attractive draw --> edges not as clean/clear
        paint.setAntiAlias(true);
    }

    /**
     * This class resets all of the global objects
     * that should change on a standard run of a lens craft activity.
     *
     * It also invalidates the view to ensure the lens/lasers are cleaned from the view
     *
     */
    public void reset(){
        lens = null;
        lasers = new ArrayList<>();
        rect = new Rect();
        scr = new Rect();
        invalidate();
    }

    /**
     * This routine sets the lens to the lens View and then
     * invalidates the drawing view to ensure the lens is drawn immediately
     *
     * @param lens the lens object to be drawn
     */
    public void drawLens(Lens lens){
        this.lens = lens;
        invalidate();
    }

    /**
     * Sets the passed array list of lasers to the global laser array list.
     *
     * A call to this class will immediately render the lasers. Assuming the passed value is
     * not null.
     *
     * @param lasers the arrayList of laser to be drawn/rendered
     */
    public void drawLasers(ArrayList<Laser> lasers){
        this.lasers = lasers;
        invalidate();
    }

    /**
     * Renders all of the non-null objects to the view
     *
     * @param canvas reference variable used to draw objects
     */
    @Override
     protected void onDraw(Canvas canvas) {
         super.onDraw(canvas); //Draws everything defined in the .xml parameters

        double interval;    //interval spacing of the lasers
        boolean draw;       //Loop flag used for drawing lines
        boolean label = false; //indicates when labels are to be drawn on the lines
        float x = startX;   //X of the current graph line (loop variable)
        float y = startY;   //Y of the current graph line (loop variable)
        int y_units;        //Converted y unit from pixels to dimensionless unit
        int x_units;        //Converted x unit from pixels to dimensionless unit

        //Sets the paint variables to draw laser lines
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3f);

        //Sets paint for drawing labels to a size relative to the view's size
        font_paint.setTextSize((float) (getHeight()/(environment_height*0.25)));

        //renders lasers if the array list is not null
        if(lasers != null){
            for(Laser l: lasers){
                //Draws each calculated segment of the laser object
                for(PointF[] f: l.getSegments()){
                    //If f is null, then the laser never ran its calculation routine
                    if(f != null){
                        //Draws laser segment point for point
                        canvas.drawLine(f[0].x, f[0].y, f[1].x, f[1].y, paint);
                    }
                }
            }
        }

        //If the lens object is not null, the lens is rendered
        if(lens != null){
            //Destination Rectangle
            rect.set(0,0,getWidth(),getHeight());

            //Source Rectangle
            scr.set(lens.getGraphic_Reference());

            //.drawBitmap(Bitmap bm, Source_Rectangle scr, Destination_Rectangle rect, Paint paint)
            canvas.drawBitmap(bm,scr,rect,paint);
        }

        //If the drawGrid is true, a grid of horizontal and vertical lines are drawn in a graph-like form
        if(drawGrid){
            //Divide the spacing interval equally between each line
            interval = getWidth() / LINE_FREQUENCY;

            //Set the paint properties of the respective lines
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(1f);

            //grab values of the height and width of the view
            double height = getHeight();
            double width = getWidth();

            //Set the draw flag to true
            draw = true;

            //Only label if the environment width and height are not equal to default values
            if(environment_width != -1 || environment_height != -1){
                label = true;
            }

            //Draw vertical lines
            while(draw){
                //Draw
                canvas.drawLine(x,0,x,(float) height, paint);

                //Calculate the line's unit label value from pixels
                x_units = (int) Math.round((x-startX) * (environment_width/(1.0*width)));

                //Draw line label
                if(label){
                    canvas.drawText(x_units + "", x,(float) (height - pixelOFFSET),font_paint);
                }

                //Increment x variable
                x += interval;

                //if x exceeds view width, stop drawing
                if(x > width){
                    draw = false;
                }
            }

            //Set interval
            interval = height / LINE_FREQUENCY;

            //Reset loop flag
            draw = true;

            //Draw Horizontal lines
            while(draw){
                //Draw line
                //-------------
                //Y value starts at the bottom of the screen and works to represent a standard graph
                canvas.drawLine(startX,y,(float) width,y,paint);

                //Calculate the line's unit label value from pixels
                y_units = (int) Math.round((height - y) * (environment_height/(1.0*height)));

                //Draw line label
                if(label){
                    canvas.drawText(y_units + "", (float) (width - pixelOFFSET - font_paint.getTextSize() ),y,font_paint);
                }

                //Decrement y value
                y -= interval;

                //Stop drawing if y value is less than 0
                if (y < 0){
                    draw = false;
                }
            }



        }


     }

    /**
     * This function is a setter, but it ensures that all neccassary
     *
     *
     * @param drawGrid boolen indicating whether the draw cycle should create a grid
     * @param startX  starting x of the grid
     * @param startY  starting y of the grid (*Note should be the y maximum - the graph is drawn
     *                from the bottom up)
     */
    public void setDrawGrid(boolean drawGrid, float startX, float startY) {
        this.drawGrid = drawGrid;
        this.startX = startX;
        this.startY = startY;
        invalidate();
    }

    //Set the Environment variables; These value must be set for labels to be drawn
    public void setEHeight(int height){ this.environment_height = height;}
    public void setEWidth(int width){ this.environment_width = width;}

    //Getter
    public float getStartX() {
        return startX;
    }
}
