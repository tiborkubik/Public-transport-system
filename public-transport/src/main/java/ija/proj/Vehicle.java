package ija.proj;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Vehicle implements Drawable, UpdateState {
    private Coordinate position;
    private double speed = 1;
    private double distance = 0;
    private List<Coordinate> path = new ArrayList();

    protected List<Shape> GUI;

    public Vehicle(Coordinate position, double speed) {
        this.position = position;
        this.speed = speed;

        this.path.add(new Coordinate(50, 400));
        this.path.add(new Coordinate(400, 900));
        this.path.add(new Coordinate(800, 900));
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

    public Coordinate getNewCoord(double distance, List<Coordinate> path) {
        double length = 0;
        double currentLength = 0;
        Coordinate a = null;
        Coordinate b = null;

        for(int i = 0; i < path.size() - 1; i++) {
            a = path.get(i);
            b = path.get(i + 1);
            currentLength = coordDistance(a, b);
            if(length + currentLength >= distance) {
                break;
            }
            length += currentLength;
        }
        if(a == null || b == null) {
            return null;
        }

        double driven = (distance - length) / currentLength;
        return new Coordinate(a.getX() + (b.getX() - a.getX()) * driven, a.getY() + (b.getY() - a.getY()) * driven);
    }

    @Override
    public void update(LocalTime time) {
        distance += speed;

        double total = 0;
        for(int i = 0; i < path.size()-1; i++) {
            total += coordDistance(path.get(i), path.get(i+1));
        }

        if(distance > total) {
            return;
        }

        Coordinate newC = getNewCoord(distance, path);
        modifyGUI(newC);
        position = newC;
    }
}
