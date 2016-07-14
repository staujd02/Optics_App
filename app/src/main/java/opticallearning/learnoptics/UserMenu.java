package opticallearning.learnoptics;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Joel on 6/27/2016.
 *
 * This menu provides the user with information about their user statistics.
 *
 */
public class UserMenu extends Activity {

    //Todo append user information to the list items
    //Todo allow user to turn hints/background on and off

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_menu);
        setTitle("User Menu");

        user = MainActivity.user;

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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myList);
        ListView lv = (ListView)findViewById(android.R.id.list);
        lv.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!user.isSetupComplete()){

        }
    }


}
