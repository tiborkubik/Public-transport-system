package ija.proj;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Subway extends Vehicle {

    public Subway(Coordinate position, double speed, Line onLine, String identifier, Street street, Stop firstStop) {
        super(position, speed, onLine, identifier, street, firstStop);

        super.setStops();

        //super.GUI = new ArrayList<>();
        Rectangle singleSub = new Rectangle();
        singleSub.setX(position.getX() - 6);
        singleSub.setY(position.getY() - 12);
        singleSub.setWidth(12);
        singleSub.setHeight(24);
        singleSub.setFill(Color.rgb(50, 50, 50));
        singleSub.setId(super.getName());
        super.GUI.add(singleSub);
    }}
