package ija.proj;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


/**
 * Class Timetable represents a timetable of departures of vehicles from the start of first street defining line.
 * Class is responsible for new vehicles start at the right time and put them into GUI
 */
public class Timetable {
    private List<Drawable> allElemets;
    /**
     * < List of all drawable elements, when its time for new departure, new vehicle is added in it
     */
    private List<Line> lines;
    /**
     * < List of all lines. Timetable is responsible for all lines to departure its vehicles
     */
    private Controller controller;
    /**
     * < Main program controller
     */
    private int id = 41999;                         /**< Id of vehicles. It is incremented so every vehicle has a unique id */

    /**
     * Constructor of a timetable
     *
     * @param allElemets List of all drawable elements
     * @param lines      List of all active lines in given city
     * @param controller Main program controller
     */
    public Timetable(List<Drawable> allElemets, List<Line> lines, Controller controller) {
        this.allElemets = allElemets;
        this.lines = lines;
        this.controller = controller;
    }


    /**
     * Method checks whether it is time to departure new vehicle. If it is, new vehicle is added to allElements and also into its line
     *
     * @param timeManager Time manager that calls this method
     * @return Updated list of drawable elements
     */
    public List<Drawable> checkTimeTable(TimeManager timeManager) {
        List<Drawable> vehiclesToAdd = new ArrayList<>();
        List<String> names = new ArrayList<>();
        boolean flag;
        for (Line line : lines) {
            for (LocalTime time : line.getTimetable()) {
                flag = true;

                if (time.getHour() == timeManager.getCurrentTime().getHour() &&
                        time.getMinute() == timeManager.getCurrentTime().getMinute() &&
                        time.getSecond() == timeManager.getCurrentTime().getSecond() &&
                        timeManager.getCurrentTime().getNano() < 50000000) {
                    id++;
                    while (flag) {
                        flag = false;
                        if (line.getType().equals("bus") && !names.contains(line.getName())) {
                            flag = true;
                            names.add(line.getName());
                            vehiclesToAdd.add(new Bus(new Coordinate(line.getStreetList().get(0).begin().getX(), line.getStreetList().get(0).begin().getY()), 0.3, line, "#" + line.getType() + id + " " + line.getName(), line.getStreetList().get(0), line.getStopList().get(0), this.controller, timeManager.getCurrentTime()));
                        }
                        if (line.getType().equals("tram") && !names.contains(line.getName())) {
                            flag = true;
                            names.add(line.getName());
                            vehiclesToAdd.add(new Tram(new Coordinate(line.getStreetList().get(0).begin().getX(), line.getStreetList().get(0).begin().getY()), 0.6, line, "#" + line.getType() + id + " " + line.getName(), line.getStreetList().get(0), line.getStopList().get(0), this.controller, timeManager.getCurrentTime()));
                        }
                        if (line.getType().equals("sub") && !names.contains(line.getName())) {
                            flag = true;
                            names.add(line.getName());
                            vehiclesToAdd.add(new Subway(new Coordinate(line.getStreetList().get(0).begin().getX(), line.getStreetList().get(0).begin().getY()), 0.9, line, "#" + line.getType() + id + " " + line.getName(), line.getStreetList().get(0), line.getStopList().get(0), this.controller, timeManager.getCurrentTime()));
                        }
                    }
                }
            }
        }
        return vehiclesToAdd;
    }
}
