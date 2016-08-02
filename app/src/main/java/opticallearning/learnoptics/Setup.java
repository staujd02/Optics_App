package opticallearning.learnoptics;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Joel on 7/13/2016.
 *
 * This class starts and manages the user setup process ran through the layout
 * setup.xml
 */
public class Setup extends Activity {

    private User user;                  //User object reference

    private final int COMPLETE = 1;     //int => User Completed all fields
    private final int NO_USERNAME = 2;  //int => User Failed to complete username field
    private final int NO_NAME = 3;      //int => User Failed to complete name field
    private final int NO_SCHOOL = 4;    //int => User opted not to fill school id field

    TextView txName;        //Textview holding user's name
    TextView txUsername;    //Textview holding user' username
    TextView txSchool;      //Textview holding user's school ID
    Spinner standing;       //Spinner indicating user's standing
    CheckBox ckHS;          //Checkbox indicating user's High School

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //
        setContentView(R.layout.setup);  //sets the content view
        setTitle("User Setup");             //Assigns specific title over default

        txName = (TextView) findViewById(R.id.txtName);
        txUsername = (TextView) findViewById(R.id.txtUsername);
        txSchool = (TextView) findViewById(R.id.txtSchool);
        standing = (Spinner) findViewById(R.id.spinStanding);
        ckHS = (CheckBox) findViewById(R.id.chkHS);
        user = MainActivity.user;   //Grabs user reference from MainActivity

        standing.setEnabled(false);  //Disable Spinner until user has interacted with school name

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.standings, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        standing.setAdapter(adapter);

        //When the user interacts with the school, enable the spinner and HS check box
        txSchool.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    standing.setEnabled(true);
                    ckHS.setEnabled(true);
                }
            }
        });

        Button b = (Button) findViewById(R.id.setupDone);   //Continue Button

        //Sets Continue's onClick() listener
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int go;         //Indication of user's progress
                String msg;     //Message of the dialog box (if applicable)
                String title;   //Title of the dialog box (if applicable)

                go = userComplete();    //Assigns variable to the response of function userComplete()

                //Interprets function's returned state
                switch (go){
                    case NO_USERNAME:
                        msg = "No Username: Please fill out this field";
                        title = "Missing Username";
                        break;
                    case NO_NAME:
                        msg = "No Name: Please fill out this field";
                        title = "Missing Name";
                        break;
                    case NO_SCHOOL:
                        msg = "Missing School ID: Continue?";
                        title = "No School";
                        break;
                    default:
                        msg = "An unknown error has occurred.";
                        title = "Error";
                        break;
                }

                //Starts the Main Menu if the user completed the field properly
                if(go == COMPLETE){
                    //Flips flag indicating the user has completed user setup
                    user.setSetupComplete(true);
                    //Saves user to file
                    user.saveUser("default.dat", getApplicationContext());
                    //Takes user to main menu
                    startActivity(new Intent(Setup.this, MainActivity.class));
                }
                //If the user opted not to fill the school ID slot, it asks for verification
                else if(go == NO_SCHOOL){
                    new AlertDialog.Builder(Setup.this)
                            .setTitle(title) //Title of the dialogue
                            .setMessage(msg) //Error message or warning
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //If the user pressed yes, it takes the user to the main menu

                                    //Set user information as final
                                    user.setName(txName.getText().toString());
                                    user.setSchool("Missing");
                                    user.setUserName(txUsername.getText().toString());
                                    user.setStanding(-1); user.setHS(false);
                                    user.setUUID(getApplicationContext());

                                    //Flips boolean indicating user has completed setup process
                                    user.setSetupComplete(true);

                                    //Saves user
                                    user.saveUser("default.dat",getApplicationContext());

                                    //Goes to main menu
                                    startActivity(new Intent(Setup.this, MainActivity.class));
                                }
                            })
                            .setNegativeButton(android.R.string.no,null)
                            .create().show(); //Shows created dialogue
                }
                //The user has failed to complete a field, notifies user
                else {
                    new AlertDialog.Builder(Setup.this)
                            .setTitle(title) //Title of the dialogue
                            .setMessage(msg) //Error message or warning
                            .setPositiveButton(android.R.string.ok,null)
                            .create().show(); //Shows created dialogue
                }
            }
        });


    }

    /**
     * Simple sub function that checks to see all field are filled and completed
     *
     * @return the state of the user completeness (whether the user has completed all fields,
     * failed to complete a field, or opted from answering the school question)
     */
    private int userComplete(){
        if(txName.getText().length() == 0){
            return NO_NAME;
        }
        else if(txUsername.getText().length() == 0){
            return  NO_USERNAME;
        }
        else if(txSchool.getText().length() == 0){
            return NO_SCHOOL;
        }
        else{
            user.setName(txName.getText().toString());
            user.setSchool(txSchool.getText().toString());
            user.setUserName(txUsername.getText().toString());
            user.setHS(ckHS.isChecked());
            user.setStanding(standing.getSelectedItemPosition());
            user.setUUID(getApplicationContext());

            return COMPLETE;
        }
    }
}
