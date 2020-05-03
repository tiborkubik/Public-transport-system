package ija.proj;

import javafx.scene.shape.Shape;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Vehicle implements Drawable, UpdateState {
    private Coordinate position;
    private double speed = 1;
    private double distance = 0;
    private Line onLine;
    protected List<Shape> GUI;
    private List<Coordinate> path = new ArrayList<>();


    public Vehicle(Coordinate position, double speed, Line onLine) {
        this.position = position;
        this.speed = speed;
        this.onLine = onLine;

    }

    protected void setStops() {
        LinkedHashMap<String, Coordinate> tempPath = this.onLine.getPath();

        tempPath.forEach((k,v)->{
            System.out.println(v.getX() + " " + v.getY());
            this.path.add(v);
        });
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
