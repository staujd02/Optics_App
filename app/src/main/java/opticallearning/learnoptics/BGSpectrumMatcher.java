package opticallearning.learnoptics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Joel on 7/1/2016.
 *
 * This loads the xml activity background for spectrum matcher.
 *
 * This xml provides the basic background of concepts to the user. This xml explains
 * what the spectrum is and how to interpret absorbency and reflectance graphs. The descriptive string
 * is stored in string.xml.
 *
 */
public class BGSpectrumMatcher extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);         //calls super constructor
        setContentView(R.layout.background);        //Sets the view
        setTitle("Background: Spectrum Matcher");   //Assigns a title

        //Continue Button
        Button b = (Button) findViewById(R.id.btnContinue);
        TextView text = (TextView) findViewById(R.id.txtDescription);

        text.setText(getResources().getString(R.string.bg_spec));

        //Sends the user on to the menu
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Starts the Spectrum menu
                startActivity(new Intent(BGSpectrumMatcher.this, MainActivity.class));
            }
        });
    }
}
