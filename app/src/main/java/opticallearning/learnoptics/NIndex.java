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
 * This module asks the user to select the correct material for the lens
 *
 */
public class NIndex extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //Calls super constructor
        setContentView(R.layout.n_index);   //sets the view
        setTitle("Index of Refraction");    //assigns descriptive title

        //Creates spinner object and assigns it to spinMaterial in n_index.xml
        final Button spinner = (Button) findViewById(R.id.spinMaterial);
        spinner.setText("Pick Lens Material");

        //Creates Adapter for reading the array materials into spinMaterials
        //spinMaterial > {"Acetone Low N","Crown Glass Med N","Flint Glass High N"}
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.materials, android.R.layout.simple_spinner_item);

        //Assigns dropdown behaviour
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Create onClickListener
        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create Dialog for the user to pick a lens
                new AlertDialog.Builder(NIndex.this)
                        //Set title and the adapter created above
                        .setTitle("Pick Lens Material")
                        .setAdapter(adapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Grabs array of choices
                                String[] s = getResources().getStringArray(R.array.materials);
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
