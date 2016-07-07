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
 * This class starts the lenscraft_extended.xml menu
 *
 * The LensCraftMenu is designed as a portal for the user
 * to engage with the other sub modules of LensCraft
 *
 * These sub modules are:
 *
 * ConcaveConvex.java,      Distances.java,
 * LensWidth.java,          NIndex.java
 * Skew.java, and           NumberOfLens.java
 *
 */
public class LensCraftMenu extends ListActivity{

    private User user;

    //TODO load lenses from database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //Call super constructor
        setContentView(R.layout.lenscraft_extended);    //set the view to Lens Crafter menu
        setTitle("Explore");    //Assign a descriptive title
        int access;

        user = MainActivity.user;

        if(user == null){
            access = 1;
        }
        else {
            access = user.getLensLVL();
        }

        //USED FOR TESTING
        access = 6;

        ArrayList<String> pages = new ArrayList<String>();

        pages.add("Background");

        //Array generated dynamically based on user's access level
        //Each item is added at the first index to ensure
        //the list is ordered correctly
        switch (access){
            case 6:
                //Highest Level
                pages.add("Number of Lens");
            case 5:
                pages.add(1,"Lens Width");
            case 4:
                pages.add(1,"Distance");
            case 3:
                pages.add(1,"Height");
            case 2:
                pages.add(1,"N Index");
            case 1:
                //Lowest Level
                pages.add(1,"Concave vs. Convex");
        }

        //Sets the list adapter to display the array pages[]
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 ,pages));
    }

    /**
     * Forces the user back to main menu (MainActivity.java, activity_main.xml)
     * instead of back to the background information
     */
    public void onBackPressed() {
        //Send the user to the main menu,
        // prevents the user from returning to the
        //background page
        startActivity(new Intent(LensCraftMenu.this, MainActivity.class));
    }

    /**
     *
     * This class starts the Activity the user selected
     *
     * @param l
     * @param v
     * @param position provides the index of the item the user touched
     * @param id
     */
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        switch (position){
            case 0:
                //Starts the background activity, b_lenscraft.xml
                startActivity(new Intent(LensCraftMenu.this, BGLensCraft.class));
                break;
            case 1:
                //Starts the ConcaveConvex sub module, concaveconvex.xml
                startActivity(new Intent(LensCraftMenu.this, ConcaveConvex.class));
                break;
            case 2:
                //Starts the N index module, n_index.xml
                startActivity(new Intent(LensCraftMenu.this, NIndex.class));
                break;
            case 3:
                //Starts the Skew module, shew.xml
                startActivity(new Intent(LensCraftMenu.this, Skew.class));
                break;
            case 4:
                //Starts the (Lens) Distances module, distance.xml
                startActivity(new Intent(LensCraftMenu.this, Distances.class));
                break;
            case 5:
                //Starts the LensWidth module, lens_width.xml
                startActivity(new Intent(LensCraftMenu.this, LensWidth.class));
                break;
            case 6:
                //Starts the Number of Lens module, number_of_lens.xml
                startActivity(new Intent(LensCraftMenu.this, NumberOfLens.class));
                break;
        }
    }
}
