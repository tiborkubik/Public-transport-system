package ija.proj;

import java.util.Objects;

public class Coordinate {
    private double x;
    private double y;


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

    public double coordsDistance(Coordinate b) {
        return Math.sqrt(Math.pow(this.getX() - b.getX(), 2) + Math.pow(this.getY() - b.getY(), 2));
    }

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
