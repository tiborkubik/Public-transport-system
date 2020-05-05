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
    protected List<Shape> GUI = new ArrayList<>();
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
        try{
            for(Shape shape : GUI) {
                shape.setTranslateX(coordinate.getX() - position.getX() + shape.getTranslateX());
                shape.setTranslateY(coordinate.getY() - position.getY() + shape.getTranslateY());
            }
        } catch (Exception e) {
            System.out.println("Accessing unexisting element");
        }
    }

    @Override
    public List<Shape> getGUI() {
        return GUI;
    }


    public Coordinate getNewCoord(double distance, List<Coordinate> path) {
        double length = 0;
        double currentLength = 0;
        Coordinate a = null;
        Coordinate b = null;

        for(int i = 0; i < path.size() - 1; i++) {
            a = path.get(i);
            b = path.get(i + 1);
            currentLength = a.coordDistance(b);
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

    public double totalPathLength() {
        double total = 0;
        for(int i = 0; i < this.path.size()-1; i++) {
            total += this.path.get(i).coordDistance(this.path.get(i+1));
        }
        return total;
    }

    @Override
    public void update(LocalTime time) {
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

        double total = totalPathLength();
        if(distance > total) {
            return;
        }

        Coordinate newC = getNewCoord(distance, path);
        for(Stop stopOnRoute : this.onLine.getStopList()) {
            if(Math.abs(stopOnRoute.getCoordinate().diffX(newC)) < this.speed/2 && Math.abs(stopOnRoute.getCoordinate().diffY(newC)) < this.speed/2) {
                cnt_time = stopOnRoute.getTime();
            }
        }

        for(Street s : this.onLine.getStreetList()) {
            if(Math.abs(s.begin().diffX(newC)) < this.speed/2 && Math.abs(s.begin().diffY(newC)) < this.speed/2) {
                GUI.get(0).setRotate(90-90*s.getSlope());
                System.out.println(s.getSlope());
            }
        }
        modifyGUI(newC);
        position = newC;
    }

}
