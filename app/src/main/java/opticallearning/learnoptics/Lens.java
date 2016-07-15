package opticallearning.learnoptics;

import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by Joel on 6/29/2016.
 *
 * Lens object responsible for holding data necessary for calculations and drawing
 * the lens bitmaps
 *
 *
 * This is intended for use in the custom views implemented in Lens Crafter (LensCraftMenu.java)
 */
public class Lens {

    private String id;              //Lens unique ID
    private String material;        //Lens material makeup
    private Rect graphic_Reference; //Lens's bitmap binding rectangle
    private double fLen;            //Lens's focal length (in units)
    private boolean concave;        //Whether or not lens is concave
    private float radius;           //Lens's radius
    private float nIdex;            //Lens's N index

    private Point origin = null;    //Lens holder location in phone's screen space (x,y)
    private int height = -1;        //Lens holder height in phone's space
    private int width = -1;         //Lens holder width in phone's space

    /**
     *  Constructor intended for loading the lens into memory from data file
     *
     * @param id        This is the the lens's unique string identifier
     * @param material  A string description of the lens's material
     * @param graphic_Reference String filename of the lens's image
     * @param fLen      Focal length of the lens
     * @param concave   boolean, if true the lens is concave, otherwise it is convex
     * @param radius    float number representative of the lens's radius
     */
    public Lens(String id, String material, Rect graphic_Reference, double fLen, boolean concave, float radius, float nIndex) {
        this.id = id;
        this.material = material;
        this.graphic_Reference = graphic_Reference;
        this.fLen = fLen;
        this.concave = concave;
        this.radius = radius;
        this.nIdex = nIndex;
    }

    /**
     * This is a deep copy constructor
     *
     * @param l Lens you wish to copy
     */
    public Lens(Lens l){
        this.id = l.getId();
        this.material = l.getMaterial();
        this.graphic_Reference = l.getGraphic_Reference();
        this.fLen = l.getfLen();
        this.concave = l.isConcave();
    }

    /**
     * Sets the location of the lens in a relative space
     *
     * @param x the x of the top left corner of where the rectangle binding
     *               space starts > the (0,0) of the lens
     * @param y the y of the top left corner of the where the rect binding starts
     * @param height The height of the lens
     * @param width  The width of the lens
     */
    public void setLocation(int x, int y, int height, int width){
        this.origin = new Point(x,y);
        this.height = height;
        this.width = width;
    }

    //Getters
    //************************************
    public Point getOrigin() {
        return origin;
    }

    public float getRadius() {return radius;}

    public float getNIndex() {return  nIdex;}

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getId() {
        return id;
    }

    public String getMaterial() {
        return material;
    }

    public Rect getGraphic_Reference() {
        return graphic_Reference;
    }

    public double getfLen() {
        return fLen;
    }

    public boolean isConcave() {
        return concave;
    }
}
