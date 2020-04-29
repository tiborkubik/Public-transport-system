package ija.proj;

import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;

public class Street implements Drawable {
    private String identifier;
    private Coordinate start;
    private Coordinate end;
    private List<Stop> stopList= new ArrayList<>();
    private List<Coordinate> coordinatesList= new ArrayList<>();

    public Street(String identifier, Coordinate start, Coordinate end) {
        this.identifier = identifier;
        this.start = start;
        this.end = end;
    }

    public Coordinate begin() {
        return start;
    }

    public Coordinate end() {
        return end;
    }

    public boolean follows(Street s) {
        Coordinate givenStart = s.begin();
        Coordinate givenEnd = s.end();

        if(start.diffX(givenStart) == 0 && start.diffY(givenStart) == 0) {
            return true;
        }
        if(start.diffX(givenEnd) == 0 && start.diffY(givenEnd) == 0) {
            return true;
        }
        if(end.diffX(givenStart) == 0 && end.diffY(givenStart) == 0) {
            return true;
        }
        if(end.diffX(givenEnd) == 0 && end.diffY(givenEnd) == 0) {
            return true;
        }

        return false;
    }

    public boolean addStop(Stop stop) {
        Coordinate stopCoordinate = stop.getCoordinate();
        int newStopX = stopCoordinate.getX();
        int newStopY = stopCoordinate.getY();

        int numberOfCoordinates = coordinatesList.size();

        for(int i = 0; i < numberOfCoordinates-1; i++) {
            if((coordinatesList.get(i).getX() == coordinatesList.get(i+1).getX()) && (coordinatesList.get(i+1).getX() == newStopX)) {
                int startingY = coordinatesList.get(i).getY();
                int endingY = coordinatesList.get(i+1).getY();

                if(startingY < endingY) {
                    if(newStopY >= startingY && newStopY <= endingY) {
                        stopList.add(stop);
                        stop.setStreet(this);
                        return true;
                    }
                } else {
                    if(newStopY <= startingY && newStopY >= endingY) {
                        stopList.add(stop);
                        stop.setStreet(this);
                        return true;
                    }
                }
            }
            if(coordinatesList.get(i).getY() == coordinatesList.get(i+1).getY() && (coordinatesList.get(i+1).getY() == newStopY)) {
                int startingX = coordinatesList.get(i).getX();
                int endingX = coordinatesList.get(i+1).getX();

                if(startingX < endingX) {
                    if(newStopX >= startingX && newStopX <= endingX) {
                        stopList.add(stop);
                        stop.setStreet(this);
                        return true;
                    }
                } else {
                    if(newStopX <= startingX && newStopX >= endingX) {
                        stopList.add(stop);
                        stop.setStreet(this);
                        return true;
                    }
                }

            }
        }
        return false;
    }

    @Override
    public List<Shape> getGUI() {
        Line singleStreet = new Line(start.getX(), start.getY(), end.getX(), end.getY());
        Text streetName = new Text(start.getX() + 10 + (Math.abs(start.diffX(end))/2), start.getY() - 10 + Math.abs(start.diffY(end))/2, identifier);
        return Arrays.asList(singleStreet, streetName);
    }
}
