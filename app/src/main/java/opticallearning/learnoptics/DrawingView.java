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

        lasers = new ArrayList<>();

        font_paint = new Paint();
        font_paint.setColor(Color.BLACK);

        //Creates a fuzzier, more attractive draw --> edges not as clean/clear
        paint.setAntiAlias(true);
    }

    public void reset(){
        init();
    }

    public void drawLens(Lens lens){
        this.lens = lens;
        invalidate();
    }

    public void drawLasers(ArrayList<Laser> lasers){
        this.lasers = lasers;
        invalidate();
    }

    @Override
     protected void onDraw(Canvas canvas) {
         super.onDraw(canvas); //Draws everything defined in the .xml parameters

        double interval;
        boolean draw;
        boolean label = false;
        float x = startX;
        float y = startY;
        int y_units;
        int x_units;

        //Use this for drawing lasers
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3f);

        font_paint.setTextSize((float) (getHeight()/(environment_height*0.25)));

        if(lasers != null){
            for(Laser l: lasers){
                ArrayList<PointF[]> points = l.getSegments();
                for(PointF[] f: points){
                    if(f != null){
                        canvas.drawLine(f[0].x, f[0].y, f[1].x, f[1].y, paint);
                    }
                }
            }
        }

        if(lens != null){
            rect.set(0,0,getWidth(),getHeight());
            scr.set(lens.getGraphic_Reference());

            canvas.drawBitmap(bm,scr,rect,paint);
        }

        if(drawGrid){
            interval = getWidth() / LINE_FREQUENCY;

            System.out.println("Interval: " + interval);
            System.out.println("Width: " + getWidth());

            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(1f);

            double height = getHeight();
            double width = getWidth();

            draw = true;

            if(environment_width != -1 || environment_height != -1){
                label = true;
            }

            while(draw){
                canvas.drawLine(x,0,x,(float) height, paint);

                x_units = (int) Math.round((x-startX) * (environment_width/(1.0*width)));

                if(label){
                    canvas.drawText(x_units + "", x,(float) (height - pixelOFFSET),font_paint);
                }

                x += interval;

                if(x > width){
                    draw = false;
                }
            }

            interval = height / LINE_FREQUENCY;
            draw = true;

            while(draw){
                canvas.drawLine(startX,y,(float) width,y,paint);

                y_units = (int) Math.round((height - y) * (environment_height/(1.0*height)));

                if(label){
                    canvas.drawText(y_units + "", (float) (width - pixelOFFSET - font_paint.getTextSize() ),y,font_paint);
                }

                y -= interval;

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

    //public float getStartY() {return startY;}

    public void setEHeight(int height){ this.environment_height = height;}
    public void setEWidth(int width){ this.environment_width = width;}

    public float getStartX() {
        return startX;
    }

    //public void drawLasers(Laser[] lasers){


    //invalidate(); //Forces refresh of the drawing
    //}

    //public void drawLens(Lens lens){


    //Refresh Canvas
    //invalidate(); //Forces refresh of the drawing
    //}
}
