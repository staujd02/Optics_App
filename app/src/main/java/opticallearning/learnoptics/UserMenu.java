package opticallearning.learnoptics;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

    //User reference from MainActivity
    User user;
    boolean userChange = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_menu);
        setTitle("User Menu");

        String background;
        String hints;



        //Grabs reference to the username text field (so the username string can be applied)
        final TextView txUserName = (TextView) findViewById(R.id.txtName);

        //Grab username reference from MainActivity
        user = MainActivity.user;

        //Copy a reference to the String array of user value labels
        final String[] values = getResources().getStringArray(R.array.user_values);

        //Generate String based on boolean value of hints and background
        if(user.getHints()){
            hints = "(On)";
        }
        else{
            hints = "(Off)";
        }

        if(user.getBackground()){
            background = "(On)";
        }
        else{
            background = "(Off)";
        }

        //Create list using user's information
        final String[] myList = {
                values[0] + " " + hints,
                values[1] + " " + background,
                values[2] + " " + user.getName(),
                values[3] + " " + user.getSchool(),
                values[4] + " " + user.getScore(),
                values[5] + " " + user.getAttempts(),
                values[6] + " " + user.getCorrect(),
                values[7] + " " + user.getIncorrect(),
                values[8] + " " + user.getLensLVL(),
                values[9] + " " + user.getSpecLVL(),
                values[10] + " " + user.getMedLVL(),
        };

        //Assigns the main title bar Username to the correct string
        txUserName.setText(user.getUserName());

        //Sets the adapter and displays
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myList);
        ListView lv = (ListView)findViewById(android.R.id.list);
        lv.setAdapter(adapter);

        //Allows for user to change
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        //Change hints
                        String hints;

                        //Swap values and strings
                        if(user.getHints()){
                            hints = "(Off)";
                            user.setHints(false);
                        }
                        else{
                            hints = "(On)";
                            user.setHints(true);
                        }

                        //update my list
                        myList[0] = values[0] + " " + hints;

                        break;
                    case 1:
                        //Change background
                        String background;

                        //Swap values and strings
                        if(user.getBackground()){
                            background = "(Off)";
                            user.setBackground(false);
                        }
                        else{
                            background = "(On)";
                            user.setBackground(true);
                        }

                        myList[1] = values[1] + " " + background;
                        break;
                }

                userChange = true;
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Save user on pause if changes were made
        if(userChange){
            user.saveUser(MainActivity.user_filename,getApplicationContext());
            userChange = false;
        }

    }
}
