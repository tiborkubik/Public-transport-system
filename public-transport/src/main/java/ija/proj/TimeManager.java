package ija.proj;

import javafx.application.Platform;
import javafx.scene.text.Text;

import java.time.LocalTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TimeManager {
    LocalTime currentTime = LocalTime.now();
    Timer timer;

    public void setDefaultTime(Text timeGUI) {
        timeGUI.setText("00:00:00");
    }

    public void setCurrentTime(Text timeGUI) {
        timeGUI.setText(currentTime.getHour() + ":" + currentTime.getMinute() + ":" + currentTime.getSecond());
    }

    /***
     * Starts timer
     * @param scale
     */
    public void startTimer(double scale, List<UpdateState> updates) {
            timer = new Timer(false);
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    currentTime = currentTime.plusSeconds(1);
                    for (UpdateState update : updates) {
                        update.update(currentTime);
                    }
                }
            }, 0, (long) (1000 / scale));
    }
}
