package ija.proj;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;

public class Tram extends Vehicle {
    public Tram(Coordinate position, double speed, Line onLine, String identifier) {
        super(position, speed, onLine, identifier);

        super.setStops();

        super.GUI = new ArrayList<>();
        Polygon singleTram = new Polygon();
        singleTram.getPoints().addAll(new Double[]{
                (double)position.getX(), (double)position.getY() + 10.0,
                (double)position.getX() - 8.0, (double)position.getY() - 10.0,
                (double)position.getX() + 8.0, (double)position.getY() - 10.0 });
        singleTram.setFill(Color.rgb(50, 50, 50));
        singleTram.setId(super.getName());
        super.GUI.add(singleTram);
    }
}
