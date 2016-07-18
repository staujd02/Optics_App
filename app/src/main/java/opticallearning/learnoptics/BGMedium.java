package opticallearning.learnoptics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Joel on 7/1/2016.
 *
 * This class loads the Background xml for "Rank the Medium" module
 *
 * The xml has a text block that explains the basic concepts behind N index and
 * and how it affects light to the user.
 */

public class BGMedium extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //Call the super constructor *Important*
        setContentView(R.layout.b_medium);
        setTitle("Background: Rank the Medium"); //Change the title from default "Learn Optics"

        //Create the continue button
        Button b = (Button) findViewById(R.id.btnMedium_c);

        //Sends the user to Rank the Medium activity
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Sends the user to Medium.class --> Rank the Medium
                startActivity(new Intent(BGMedium.this, MainActivity.class));
            }
        });
    }
}
