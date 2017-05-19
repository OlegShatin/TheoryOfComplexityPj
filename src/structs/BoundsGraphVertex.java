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
    public BoundsGraphVertex(double x, double y){
        this.x = x;
        this.y = y;
    }
    @XmlElement
    private double x;
    @XmlElement
    private double y;

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
    public static  double distance(BoundsGraphVertex start, BoundsGraphVertex dest){
        double deltaX = start.getX() - dest.getX();
        double deltaY = start.getY() - dest.getY();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
}
