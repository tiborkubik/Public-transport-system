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
    private List<Shape> GUI;

    public Vehicle(Coordinate position, double speed) {
        this.position = position;
        this.speed = speed;
        GUI = new ArrayList<>();
        GUI.add(new Circle(position.getX(), position.getY(), 8, Color.BLACK));
    }

    // TODO
    private void modifyGUI(Coordinate coordinate) {

    }

    @Override
    public List<Shape> getGUI() {
        return GUI;
    }

    @Override
    public void update(LocalTime time) {

    }
}
