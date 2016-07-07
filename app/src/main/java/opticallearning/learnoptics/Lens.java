package opticallearning.learnoptics;

import android.graphics.Point;

/**
 * Created by Joel on 6/29/2016.
 *
 * Lens object responsible for holding data necessary for calculations and drawing
 * the lens bitmaps
 *
 * This is intended for use in the custom views implemented in Lens Crafter (LensCraft.java)
 */
public class Lens {

    private String id;
    private String material;
    private String graphic_Reference;
    private int fLen;
    private int nIndex;
    private boolean concave;

    private Point origin = null;
    private int height = -1;
    private int width = -1;

    public Lens(String id, String material, String graphic_Reference, int fLen, int nIndex, boolean concave) {
        this.id = id;
        this.material = material;
        this.graphic_Reference = graphic_Reference;
        this.fLen = fLen;
        this.nIndex = nIndex;
        this.concave = concave;
    }

    public void setLocation(Point origin, int height, int width){
        this.origin = origin;
        this.height = height;
        this.width = width;
    }

    public Point getOrigin() {
        return origin;
    }

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

    public String getGraphic_Reference() {
        return graphic_Reference;
    }

    public int getfLen() {
        return fLen;
    }

    public int getnIndex() {
        return nIndex;
    }

    public boolean isConcave() {
        return concave;
    }
}
