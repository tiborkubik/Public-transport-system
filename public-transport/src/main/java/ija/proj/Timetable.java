package ija.proj;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Timetable {
    private List<Drawable> allElemets;
    private List<Line> lines;
    private View view;
    private Controller controller;

    public Timetable(List<Drawable> allElemets, List<Line> lines, View view, Controller controller) {
        this.allElemets = allElemets;
        this.lines = lines;
        this.view = view;
        this.controller = controller;
    }


    public List <Drawable> checkTimeTable(TimeManager timeManager) {
        List <Drawable> vehiclesToAdd= new ArrayList<>();
        int id = 41999;
        List <String> names = new ArrayList<>();
        boolean flag;
        for(Line line : lines) {
            for(LocalTime time : line.getTimetable()) {
                flag = true;

                if(time.getHour() == timeManager.getCurrentTime().getHour() &&
                        time.getMinute() == timeManager.getCurrentTime().getMinute() &&
                        time.getSecond() == timeManager.getCurrentTime().getSecond() &&
                        timeManager.getCurrentTime().getNano() < 100000000){
                        id++;
                        while (flag){
                            flag = false;
                            if (line.getType().equals("bus") && !names.contains(line.getName())){
                                flag = true;
                                names.add(line.getName());
                                vehiclesToAdd.add(new Bus(new Coordinate(line.getStreetList().get(0).begin().getX(), line.getStreetList().get(0).begin().getY()), 0.3, line, "#" +line.getType() + id, line.getStreetList().get(0), line.getStopList().get(0), this.controller));
                            }
                            if (line.getType().equals("tram") && !names.contains(line.getName())){
                                flag = true;
                                names.add(line.getName());
                                vehiclesToAdd.add(new Tram(new Coordinate(line.getStreetList().get(0).begin().getX(), line.getStreetList().get(0).begin().getY()), 0.6, line, "#" +line.getType() + id, line.getStreetList().get(0), line.getStopList().get(0), this.controller));
                            }
                            if (line.getType().equals("sub") && !names.contains(line.getName())){
                                flag = true;
                                names.add(line.getName());
                                vehiclesToAdd.add(new Subway(new Coordinate(line.getStreetList().get(0).begin().getX(), line.getStreetList().get(0).begin().getY()), 1, line, "#" +line.getType() + id, line.getStreetList().get(0), line.getStopList().get(0), this.controller));
                            }
                        }
                }
            }
        }

        return  vehiclesToAdd;
    }
}
