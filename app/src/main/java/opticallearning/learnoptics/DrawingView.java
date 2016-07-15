package opticallearning.learnoptics;

import android.content.Context;
import android.graphics.*;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

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
    private ArrayList<Laser> lasers;
    private Lens lens;
    private Bitmap bm;

    private float startX;
    private float startY;
    private boolean drawGrid;

    private Rect rect;
    private Rect scr;

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
    public void init(){
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

        //Creates a fuzzier, more attractive draw --> edges not as clean/clear
        paint.setAntiAlias(true);
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
        float x = startX;
        float y = startY;

        //Use this for drawing lasers
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3f);

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
            interval = getWidth() / 20.0;

            System.out.println("Interval: " + interval);
            System.out.println("Width: " + getWidth());

            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(1f);

            double height = getHeight();
            double width = getWidth();

            draw = true;

            while(draw){
                canvas.drawLine(x,0,x,(float) height, paint);
                x += interval;

                if(x > width){
                    draw = false;
                }
            }

            interval = getHeight() / 20.0;
            draw = true;

            while(draw){
                canvas.drawLine(startX,y,(float) width,y,paint);
                y -= interval;

                System.out.println("Start X:" + startX);

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

    public float getStartY() {
        return startY;
    }

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
