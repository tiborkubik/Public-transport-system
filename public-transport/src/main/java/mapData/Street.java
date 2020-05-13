package mapData;

import vehicles.Drawable;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/***
 * class Street represents one street - from start to end point. Each street has unique identifier and stops that are located on it.
 */
public class Street implements Drawable {
    private String identifier;                  /**< Unique street name */
    private Coordinate start;                   /**< Start coordinate of street */
    private Coordinate end;                     /**< End of street */
    private List<Stop> stopList = new ArrayList<>();                /**< List of all stops located on street */
    private List<Coordinate> coordinatesList = new ArrayList<>();   /**< List of all coordinates of street  */
    private int n_lines = 0;                 /**< Number of lines passing street */
    private int trafficDensity = 1;         /**<  Traffic density on given street. from 1 to 9, 1 is normal flow */


    /**
     * Constructor of street. Start and end coordinate are added into coordinates list
     *
     * @param identifier    Unique street name
     * @param start         Start coordinate
     * @param end           End coordinate
     */
    public Street(String identifier, Coordinate start, Coordinate end) {
        this.identifier = identifier;
        this.start = start;
        this.end = end;

        coordinatesList.add(start);
        coordinatesList.add(end);
    }

    /**
     * Empty constructor
     */
    public Street() {

    }


    /**
     * @return returns number of stops on given street
     */
    public int getNStops() {
        return this.stopList.size();
    }

    /**
     * @return Value of traffic density
     */
    public int getTrafficDensity() {
        return trafficDensity;
    }

    /**
     * Method sets traffic density on given street
     *
     * @param trafficDensity Value of traffic flow, from 1 to 9, 1 is normal, 9 is worst
     */
    public void setTrafficDensity(int trafficDensity) {
        this.trafficDensity = trafficDensity;
    }

    /**
     * @return Number of lines passing street
     */
    public int getNLines() {
        return this.n_lines;
    }

    /**
     * Increments number of lines attribute when new line passes through street
     */
    public void addLine() {
        this.n_lines++;
    }

    /***
     * Method iterates through list of drawable elements containing streets and tries to find out street with given name
     * @param listStreet List of streets in which a street is looked for
     * @param identifier Name of street which will be searched in given list
     * @return found street, if not found null
     */
    public void findStreetByName(List<Drawable> listStreet, String identifier) {
        for (Drawable street : listStreet) {
            Street typedStreet = (Street) street;
            if (typedStreet.getName().equals(identifier)) {
                this.identifier = typedStreet.getName();
                this.start = typedStreet.begin();
                this.end = typedStreet.end();
                this.n_lines = typedStreet.getNLines();

                coordinatesList.add(this.start);
                coordinatesList.add(this.end);
            }
        }
    }


    /**
     * @return Begin coordinate
     */
    public Coordinate begin() {
        return start;
    }

    /**
     * @return End coordinate
     */
    public Coordinate end() {
        return end;
    }

    /**
     * @return Unique street name
     */
    public String getName() {
        return this.identifier;
    }

    /***
     * Check if street s follows current this.street
     * @param s street
     * @return true if streets follows or false if they don't
     */
    public boolean follows(Street s) {
        Coordinate givenStart = s.begin();
        Coordinate givenEnd = s.end();

        if (start.diffX(givenStart) == 0 && start.diffY(givenStart) == 0) {
            return true;
        }
        if (start.diffX(givenEnd) == 0 && start.diffY(givenEnd) == 0) {
            return true;
        }
        if (end.diffX(givenStart) == 0 && end.diffY(givenStart) == 0) {
            return true;
        }
        return end.diffX(givenEnd) == 0 && end.diffY(givenEnd) == 0;
    }

    /***
     * Adds stop if it lies on the street
     * @param stop stop to add
     * @return returns true if the stop lies on the street, false if they don't
     */
    public boolean addStop(Stop stop) {
        Coordinate stopCoordinate1 = stop.getCoordinate();

        double distance = distance(start, end);     // original distance of the street
        double distance_with_stop = distance(start, stopCoordinate1) + distance(stopCoordinate1, end); //distance of the street trought the new stop
        double delta = 0.00001;     // allowed distance from the line (proportional to street length)
        //check if the distances and approximately the same
        if (delta > Math.abs(distance - distance_with_stop) / Math.max(Math.abs(distance), Math.abs(distance_with_stop))) {
            stopList.add(stop);
            stop.setStreet(this);
            return true;
        }
        return false;
    }

    /***
     * distance between 2 coordinates
     * @param a first coordinate
     * @param b second coordinate
     * @return distance
     */
    private double distance(Coordinate a, Coordinate b) {
        return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
    }

    /**
     * @return Slope of street
     */
    public double getSlope() {
        // vertical line has special value
        if ((this.end.getX() - this.start.getX()) == 0.0) {
            return 2.0;
        }
        return (this.end.getY() - this.start.getY()) / (this.end.getX() - this.start.getX());
    }

    @Override
    public List<Shape> getGUI() {
        Line singleStreet = new Line(start.getX(), start.getY(), end.getX(), end.getY());

        Text streetName;
        if (start.getY() == end.getY()) {
            streetName = new Text((start.getX() + end.getX()) / 2 - 20, (start.getY() + end.getY()) / 2 - 10, identifier);
        } else if (start.getX() == end.getX()) {
            streetName = new Text((start.getX() + end.getX()) / 2 + 20, (start.getY() + end.getY()) / 2 - 10, identifier);
        } else if (start.getX() > end.getX() && start.getY() > end.getY()) {
            streetName = new Text((start.getX() + end.getX()) / 2 + 20, (start.getY() + end.getY()) / 2 - 10, identifier);
        } else if (start.getX() > end.getX() && start.getY() < end.getY()) {
            streetName = new Text((start.getX() + end.getX()) / 2 + 30, (start.getY() + end.getY()) / 2 - 10, identifier);
        } else if (start.getX() < end.getX() && start.getY() > end.getY()) {
            streetName = new Text((start.getX() + end.getX()) / 2 + 40, (start.getY() + end.getY()) / 2 - 10, identifier);
        } else {
            streetName = new Text((start.getX() + end.getX()) / 2 + 10, (start.getY() + end.getY()) / 2 - 10, identifier);
        }

        streetName.setFont(Font.font("SimSun", 12));
        streetName.setFill(Color.GRAY);
        singleStreet.setStroke(Color.rgb(150, 150, 150));
        singleStreet.setStrokeWidth(6);
        singleStreet.setSmooth(true);
        singleStreet.toFront();
        singleStreet.setStrokeLineCap(StrokeLineCap.ROUND);
        singleStreet.setStrokeLineJoin(StrokeLineJoin.ROUND);
        singleStreet.setId(identifier);
        return Arrays.asList(singleStreet, streetName);
    }

}
