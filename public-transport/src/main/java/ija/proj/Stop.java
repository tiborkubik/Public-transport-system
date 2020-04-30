package ija.proj;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
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
        Circle stopCirlce = new Circle(location.getX(), location.getY(), 5, Color.BLACK);
        Text stopName = new Text(location.getX() + 10, location.getY() - 10, identifier);
        stopName.setFont(Font.font ("Impact", 12));
        stopCirlce.setId(identifier);
        stopName.setId(identifier);
        return Arrays.asList(stopCirlce, stopName);
    }
}
