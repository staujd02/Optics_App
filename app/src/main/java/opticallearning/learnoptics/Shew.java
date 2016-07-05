package opticallearning.learnoptics;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by Joel on 7/1/2016.
 *
 * This is a branching activity of Lens Crafter menu (LensCraftMenu.java)
 *
 * The user should select how to 'shew' the lens up, down, or in the middle to refract the laser
 * towards the photodetectors
 *
 */
public class Shew extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //call super constructor
        setContentView(R.layout.skew);  //sets the view
        setTitle("Lens Height");        //assigns descriptive title

        //Creates spinner object and assigns it to spinSkew in skew.xml
        Spinner spinner = (Spinner) findViewById(R.id.spinSkew);
        //Creates array adapter for reading skew[] into spinSkew
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.skew, android.R.layout.simple_spinner_item);
        //Assigns dropdown behaviour of adapter
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //assigns adapter to spinner
        spinner.setAdapter(adapter);

        //todo check if hints are enable or not

        //Provides the user a hint on how they should proceed
        new AlertDialog.Builder(Shew.this)
                //Sets the dialog's title
                .setTitle("Directions")
                //sets the dialog's message
                .setMessage("Select the correct height of the lens to focus the light onto the photoreceptors.")
                //Creates an OK button for the user to dismiss the dialog
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //User pressed yes
                    }
                })
                .show();
                //.setIcon(android.R.drawable.ic_dialog_alert)
                //.show();
        //End of Dialogue


    }
}
