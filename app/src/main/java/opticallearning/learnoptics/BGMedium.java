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
 * This class loads the Background xml for "Rank the Medium" module
 *
 * The xml will have a text block that explains the basic concepts behind N index and
 * and how it affects light to the user.
 */

public class BGMedium extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        //Calls the super constructor *Important*
        setContentView(R.layout.background);
        setTitle("Background: Rank the Medium"); //Changes the title from default "Learn Optics"

        //Create the continue button
        Button b = (Button) findViewById(R.id.btnContinue);
        TextView text = (TextView) findViewById(R.id.txtDescription);

        text.setText(getResources().getString(R.string.bg_medium));

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
