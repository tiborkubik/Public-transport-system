package ija.proj;

import java.util.List;
import java.util.Objects;

public class Coordinate extends java.lang.Object {

    private double x;
    private double y;


    public Coordinate(double inputX, double inputY) {
        x = inputX;
        y = inputY;
    }

    public static Coordinate create(int inputX, int inputY) {

        if(inputX >= 0 && inputY >= 0) {
            Coordinate c = new Coordinate(inputX, inputY);
            return c;
        } else {
            return null;
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
