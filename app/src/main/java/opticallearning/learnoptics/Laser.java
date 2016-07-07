package opticallearning.learnoptics;

import android.graphics.Point;
import android.graphics.PointF;
import android.view.MotionEvent;

import java.util.ArrayList;

/*
 * Created by Joel on 6/29/2016.
 *
 * Laser object that handles the tracking of the laser's direction
 * and sub components
 *
 * Used by the custom view implemented in the sub modules of Lens Crafter
 */
public class Laser {

    private static final int TRACE_DEFINITION = 1;

    private Point MAX_POINT; //The maximum point of the enviroment
                                    //used to determine room size
    //private Color laserColor; //Laser's color

    private PointF start;    //This must be defined in constructor;
    private PointF End;     //Laser end destination, this must be calculated by the laser class

    private ArrayList<Lens> lenses;     //Array of lens interacting with the laser
    private ArrayList<LaserLens> decoratedLenses;   //Decorating class that adds information about
                                                    //the laser interaction with the lens
    private ArrayList<LaserLine> segs;  //Array of individual line segments of the laser
                                        //this is what will be rendered after calculation

    /**
     * Basic Laser constructor
     *
     * @param start     Laser's starting point
     * @param maxPoint  The maximum point of the given area
     */
    public Laser(PointF start,Point maxPoint ){
        this.start = start;
        MAX_POINT = maxPoint;
    }

    /**
     * Basic laser constructor that sends the points by their primitive values
     *
     * @param x     X value of the starting point
     * @param y     Y value of the starting point
     * @param max_X X value of the maximum point of the given area
     * @param max_Y Y value of the maximum point of the given area
     */
    public Laser(float x, float y, int max_X, int max_Y){
        start = new PointF(x,y);
        MAX_POINT = new Point(max_X, max_Y);
    }

    /**
     * This adds a lens to the array of lenses the laser will interact with
     *
     * This is only for use when multiple lenses are in effect, otherwise, if only one
     * lens is in use, you must use the more descriptive constructor when adding a lens
     *
     * @param lens lens object you wish to add to the laser's interaction
     */
    public void addLens(Lens lens){
        lenses.add(lens);
    }

    /**
     * This is the more descriptive constructor and it must be used for the first lens
     * Otherwise, the laser cannot begin a calculation
     *
     * @param lens  The lens you wish to add to the laser's interaction
     *              *NOTE: the Lens must have a valid location for the calculation to proceed
     *
     * @param angle The angle at which the laser is entering the lens (Typically 90 degrees)
     */
    public void addInitialLens(Lens lens, float angle){
        LaserLens len = new LaserLens(lens, angle);
        //The lens is added to the front of the array
        lenses.add(0,lens);
    }

    //Calculate(Point start, or StartX, StartY)
    //have to fetch focal length of lens
    // - no lens null focal length
    public void calculate(){
        PointF lensCenter;

        //y = mx + b
        double x;
        double m = 0;   //Slope of initial Laser line segment
        double b = start.y; //Y intercept of Laser line segment
        LaserLine line;
        PointF start = this.start;
        PointF focalPoint = new PointF();
        PointF impact;

        //Loop through lenses
        for (Lens l: lenses) {
            impact = impact(l,m,b);

            if(impact != null){
                //Impact occurred
                segs.add(new LaserLine(start, impact)); //make line segment to lens center
                start = impact; //Swap the old start to impact for continuity

                //THIS FORMULA CURRENTLY ASSUMES THE ANGLE OF INCIDENCE IS 90 DEGREES
                //calculate exit angle / line
                //TODO Modify this formula for multiple angles of incidence

                //FIND THE FOCAL POINT
                //Find y of focal point -> average of lowest y + highest y (vertical midpoint)
                focalPoint.y = (l.getOrigin().y + (l.getOrigin().y + l.getHeight())) / 2;
                //Find horizontal midpoint -> lowest x plus highest x divided by two
                focalPoint.x = (l.getOrigin().x + l.getWidth()+l.getOrigin().x) / 2;
                //Add focal length for x
                focalPoint.x = focalPoint.x + l.getfLen();

                //slope equation - NEW SLOPE
                m = (focalPoint.y - impact.y) / (focalPoint.x - impact.x);

                //Point-Slope Form modified for b
                // y - y1 = m(x - x1)
                // y = m(x - x1) + y1
                // y = mx - mx1 + y1  --> disregard mx and y
                // b = - (mx1) + y1
                // NEW B
                b = -(m * impact.x) + impact.y;

                //End of iteration
            }
            else{
                break;
            }
        }

        //Make last segment to impact edge of screen
        //This is where you would add if the laser was reflected
        //if the angle of incidence was greater than the critical angle

        impact = new PointF(start.x, start.y);

        //Trace steps by incrementing x until it leaves the binding rectangle
        while(impact.x > 0 && impact.x < MAX_POINT.x && impact.y > 0 && impact.y < MAX_POINT.y){
            impact.x = impact.x + TRACE_DEFINITION;
            impact.y = (float) (impact.x * m + b);
        }

        segs.add(new LaserLine(start, impact));
    }

