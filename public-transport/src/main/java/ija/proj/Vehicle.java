package ija.proj;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Vehicle implements Drawable, UpdateState {
    private Coordinate position;
    private double speed;
    private double distance = 0;

    protected List<Shape> GUI;

    public Vehicle(Coordinate position, double speed) {
        this.position = position;
        this.speed = speed;
    }

    // TODO
    private void modifyGUI(Coordinate coordinate) {
        for(Shape shape : GUI) {
            shape.setTranslateX(coordinate.getX() - position.getX() + shape.getTranslateX());
            shape.setTranslateY(coordinate.getY() - position.getY() + shape.getTranslateY());
        }
    }

    @Override
    public List<Shape> getGUI() {
        return GUI;
    }

    public double coordDistance(Coordinate a, Coordinate b) {
        return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
    }

    public Coordinate getNewCoord(double distance, Coordinate a, Coordinate b) {

        double currentLength;

        currentLength = coordDistance(a, b);

        double driven = (distance) / currentLength;
        return new Coordinate(a.getX() + (b.getX() - a.getX()) * driven, a.getY() + (b.getY() - a.getY()) * driven);
    }

    @Override
    public void update(Coordinate a, Coordinate b) {
        distance += speed;


        double total = coordDistance(a, b);
        if(distance > total) {
            distance = 0;
            return;
        }

        Coordinate newC = getNewCoord(distance, a, b);
        modifyGUI(newC);
        position = newC;
    }
}
