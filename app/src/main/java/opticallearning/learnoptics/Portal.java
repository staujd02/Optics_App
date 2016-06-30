package opticallearning.learnoptics;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.net.URI;

/**
 * Created by Joel on 6/27/2016.
 */
public class Portal extends ListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.portal);

        String[] links = {"OSA News","Optics and Photonics Journal","Business: Optics.org","The Basics","Advanced"};

        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 ,links));


    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        switch (position){
            case 0:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.osa-opn.org/home/")));
                break;
            case 1:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.scirp.org/journal/opj/")));
                break;
            case 2:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://optics.org")));
                break;
            case 3:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://optics.synopsys.com/learn/learn-optics-kids.html")));
                break;
            case 4:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.learnoptics.com/")));
                break;
        }
    }
}
