package opticallearning.learnoptics;

import android.content.Context;
import android.graphics.*;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Joel on 6/29/2016.
 */
public class DrawingView extends View implements View.OnTouchListener{

    Paint paint = new Paint();

    Boolean lens;


    public DrawingView(Context context)
    {
        super(context);
        init();
    }
    public DrawingView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }
    public DrawingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    public void init(){
        setFocusable(true);
        setFocusableInTouchMode(true);
        setWillNotDraw(false);

        this.setOnTouchListener(this);

        paint.setAntiAlias(true);
    }

     protected void onDraw(Canvas canvas) {
         super.onDraw(canvas);

         //Use this for drawing lasers
         paint.setColor(Color.RED);
         paint.setStrokeWidth(3f);
         //canvas.drawLine(0.0f, 0.0f, 15f, 15f, paint);
     }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return false;
    }

    //public void drawLasers(Laser[] lasers){


    //invalidate();
    //}

    //public void drawLens(Lens lens){


    //Refresh Canvas
    //}
}
