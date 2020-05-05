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

    public Drawable checkTimeTable(TimeManager timeManager, Pane mapContent) {
        for(Line line : lines) {
            for(LocalTime time : line.getTimetable()) {
                if( time.getHour() == timeManager.getCurrentTime().getHour() &&
                    time.getMinute() == timeManager.getCurrentTime().getMinute()
                ) {
                    if(line.getType().equals("bus")) {
                        Bus bus = new Bus(new Coordinate(lines.get(0).getStreetList().get(0).begin().getX(), lines.get(0).getStreetList().get(0).begin().getY()), 0.5, lines.get(0), "Bus#45001");

                        return  bus;
                    }
                    if(line.getType().equals("tram")) {
                        Tram tram = new Tram(new Coordinate(lines.get(2).getStreetList().get(0).begin().getX(), lines.get(2).getStreetList().get(0).begin().getY()), 1, lines.get(2), "Tram#45001");
                        System.out.println("1: " + tram);
                        return tram;
                    }
                    if(line.getType().equals("sub")) {
                        Subway sub = new Subway(new Coordinate(100, 100), 2, lines.get(1), "Sub#693");
                        return  sub;
                    }
                }
            }
        }
        return  null;
    }
}
