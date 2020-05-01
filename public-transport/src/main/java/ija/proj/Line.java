package ija.proj;

//import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Line implements Drawable  {
    private String identifier;
    private String type;  // tram, sub, bus..
    private List<Street> streetList = new ArrayList<>();
    private List<Stop> stopList = new ArrayList<>();

    private List<Coordinate> start_c = new ArrayList<>();
    private List<Coordinate> end_c = new ArrayList<>();
    private javafx.scene.paint.Color col;


    public Line(String identifier, String type, List<Street> streetList, List<Stop> stopList) {

        this.identifier = identifier;
        this.type = type;
        this.streetList = streetList;
        this.stopList = stopList;
        set_color();
    }

    public void set_color(){

        Random random = new Random();
        final float hue = random.nextFloat();
        final Color color = Color.getHSBColor(hue, 1.0f, 1.0f);

        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        this.col = javafx.scene.paint.Color.rgb(r, g, b);
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

    public void addCoordinates(Coordinate start, Coordinate end) {
        start_c.add(start);
        end_c.add(end);
    }

    /***
     * sets type to the line
     * @param type
     */
    public void setType(String type) {
        this.type = type;
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
        for (Street street : streetList){

            Coordinate start = start_c.get(i);
            Coordinate end = end_c.get(i);
            if(street.getN_lines() == 1){
                int slope = (int)street.getSlope()*6;
                boolean s = start_c.get(i).change(start.getX()+slope,start.getY());
                boolean e =end_c.get(i).change(end.getX()+slope,end.getY());
                if (s ==false || e == false){
                    System.err.println("Error: failed to change coordinate. (Line.java)");
                }
            }
            javafx.scene.shape.Line singleStreet = new javafx.scene.shape.Line(start.getX(), start.getY(), end.getX(), end.getY());
            singleStreet.setStroke(this.col);
            singleStreet.setStrokeWidth(6);
            singleStreet.setSmooth(true);
            singleStreet.toFront();
            singleStreet.setStrokeLineCap(StrokeLineCap.ROUND);
            singleStreet.setStrokeLineJoin(StrokeLineJoin.ROUND);
            line.add(singleStreet);
            i++;
        }

        return line;
    }

}
