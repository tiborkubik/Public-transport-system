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
    private String identifier;                 /**< Unique identifier of a stop within given map */
    private Coordinate location = null;        /**< Location of stop within canvas */
    private Street onStreet = null;            /**< Definition of street on which a stop lies */
    private int time_to_stay;                  /**< Time which vehicles spend on given stop */

    public Stop(String identifier, Coordinate location, Street onStreet, int time_to_stay) {
        this.identifier = identifier;
        this.location = location;
        this.onStreet = onStreet;
        this.time_to_stay = time_to_stay;
    }

    public Stop() {

    }

    public int getTime(){
        return this.time_to_stay;
    }

    /***
     * @return name of this stop
     */
    public String getName() {
        return this.identifier;
    }

    /***
     * @param s street of the stop
     */
    public void setStreet(Street s) {
        onStreet = s;
    }


    public Street getStreet() {
        if(onStreet == null) {
            return null;
        } else {
            return onStreet;
        }
    }

    /***
     * returns coordinates of the stop
     */
    public Coordinate getCoordinate() {
        if(location == null) {
            return null;
        } else {
            return location;
        }
    }

    @Override
    public List<Shape> getGUI() {
        Line stopLine = new Line(location.getX(), location.getY(), location.getX() + 14, location.getY() - 14);

        Text stopName = new Text(location.getX() + 18, location.getY() - 18, identifier);
        stopName.setFont(Font.font ("Impact", 12));
        stopName.setFill(Color.rgb(50, 50, 50));

        stopLine.setId(this.onStreet.getName()+"Stop");

        stopName.setId(identifier+"Stop");
        stopLine.setSmooth(true);
        stopLine.setStroke(Color.rgb(50, 50, 50));
        stopLine.setStrokeWidth(6);
        stopLine.toFront();
        stopLine.setStrokeLineCap(StrokeLineCap.ROUND);
        stopLine.setStrokeLineJoin(StrokeLineJoin.ROUND);
        return Arrays.asList(stopLine, stopName);
    }
}
