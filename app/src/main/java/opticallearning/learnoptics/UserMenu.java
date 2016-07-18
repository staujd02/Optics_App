package opticallearning.learnoptics;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Joel on 6/27/2016.
 *
 * This menu provides the user with information about their user statistics.
 *
 */
public class UserMenu extends Activity {

    //Todo allow user to turn background on and off

    private User user; //User reference from MainActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_menu);
        setTitle("User Menu");

        //Grabs reference to the username text field (so the username string can be applied)
        TextView txUserName = (TextView) findViewById(R.id.txtName);

        //Grab username reference from MainActivity
        user = MainActivity.user;

        //Create list using user's information
        String[] myList = {
                "Name: " + user.getName(),
                "School: " + user.getSchool(),
                "Score: " + user.getScore(),
                "Attempts: " + user.getAttempts(),
                "Correct: " + user.getCorrect(),
                "Incorrect: " + user.getIncorrect(),
                "Lens Crafter Lvls: " + user.getLensLVL(),
                "Spectrum Lvls: " + user.getSpecLVL(),
                "Medium Lvls: " + user.getMedLVL()
        };

        //Assigns the main title bar Username to the correct string
        txUserName.setText(user.getUserName());

        //Sets the adapter and displays
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myList);
        ListView lv = (ListView)findViewById(android.R.id.list);
        lv.setAdapter(adapter);
    }

}
