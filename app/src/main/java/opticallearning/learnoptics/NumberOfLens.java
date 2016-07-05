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
 * The user should select the correct number of lens of a certain type
 * to use.
 */
public class NumberOfLens extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //call super constructor
        setContentView(R.layout.number_of_lens);    //sets the view
        setTitle("Number of Lens");         //assigns descriptive title

        //Creates spinner object and assigns it to spinCount in number_of_lens.xml
        Spinner spinner = (Spinner) findViewById(R.id.spinCount);
        //Creates array adapter for reading array lens_Nums into the spinner
        //lens_Nums = {"1","2","3"}
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.lens_Nums, android.R.layout.simple_spinner_item);
        //Assigns dropdown behaviour to spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Assigns the adapter to the spinner
        spinner.setAdapter(adapter);

        //todo check if hints are enable or not

        //Creates an Alert Dialogue for giving the user directions
        new AlertDialog.Builder(NumberOfLens.this)
                //Sets the title of the dialog
                .setTitle("Directions")
                //Sets the message of the dialog
                .setMessage("Pick the correct number of lens to focus the light on the photodetector.")
                //Creates an OK button for the user to dismiss the dialog
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //User pressed yes
                    }
                })
                .show();


    }
}
