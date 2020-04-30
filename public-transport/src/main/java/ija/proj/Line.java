package ija.proj;

import java.util.ArrayList;
import java.util.List;

public class Line {
    private String identifier;
    private String type;  // tram, sub, bus..
    private List<Street> streetList = new ArrayList<>();
    private List<Stop> stopList = new ArrayList<>();

    public Line(String identifier, String type, List<Street> streetList, List<Stop> stopList) {
        this.identifier = identifier;
        this.type = type;
        this.streetList = streetList;
        this.stopList = stopList;
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

}
