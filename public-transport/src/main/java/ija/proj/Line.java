package ija.proj;

import java.util.ArrayList;
import java.util.List;

public class Line {
    private String identifier;
    private List<Street> streetList= new ArrayList<>();
    private List<Stop> stopList= new ArrayList<>();

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



        if(stop.getStreet().follows(streetList.get(streetList.size()-1)) == false) {
            return false;
        }

        stopList.add(stop);
        streetList.add(stop.getStreet());
        return true;
    }

}
