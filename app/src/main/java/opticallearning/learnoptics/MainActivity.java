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
 */
public class MainActivity extends Activity {

    static protected User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //calls the super constructor
        setContentView(R.layout.activity_main); //sets the layout
        setTitle("Learn Optics");   //Assigns a descriptive title to the title bar

        //Load user
        user = User.loadUser("default.dat", getApplicationContext());

        //Check User
        if(user == null){
            //If the reference is null, create default user
            user = new User("Galileo");
            user.saveUser("default.dat", this.getApplicationContext());
        }

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
                    startActivity(new Intent(MainActivity.this, BGMedium.class));
                }
                else{
                    startActivity(new Intent(MainActivity.this, Medium.class));
                }
            }
        });

        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            //Spectrum Matcher
            public void onClick(View v) {
                if(user.getBackground()) {
                    startActivity(new Intent(MainActivity.this, BGSpectrumMatcher.class));
                }
                else{
                    startActivity(new Intent(MainActivity.this, SpectrumMenu.class));
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
