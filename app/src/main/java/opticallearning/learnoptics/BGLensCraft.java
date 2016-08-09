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
 * This is the background activity for Lens Crafter Module
 *
 * This activity provides the user a basic overview of the concepts
 * involved with this module and its sub-modules. The description string is stored in strings.xml
 */
public class BGLensCraft extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     //Call the super constructor
        setContentView(R.layout.background);   //Set the content view to background background.xml
        setTitle("Background: Lens Crafter");   //Set the title

        //Continue button
        Button b = (Button) findViewById(R.id.btnContinue);
        TextView text = (TextView) findViewById(R.id.txtDescription);

        text.setText(getResources().getString(R.string.bg_lens));

        //Assign the continue button listener that
        //Sends the user to the Lens Crafter main menu
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Starts the Lens Crafter menu on click
                startActivity(new Intent(BGLensCraft.this, LensCraftMenu.class));
            }
        });
    }
}
