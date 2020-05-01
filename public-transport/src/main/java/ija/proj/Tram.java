package ija.proj;

import javafx.scene.shape.Polygon;

import java.util.ArrayList;

public class Tram extends Vehicle {
    public Tram(Coordinate position, double speed) {
        super(position, speed);

        super.GUI = new ArrayList<>();
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(new Double[]{
                (double)position.getX(), (double)position.getY(),
                (double)position.getX() - 8.0, (double)position.getY() - 20.0,
                (double)position.getX() + 8.0, (double)position.getY() - 20.0 });
        super.GUI.add(polygon);
    }
}
