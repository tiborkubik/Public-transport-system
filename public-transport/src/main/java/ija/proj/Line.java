package ija.proj;

import java.util.ArrayList;
import java.util.List;

public class Line {
    private String identifier;
    private String type;  // tram, sub, bus..
    private List<Street> streetList= new ArrayList<>();
    private List<Stop> stopList= new ArrayList<>();

    public Line(String identifier, String type, List<Street> streetList, List<Stop> stopList) {
        this.identifier = identifier;
        this.type = type;
        this.streetList = streetList;
        this.stopList = stopList;
    }

    public List<Stop> getStopList() {
        return this.stopList;
    }

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

    public void setType(String type) {
        this.type = type;
    }

    public boolean addStop(Stop stop) {
        //System.out.println(stop.getCoordinate().getY());
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
