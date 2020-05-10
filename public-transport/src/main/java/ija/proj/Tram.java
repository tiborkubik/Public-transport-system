package ija.proj;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 * Class tram extends vehicle so it is a special type of vehicle. This kind of vehicle can maintain on Tram Lines only
 * and has special representation on map - a square.
 */
public class Tram extends Vehicle {
    public Tram(Coordinate position, double speed, Line onLine, String identifier, Street street, Stop firstStop, Controller controller) {
        super(position, speed, onLine, identifier, street, firstStop, controller);

        super.setInitialStop();
        super.setStops();

        //.GUI = new ArrayList<>();
        Polygon singleTram = new Polygon();
        singleTram.getPoints().addAll((double)position.getX(), (double)position.getY() - 8,
                (double)position.getX() + 8, (double)position.getY(),
                (double)position.getX(), (double)position.getY() + 8,
                (double)position.getX() - 8, (double)position.getY());
        singleTram.setFill(Color.rgb(50, 50, 50));
        singleTram.setStroke(onLine.getColor());
        singleTram.setId(super.getName());
        super.GUI.add(singleTram);
    }
}
