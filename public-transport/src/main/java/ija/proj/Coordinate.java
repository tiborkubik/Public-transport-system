package ija.proj;

import java.util.Objects;


/**
 * Class Coordinate defines one point in the map of the town, it is defined in 2D space, therefore it is defined by two attributes
 * which are x and y
 * Point with coordinates [0,0] lies in the upper left corner of the map
 */
public class Coordinate {
    private double x;    /**< X attribute of the coordinate */
    private double y; /**< Y attribute of the coordinate */

    /**
     * Coordinate constructor
     *
     * @param inputX X value of coordinate
     * @param inputY Y value of coordinate
     */
    public Coordinate(double inputX, double inputY) {
        x = inputX;
        y = inputY;
    }

    /**
     * Returns X value of coordinate
     *
     * @return X value
     */
    public double getX() {
        return this.x;
    }

    /**
     * Method sets X value of a coordinate
     *
     * @param inputX This value will be set as X value of given coordinate
     */
    public void setX(double inputX) {
        this.x = inputX;
    }

    /**
     * Returns Y value of coordinate
     *
     * @return Y value
     */
    public double getY() {
        return this.y;
    }

    /**
     * Method sets Y value of a coordinate
     *
     * @param inputY This value will be set as Y value of given coordinate
     */
    public void setY(double inputY) {
        this.y = inputY;
    }

    /**
     * Distance between 2 coordinates
     *
     * @param a First coordinate
     * @param b Second coordinate
     * @return Distance between coordinate a and coordinate b
     */
    private double distance(Coordinate a, Coordinate b) {
        return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
    }

    /**
     * Distance between coordinate on which method is called and coordinate as parameter
     *
     * @param b One of ends between which distance is calculated
     * @return Distance between coordinate b and coordinate on which method is called
     */
    public double coordsDistance(Coordinate b) {
        return Math.sqrt(Math.pow(this.getX() - b.getX(), 2) + Math.pow(this.getY() - b.getY(), 2));
    }

    /**
     * Method checks whether coordinate lies on street passed as parameter
     *
     * @param s Street on which the point should lie
     * @return True if the point lies on the line, otherwise false
     */
    public boolean liesOn(Street s) {
        Coordinate stopToAdd = this;

        double distance = distance(s.begin(), s.end());                                             // original distance of the street
        double distanceWithStop = distance(s.begin(), stopToAdd) + distance(stopToAdd, s.end());    //distance of the street trought the new stop
        double delta = 0.00001;                                                                     // allowed distance from the line (proportional to street length)

        //check if the distances and approximately the same
        return delta > Math.abs(distance - distanceWithStop) / Math.max(Math.abs(distance), Math.abs(distanceWithStop));
    }

    /**
     * Tries to change x and y attributes of the coordinate
     *
     * @param inputX Value to which the X attribute will be changed
     * @param inputY Value of new Y
     * @return True if coordinates were swapped, otherwise false
     */
    public boolean change(double inputX, double inputY) {
        if (inputX >= 0 && inputY >= 0) {
            this.x = inputX;
            this.y = inputY;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method calculates the difference of X values of coordinates
     *
     * @param c Second coordinate, whose X value will be used
     * @return Difference between X values
     */
    public double diffX(Coordinate c) {
        double x1 = this.x;
        double x2 = c.getX();
        return x1 - x2;
    }

    /**
     * Method calculates the difference of Y values of coordinates
     *
     * @param c Second coordinate, whose Y value will be used
     * @return Difference between Y values
     */
    public double diffY(Coordinate c) {
        double y1 = this.y;
        double y2 = c.getY();
        return y1 - y2;
    }

    /***
     * Swaps x and y attributes of the coordinates
     *
     * @param c Coordinate with with the attributes will be swapped
     */
    public void swapCoordinates(Coordinate c) {
        Coordinate tempC = new Coordinate(this.x, this.y);
        this.x = c.getX();
        this.y = c.getY();
        c.x = tempC.getX();
        c.y = tempC.getY();
    }

    /***
     * Compares coordinates
     *
     * @param o Coordinate to compared with
     *
     * @return return true if coordinates are the same, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x &&
                y == that.y;
    }

    /**
     * @return Hashcode of given object
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
