package opticallearning.learnoptics;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Joel on 7/1/2016.
 *
 * This menu functions much like the LensCraft Menu, providing a
 * portal to sub modules for the user to complete
 *
 * Currently this module references only dummy classes
 *
 */
public class SpectrumMenu extends ListActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lenscraft_extended);
        setTitle("Explore");

        //User's access level
        int access;

        if(MainActivity.user != null){
            access = MainActivity.user.getSpecLVL();
        }
        else{
            access = 1;
        }

        //Initialize the pages array
        ArrayList<String> pages = new ArrayList<>();

        //Add background to the beginning of the list (constant not dependant on access lvl)
        pages.add("Background");

        //Array generated dynamically based on user's access level
        //Each item is added at the first index to ensure
        //the list is ordered correctly
        switch (access){
            //Highest Level
            //DO NOT ADD BREAK STATEMENT!
            case 5:
                pages.add(1,"Opacity");
            case 4:
                pages.add(1,"Compound Absorbance");
            case 3:
                pages.add(1,"Absorbance");
            case 2:
                pages.add(1,"Compound Reflectance");
            case 1:
                pages.add(1,"Reflectance");
                //Lowest Level
        }

        //Sets the list adapter to display the array pages[]
        setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1 ,pages));
    }

    //Sends the user back to the main menu rather than background xml
    public void onBackPressed() {
        startActivity(new Intent(SpectrumMenu.this, MainActivity.class));
    }


    //This needs to be updated to the spectrum matcher choices

    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        //References dummy classes at the moment
        switch (position){
            case 0:
                startActivity(new Intent(SpectrumMenu.this, BGSpectrumMatcher.class));
                break;
            case 1:
                startActivity(new Intent(SpectrumMenu.this, SpectrumMatcher.class));
                break;
            case 2:
                startActivity(new Intent(SpectrumMenu.this, SpectrumMatcher.class));
                break;
            case 3:
                startActivity(new Intent(SpectrumMenu.this, SpectrumMatcher.class));
                break;
            case 4:
                startActivity(new Intent(SpectrumMenu.this, SpectrumMatcher.class));
                break;
            case 5:
                startActivity(new Intent(SpectrumMenu.this, SpectrumMatcher.class));
                break;
        }
    }


}
