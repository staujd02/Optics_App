package opticallearning.learnoptics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Learn Optics");

        ImageButton[] buttons = {
                (ImageButton) findViewById(R.id.btnMedium),         //0
                (ImageButton) findViewById(R.id.btnSpectrum),       //1
                (ImageButton) findViewById(R.id.btnLensCraft),      //2
                (ImageButton) findViewById(R.id.btnBrightSource),   //3
                (ImageButton) findViewById(R.id.btnCredits),        //4
                (ImageButton) findViewById(R.id.btnPortal)          //5
        };

        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            //Pick the Medium
            public void onClick(View v) {
              startActivity(new Intent(MainActivity.this, BGMedium.class));
            }
        });

        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            //Spectrum Matcher
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BGSpectrumMatcher.class));
            }
        });

        buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            //LensCraft
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BGLensCraft.class));
            }
        });

        buttons[3].setOnClickListener(new View.OnClickListener() {
            @Override
            //BrightSource
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BGBrightSource.class));
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
