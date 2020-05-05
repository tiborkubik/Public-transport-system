package ija.proj;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Bus extends Vehicle {

    public Bus(Coordinate position, double speed, Line onLine, String identifier) {
        super(position, speed, onLine, identifier);

        super.setStops();
        Circle singleBus = new Circle(position.getX(), position.getY(), 8, Color.rgb(50, 50, 50));
        singleBus.setId(super.getName());
        super.GUI.add(singleBus);
    }
}
