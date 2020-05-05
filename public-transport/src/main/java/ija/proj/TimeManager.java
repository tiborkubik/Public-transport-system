package ija.proj;

import javafx.application.Platform;
import javafx.scene.text.Text;

import java.time.LocalTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TimeManager {
    LocalTime currentTime = LocalTime.now();
    double scaleForSpeed = 1.0;
    Timer timer;

    public void setDefaultTime(Text timeGUI) {
        timeGUI.setText("00:00:00");
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
    public void startTimer(List<UpdateState> updates, Text timeGUI) {
            timer = new Timer(false);
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    currentTime = currentTime.plusSeconds(1);
                    for (UpdateState update : updates) {
                        update.update(currentTime);
                    }
                    timeGUI.setText(formatTime(currentTime.getHour(), currentTime.getMinute(), currentTime.getSecond()));

                }
            }, 0, (long) (1000 / scaleForSpeed));
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
