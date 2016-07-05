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
 * This loads the xml activity background for spectrum matcher.
 *
 * This xml provides the basic background of concepts to the user. This xml explains
 * what the spectrum is and how to interpret absorbance and reflectance graphs.
 *
 */
public class BGSpectrumMatcher extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //super constructor
        setContentView(R.layout.b_spectrummatcher); //Set the view
        setTitle("Background: Spectrum Matcher");   //Assign a title

        //Continue Button
        Button b = (Button) findViewById(R.id.btnSpec_c);

        //Sends the user on to the menu
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Starts the Spectrum menu
                startActivity(new Intent(BGSpectrumMatcher.this, SpectrumMenu.class));
            }
        });
    }
}
