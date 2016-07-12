package opticallearning.learnoptics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by Joel on 7/1/2016.
 *
 * This is the background activity for Lens Crafter Module
 *
 *  It provides the user a basic overview of the concepts
 *   involved with this module and its sub-modules
 */
public class BGLensCraft extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     //Call the super constructor
        setContentView(R.layout.b_lenscraft);   //Set the content view to background lenscraft .xml
        setTitle("Background: Lens Crafter");   //Set the title

        //Continue button
        Button b = (Button) findViewById(R.id.btnLens_c);

        //Assign the continue button listener
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start the Lens Crafter menu after the user touches continue
                startActivity(new Intent(BGLensCraft.this, LensCraftMenu.class));
            }
        });
    }
}
