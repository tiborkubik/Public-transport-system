package ija.proj;

import javafx.scene.shape.Shape;

import java.time.LocalTime;
import java.util.List;

public class Vehicle implements Drawable, UpdateState {
    private Coordinate position;
    private double speed;
    protected List<Shape> GUI;

    public Vehicle(Coordinate position, double speed) {
        this.position = position;
        this.speed = speed;
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
