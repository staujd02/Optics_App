package opticallearning.learnoptics;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Joel on 7/13/2016.
 */
public class Setup extends Activity {

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distance);  //sets the content view
        setTitle("User Setup");          //Assigns specific title over default

        user = MainActivity.user;



    }
}
