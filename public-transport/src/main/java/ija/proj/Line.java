package ija.proj;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

import java.time.LocalTime;
import java.util.*;


public class Line implements Drawable  {
    private String identifier;
    private String type;  // tram, sub, bus..
    private List<Street> streetList;
    private List<Stop> stopList;
    private LinkedHashMap<String, Coordinate> path = new LinkedHashMap<>();

    private List<Coordinate> start_c = new ArrayList<>();
    private List<Coordinate> end_c = new ArrayList<>();
    private List<LocalTime> timetable = new ArrayList<>();

    private javafx.scene.paint.Color col;

    public Line(String identifier, String type, List<Street> streetList, List<Stop> stopList) {
        this.identifier = identifier;
        this.type = type;
        this.streetList = streetList;
        this.stopList = stopList;
        setPath();

    }

    public String getType() {
        return type;
    }

    public double totalPathLength() {
        double total = 0;

        Collection<Coordinate> c = path.values();
        ArrayList<Coordinate> coords = new ArrayList<>(c);

        for(int i = 0; i < coords.size()-1; i++) {
            total += coords.get(i).coordDistance(coords.get(i+1));
        }
        return total;
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
            if(act_stop.getStreet().getName().equals(act_street.getName())){
//                System.out.println("stop: " + stopList.get(stop_id).getName());
                this.path.put("stop: " + stopList.get(stop_id).getName(),stopList.get(stop_id).getCoordinate());
                stop_id++;
            }
            else {
//                System.out.println("street: " + streetList.get(street_id).getName());
                this.path.put("street: " + streetList.get(street_id).getName(),streetList.get(street_id).end());
                street_id++;
            }

//            System.out.println(this.getName() + " " + this.path.get(i).toString());
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

    /***
     * Adds street to the line
     * @param street street to be added
     * @return if it was successful then returns true, else false
     */
    public boolean addStreet(Street street) {
        if(streetList.size() == 0) {
            return false;
        }

        Street lastInserted = streetList.get(streetList.size()-1);      // last inserted street

        if(street.follows(lastInserted)) {
            streetList.add(street);
            return true;
        } else {
            return false;
        }
    }

    public boolean streetInLineStreets(String streetName) {
        for(Street s : this.streetList) {
            if(s.getName() == streetName) {
                return true;
            }
        }
        return false;
    }

    public boolean isCircular() {
        Coordinate firstStreetStart = this.start_c.get(0);
        Coordinate lastStreetEnd = this.end_c.get(this.end_c.size());

        return firstStreetStart.equals(lastStreetEnd);
    }

    public void addCoordinates(Coordinate start, Coordinate end) {
        this.start_c.add(start);
        this.end_c.add(end);
    }

    /***
     * sets type to the line
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    public Color getColor() {
        return this.col;
    }

    /***
     * If successful then stop is added to stopList and it's street to streetList
     * @param stop stop to add
     * @return true if adding stop was successful, false if it wasn't
     */
    public boolean addStop(Stop stop) {
        if(stop.getStreet() == null) {
            return false;
        }
        if(stopList.size() == 0) {
            stopList.add(stop);
            streetList.add(stop.getStreet());
            return true;
        }

        if(!stop.getStreet().follows(streetList.get(streetList.size() - 1))) {
            return false;
        }
        stopList.add(stop);
        streetList.add(stop.getStreet());
        return true;
    }

    @Override
    public List<Shape> getGUI() {
        List<Shape> line  = new ArrayList<>();
        int i = 0;
        for (Street street : streetList) {

            Coordinate start = start_c.get(i);
            Coordinate end = end_c.get(i);
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
//                List<String> pathStops = new ArrayList<>();
//                List<Coordinate> pathVals = new ArrayList<>();
//
//                this.path.forEach((k,v)->{
//                    pathStops.add(k);
//                    pathVals.add(v);
//                });
//
//                for(int j = 0; j < pathStops.size()-1; j++) {
//                    if(pathStops.get(j).contains(street.getName())) {
//                        int k = j-1;
//                        if (k==-1)
//                            continue;
//                        while(!pathStops.get(k).contains("Street:") && k > 0) {
//                            this.path.put(pathStops.get(k), new Coordinate(pathVals.get(k).getX()+6, pathVals.get(k).getY()));
//                            --k;
//                            System.out.println("Toto posun");
//                        }
//                    }
//                }

            } else {
                boolean s = start.change(start.getX(), start.getY()-(6*n_lines));
                boolean e = end.change(end.getX(), end.getY()-(6*n_lines));
                if (!s || !e) {
                    System.err.println("Error: failed to change coordinate. (Line.java)");
                }

//                List<String> pathStops = new ArrayList<>();
//                List<Coordinate> pathVals = new ArrayList<>();
//
//                this.path.forEach((k,v)->{
//                    pathStops.add(k);
//                    pathVals.add(v);
//                });
//
//                for(int j = 0; j < pathStops.size()-1; j++) {
//                    if(pathStops.get(j).contains(street.getName())) {
//                        int k = j-1;
//                        if (k==-1)
//                            continue;
//
//                        while(!pathStops.get(k).contains("Street:") && k > 0) {
//                            this.path.put(pathStops.get(k), new Coordinate(pathVals.get(k).getX(), pathVals.get(k)getY()-(6*n_lines)));
//                            --k;
//                            System.out.println("Toto posun");
//                        }
//                    }
//                }


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

    public void addToTimetable(LocalTime l){
        timetable.add(l);
    }

    public List<LocalTime> getTimetable(){
        return timetable;
    }
}
