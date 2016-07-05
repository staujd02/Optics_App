package opticallearning.learnoptics;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Joel on 6/27/2016.
 *
 * This activity opens the credits.xml
 *
 * The credits.xml page informs the user of
 * the creators, sponsors, and endorsers of this app.
 *
 * Should follow specific format outlined by sponsors.
 *
 */
public class Credits extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //Calls super constructor
        setContentView(R.layout.credits);   //Sets the view to credits.xml
        setTitle("Acknowledgements");       //Assigns string to title bar (android:theme)
    }
}
