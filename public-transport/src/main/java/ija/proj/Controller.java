package ija.proj;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    @FXML
    private Pane mapContent;
    @FXML
    private Text nextStopInfo;
    @FXML
    private ScrollPane scrollP;
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
    private Rectangle rightBlur1;
    @FXML
    private Rectangle rightBlur111;
    @FXML
    private Text linesSign;
    @FXML
    private Button saveExitEditing;
    @FXML
    private  Rectangle saveBackground;
    @FXML
    private Button plusH;
    @FXML
    private Button plusM;
    @FXML
    private Button plusS;
    @FXML
    private Button minusH;
    @FXML
    private Button minusM;
    @FXML
    private Button minusS;
    @FXML
    private TextArea editJamsInfo;
    @FXML
    private Spinner trafficSpinner;
    @FXML
    private Button setIntensity;


    SpinnerValueFactory<Integer> spinnerVal = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, 1);
    private Scene mainScene;
    Image bg = new Image("mapa1.jpg");
    BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);

    private List<Line> lines = new ArrayList<>();
    private List<UpdateState> updates = new ArrayList<>();
    private List<Vehicle> allVehicles = new ArrayList<>();
    private boolean inEditTrafficMode = false;
    private Timetable timeTable;
    private String normalLine = getClass().getResource("/normalLine.css").toExternalForm();
    private String dashedLine = getClass().getResource("/dashedLine.css").toExternalForm();
    private View view = new View(this);

    private TimeManager timeManager = new TimeManager(view, this);

    @FXML
    private void exitProgram() {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    private void startEditingTraffic() {
        timeManager.setScale(1);
        timeManager.changeSpeed();

        inEditTrafficMode = true;

        trafficSpinner.setValueFactory(spinnerVal);

        view.prepareGUIforAdmin(
                                rightBlur1,
                                rightBlur111,
                                linesInfo,
                                linesSign,
                                speedChange,
                                bottomWindow,
                                saveExitEditing,
                                saveBackground,
                                plusH,
                                plusM,
                                plusS,
                                minusH,
                                minusM,
                                minusS,
                                editJamsInfo);
    }

    @FXML
    private void startEditingDetours() {
        timeManager.setScale(1);
        timeManager.changeSpeed();

        inEditTrafficMode = false;

        view.prepareGUIforAdmin(
                rightBlur1,
                rightBlur111,
                linesInfo,
                linesSign,
                speedChange,
                bottomWindow,
                saveExitEditing,
                saveBackground,
                plusH,
                plusM,
                plusS,
                minusH,
                minusM,
                minusS,
                editJamsInfo);

        inEditTrafficMode = false;
    }

    @FXML
    private void exitEditing() {
        timeManager.setScale((float)speedChange.getValue());
        timeManager.changeSpeed();

        view.exitGUIAdmin(
                rightBlur1,
                rightBlur111,
                linesInfo,
                linesSign,
                speedChange,
                bottomWindow,
                saveExitEditing,
                saveBackground,
                plusH,
                plusM,
                plusS,
                minusH,
                minusM,
                minusS,
                editJamsInfo);

        inEditTrafficMode = false;
    }
    /***
     * changes speed
     */
    @FXML
    private void speedChanged() {
        float scaleForSpeed = (float) speedChange.getValue();
        timeManager.setScale(scaleForSpeed);
        timeManager.changeSpeed();
    }

    @FXML
    private void plusHour() {
        LocalTime time = timeManager.getCurrentTime();
        timeManager.timer.stop();
        timeManager.moveInTime(timeManager.formatTime(time.getHour()+1, time.getMinute(), time.getSecond()-1),updates, timeGUI, timeTable, mapContent,1, speedChange);
        List<Drawable> vehList = timeManager.getVehicleToAdd();
        if (vehList != null) {
            for(Drawable veh : vehList) {
                if (veh != null)  {
                    view.addElement(veh, mapContent);
                }
            }
        }
    }

    @FXML
    private void minusHour() {
        LocalTime time = timeManager.getCurrentTime();
        timeManager.timer.stop();
        timeManager.moveInTime(timeManager.formatTime(time.getHour()-1, time.getMinute(), time.getSecond()-1),updates, timeGUI, timeTable, mapContent,1, speedChange);
        List<Drawable> vehList = timeManager.getVehicleToAdd();
        if (vehList != null) {
            for(Drawable veh : vehList) {
                if (veh != null)  {
                    view.addElement(veh, mapContent);
                }
            }
        }
    }

    @FXML
    private void plusMinute() {
        LocalTime time = timeManager.getCurrentTime();
        timeManager.timer.stop();
        timeManager.moveInTime(timeManager.formatTime(time.getHour(), time.getMinute()+1, time.getSecond()),updates, timeGUI, timeTable, mapContent,2, speedChange);
        timeManager.changeSpeed();
        List<Drawable> vehList = timeManager.getVehicleToAdd();
        if (vehList != null) {
            for(Drawable veh : vehList) {
                if (veh != null)  {
                    view.addElement(veh, mapContent);
                }
            }
        }
    }

    @FXML
    private void minusMinute() {
        LocalTime time = timeManager.getCurrentTime();
        timeManager.timer.stop();
        timeManager.moveInTime(timeManager.formatTime(time.getHour(), time.getMinute()-1, time.getSecond()),updates, timeGUI, timeTable, mapContent,2, speedChange);
        float scaleForSpeed = (float) speedChange.getValue();
        timeManager.setScale(scaleForSpeed);
        timeManager.changeSpeed();
        List<Drawable> vehList = timeManager.getVehicleToAdd();
        if (vehList != null) {
            for(Drawable veh : vehList) {
                if (veh != null)  {
                    view.addElement(veh, mapContent);
                }
            }
        }
    }

    @FXML
    private void plusSecond() {
        LocalTime time = timeManager.getCurrentTime();
        timeManager.timer.stop();
        timeManager.moveInTime(timeManager.formatTime(time.getHour(), time.getMinute(), time.getSecond()+1),updates, timeGUI, timeTable, mapContent,3, speedChange);
        float scaleForSpeed = (float) speedChange.getValue();
        timeManager.setScale(scaleForSpeed);
        timeManager.changeSpeed();

        List<Drawable> vehList = timeManager.getVehicleToAdd();
        if (vehList != null) {
            for(Drawable veh : vehList) {
                if (veh != null)  {
                    view.addElement(veh, mapContent);
                }
            }
        }
    }

    @FXML
    private void minusSecond() {
        LocalTime time = timeManager.getCurrentTime();
        timeManager.timer.stop();
        timeManager.moveInTime(timeManager.formatTime(time.getHour(), time.getMinute(), time.getSecond()-2),updates, timeGUI, timeTable, mapContent,3, speedChange);
        float scaleForSpeed = (float) speedChange.getValue();
        timeManager.setScale(scaleForSpeed);
        timeManager.changeSpeed();
        List<Drawable> vehList = timeManager.getVehicleToAdd();
        if (vehList != null) {
            for(Drawable veh : vehList) {
                if (veh != null)  {
                    view.addElement(veh, mapContent);
                }
            }
        }
    }

    @FXML
    private void setTimeDefault() {
        timeManager.setDefaultTime(timeGUI);
        timeManager.timer.stop();
        timeManager.moveInTime("23:59:59",updates, timeGUI, timeTable, mapContent,1, speedChange);
        float scaleForSpeed = (float) speedChange.getValue();
        timeManager.setScale(scaleForSpeed);
    }

    @FXML
    private void zooming(ScrollEvent event) {
        event.consume();
        mapContent.layout();
        double zoomFactor = 1.5;
        if (event.getDeltaY() <= 0) {
            // zoom out
            zoomFactor = 1 / zoomFactor;
        }

        double oldScale = mapContent.getScaleX();
        double scale = oldScale * zoomFactor;
        double f = (scale / oldScale) - 1;

        // determine offset that we will have to move the node
        Bounds bounds = mapContent.localToScene(mapContent.getBoundsInLocal());

        double dx = (event.getSceneX() - (bounds.getWidth() / 2 + bounds.getMinX()));
        double dy = (event.getSceneY() - (bounds.getHeight() / 2 + bounds.getMinY()));
        Timeline timeline = new Timeline(60);

        scrollP.setFitToHeight(true);
        scrollP.setFitToWidth(true);
        // timeline that scales and moves the node
        timeline.getKeyFrames().clear();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(200), new KeyValue(mapContent.translateXProperty(), mapContent.getTranslateX() - f * dx)),
                new KeyFrame(Duration.millis(200), new KeyValue(mapContent.translateYProperty(), mapContent.getTranslateY() - f * dy)),
                new KeyFrame(Duration.millis(200), new KeyValue(mapContent.scaleXProperty(), scale)),
                new KeyFrame(Duration.millis(200), new KeyValue(mapContent.scaleYProperty(), scale))
        );
        timeline.play();
    }

    public boolean inEditTrafficMode() {
        return this.inEditTrafficMode;
    }

    public void setScene(Scene scene) {
        this.mainScene = scene;
    }

    public void editTrafficLine(List<Line> lines) {
        System.out.println("true");
        ObservableList<Node> x = mapContent.getChildren();

//        for(Node sg : x) {
//            if(sg instanceof javafx.scene.shape.Line) {
//                sg.setOnMouseClicked(event -> {
//                    for(Line line : lines) {
//                        for(Street st : line.getStreetList()) {
//                            if(sg.getId().contains(st.getName())) {
//                                bottomWindow.getChildren().removeIf(sg2 -> sg2.getId().contains("RouteStop"));
//                            }
//                            ((javafx.scene.shape.Line) sg).setFill(Color.ORANGE);
//                        }
//                    }
//                });
//            }
//        }
    }

    public void setBackground() {
        mapContent.setBackground(new Background(new BackgroundImage(bg,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                bSize)));
    }

    public void setTimeTable(Timetable timeTable) {
        this.timeTable = timeTable;
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

        timeManager.startTimer(updates, timeGUI, timeTable, mapContent, 15, speedChange);
        List<Drawable> vehList = timeManager.getVehicleToAdd();
        if (vehList != null) {
            for(Drawable veh : vehList) {
                if (veh != null)  {
                    view.addElement(veh, mapContent);
                }
            }
        }

    }

    public void addVehicleToController(Vehicle e) {
        allVehicles.add(e);
    }

    public void setBasicSettings(List<Line> lines) {
        background.setOnMousePressed(event -> {
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

                // Traffic density on given street on hover
                sg.setOnMouseEntered(event -> {

                });
                sg.setOnMouseExited(event -> {

                });
                sg.setOnMouseClicked(event -> {
                    System.out.println(inEditTrafficMode);
                    boolean onLine = false;
                    for(Line line : lines) {
                        for(Street st : line.getStreetList()) {
                            if(!inEditTrafficMode){
                                if(sg.getId().contains(st.getName())) {
                                    bottomWindow.getChildren().removeIf(sg2 -> sg2.getId().contains("RouteStop"));
                                }
                                view.clickedOnLine(finalStopInfo, finalStopText);
                                onLine = true;
                            } else {
                                if(sg.getId().contains(st.getName())) {
                                    try {
                                        mainScene.getStylesheets().remove(normalLine);
                                    } catch (Exception e){
                                    }
                                    mainScene.getStylesheets().add(dashedLine);
                                    sg.getStyleClass().add("dashedLine");
                                    trafficSpinner.setVisible(true);
                                    setIntensity.setVisible(true);
                                    spinnerVal.setValue(st.getTrafficDensity());

                                    setIntensity.setOnMouseClicked(event2 -> {
                                        st.setTrafficDensity((Integer) trafficSpinner.getValue());

                                        if(st.getTrafficDensity() == 1) {
                                            mainScene.getStylesheets().add(normalLine);
                                            sg.getStyleClass().add("normalLine");
                                            try {
                                                mainScene.getStylesheets().remove(dashedLine);
                                            } catch (Exception e){
                                            }
                                        }

                                        trafficSpinner.setVisible(false);
                                        setIntensity.setVisible(false);
                                    });
                                }
                            }
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

    public void showVehicleRoute() {
        view.showVehicleRoute(mapContent,
                                background,
                                bottomWindow,
                                nextStopInfo,
                                nextStopText,
                                finalStopInfo,
                                finalStopText,
                                delayText,
                                allVehicles,
                                vehicleRoute,
                                lines);
    }

    public void setCurrentTime() {
        timeManager.setCurrentTime(timeGUI);
    }
}
