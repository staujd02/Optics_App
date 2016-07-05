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
 * In this module, the user attempts to select the lens with the correct width
 * to focus/diffuse the laser.
 */
public class LensWidth extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //call the super constructor
        setContentView(R.layout.lens_width);//sets the view
        setTitle("Lens Width"); //Assigns a descriptive title

        //Creates a spinner object and references spinWidth in lens_width.xml
        Spinner spinner = (Spinner) findViewById(R.id.spinWidth);
        //Creates adapter to load array lens_widths into the spinner
        //lens_widths > {"Thin","Normal","Thick"}
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.lens_widths, android.R.layout.simple_spinner_item);
        //Assigns dropdown behaviour to adapter
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Assigns adapter to spinner
        spinner.setAdapter(adapter);

        //todo check if hints are enable or not

        //Creates a Hint dialog for instructing the user what to do
        new AlertDialog.Builder(LensWidth.this)
                //Sets title of dialog
                .setTitle("Directions")
                //Sets message
                .setMessage("Select the correct lens thickness to focus the light on the photodetectors.")
                //Creates OK button for the user to dismiss the dialog
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //User pressed yes
                    }
                })
                //Shows the dialog
                .show();


    }
}
