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
 *
 * This class loads a listView intended for sending the user to other
 * locations for more reading
 *
 * Links are arranged in a ListView for the user to select from
 */
public class Portal extends ListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //Calls the super constructor
        setContentView(R.layout.portal);    //sets the view
        setTitle("Extra Info Portal");      //assigns a descriptive title

        //All of the link descriptions/Titles
        String[] links = {"OSA News","Optics and Photonics Journal","Business: Optics.org","The Basics","Advanced"};

        //Sets list adapter for reading links[] into listView
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 ,links));
    }

    /**
     *
     * Handles the behaviour of when the user clicks on the list
     *
     * @param l
     * @param v
     * @param position the index of item the user selected
     * @param id
     */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);//Android adds extra behaviours onClick()

        //
        switch (position){
            case 0:
                //Sends the user to OSA News Website
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.osa-opn.org/home/")));
                break;
            case 1:
                //Sends the user to a Optics and Photonics journal
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.scirp.org/journal/opj/")));
                break;
            case 2:
                //Site that focuses on the business aspects of optics and photonics
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://optics.org")));
                break;
            case 3:
                //Sends the user to a younger kid-oriented site explaining of optics and light
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://optics.synopsys.com/learn/learn-optics-kids.html")));
                break;
            case 4:
                //Sends the user to a professor (Indiana University) maintained website
                //This site contains advanced concepts of optics and photonics
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.learnoptics.com/")));
                break;
        }
    }
}
