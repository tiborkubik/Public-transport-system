package ija.proj;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

import java.time.LocalTime;
import java.util.*;


/**
 * Class line defines one line of town, eg. Metro Line A, Bus Line n. 67, etc.
 * Line is defined by streets and stops together with a timetable of departures.
 * Each line within one map has special color. Type of line defines what kind of vehicles are used - trams, subways, buses..
 */
public class Line implements Drawable  {
    private String identifier;                                                  /**< Unique line identifier */
    private String type;                                                        /**< Type of line - Bus/Tram/Subway */
    private javafx.scene.paint.Color col;                                       /**< Unique color of given line */
    private List<LocalTime> timetable = new ArrayList<>();                      /**< Timetable of scheduled departures */

    private List<Street> streetList;                                            /**< List of all streets that define given line */
    private List<Stop> stopList;                                                /**< List of all stops that define given line */
    private LinkedHashMap<String, Coordinate> path = new LinkedHashMap<>();     /**< Path of a line - stops + end of lines */
    private List<Coordinate> streetsBegins = new ArrayList<>();                 /**< List of all stats of lines */
    private List<Coordinate> streetsEnds = new ArrayList<>();                   /**< List of all ends of lines */


    public Line(String identifier, String type, List<Street> streetList, List<Stop> stopList) {
        this.identifier = identifier;
        this.type = type;
        this.streetList = streetList;
        this.stopList = stopList;
        setPath();

    }

    public void setStreets(List<Street> newStreets, List<Street> toAdd, Street detoured){
        this.streetList = newStreets;
        this.setPathDetour(toAdd, detoured);
    }

    public String getType() {
        return type;
    }

    public void deleteStops(Street s){
        stopList.removeIf(stp -> stp.getStreet().getName().equals(s.getName()));
    }

    public double totalPathLength() {
        double total = 0;

        Collection<Coordinate> c = path.values();
        ArrayList<Coordinate> coords = new ArrayList<>(c);

        for(int i = 0; i < coords.size()-1; i++) {
            total += coords.get(i).coordsDistance(coords.get(i+1));
        }
        return total;
    }

    private void setPathDetour(List<Street> toAdd, Street detoured){
        LinkedHashMap<String, Coordinate> newPath = new LinkedHashMap<>();

        path.forEach((k,v)-> {
            if(v.equals(detoured.begin())) {
                newPath.put(k, v);
                for(Street s : toAdd)
                   newPath.put("Street " + s.getName(), s.end());
            }
            else if(v.liesOn(detoured)){
            }
            else
                newPath.put(k, v);
        });
        this.path = newPath;
    }

    private void setPath(){
        int n_places = stopList.size()+streetList.size();
        int street_id =0;
        int stop_id =0;
        this.path.put("street " + streetList.get(0).getName(),streetList.get(0).begin());

        for (int i = 0; i < n_places; i++) {

            Street act_street = new Street();
            Stop act_stop = new Stop();

            try {
                act_street = streetList.get(street_id);
                act_stop = stopList.get(stop_id);
            } catch (Exception e) {
                act_stop.setStreet(streetList.get(0));
            }

            if (act_street.getNStops() == 0) {
                this.path.put("street: " + streetList.get(street_id).getName(), streetList.get(street_id).end());
                street_id++;
            } else {
                if(act_stop.getStreet().getName().equals(act_street.getName())){
                    this.path.put("stop: " + stopList.get(stop_id).getName(),stopList.get(stop_id).getCoordinate());
                    stop_id++;
                } else {
                    this.path.put("street: " + streetList.get(street_id).getName(),streetList.get(street_id).end());
                    street_id++;
                }
            }
        }
    }

    public LinkedHashMap<String, Coordinate> getPath() {
        return this.path;
    }

    public void setColor(Color c) {
        this.col = c;
    }

    public String getName() {
        return this.identifier;
    }

    public List<Stop> getStopList() {
        return this.stopList;
    }

    public List<Street> getStreetList() {
        return this.streetList;
    }


    public void addToTimetable(LocalTime l){
        timetable.add(l);
    }

    public List<LocalTime> getTimetable(){
        return timetable;
    }

    public void addCoordinates(Coordinate start, Coordinate end) {
        this.streetsBegins.add(start);
        this.streetsEnds.add(end);
    }

    public void setType(String type) {
        this.type = type;
    }

    public Color getColor() {
        return this.col;
    }

    @Override
    public List<Shape> getGUI() {
        List<Shape> line  = new ArrayList<>();
        int i = 0;
        for (Street street : streetList) {

            Coordinate start = streetsBegins.get(i);
            Coordinate end = streetsEnds.get(i);
            int n_lines = street.getN_lines();


            //fix endings when last street was shifted by n*6
            if(i > 0 && streetList.get(i-1).getN_lines() != 0 && streetList.get(i-1).getSlope() != street.getSlope()) {
                if(streetList.get(i-1).getSlope() == 2.0) {
                    start.setX(start.getX() + 6 *( streetList.get(i-1).getN_lines()));
                }
                if(streetList.get(i-1).getSlope() == 0.0) {
                    start.setY(start.getY() - 6 * (streetList.get(i-1).getN_lines()));
                }
            }

            if(i > 0 && streetList.get(i-1).getN_lines() == 0 && streetList.get(i-1).getSlope() != street.getSlope()) {
                if(streetList.get(i-1).getSlope() == 0.0) {

                    start.setX(start.getX() + 6 * streetList.get(i-1).getN_lines());
                }
            }

            if(i < streetList.size() - 1 && streetList.get(i+1).getN_lines() == 1) {
                if(streetList.get(i+1).getSlope() != 2.0) {
                    end.setX(end.getX() + 6 * streetList.get(i+1).getN_lines());
                }
                if(streetList.get(i+1).getSlope() == 2.0 && streetList.get(i).getSlope() == 0.0) {
                    end.setX(end.getX() + 6 * streetList.get(i+1).getN_lines());
                }
            }
            if(i < streetList.size() - 1 && streetList.get(i+1).getN_lines() != 1) {
                if (streetList.get(i + 1).getSlope() == 2.0 && streetList.get(i).getSlope() == 0.0) {
                    end.setX(end.getX() + 6 * streetList.get(i + 1).getN_lines());
                }
            }

            if(street.getSlope() != 0.0 && street.getN_lines() != 0) {
                boolean s = start.change(start.getX() + (6), start.getY());
                boolean e = end.change(end.getX() + (6), end.getY());

                if (!s || !e) {
                    System.err.println("Error: failed to change coordinate. (Line.java)");
                }
            } else {
                boolean s = start.change(start.getX(), start.getY()-(6*n_lines));
                boolean e = end.change(end.getX(), end.getY()-(6*n_lines));
                if (!s || !e) {
                    System.err.println("Error: failed to change coordinate. (Line.java)");
                }
            }

            javafx.scene.shape.Line singleStreet = new javafx.scene.shape.Line(start.getX(), start.getY(), end.getX(), end.getY());
            singleStreet.setStroke(this.col);
            singleStreet.setStrokeWidth(6);
            singleStreet.setSmooth(true);
            singleStreet.toFront();
            String id = street.getName()+this.identifier;
            singleStreet.setId(id);
            singleStreet.setStrokeLineCap(StrokeLineCap.ROUND);
            singleStreet.setStrokeLineJoin(StrokeLineJoin.ROUND);
            line.add(singleStreet);
            i++;
        }

        return line;
    }
}
