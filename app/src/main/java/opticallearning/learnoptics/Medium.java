package opticallearning.learnoptics;

import android.app.Activity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Joel on 6/27/2016.
 */
public class Medium extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medium);

        ImageView[] tiles = new ImageView[4];

        tiles[0] = (ImageView) findViewById(R.id.tileOne);
        tiles[1] = (ImageView) findViewById(R.id.tileTwo);
        tiles[2] = (ImageView) findViewById(R.id.tileThree);
        tiles[3] = (ImageView) findViewById(R.id.tileFour);

        for(int i = 0; i < tiles.length; i++){

            tiles[i].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                        //Touch Down
                        //----------
                        //Swap image with dummy image
                        //  - set imageView scr to dummy image
                        //Draw image where finger is
                        //  - use object's image to draw at finger's coordinates
                        //  -  use offset of where the user touched > save to variable > draw
                    } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                        //Touch Up
                        //----------
                        //Swap with holder variable
                        // - use holder variable to know which object to switch with
                        // - swap this object's image into the other object's current image slot
                        // - replace this object's image into this object's image slot
                    }

                    return false;
                }
            });

            tiles[i].setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    //Capture Finger Location
                    //Draw image where the finger is
                    return false;
                }
            });


        }

    }
}
