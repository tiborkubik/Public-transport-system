package mapData;

import vehicles.Drawable;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Class line defines one line of town, eg. Metro Line A, Bus Line n. 67, etc.
 * Line is defined by streets and stops together with a timetable of departures.
 * Each line within one map has special color. Type of line defines what kind of vehicles are used - trams, subways, buses..
 */
public class Line implements Drawable {
    private String identifier;                              /**< Unique line identifier */
    private String type;                                    /**< Type of line - Bus/Tram/Subway */
    private javafx.scene.paint.Color col;                   /**< Unique color of given line */
    private List<LocalTime> timetable = new ArrayList<>();  /**< Timetable of scheduled departures */
    private List<Street> streetList;                        /**< List of all streets that define given line */
    private List<Stop> stopList;                            /**< List of all stops that define given line */
    private LinkedHashMap<String, Coordinate> path = new LinkedHashMap<>(); /**< Path of a line = stops + end of lines */
    private int delay = 0;
    private List<Coordinate> streetsBegins = new ArrayList<>();     /**< List of all stats of lines */
    private List<Coordinate> streetsEnds = new ArrayList<>();       /**< List of all ends of lines */


    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay += delay;
    }

    /**
     * Method creates a new line of public transport system in given city
     *
     * @param identifier Unique name of line
     * @param type       Specifies what kind of vehicles are transporting people, e.g Bus, Tram etc
     * @param streetList List of streets that define given line - through these streets line passes
     * @param stopList   List of stops on which vehicles of this line stop
     */
    public Line(String identifier, String type, List<Street> streetList, List<Stop> stopList) {
        this.identifier = identifier;
        this.type = type;
        this.streetList = streetList;
        this.stopList = stopList;
        setPath();
    }

    /**
     * Method sets new streets that define given line when a detour is specified
     *
     * @param newStreets List of streets that are used as a detour streets
     * @param toAdd      List of old streets together with inserted new streets
     * @param detoured   A street that is being detoured
     */
    public double setStreets(List<Street> newStreets, List<Street> toAdd, Street detoured) {
        double pathBeforeDetouring = totalPathLength();
        int nStops = detoured.getNStops();
        this.streetList = newStreets;
        double diff = this.setPathDetour(toAdd, detoured, pathBeforeDetouring, nStops);
        return  diff;
    }

    /**
     * Method fills an attribute path according to stopList and streetList
     */
    private void setPath() {
        int nPlaces = stopList.size() + streetList.size();
        int streetId = 0;
        int stopId = 0;
        this.path.put("street " + streetList.get(0).getName(), streetList.get(0).begin());

        for (int i = 0; i < nPlaces; i++) {
            Street actStreet = new Street();
            Stop actStop = new Stop();

            try {
                actStreet = streetList.get(streetId);
                actStop = stopList.get(stopId);
            } catch (Exception e) {
                actStop.setStreet(streetList.get(0));
            }

            if (actStreet.getNStops() == 0) {
                this.path.put("street: " + streetList.get(streetId).getName(), streetList.get(streetId).end());
                streetId++;
            } else {
                if (actStop.getStreet().getName().equals(actStreet.getName())) {
                    this.path.put("stop: " + stopList.get(stopId).getName(), stopList.get(stopId).getCoordinate());
                    stopId++;
                } else {
                    this.path.put("street: " + streetList.get(streetId).getName(), streetList.get(streetId).end());
                    streetId++;
                }
            }
        }
    }

    /**
     * @return Path of line in a form of Map
     */
    public LinkedHashMap<String, Coordinate> getPath() {
        return this.path;
    }

    /**
     * @return Identifier of line
     */
    public String getName() {
        return this.identifier;
    }

    /**
     * @return List of all stops that define line
     */
    public List<Stop> getStopList() {
        return this.stopList;
    }

    /**
     * @return List of all streets that define line
     */
    public List<Street> getStreetList() {
        return this.streetList;
    }

    /**
     * @return Color value of line
     */
    public Color getColor() {
        return this.col;
    }

    /**
     * Method sets color of line
     *
     * @param c New color of line
     */
    public void setColor(Color c) {
        this.col = c;
    }

    /**
     * Method returns type of vehicles that transport people on given line
     *
     * @return Type of line, e.g Tram, Subway..
     */
    public String getType() {
        return type;
    }

    /**
     * Method sets type into a line
     *
     * @param type Type of line, e.g sub, tram, bus
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return Timetable of given line
     */
    public List<LocalTime> getTimetable() {
        return timetable;
    }

    /**
     * Method deletes stops from stopList on specific street that was defined when constructing line.
     * This method is used when creating a detour - vehicles of given line no longer stop on stops on detoured street.
     *
     * @param s Street, where stops which are going to be deleted are located
     */
    public void deleteStops(Street s) {
        stopList.removeIf(stp -> stp.getStreet().getName().equals(s.getName()));
    }

    /**
     * Method calculates length from the beginning to the end of line in pixels
     *
     * @return Length of line
     */
    public double totalPathLength() {
        double total = 0;

        Collection<Coordinate> c = path.values();
        ArrayList<Coordinate> coords = new ArrayList<>(c);

        for (int i = 0; i < coords.size() - 1; i++) {
            total += coords.get(i).coordsDistance(coords.get(i + 1));
        }
        return total;
    }

    /**
     * Method defines new path of line when admin creates a detour
     *
     * @param toAdd    List of streets that are going to define line after detour
     * @param detoured Detoured street
     */
    private double setPathDetour(List<Street> toAdd, Street detoured, double pathBeforeDetouring, int nStops) {
        LinkedHashMap<String, Coordinate> newPath = new LinkedHashMap<>();

        path.forEach((k, v) -> {
            if (v.equals(detoured.begin())) {
                newPath.put(k, v);
                for (Street s : toAdd)
                    newPath.put("Street " + s.getName(), s.end());
            } else {
                if (!v.liesOn(detoured)) {
                    newPath.put(k, v);
                }
            }
        });
        this.path = newPath;

        double afterAddingDetour = totalPathLength();

        double diff = afterAddingDetour - pathBeforeDetouring;// - pathBeforeDetouring-(nStops*);
        return diff;
    }




    /**
     * Method adds a time into line's timetable
     * After a time is set into its timetable, vehicles will begin its tour at that time from the beginning of first street
     *
     * @param l New time that will be put into timetable
     */
    public void addToTimetable(LocalTime l) {
        timetable.add(l);
    }

    /**
     * Method adds start and end of a line into auxiliary lists
     *
     * @param start Start coordinate of a street
     * @param end   End coordinate of a street
     */
    public void addCoordinates(Coordinate start, Coordinate end) {
        this.streetsBegins.add(start);
        this.streetsEnds.add(end);
    }

    @Override
    public List<Shape> getGUI() {
        List<Shape> line = new ArrayList<>();
        int i = 0;
        for (Street street : streetList) {

            Coordinate start = streetsBegins.get(i);
            Coordinate end = streetsEnds.get(i);
            int n_lines = street.getNLines();

            //fix endings when last street was shifted by n*6
            if (i > 0 && streetList.get(i - 1).getNLines() != 0 && streetList.get(i - 1).getSlope() != street.getSlope()) {
                if (streetList.get(i - 1).getSlope() == 2.0) {
                    start.setX(start.getX() + 6 * (streetList.get(i - 1).getNLines()));
                }
                if (streetList.get(i - 1).getSlope() == 0.0) {
                    start.setY(start.getY() - 6 * (streetList.get(i - 1).getNLines()));
                }
            }
            if (i > 0 && streetList.get(i - 1).getNLines() == 0 && streetList.get(i - 1).getSlope() != street.getSlope()) {
                if (streetList.get(i - 1).getSlope() == 0.0) {

                    start.setX(start.getX() + 6 * streetList.get(i - 1).getNLines());
                }
            }
            if (i < streetList.size() - 1 && streetList.get(i + 1).getNLines() == 1) {
                if (streetList.get(i + 1).getSlope() != 2.0) {
                    end.setX(end.getX() + 6 * streetList.get(i + 1).getNLines());
                }
                if (streetList.get(i + 1).getSlope() == 2.0 && streetList.get(i).getSlope() == 0.0) {
                    end.setX(end.getX() + 6 * streetList.get(i + 1).getNLines());
                }
            }
            if (i < streetList.size() - 1 && streetList.get(i + 1).getNLines() != 1) {
                if (streetList.get(i + 1).getSlope() == 2.0 && streetList.get(i).getSlope() == 0.0) {
                    end.setX(end.getX() + 6 * streetList.get(i + 1).getNLines());
                }
            }

            if (street.getSlope() != 0.0 && street.getNLines() != 0) {
                boolean s = start.change(start.getX() + (6), start.getY());
                boolean e = end.change(end.getX() + (6), end.getY());

                if (!s || !e) {
                    System.err.println("Error: failed to change coordinate. (Line.java)");
                }
            } else {
                boolean s = start.change(start.getX(), start.getY() - (6 * n_lines));
                boolean e = end.change(end.getX(), end.getY() - (6 * n_lines));
                if (!s || !e) {
                    System.err.println("Error: failed to change coordinate. (Line.java)");
                }
            }

            javafx.scene.shape.Line singleStreet = new javafx.scene.shape.Line(start.getX(), start.getY(), end.getX(), end.getY());
            singleStreet.setStroke(this.col);
            singleStreet.setStrokeWidth(6);
            singleStreet.setSmooth(true);
            singleStreet.toFront();
            String id = street.getName() + this.identifier;
            singleStreet.setId(id);
            singleStreet.setStrokeLineCap(StrokeLineCap.ROUND);
            singleStreet.setStrokeLineJoin(StrokeLineJoin.ROUND);
            line.add(singleStreet);
            i++;
        }
        return line;
    }
}
