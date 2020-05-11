package ija.proj;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class TimeManager controls time of  the application and ensures time jumps, and flow of the time of application
 */
public class TimeManager {
    View view;              /**< Instance of the class view */
    Controller controller;  /**< Instance of the class controller */
    Timeline timer;         /**< Instance of the class Timeline which keep the time running */
    LocalTime timeToJump;   /**< Time would like to jump to  */
    LocalTime begin;        /**< Used for storing current time while time jumping  */
    LocalTime currentTime = LocalTime.now().minusHours(1); /**< Time of the application (at the start 1hour earlier to ensure that lines will be spawned) */

    List<Drawable> vehiclesToAdd = new ArrayList<>();      /**< Vehicles added to be shown, according timetable */
    List<Line> lines = new ArrayList<>();                  /**< List of lines */

    double scaleForSpeed = 1.0; /**< speed of time */
    double timeMultiplier = 15; /**< timer is called "timeMultiplier" times per second */
    long elapse_time = 0;       /**< time of elapse */


    /***
     * Sets scale for speed
     *
     * @param scale scale for time
     */
    public void setScale(float scale) {
        this.scaleForSpeed = scale;
    }

    /***
     * 
     *
     * @param lines
     */
    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    /***
     * Sets default time for the application "23:59:59"
     *
     * @param timeGUI
     */
    public void setDefaultTime(Text timeGUI) {
        timeGUI.setText("23:59:59");
    }

    public TimeManager(View view, Controller controller) {
        this.view = view;
        this.controller = controller;
    }

    public LocalTime getCurrentTime() {
        return this.currentTime;
    }

    public List<Drawable> getVehicleToAdd() {
        return  this.vehiclesToAdd;
    }

    /***
     * Starts timer
     */
    public void changeSpeed() {
        timer.setRate(scaleForSpeed);
    }


    public void startTimer(List<UpdateState> updates, Text timeGUI, Timetable timeTable, Pane mapContent, double coefficient, Slider speedChange) {
            TimeManager thisTM = this;
            List<Drawable> vehiclesToAddT = this.vehiclesToAdd;

            double d_coefficient = 1000/coefficient;

            timer = new Timeline(new KeyFrame(javafx.util.Duration.millis(d_coefficient), event -> {
                currentTime = currentTime.plusNanos(1000000000/15);
                timeGUI.setText(formatTime(currentTime.getHour(), currentTime.getMinute(), currentTime.getSecond()));
                try {
                    for (UpdateState update : updates) {
                        Vehicle currentV = (Vehicle)update;
                        int trafficCoeff = currentV.getCurrentStreet().getTrafficDensity();
                        update.update(currentTime,15/this.timeMultiplier, trafficCoeff);
                        if(controller.getFocusedVehicle() != null) {
                            controller.getNextStopText().setText(controller.getFocusedVehicle().getNextStop().getName());
                            controller.getVehicleOnRoute().setCenterX(50+controller.getFocusedVehicle().getPassedDistance()/controller.getFocusedVehicle().getLine().totalPathLength()*850);
                        }
                        else
                            controller.getNextStopText().setText("-");
                    }
                } catch (Exception ignored) {
                }

                if(currentTime.getSecond() == 0) {
                        try {
                            this.vehiclesToAdd = timeTable.checkTimeTable(thisTM);
                            controller.setVehicleInfo();
                        } catch (Exception ignored){
                        }
                    for(Drawable x : vehiclesToAdd){
                        view.addElement(x, mapContent);
                        updates.add((UpdateState)x);
                        controller.addVehicleToController((Vehicle)x);
                    }
                }

                if (timeToJump != null){
                    if (elapse_time == 1)
                        setScale(10);
                    else if(elapse_time == 2)
                        setScale(100);

                    changeSpeed();
                    if (    timeToJump.getHour() == currentTime.getHour() &&
                            timeToJump.getMinute() == currentTime.getMinute() &&
                            timeToJump.getSecond() == currentTime.getSecond() &&
                            this.elapse_time != 0
                    )
                    {
                        timer.stop();
                        startTimer(updates, timeGUI, timeTable, mapContent, 15, speedChange);

                        float scaleForSpeed = (float) speedChange.getValue();
                        setScale(scaleForSpeed);
                        changeSpeed();

                        this.elapse_time = 0;
                        timeToJump = null;
                    }
                }
            }));
            this.vehiclesToAdd = vehiclesToAddT;
            timer.setCycleCount(Timeline.INDEFINITE);
            timer.play();
            this.vehiclesToAdd =  new ArrayList<>();
    }


    public void moveInTime(String newTime,List <UpdateState> updates, Text timeGUI,Timetable timeTable, Pane mapContent,int flag, Slider speedChange) {

        this.timeToJump = LocalTime.parse(newTime);
        this.begin = currentTime;
        this.elapse_time = 1;

        if (flag == 1){
            if (timeToJump.getHour() < currentTime.getHour())
                elapse_time = 2;
            startTimer(updates, timeGUI, timeTable, mapContent, 12000.0, speedChange);

        }
        else if (flag == 2) {
            if (timeToJump.getMinute() < currentTime.getMinute()) {
                elapse_time = 2;
                startTimer(updates, timeGUI, timeTable, mapContent, 12000.0, speedChange);
            } else startTimer(updates, timeGUI, timeTable, mapContent, 1000.0, speedChange);
        }
        else if (flag == 3){
            if (timeToJump.getSecond() < currentTime.getSecond()){
                elapse_time = 2;
                startTimer(updates, timeGUI, timeTable, mapContent, 12000.0, speedChange);
            }
            else startTimer(updates, timeGUI, timeTable, mapContent, 25.0, speedChange);
        }


    }

    public String formatTime(int hour, int minute, int second) {
        String hours = String.valueOf(hour);
        String minutes = String.valueOf(minute);
        String seconds = String.valueOf(second);

        if(hour == -1) {
            hours = "23";
            hour = 23;
        }
        if(hour == 24) {
            hours = "0";
            hour = 0;
        }
        if(minute == -1) {
            minutes = "59";
            minute = 59;
        }
        if(minute == 60) {
            minutes = "0";
            minute = 0;
        }
        if(second == -1) {
            seconds = "59";
            second = 59;
        }
        if(second == 60) {
            seconds = "0";
            second = 0;
        }

        if(hour < 10) hours = "0" + hours;
        if(minute < 10) minutes = "0" + minutes;
        if(second < 10)  seconds = "0" + seconds;

        return hours + ":" + minutes + ":" + seconds;
    }
}
