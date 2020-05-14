package vehicles;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import main.Controller;
import mapData.Coordinate;
import mapData.Line;
import mapData.Stop;
import mapData.Street;

import java.time.LocalTime;

/**
 * Class tram extends vehicle so it is a special type of vehicle. This kind of vehicle can maintain on Tram Lines only
 * and has special representation on map - a square.
 */
public class Tram extends Vehicle {

    /**
     * Tram constructor
     * Method creates new instance of Tram and creates a GUI object on canvas.
     *
     * @param position   Coordinate of Trams within map
     * @param speed      Movement speed of given Tram
     * @param onLine     Line on which Tram transports people
     * @param identifier Unique Tram name
     * @param street     Dynamically changing street, on which Tram currently locates
     * @param firstStop  First stop on which Tram stops
     * @param controller Main controller
     */
    public Tram(Coordinate position, double speed, Line onLine, String identifier, Street street, Stop firstStop, Controller controller, LocalTime time) {
        super(position, speed, onLine, identifier, street, firstStop, controller, time);

        super.setInitialStop();
        super.setStops();

        Polygon singleTram = new Polygon();
        singleTram.getPoints().addAll(position.getX(), position.getY() - 8,
                position.getX() + 8, position.getY(),
                position.getX(), position.getY() + 8,
                position.getX() - 8, position.getY());
        singleTram.setFill(Color.rgb(50, 50, 50));
        singleTram.setStroke(onLine.getColor());
        singleTram.setId(super.getName());
        super.GUI.add(singleTram);
    }
}
