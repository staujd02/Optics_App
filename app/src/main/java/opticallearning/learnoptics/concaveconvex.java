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
 * Asks the user to choose between a concave lens and a convex lens
 *
 */
public class ConcaveConvex extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //call super constructor
        setContentView(R.layout.concaveconvex); //set the view
        setTitle("Concave vs. Convex"); //Assign title

        //Create spinner object and connect to spinCC in concaveconvex.xml
        Spinner spinner = (Spinner) findViewById(R.id.spinCC);

        //Assign to string array lensTypes {"Pick One", "Concave", "Convex"}
        //using an adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.lensTypes, android.R.layout.simple_spinner_item);
        //Sets the drop down view type
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Assigns the created adapter to spinner
        spinner.setAdapter(adapter);

        //todo check if hints are enable or not

        //Directions Alert Dialogue
        new AlertDialog.Builder(ConcaveConvex.this)
                .setTitle("Directions") //Sets the title of the dialogue
                .setMessage("Pick the lens type, concave or convex, to direct the laser to the photodetectors.") //Sets the Message
                //Creates OK button for user interaction (Dismisses Dialogue)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //User pressed yes
                    }
                })
                .show(); //Shows created dialogue

    }
}
