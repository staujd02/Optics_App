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
 * This module asks the user to select the correct material for the lens
 *
 */
public class NIndex extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //Calls super constructor
        setContentView(R.layout.n_index);   //sets the view
        setTitle("Index of Refraction");    //assigns descriptive title

        //Creates spinner object and assigns it to spinMaterial in n_index.xml
        Spinner spinner = (Spinner) findViewById(R.id.spinMaterial);
        //Creates Adapter for reading the array materials into spinMaterials
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.materials, android.R.layout.simple_spinner_item);
        //Assigns dropdown behaviour
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Assigns the adapter to the spinner
        spinner.setAdapter(adapter);


        //todo check if hints are enable or not

        //Creates a dialogue for giving instructions to the user
        new AlertDialog.Builder(NIndex.this)
                //Sets the title of the dialog
                .setTitle("Directions")
                //Sets the message of the dialog
                .setMessage("Select the correct N index to focus the light on the photodetectors.")
                //Creates an OK button for the user to dismiss the dialog
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //User pressed yes
                    }
                })
                .show();

    }
}
