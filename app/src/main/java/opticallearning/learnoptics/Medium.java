package opticallearning.learnoptics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Joel on 6/27/2016.
 *
 * This module is designed for the user to rank objects by their N index
 *
 * This module is active but not going to be implemented this way upon release
 *
 */
public class Medium extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //calls super constructor
        setContentView(R.layout.medium);    //sets the view
        setTitle("Rank the Medium");    //assigns descriptive title

        //Creates array of image tiles
        ImageView[] tiles = new ImageView[4];

        //assigns reference values to each of the indices to their respective tiles
        tiles[0] = (ImageView) findViewById(R.id.tileOne);
        tiles[1] = (ImageView) findViewById(R.id.tileTwo);
        tiles[2] = (ImageView) findViewById(R.id.tileThree);
        tiles[3] = (ImageView) findViewById(R.id.tileFour);


        //Possible sources of implementing drag/drop behaviour
        //----------------------------------------------------
        //https://github.com/askerov/DynamicGrid
        //https://github.com/h6ah4i/android-advancedrecyclerview
        //https://github.com/koduribalaji/AndroidCoolDragAndDropGridView
        //----------------------------------------------------

        for(ImageView til: tiles){

            til.setOnTouchListener(new View.OnTouchListener() {
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

            til.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    //Capture Finger Location
                    //Draw image where the finger is
                    return false;
                }
            });


        }

    }

    @Override
    public void onBackPressed() {
        //your code when back button pressed
        startActivity(new Intent(Medium.this, MainActivity.class));
    }
}
