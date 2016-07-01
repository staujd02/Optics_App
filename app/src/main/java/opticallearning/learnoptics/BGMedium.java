package opticallearning.learnoptics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by Joel on 7/1/2016.
 */
public class BGMedium extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b_medium);
        setTitle("Background: Rank the Medium");

        Button b = (Button) findViewById(R.id.btnMedium_c);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BGMedium.this, Medium.class));
            }
        });
    }
}
