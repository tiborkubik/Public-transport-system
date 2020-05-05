package ija.proj;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import javafx.scene.text.Text;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    @FXML
    private Pane mapContent;
    @FXML
    private Text nextStopInfo;
    @FXML
    private Text nextStopText;
    @FXML
    private Text finalStopInfo;
    @FXML
    private Text finalStopText;
    @FXML
    private Text delayText;
    @FXML
    private Text timeGUI;
    @FXML
    private Pane sideWindow;
    @FXML
    private Pane bottomWindow;
    @FXML
    private Rectangle background;
    @FXML
    private Slider speedChange;
    @FXML
    private ListView linesInfo;
    @FXML
    private javafx.scene.shape.Line vehicleRoute;
    @FXML
    private Button plusH;
    @FXML
    private Button minusH;
    @FXML
    private Button plusM;
    @FXML
    private Button minusM;
    @FXML
    private Button plusS;
    @FXML
    private Button minusS;

    private List<Line> lines = new ArrayList<>();

    private List<UpdateState> updates = new ArrayList<>();

    TimeManager timeManager = new TimeManager();

    View view = new View();

    /***
     * changes speed
     */
    @FXML
    private void speedChanged() {
        float scaleForSpeed = (float) speedChange.getValue();
        timeManager.setScale(scaleForSpeed);
        timeManager.timer.cancel();
        timeManager.startTimer(updates, timeGUI);
    }

    @FXML
    private void plusHour() {
        LocalTime time = timeManager.getCurrentTime();

        timeManager.moveInTime(timeManager.formatTime(time.getHour()+1, time.getMinute(), time.getSecond()));
        timeManager.timer.cancel();
        timeManager.startTimer(updates, timeGUI);
    }

    @FXML
    private void minusHour() {
        LocalTime time = timeManager.getCurrentTime();

        timeManager.moveInTime(timeManager.formatTime(time.getHour()-1, time.getMinute(), time.getSecond()));
        timeManager.timer.cancel();
        timeManager.startTimer(updates, timeGUI);
    }

    @FXML
    private void plusMinute() {
        LocalTime time = timeManager.getCurrentTime();

        timeManager.moveInTime(timeManager.formatTime(time.getHour(), time.getMinute()+1, time.getSecond()));
        timeManager.timer.cancel();
        timeManager.startTimer(updates, timeGUI);
    }

    @FXML
    private void minusMinute() {
        LocalTime time = timeManager.getCurrentTime();

        timeManager.moveInTime(timeManager.formatTime(time.getHour(), time.getMinute()-1, time.getSecond()));
        timeManager.timer.cancel();
        timeManager.startTimer(updates, timeGUI);
    }

    @FXML
    private void plusSecond() {
        LocalTime time = timeManager.getCurrentTime();

        timeManager.moveInTime(timeManager.formatTime(time.getHour(), time.getMinute(), time.getSecond()+1));
        timeManager.timer.cancel();
        timeManager.startTimer(updates, timeGUI);
    }

    @FXML
    private void minusSecond() {
        LocalTime time = timeManager.getCurrentTime();

        timeManager.moveInTime(timeManager.formatTime(time.getHour(), time.getMinute(), time.getSecond()-2));
        timeManager.timer.cancel();
        timeManager.startTimer(updates, timeGUI);
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

        Platform.runLater(() -> view.zoom(mapContent, zoomValue));
        mapContent.layout();
    }

    /***
     * displays gui elements
     * @param GUIelements - list of elements to display
     */
    public void setGUIelements(List<Drawable> GUIelements) {
        view.setLineInfoDefault(nextStopInfo, nextStopText, finalStopInfo, finalStopText, delayText, vehicleRoute);
        view.showMapContent(GUIelements, mapContent);

        for(Drawable obj : GUIelements) {
            if(obj instanceof UpdateState) {
                updates.add((UpdateState) obj);
            }
        }

        timeManager.startTimer(updates, timeGUI);
    }

    public void setBasicSettings(List<Line> lines) {
        background.setOnMouseClicked(event -> {
            view.setLineInfoDefault(nextStopInfo, nextStopText, finalStopInfo, finalStopText, delayText, vehicleRoute);
            for(int i = 0; i < lines.size(); i++) {
                view.changeLineColor(mapContent, lines.get(i), view.colorsForLines.get(i));
            }

            view.cleanRouteFromStops(bottomWindow);
        });
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    /***
     * Adds information about line to the navigation
     * @param lines
     */
    public void setLinesInfo(List<Line> lines) {
        view.viewLinesInfo(lines, linesInfo);
    }

    /***
     * changes cursor according it's position + expanding of line information after clicking
     */
    public void setCursor(List<Line> lines) {
        ObservableList<Node> x = mapContent.getChildren();
        for(Node sg : x) {

            if(sg != background)
                sg.setCursor(Cursor.HAND);

            if(sg instanceof javafx.scene.shape.Line) {
                sg.setOnMouseClicked(event -> {
                    boolean onLine = false;
                    for(Line line : lines) {
                        for(Street st : line.getStreetList()) {
                            if(sg.getId().contains(st.getName())) {
                                bottomWindow.getChildren().removeIf(sg2 -> sg2.getId().contains("RouteStop"));
                            }
                            view.clickedOnLine(finalStopInfo, finalStopText);
                            onLine = true;
                        }
                    }

                    if(!onLine) {
                        view.clickedOnVoid(finalStopInfo, finalStopText, bottomWindow);
                    }

                    nextStopInfo.setOpacity(0.5);
                    nextStopText.setOpacity(0.5);
                    delayText.setOpacity(0.5);

                   for(Line line : lines) {
                     if(sg.getId().contains(line.getName())) {

                         generateStopsOnPath(line);

                         view.colorRoute(vehicleRoute, line.getColor().saturate().saturate());
                         view.changeFinalStopText(finalStopText, line);

                           for(Line otherLine : lines) {
                               if (otherLine.getName() != line.getName()) {
                                   view.changeLineColor(mapContent, otherLine, otherLine.getColor().desaturate().desaturate().desaturate().desaturate());
                               } else {
                                   view.changeLineColor(mapContent, otherLine, otherLine.getColor().saturate().saturate());
                               }
                           }
                       }
                   }
                });
            }
        }
    }

    public void generateStopsOnPath(Line line) {
        List<Stop> allStops = line.getStopList();

        double realToImPath = line.totalPathLength()/850;

        double distFromStart = allStops.get(0).getCoordinate().coordDistance(line.getStreetList().get(0).begin());

        for(int i = 1; i < allStops.size()-1; i++) {
            if(i == 1) {
                view.addStopToRoute(vehicleRoute, distFromStart, realToImPath, i, allStops, bottomWindow);
            }

            distFromStart += allStops.get(i).getCoordinate().coordDistance(allStops.get(i+1).getCoordinate());

            view.addStopToRoute(vehicleRoute, distFromStart, realToImPath, i, allStops, bottomWindow);
        }
    }

    public void generateStopsOnPath(Vehicle singleV) {
        double realToImPath = singleV.totalPathLength()/850;

        double distFromStart = singleV.getLine().getStopList().get(0).getCoordinate().coordDistance(singleV.getLine().getStreetList().get(0).begin());

        for(int i = 1; i < singleV.getLine().getStopList().size()-1; i++) {
            if(i == 1) {
                view.addStopToRoute(vehicleRoute, distFromStart, realToImPath, i, singleV, bottomWindow);
            }

            distFromStart += singleV.getLine().getStopList().get(i).getCoordinate().coordDistance(singleV.getLine().getStopList().get(i+1).getCoordinate());

            view.addStopToRoute(vehicleRoute, distFromStart, realToImPath, i, singleV, bottomWindow);
        }
    }

    public void highlightRouteFromList(List<Line> lines) {
        linesInfo.setOnMouseClicked(event ->{
            view.cleanRouteFromStops(bottomWindow);

            for(Line line : lines) {
                if(line.getName().equals(linesInfo.getSelectionModel().getSelectedItem())) {
                    generateStopsOnPath(line);
                    view.colorRoute(vehicleRoute, line.getColor().saturate().saturate());
                    finalStopText.setOpacity(1.0);
                    view.changeFinalStopText(finalStopText, line);

                    for(Line otherLine : lines) {
                        if (otherLine.getName() != line.getName()) {
                            view.changeLineColor(mapContent, otherLine, otherLine.getColor().desaturate().desaturate().desaturate().desaturate());
                        } else {
                            view.changeLineColor(mapContent, otherLine, otherLine.getColor().saturate().saturate());
                        }
                    }
                }
            }
        });
    }

    public void showVehicleRoute(List<Vehicle> allVehicles) {
        ObservableList<Node> x = mapContent.getChildren();
        for(Node sg : x) {
            if(sg instanceof Circle || sg instanceof Polygon || (sg instanceof Rectangle && !sg.equals(background))) {
                sg.setOnMouseClicked(event ->{
                    view.cleanRouteFromStops(bottomWindow);

                    view.setLineInfoFocused(nextStopInfo, nextStopText, finalStopInfo, finalStopText, delayText);

                    for(Vehicle singleV : allVehicles) {
                        if(singleV.getName().equals(sg.getId())) {
                            finalStopText.setText(singleV.getLine().getStopList().get(singleV.getLine().getStopList().size()-1).getName());

                            generateStopsOnPath(singleV);

                            view.colorRoute(vehicleRoute, singleV.getLine().getColor().saturate().saturate());

                            for(Line otherLine : lines) {
                                if (otherLine.getName() != singleV.getLine().getName()) {
                                    view.changeLineColor(mapContent, otherLine, otherLine.getColor().desaturate().desaturate().desaturate().desaturate());
                                } else {
                                    view.changeLineColor(mapContent, otherLine, otherLine.getColor().saturate().saturate());
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    public void setCurrentTime() {
        timeManager.setCurrentTime(timeGUI);
    }
}
