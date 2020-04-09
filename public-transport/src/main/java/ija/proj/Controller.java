package ija.proj;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {

    // GUI elements
    @FXML
    private Pane mapContent;

    @FXML
    private Slider speedChange;

    private List<Drawable> GUIelements = new ArrayList<>();
    private Timer timer;
    private LocalTime currentTime = LocalTime.now();

    @FXML
    private void speedChanged() {
        float scaleForSpeed = (float) speedChange.getValue();
        timer.cancel();
        startTimer(scaleForSpeed);
    }

    @FXML
    private void zooming(ScrollEvent event) {
        event.consume();                    // to ensure that only the part of map will be zoomed, not whole window
        double zoomValue;

        if(event.getDeltaY() > 0) {
            zoomValue = 1.2;
        } else {
            zoomValue = 0.8;
        }

        // scaling
        mapContent.setScaleX(zoomValue * mapContent.getScaleX());
        mapContent.setScaleY(zoomValue * mapContent.getScaleY());
        mapContent.layout();
    }

    public void setGUIelements(List<Drawable> GUIelements) {
        this.GUIelements = GUIelements;
        for(Drawable obj : GUIelements) {
            mapContent.getChildren().addAll(obj.getGUI());
        }
    }

    // Timer will start
    public void startTimer(double scale) {
        timer = new Timer(false);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                currentTime = currentTime.plusSeconds(1);
            }
        }, 0, (long) (1000 / scale));
    }
}
