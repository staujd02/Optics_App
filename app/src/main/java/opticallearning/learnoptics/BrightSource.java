package opticallearning.learnoptics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Joel on 6/27/2016.
 */
public class BrightSource extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brightsource);
        setTitle("Bright Source");
    }

    @Override
    public void onBackPressed() {
        //your code when back button pressed
        startActivity(new Intent(BrightSource.this, MainActivity.class));
    }
}
