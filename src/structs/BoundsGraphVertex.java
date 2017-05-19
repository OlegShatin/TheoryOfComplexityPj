package structs;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

/**
 * @author Oleg Shatin
 *         11-501
 */
public class BoundsGraphVertex implements Serializable {
    public BoundsGraphVertex(){

    }
    public BoundsGraphVertex(double x, double y, String name){
        this.x = x;
        this.y = y;
        this.name = name;
    }
    @XmlElement
    private double x;
    @XmlElement
    private double y;
    @XmlElement
    private String name;
    @XmlElement
    private boolean isMarked;

    public boolean isMarked() {
        return isMarked;
    }

    public void setMarked(boolean marked) {
        isMarked = marked;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static  double distance(BoundsGraphVertex start, BoundsGraphVertex dest){
        double deltaX = start.getX() - dest.getX();
        double deltaY = start.getY() - dest.getY();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
}
