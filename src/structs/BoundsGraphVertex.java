package structs;

/**
 * @author Oleg Shatin
 *         11-501
 */
public class BoundsGraphVertex<TCont> {
    public BoundsGraphVertex(double x, double y, TCont content){
        this.x = x;
        this.y = y;
        this.content = content;
    }
    private double x;
    private double y;
    private TCont content;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public TCont getContent() {
        return content;
    }
    public static double distance(BoundsGraphVertex start, BoundsGraphVertex dest){
        double deltaX = start.getX() - dest.getX();
        double deltaY = start.getY() - dest.getY();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
}
