package ija.proj;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


/**
 * Class subway extends vehicle so it is a special type of vehicle. This kind of vehicle can maintain on Subway Lines only
 * and has special representation on map - a rectangle.
 */
public class Subway extends Vehicle {

    public Subway(Coordinate position, double speed, Line onLine, String identifier, Street street, Stop firstStop, Controller controller) {
        super(position, speed, onLine, identifier, street, firstStop, controller);

        super.setInitialStop();
        super.setStops();

        Rectangle singleSub = new Rectangle();
        singleSub.setX(position.getX() - 6);
        singleSub.setY(position.getY() - 12);
        singleSub.setWidth(12);
        singleSub.setHeight(24);
        singleSub.setStroke(onLine.getColor());
        singleSub.setFill(Color.rgb(50, 50, 50));
        singleSub.setId(super.getName());
        super.GUI.add(singleSub);
    }}
