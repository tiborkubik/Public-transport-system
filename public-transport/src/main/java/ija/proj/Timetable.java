package ija.proj;

import javafx.scene.layout.Pane;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.time.*;
import java.util.TimerTask;

public class Timetable {
    List<Drawable> allElemets;
    List<Line> lines;
    View view;

    public Timetable(List<Drawable> allElemets, List<Line> lines, View view) {
        this.allElemets = allElemets;
        this.lines = lines;
        this.view = view;
    }

    public void generateNewVehicle(TimeManager timeManager) {
            //checkTimeTable(timeManager);
    }

    public Drawable checkTimeTable(TimeManager timeManager, String type) {
        for(Line line : lines) {
            for(LocalTime time : line.getTimetable()) {
                if( time.getHour() == timeManager.getCurrentTime().getHour() &&
                    time.getMinute() == timeManager.getCurrentTime().getMinute()
                ) {
                    if(line.getType().equals("bus") && type.equals("bus")) {
                        System.out.println("bus");
                        Bus bus = new Bus(new Coordinate(line.getStreetList().get(0).begin().getX(), line.getStreetList().get(0).begin().getY()), 0.5, line, "Bus#45001");
                        return bus;
                    }
                    if(line.getType().equals("tram") && type.equals("tram")) {
                        System.out.println("tram");
                        Tram tram = new Tram(new Coordinate(line.getStreetList().get(0).begin().getX(), line.getStreetList().get(0).begin().getY()), 1, line, "Tram#45001");
                        return tram;
                    }
                    if(line.getType().equals("sub") && type.equals("sub")) {
                        System.out.println("sub");
                        Subway sub = new Subway(new Coordinate(line.getStreetList().get(0).begin().getX(), line.getStreetList().get(0).begin().getY()),2, line, "Sub#693");
                        return  sub;
                    }
                }
            }
        }
        return  null;
    }
}
