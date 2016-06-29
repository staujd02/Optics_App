package opticallearning.learnoptics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Joel on 6/29/2016.
 */
public class DrawingView extends View {

    Paint paint;

    Boolean lens;

    public DrawingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setWillNotDraw(false);

        paint = new Paint();
    }

     protected void onDraw(Canvas canvas) {
         super.onDraw(canvas);

         //Use this for drawing lasers
         paint.setColor(Color.RED);
         paint.setStrokeWidth(3f);
         //canvas.drawLine(0.0f, 0.0f, 15f, 15f, paint);
     }

    //public void drawLasers(Laser[] lasers){


    //Refresh Canvas
    //}

    //public void drawLens(Lens lens){


    //Refresh Canvas
    //}
}
