package opticallearning.learnoptics;

import android.graphics.Point;

/**
 * Created by Joel on 6/29/2016.
 *
 * Laser object that handles the tracking of the laser's direction
 * and sub components
 *
 * Used by the custom view implemented in the sub modules of Lens Crafter
 */
public class Laser {

    //Point start - this must be defined in constructor;
    //Point end - this must be calculated

    //Array of line segments defined in points
    // - Start Point
    // - End Point

    //Color?
    // - defined in constructor

    //Calculate(Point start, or StartX, StartY)
    //have to fetch focal length of lens
    // - no lens null focal length

    //getSegments()
    //returns a 2d array of start and end points
    // - for drawing

    //Make a y=mx+b and plug in the last simulated x, then you get y
    //end point
}
