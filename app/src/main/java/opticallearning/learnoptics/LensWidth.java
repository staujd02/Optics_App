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
 * In this module, the user attempts to select the lens with the correct width
 * to focus/diffuse the laser.
 */
public class LensWidth extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //call the super constructor
        setContentView(R.layout.lens_width);//sets the view
        setTitle("Lens Width"); //Assigns a descriptive title

        //Creates a spinner object and references spinWidth in lens_width.xml
        final Button spinner = (Button) findViewById(R.id.spinWidth);
        spinner.setText("Pick Lens Width");

        //Creates adapter to load array lens_widths into the spinner
        //lens_widths > {"Thin","Normal","Thick"}
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.lens_widths, android.R.layout.simple_spinner_item);

        //Assigns dropdown behaviour to adapter
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Create onClickListener
        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create Dialog for the user to pick a lens
                new AlertDialog.Builder(LensWidth.this)
                        //Set title and the adapter created above
                        .setTitle("Pick Lens Width")
                        .setAdapter(adapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Grabs array of choices
                                String[] s = getResources().getStringArray(R.array.lens_widths);
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
