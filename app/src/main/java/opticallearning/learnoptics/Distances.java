package opticallearning.learnoptics;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
        final Button spinner = (Button) findViewById(R.id.spinDistance);
        spinner.setText("Pick Distance");

        //Creates array adapter for loading string array into the spinner,
        //distances is in strings.xml --> {"Short", "Medium", "Far"}
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.distances, android.R.layout.simple_spinner_item);

        //Assign drop down behaviour
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Create onClickListener
        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create Dialog for the user to pick a lens
                new AlertDialog.Builder(Distances.this)
                        //Set title and the adapter created above
                        .setTitle("Pick Distance")
                        .setAdapter(adapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Grabs array of choices
                                String[] s = getResources().getStringArray(R.array.distances);
                                //Sets the user's choice as the button's text
                                spinner.setText(s[which]);

                                //Ends the dialog
                                dialog.dismiss();
                            }
                            //creates dialog object and displays it
                        }).create().show();
            }
        });

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
