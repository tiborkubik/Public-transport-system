package ija.proj;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.List;

public class Stop implements Drawable{
    private String identifier;
    private Coordinate location = null;
    private Street onStreet = null;


    public Stop(String identifier, Coordinate location, Street onStreet) {
        this.identifier = identifier;
        this.location = location;
        this.onStreet = onStreet;
    }

    public void setStreet(Street s) {
        onStreet = s;
    }

    @Override
    public List<Shape> getGUI() {
        Circle stopCirlce = new Circle(location.getX(), location.getY(), 5, Color.BLACK);
        Text stopName = new Text(location.getX() + 10, location.getY() - 10, identifier);
        return Arrays.asList(stopCirlce, stopName);
    }

    public Street getStreet() {
        if(onStreet == null) {
            return null;
        } else {
            return onStreet;
        }
    }
    public Coordinate getCoordinate() {
        if(location == null) {
            return null;
        } else {
            return location;
        }
    }
}
