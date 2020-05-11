package ija.proj;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


/**
 * Class bus extends vehicle so it is a special type of vehicle. This kind of vehicle can maintain on Bus Lines only
 * and has special representation on map - a circle.
 */
public class Bus extends Vehicle {

    /**
     * Bus constructor
     * Method creates new instance of Bus and creates a GUI object on canvas.
     * @param position Coordinate of bus within map
     * @param speed Movement speed of given bus
     * @param onLine Line on which bus transports people
     * @param identifier Unique bus name
     * @param street Dynamically changing street, on which bus currently locates
     * @param firstStop First stop on which bus stops
     * @param controller Main controller
     */
    public Bus(Coordinate position, double speed, Line onLine, String identifier, Street street, Stop firstStop, Controller controller) {
        super(position, speed, onLine, identifier, street, firstStop, controller);

        super.setInitialStop();
        super.setStops();
        Circle singleBus = new Circle(position.getX(), position.getY(), 8, Color.rgb(50, 50, 50));
        singleBus.setStroke(onLine.getColor());
        singleBus.setId(super.getName());
        super.GUI.add(singleBus);
    }
}
