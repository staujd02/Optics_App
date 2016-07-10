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
public class DrawingView extends View implements View.OnTouchListener{

    //Create paint object for drawing graphics
    Paint paint = new Paint();
    ArrayList<Laser> lasers;
    Lens lens;

    /**
     * First constructor and simplest constructor
     * that can be called
     *
     * @param context
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
     * @param context
     * @param attrs
     */
    public DrawingView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    /**
     * Most specific constructor
     *
     * @param context
     * @param attrs
     * @param defStyle
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

        lasers = new ArrayList<>();

        //Assign the onTouchListener, defined below, to the object's own defined onTouchListener
        this.setOnTouchListener(this);

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
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.lens);
            Rect rect = new Rect(0,0,getMeasuredWidth(),getMeasuredHeight());
            Rect scr = new Rect(lens.getGraphic_Reference());

            canvas.drawBitmap(bm,scr,rect,paint);
        }


     }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //Might use this method...
        return false;
    }

    //public void drawLasers(Laser[] lasers){


    //invalidate(); //Forces refresh of the drawing
    //}

    //public void drawLens(Lens lens){


    //Refresh Canvas
    //invalidate(); //Forces refresh of the drawing
    //}
}
