package ija.proj;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;

public class Tram extends Vehicle {
    public Tram(Coordinate position, double speed, Line onLine, String identifier) {
        super(position, speed, onLine, identifier);

        super.setStops();

        super.GUI = new ArrayList<>();
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(new Double[]{
                (double)position.getX(), (double)position.getY(),
                (double)position.getX() - 8.0, (double)position.getY() - 20.0,
                (double)position.getX() + 8.0, (double)position.getY() - 20.0 });
        polygon.setFill(Color.rgb(50, 50, 50));
        super.GUI.add(polygon);
    }
}