    public PointF impact(Lens l,double m, double b){
        double y;       //laser's entry y
        double low_Y;   //lowest y of lens window
        double high_Y;  //greatest y of lens window
        double midpoint_X;//midpoint x of the lens

        //Calculate x midpoint -> lowest x plus greatest x divided by 2
        midpoint_X = (l.getOrigin().x + (l.getWidth() + l.getOrigin().x)) / 2;

        //Calculate the y of the laser line at the given x
        y = m*midpoint_X + b;

        //Set the window of the lens height
        low_Y = l.getOrigin().y;
        high_Y = l.getOrigin().y + l.getHeight();

        //If the laser's y falls within the lens window
        if(y > low_Y && y < high_Y){
            //return point of impact
            return new PointF( (float) midpoint_X, (float) y);
        }
        else{
            //no impact > return null
            return  null;
        }
    }

    /**
     * This class takes all of the LaserLines in the segs[] array and breaks them into
     * start and end points for the rendering of the laser's line segments
     *
     * @return Returns an array of PointF arrays containing the start and end points of
     *          the laser's line segments
     */
    public ArrayList<PointF[]> getSegments(){
        //Holder array
        ArrayList<PointF[]> array = new ArrayList<>();

        //Loops through each element in the segs[] array
        for(int i = 0; i < segs.size(); i++){
            //calls function of LaserLine to convert the LaserLine
            // into a PointF array of length 2 [start point, end point]
            array.add(segs.get(i).toArray());
        }

        return array;
    }

    /**
     * This is used by the Laser's calculate function to create and store segments
     *
     * @param start Line's start point
     * @param end   Line's end point
     */
    private void newLaserLine(PointF start, PointF end){
        LaserLine ll = new LaserLine(start,end);
        segs.add(ll);   //Add to linesegments array
    }

    /**
     * Decorator class:
     *
     * Adds an entry angle component
     *
     * Created on laser interaction with lens
     */
    private class LaserLens {

        Lens lens;          //Decorated Lens
        float entry_Angle;  //Entry angle of the laser (Given or calculated)

        public  LaserLens(Lens lens, float entry_Angle){
            this.lens = lens;
            this.entry_Angle = entry_Angle;
        }

    }

    /**
     * Line segment object
     *
     * This class holds the essential information calculated by the
     * laser calculate function
     *
     * After calculate is called, this information is disseminated into points and
     * then drawn by the activity's graphics component
     */
    private class LaserLine {
        PointF start;   //Start point of line segment
        PointF end;     //End point

        /**
         * Basic constructor
         *
         * @param start point
         * @param end   point
         */
        public LaserLine(PointF start, PointF end){
            this.start = start;
            this.end = end;
        }

        /**
         * Basic constructor alternative, takes primitives for arguments
         *
         * @param x1    start point's x
         * @param y1    start point's y
         * @param x2    end point's x
         * @param y2    end point's y
         */
        public LaserLine(float x1, float y1, float x2, float y2){
            start = new PointF(x1,y1);
            end  = new PointF(x2,y2);
        }

        /**
         * This function breaks the segment into a PointF[] array
         * of size two.
         *
         * @return  array of 2 points [start point, end point]
         */
        public PointF[] toArray(){
            PointF[] array = new PointF[2];
            array[0] = start;
            array[1] = end;

            return array;
        }
    }

}
