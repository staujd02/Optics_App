package opticallearning.learnoptics;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.hardware.camera2.params.LensShadingMap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Joel on 7/1/2016.
 */
public class LensCraftMenu extends ListActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lenscraft_extended);
        setTitle("Explore");

        String[] pages = {"Concave vs. Convex","N Index","Beam Count","Distance","Lens Width","Number of Lens"};

        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 ,pages));
    }

    public void onBackPressed() {
        startActivity(new Intent(LensCraftMenu.this, MainActivity.class));
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        switch (position){
            case 0:
                startActivity(new Intent(LensCraftMenu.this, concaveconvex.class));
                break;
            case 1:
                startActivity(new Intent(LensCraftMenu.this, NIndex.class));
                break;
            case 2:
                startActivity(new Intent(LensCraftMenu.this, BeamCount.class));
                break;
            case 3:
                startActivity(new Intent(LensCraftMenu.this, Distances.class));
                break;
            case 4:
                startActivity(new Intent(LensCraftMenu.this, LensWidth.class));
                break;
            case 5:
                startActivity(new Intent(LensCraftMenu.this, NumberOfLens.class));
                break;
        }
    }
}
