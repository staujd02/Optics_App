package opticallearning.learnoptics;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by Joel on 7/1/2016.
 */
public class Shew extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skew);
        setTitle("Number of Beams");

        Spinner spinner = (Spinner) findViewById(R.id.spinBeamCount);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.skew, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        new AlertDialog.Builder(Shew.this)
                .setTitle("Directions")
                .setMessage("Select the correct height of the lens to focus the light onto the photoreceptors.")
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
