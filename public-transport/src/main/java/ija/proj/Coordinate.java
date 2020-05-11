package ija.proj;

import java.util.Objects;

/**
 * Class Coordinate defines one point in the map of the town, it is defined in 2D space, therefore it is defined by two attributes
 * which are x and y
 * Poit with coordinates [0,0] lies in the upper left corner of the map
 */
public class Coordinate {
    private double x; /**< X attribute of the coordinate */
    private double y; /**< Y attribute of the coordinate */

    public Coordinate(double inputX, double inputY) {
        x = inputX;
        y = inputY;
    }

    public void setX(double inputX) {
        this.x = inputX;
    }
    public void setY(double inputY) {
        this.y = inputY;
    }



    /***
     * distance between 2 coordinates
     * @param a first coordinate
     * @return distance
     */
    private double distance(Coordinate a, Coordinate b){
        return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
    }

    public double coordsDistance(Coordinate b) {
        return Math.sqrt(Math.pow(this.getX() - b.getX(), 2) + Math.pow(this.getY() - b.getY(), 2));
    }


    /***
     * Returns true of the Coordinate lies on the street
     * @param s street on which the point should lie
     * @return true if the point lies on the line
     */
    public boolean liesOn(Street s){
        Coordinate stop_to_add = this;

        double distance = distance(s.begin(), s.end());     // original distance of the street
        double distance_with_stop = distance(s.begin(), stop_to_add) + distance(stop_to_add, s.end()); //distance of the street trought the new stop
        double delta = 0.00001;     // allowed distance from the line (proportional to street length)
        //check if the distances and approximately the same
        if (delta> Math.abs(distance- distance_with_stop) / Math.max(Math.abs(distance), Math.abs(distance_with_stop))){
            return true;
        }
        return false;
    }

    /***
     * Tries to change x and y atributes of the coordinate
     * @param inputX value to which the X attribute will be changed
     * @param inputY
     * @return
     */
    public boolean change(double inputX, double inputY){
        if(inputX >= 0 && inputY >= 0) {
            this.x = inputX;
            this.y = inputY;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Vrací hodnotu souřadnice x.
     *
     * @return Souřadnice x.
     */
    public double getX() {
        return this.x;
    }

    /**
     * Vrací hodnotu souřadnice y.
     *
     * @return Souřadnice y.
     */
    public double getY() {
        return this.y;
    }

    public double diffX(Coordinate c) {
        double x1 = this.x;
        double x2 = c.getX();
        return x1 - x2;
    }

    public double diffY(Coordinate c) {
        double y1 = this.y;
        double y2 = c.getY();
        return y1 - y2;
    }

    /***
     * swap x and y attributes of the coordinates
     * @param c Coordinate with witch the attributes will be swapped
     */
    public void swapCoordinates(Coordinate c) {
        Coordinate temp_c = new Coordinate(this.x,this.y);
        this.x = c.getX();
        this.y = c.getY();
        c.x = temp_c.getX();
        c.y = temp_c.getY();
    }

    /***
     * compares Coordinates
     * @param o coordinate to compared with
     * @return return true if Coordinates are the same
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x &&
                y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
