package ija.proj;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Tram extends Vehicle {
    public Tram(Coordinate position, double speed, Line onLine, String identifier, Street street, Stop firstStop, Controller controller) {
        super(position, speed, onLine, identifier, street, firstStop, controller);

        super.setStops();

        //.GUI = new ArrayList<>();
        Polygon singleTram = new Polygon();
        singleTram.getPoints().addAll(new Double[]{
                (double)position.getX(), (double)position.getY() - 8,
                (double)position.getX() + 8, (double)position.getY(),
                (double)position.getX(), (double)position.getY() + 8,
                (double)position.getX() - 8, (double)position.getY(),
                });
        singleTram.setFill(Color.rgb(50, 50, 50));
        singleTram.setId(super.getName());
        super.GUI.add(singleTram);
    }
}
