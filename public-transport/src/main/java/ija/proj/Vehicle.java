package ija.proj;

import javafx.scene.shape.Shape;

import java.time.LocalTime;
import java.util.*;

public class Vehicle implements Drawable, UpdateState {
    private Coordinate position;
    private double speed;
    private double constantSpeed;

    private double distance = 0;
    private String identifier;
    private Line onLine;
    protected List<Shape> GUI;
    private List<Coordinate> path = new ArrayList<>();
    private int cnt_time = 0;

    public Vehicle(Coordinate position, double speed, Line onLine, String identifier) {
        this.position = position;
        this.speed = speed;
        this.onLine = onLine;
        this.identifier = identifier;
        this.constantSpeed = speed;
    }

    public String getName() {
        return this.identifier;
    }

    public Line getLine() {
        return this.onLine;
    }

    protected void setStops() {
        LinkedHashMap<String, Coordinate> tempPath = this.onLine.getPath();

        tempPath.forEach((k,v)->{
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
        System.out.println(cnt_time);
        if (cnt_time >= 0){
            cnt_time--;
            if(cnt_time == 0){
                this.speed = this.constantSpeed;
            }
            else if (cnt_time < 50){
                this.speed = this.constantSpeed - this.constantSpeed* cnt_time/50;
            }
            else {
                return;
            }
        }

        distance += speed;

        double total = 0;
        for(int i = 0; i < path.size()-1; i++) {
            total += coordDistance(path.get(i), path.get(i+1));
        }

        if(distance > total) {
            return;
        }

        Coordinate newC = getNewCoord(distance, path);
        for(Stop stopOnRoute : this.onLine.getStopList()) {
            if(Math.abs(stopOnRoute.getCoordinate().diffX(newC)) < this.speed/2 && Math.abs(stopOnRoute.getCoordinate().diffY(newC)) < this.speed/2) {
                cnt_time = stopOnRoute.getTime();
            }
        }
        modifyGUI(newC);
        position = newC;
    }

}
