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
 * Asks users to select the correct distance from the lens to the laser
 *
 */
public class Distances extends Activity {

    /**
     *
     * Creates the Activity and loads initial objects
     *
     * @param savedInstanceState used by the super constructor
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //Calls super constructor
        setContentView(R.layout.distance);  //sets the content view
        setTitle("Lens Distance");          //Assigns specific title over default

        //Creates spinner object and sets the reference to spinner spinDistance in distance.xml
        Spinner spinner = (Spinner) findViewById(R.id.spinDistance);

        //Creates array adapter for loading string array into the spinner,
        //distances is in strings.xml --> {"Short", "Medium", "Long"}
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.distances, android.R.layout.simple_spinner_item);
        //Assign drop down behaviour
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Assigns created adapter to spinner
        spinner.setAdapter(adapter);

        //todo check if hints are enable or not

        //Creates an AlertDialog to provide instructions to the user
        new AlertDialog.Builder(Distances.this)
                //Sets the title of the dialog
                .setTitle("Directions")
                //Sets the message of the dialog
                .setMessage("Select the correct distance to place the lens from the laser to focus the light on the photodetectors.")
                //Creates OK button for the user to dismiss the dialog
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //User pressed yes
                    }
                })
                //Displays dialog to the user
                .show();


    }
}
