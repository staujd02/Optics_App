package opticallearning.learnoptics;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Joel on 6/27/2016.
 */
public class UserMenu extends Activity {

    //Todo append user information to the list items
    //Todo allow user to turn hints/background on and off

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_menu);
        setTitle("User Menu");

        String[] myList = {
                "Name: ",
                "Score: ",
                "Attempts: ",
                "Correct: ",
                "Incorrect: ",
                "Lens Crafter Lvls: ",
                "Spectrum Lvls: ",
                "Medium Lvls: "
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myList);
        ListView lv = (ListView)findViewById(android.R.id.list);
        lv.setAdapter(adapter);



    }

}
