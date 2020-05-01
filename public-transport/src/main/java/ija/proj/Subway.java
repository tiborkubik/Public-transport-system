package ija.proj;

import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Subway extends Vehicle {

    public Subway(Coordinate position, double speed) {
        super(position, speed);

        super.GUI = new ArrayList<>();
        Rectangle singleSub = new Rectangle();
        singleSub.setX(position.getX() - 5);
        singleSub.setY(position.getY() - 10);
        singleSub.setWidth(10);
        singleSub.setHeight(20);
        super.GUI.add(singleSub);
    }}
