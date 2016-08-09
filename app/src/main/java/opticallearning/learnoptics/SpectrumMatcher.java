package opticallearning.learnoptics;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Joel on 6/27/2016.
 *
 * This is a dummy holder class, along with its spectrummatcher.xml layout,
 * it will only exist as a template in the future.
 * After that period it will likely be deleted...
 *
 */
public class SpectrumMatcher extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //Calls the super constructor
        setContentView(R.layout.spectrummatcher);   //sets the view
        setTitle("Spectrum Matcher");   //assigns descriptive title
    }
}
