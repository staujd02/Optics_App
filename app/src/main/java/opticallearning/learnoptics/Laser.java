package opticallearning.learnoptics;

import android.graphics.Point;
import android.graphics.PointF;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Joel on 6/29/2016.
 *
 * Laser object that handles the tracking of the laser's direction
 * and sub components
 *
 * Used by the custom view implemented in the sub modules of Lens Crafter
 */
public class Laser {

    private Point MAX_POINT;

    private PointF start;    //This must be defined in constructor;
    private PointF End;

    private ArrayList<Lens> lenses;
    private ArrayList<LaserLens> decoratedLenses;

    private ArrayList<LaserLine> segs;

    public Laser(PointF start,Point maxPoint ){
        this.start = start;
        MAX_POINT = maxPoint;
    }

    public Laser(float x, float y){

    }

    public void addLens(Lens lens){
        lenses.add(lens);
    }

    public void addIntialLens(Lens lens, float angle, float distance){
        LaserLens len = new LaserLens(lens, angle);
        lenses.add(0,lens);
    }


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
    public ArrayList<PointF[]> getSegments(){
        ArrayList<PointF[]> array = new ArrayList<PointF[]>();
        LaserLine ll;

        for(int i = 0; i < segs.size(); i++){
            array.add(segs.get(i).toArray());
        }

        return array;
    }

    private void newLaserLine(PointF start, PointF end){
        LaserLine ll = new LaserLine(start,end);
        segs.add(ll);
    }

    //Make a y=mx+b and plug in the last simulated x, then you get y
    //end point

    private class LaserLens {

        Lens lens;
        float entry_Angle;
        float distance;

        public  LaserLens(Lens lens, float distance){
            this.lens = lens;
            this.distance = distance;
        }

        public  LaserLens(Lens lens,float distance, float entry_Angle){
            this.lens = lens;
            this.distance = distance;
            this.entry_Angle = entry_Angle;
        }

    }

    private class LaserLine {
        PointF start;
        PointF end;

        public LaserLine(PointF start, PointF end){
            this.start = start;
            this.end = end;
        }

        public LaserLine(float x1, float y1, float x2, float y2){
            start = new PointF(x1,y1);
            end  = new PointF(x2,y2);
        }

        public PointF[] toArray(){
            PointF[] array = new PointF[2];
            array[0] = start;
            array[1] = end;

            return array;
        }
    }

}
