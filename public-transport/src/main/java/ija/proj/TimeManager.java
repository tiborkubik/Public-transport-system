package ija.proj;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TimeManager {
    LocalTime currentTime = LocalTime.now();
    double scaleForSpeed = 1.0;
    List<Drawable> vehiclesToAdd = new ArrayList<>();
    View view;
    Controller controller;
    Timeline timer;

    public void setDefaultTime(Text timeGUI) {
        timeGUI.setText("00:00:00");
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
    public void changeSpeed(Timetable timeTable) {
        TimeManager thisTM = this;

        timer.setRate(scaleForSpeed);

//        if(currentTime.getSecond() == 0) {
//            vehiclesToAdd.add(timeTable.checkTimeTable(thisTM));
//        } else {
//            vehiclesToAdd = null;
//        }
    }

    public void startTimer(List<UpdateState> updates, Text timeGUI, Timetable timeTable, Pane mapContent) {
            TimeManager thisTM = this;

            List<Drawable> vehiclesToAddT = this.vehiclesToAdd;
            timer = new Timeline(new KeyFrame(Duration.millis(1000/10), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    currentTime = currentTime.plusNanos(1000000000/10);

                    timeGUI.setText(formatTime(currentTime.getHour(), currentTime.getMinute(), currentTime.getSecond()));

                    for (UpdateState update : updates) {
                        update.update(currentTime);
                    }

                    if(currentTime.getSecond() == 0) {
                        Drawable a = timeTable.checkTimeTable(thisTM, "bus");
                        Drawable b = timeTable.checkTimeTable(thisTM, "tram");
                        Drawable c = timeTable.checkTimeTable(thisTM, "sub");

                        if(!(a == null && b == null && c == null)) {
                            try {
                                vehiclesToAddT.add(a);
                            } catch (Exception e){

                            }
                            try {
                                vehiclesToAddT.add(b);
                            } catch (Exception e){

                            }
                            try {
                                vehiclesToAddT.add(c);
                            } catch (Exception e){

                            }
                            try{

                                for(Drawable x : vehiclesToAddT){
                                    view.addElement(x, mapContent);
                                    updates.add((UpdateState)x);
                                    controller.addVehicleToController((Vehicle)x);
                                }
                            }
                            catch (Exception e){

                            }
                        }
                    }
                }
            }));
            this.vehiclesToAdd = vehiclesToAddT;
            timer.setCycleCount(Timeline.INDEFINITE);
            timer.play();
            this.vehiclesToAdd =  new ArrayList<>();
    }

    public List<Drawable> getVehicleToAdd() {
        return  this.vehiclesToAdd;
    }

    public void moveInTime(String newTime) {
        this.currentTime = LocalTime.parse(newTime);
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
