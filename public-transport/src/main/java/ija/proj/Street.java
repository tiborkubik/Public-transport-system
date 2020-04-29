package ija.proj;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
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

        coordinatesList.add(start);
        coordinatesList.add(end);
    }

    public Street(){

    }

    /** Method iterates through list of drawable elements containing streets and tries to find out street with given name
     *
     * @param listStreet List of streets in which a street is looked for
     * @param identifier Name of street which will be searched in given list
     * @return found street, if not found null
     */
    public void findStreetByName(List<Drawable> listStreet, String identifier) {
        for(Drawable street : listStreet) {
            Street typedStreet = (Street) street;
            if(typedStreet.getName().equals(identifier)) {
                this.identifier = typedStreet.getName();
                this.start = typedStreet.begin();
                this.end = typedStreet.end();

                coordinatesList.add(this.start);
                coordinatesList.add(this.end);
            }
        }
    }

    public Coordinate begin() {
        return start;
    }

    public Coordinate end() {
        return end;
    }

    public String getName() {
        return this.identifier;
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
        streetName.setFont(Font.font ("Impact", 12));

        streetName.setFill(Color.GRAY);
        singleStreet.setStroke(Color.rgb(125, 125, 125));
        singleStreet.setStrokeWidth(6);
        return Arrays.asList(singleStreet, streetName);
    }
}
