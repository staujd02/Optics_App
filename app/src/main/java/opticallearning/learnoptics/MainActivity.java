package opticallearning.learnoptics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * This is the first activity called by the Android OS
 *
 * This loads the main menu, databases, and user info
 *
 * This class first checks if the current User reference variable is null, if so, the class
 * attempts to load the user from a file using the default filename. If that attempt fails, it creates
 * a default user and will then send the user through the setup process until the user has completed
 * the process.
 *
 * The class then assigns onClick listeners to each of the buttons so the user is sent to the
 * correct corresponding activity.
 *
 */
public class MainActivity extends Activity {

    static protected User user;
    static protected String user_filename = "default.dat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //calls the super constructor
        setContentView(R.layout.activity_main); //sets the layout
        setTitle("Learn Optics");   //Assigns a descriptive title to the title bar

        //Check if user has been loaded
        if(user == null){
            //Load user
            user = User.loadUser(user_filename, getApplicationContext());
        }

        //Verify User
        if(user == null){
            //If the reference is null, create default user
            user = new User("Galileo");
            user.saveUser(user_filename, this.getApplicationContext());
        }

        //Send user through user creation process if setup isn't complete
        if(!user.isSetupComplete()){startActivity(new Intent(MainActivity.this, Setup.class));}

        //Create image buttons
        ImageButton[] buttons = {
                (ImageButton) findViewById(R.id.btnMedium),         //0
                (ImageButton) findViewById(R.id.btnSpectrum),       //1
                (ImageButton) findViewById(R.id.btnLensCraft),      //2
                (ImageButton) findViewById(R.id.btnUserMenu),       //3
                (ImageButton) findViewById(R.id.btnCredits),        //4
                (ImageButton) findViewById(R.id.btnPortal)          //5
        };

        //Assign onClickListeners to each button
        //--> Takes the user to the depicted module
        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            //Pick the Medium
            public void onClick(View v) {
                if(user.getBackground()) {
                    //This is the background information activity that should
                    //introduce the user to what the following module is all about
                    startActivity(new Intent(MainActivity.this, BGMedium.class));
                }
                else{
                    //HEY LOOK HERE
                    //This is referencing a holder class at the moment, change this
                    //when the actual menu/activity becomes available
                    //------------------
                    //The user should be able to skip the background information if they
                    //choose to do so
                    startActivity(new Intent(MainActivity.this, BGMedium.class));
                }
            }
        });

        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            //Spectrum Matcher
            public void onClick(View v) {
                if(user.getBackground()) {
                    //This is the background information activity that should
                    //introduce the user to what the following module is all about
                    startActivity(new Intent(MainActivity.this, BGSpectrumMatcher.class));
                }
                else{
                    //HEY LOOK HERE
                    //This is referencing a holder class at the moment, change this
                    //when the actual menu/activity becomes available
                    //------------------
                    //The user should be able to skip the background information if they
                    //choose to do so
                    startActivity(new Intent(MainActivity.this, BGSpectrumMatcher.class));
                }
            }
        });

        buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            //LensCraft Menu
            public void onClick(View v) {
                if(user.getBackground()) {
                    startActivity(new Intent(MainActivity.this, BGLensCraft.class));
                }
                else{
                    startActivity(new Intent(MainActivity.this, LensCraftMenu.class));
                }
            }
        });

        buttons[3].setOnClickListener(new View.OnClickListener() {
            @Override
            //UserMenu
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UserMenu.class));
            }
        });

        buttons[4].setOnClickListener(new View.OnClickListener() {
            @Override
            //Credits
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Credits.class));
            }
        });

        buttons[5].setOnClickListener(new View.OnClickListener() {
            @Override
            //Portal (extended reading)
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Portal.class));
            }
        });

    }

}
