package ija.proj;

import java.util.Objects;

public class Coordinate extends java.lang.Object {

    private int x;
    private int y;

    public Coordinate(int inputX, int inputY) {
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
    public int getX() {
        return this.x;
    }

    /**
     * Vrací hodnotu souřadnice y.
     *
     * @return Souřadnice y.
     */
    public int getY() {
        return this.y;
    }

    public int diffX(Coordinate c) {
        int x1 = this.x;
        int x2 = c.getX();
        return x1 - x2;
    }


    public int diffY(Coordinate c) {
        int y1 = this.y;
        int y2 = c.getY();
        return y1 - y2;
    }

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
