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

public class Stop implements Drawable {
    private String identifier;
    private Coordinate location = null;
    private Street onStreet = null;


    public Stop(String identifier, Coordinate location, Street onStreet) {
        this.identifier = identifier;
        this.location = location;
        this.onStreet = onStreet;
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
        Line stopLine = new Line(location.getX(), location.getY(), location.getX() + 10, location.getY() - 10);

        Text stopName = new Text(location.getX() + 14, location.getY() - 14, identifier);
        stopName.setFont(Font.font ("Impact", 12));
        stopName.setFill(Color.rgb(50, 50, 50));

        stopLine.setId(this.onStreet.getName());

        stopName.setId(identifier);
        stopLine.setSmooth(true);
        stopLine.setStroke(Color.rgb(50, 50, 50));
        stopLine.setStrokeWidth(6);
        stopLine.toBack();
        stopLine.setStrokeLineCap(StrokeLineCap.ROUND);
        stopLine.setStrokeLineJoin(StrokeLineJoin.ROUND);
        return Arrays.asList(stopLine, stopName);
    }
}
