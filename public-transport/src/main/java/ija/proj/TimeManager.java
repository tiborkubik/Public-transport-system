package ija.proj;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TimeManager {
    LocalTime currentTime = LocalTime.now();
    double scaleForSpeed = 1.0;
    List<Drawable> vehiclesToAdd = new ArrayList<>();
    List<Drawable> vehiclesToDelete = new ArrayList<>();
    List<Line> lines = new ArrayList<>();
    double timeMultiplier = 15;
    View view;
    Controller controller;
    Timeline timer;
    LocalTime timeToJump;
    long elapse_time = 0;

    LocalTime begin;

    public void setDefaultTime(Text timeGUI) {
        timeGUI.setText("23:59:59");
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public TimeManager(View view, Controller controller) {
        this.view = view;
        this.controller = controller;
    }


    public void setScale(float scale) {
        this.scaleForSpeed = scale;
    }

    public LocalTime getCurrentTime() {
        return this.currentTime;
    }

    public void setCurrentTime(Text timeGUI) {
        timeGUI.setText(currentTime.getHour() + ":" + currentTime.getMinute() + ":" + currentTime.getSecond());
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

//                for(UpdateState update : updates) {
//                    Vehicle currentV = (Vehicle)update;
//                    if (currentV.getLine().getStreetList().get(currentV.getLine().getStreetList().size()-1).end().equals(currentV.getPosition())){
//                        updates.remove(update);
//
//                        ObservableList<Node> mapNodes = mapContent.getChildren();
//                        for(Node singleNode : mapNodes) {
//                            System.out.println(currentV.getName() + "   -   " + singleNode.getId());
//                            if(currentV.getName().equals(singleNode.getId())){
//                                System.out.println("koacskoa");
//                                mapContent.getChildren().remove(singleNode);
//                            }
//                        }
//                        System.out.println("here I am");
//                    }
//                }
                try {
                    for (UpdateState update : updates) {
                        Vehicle currentV = (Vehicle)update;
                        int trafficCoeff = currentV.getCurrentStreet().getTrafficDensity();
                        update.update(currentTime,15/this.timeMultiplier, trafficCoeff);
                    }
                } catch (Exception e) {
                }

                if(currentTime.getSecond() == 0) {
                        try {
                            this.vehiclesToAdd = timeTable.checkTimeTable(thisTM);
                        } catch (Exception e){
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

//            for (Drawable veh : vehiclesToDelete){
//                view.deleteElement(veh, mapContent);
//            }

    }

    public List<Drawable> getVehicleToAdd() {
        return  this.vehiclesToAdd;
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

        else if (flag == 2){
            if (timeToJump.getMinute() < currentTime.getMinute()){
                elapse_time = 2;
                startTimer(updates, timeGUI, timeTable, mapContent, 12000.0, speedChange);
            }
            else startTimer(updates, timeGUI, timeTable, mapContent, 1000.0, speedChange);
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
