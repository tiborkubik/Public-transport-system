package ija.proj;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.List;


/**
 * Class Stop represents stops on line. Stops are located on streets and lines define, whether they stop on them or not when passing
 * given street. Stops are always connected to specific street and cannot be on their own.
 */
public class Stop implements Drawable {
    private String identifier;          /**< Unique identifier of a stop within given map */
    private Coordinate location = null; /**< Location of stop within canvas */
    private Street onStreet = null;     /**< Definition of street on which a stop lies */
    private int time_to_stay;           /**< Time which vehicles spend on given stop */

    /**
     * Stop constructor. All its attributes must be set
     *
     * @param identifier   Unique stop name
     * @param location     Location of stop within city map
     * @param onStreet     Street on which stop lies
     * @param time_to_stay Duration, which vehicles spend on given stop
     */
    public Stop(String identifier, Coordinate location, Street onStreet, int time_to_stay) {
        this.identifier = identifier;
        this.location = location;
        this.onStreet = onStreet;
        this.time_to_stay = time_to_stay;
    }

    /**
     * Overloading of constructor when no attributes must be specified
     */
    public Stop() {
    }

    /**
     * @return Duration which vehicles spend on given stop
     */
    public int getTime() {
        return this.time_to_stay;
    }

    /**
     * @return Name of this stop
     */
    public String getName() {
        return this.identifier;
    }

    /**
     * @return Street on which stop lies
     */
    public Street getStreet() {
        if (onStreet == null) {
            return null;
        } else {
            return onStreet;
        }
    }

    /**
     * @param s Street of the stop
     */
    public void setStreet(Street s) {
        onStreet = s;
    }

    /**
     * @return Location of stop in a form of Coordinate
     */
    public Coordinate getCoordinate() {
        if (location == null) {
            return null;
        } else {
            return location;
        }
    }

    @Override
    public List<Shape> getGUI() {
        Line stopLine = new Line(location.getX(), location.getY(), location.getX() + 14, location.getY() - 14);
        Text stopName = new Text(location.getX() + 18, location.getY() - 18, identifier);

        stopName.setFont(Font.font("Impact", 12));
        stopName.setFill(Color.rgb(50, 50, 50));

        stopLine.setId(this.onStreet.getName() + "Stop");
        stopName.setId(identifier + "Stop");
        stopLine.setSmooth(true);
        stopLine.setStroke(Color.rgb(50, 50, 50));
        stopLine.setStrokeWidth(6);
        stopLine.toFront();
        stopLine.setStrokeLineCap(StrokeLineCap.ROUND);
        stopLine.setStrokeLineJoin(StrokeLineJoin.ROUND);

        return Arrays.asList(stopLine, stopName);
    }
}
