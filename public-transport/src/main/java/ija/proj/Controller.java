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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.time.LocalTime;
import java.util.*;

/***
 * Class controller serves for controlling the application by invoking object according
 * changes made by user to view or timeline
 */
public class Controller {
    @FXML
    private Pane mapContent;        /**< main visual component which contains streets,lines,stops,vehicles and background */
    @FXML
    private Text nextStopInfo;      /**< Information about the following stop */
    @FXML
    private ScrollPane scrollP;     /**< Wrapper element for map */
    @FXML
    private Text nextStopText;      /**< Unique line identifier */
    @FXML
    private Text finalStopInfo;     /**< Name of the next stop */
    @FXML
    private Text finalStopText;     /**< Name of the Last stop */
    @FXML
    private Text delayText;         /**< Displays delay */
    @FXML
    private Text timeGUI;           /**< Shows time in GUI */
    @FXML
    private Pane bottomWindow;      /**< Bottom pane element of the GUI */
    @FXML
    private Rectangle background;   /**< Background of the city map */
    @FXML
    private Slider speedChange;     /**< Defines pace of the time */
    @FXML
    private ListView<Object> linesInfo;             /**< Information about lines */
    @FXML
    private javafx.scene.shape.Line vehicleRoute;   /**< Bottom line which represents selected line */
    @FXML
    private Rectangle rightBlur1;   /**< Blur effect */
    @FXML
    private Rectangle rightBlur111; /**< Blur effect */
    @FXML
    private Text linesSign;         /**< Sign of the line */
    @FXML
    private Button saveExitEditing; /**< Button to save changes made by admin */
    @FXML
    private  Rectangle saveBackground;  /**< saves background image */
    @FXML
    private Button plusH;           /**< Move one hour forward */
    @FXML
    private Button plusM;           /**< Move one minute forward */
    @FXML
    private Button plusS;           /**< Move one second forward */
    @FXML
    private Button minusH;          /**< Move one hour backward */
    @FXML
    private Button minusM;          /**< Move one minute backward */
    @FXML
    private Button minusS;          /**< Move one second backward */
    @FXML
    private TextArea editJamsInfo;  /**< to edit jam degree of the street */
    @FXML
    private TextArea editDetoursInfo;       /**< to edit detours of the line */
    @FXML
    private Spinner<Integer> trafficSpinner;/**< degree of the jam */
    @FXML
    private Button setIntensity;            /**< button to save jam degree*/
    @FXML
    private Circle vehicleOnRouteBus;          /**< representation of vehicle on the bottom route */
    @FXML
    private Rectangle vehicleOnRouteSub;
    @FXML
    private Rectangle vehicleOnRouteTram;

    private SpinnerValueFactory<Integer> spinnerVal = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, 1); /**< Stores degree of the traffic jam */
    private BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true); /**< Defines size of the background picture */
    private String normalLine = getClass().getResource("/normalLine.css").toExternalForm(); /**< Path to style normalize file */
    private String dashedLine = getClass().getResource("/dashedLine.css").toExternalForm(); /**< Path to styles of dashed lines */
    private Image bg = new Image("mapa1.jpg"); /**< Path to background image */
    private Scene mainScene;    /**< Main scene */
    private List<Line> lines = new ArrayList<>();   /**< Stores all line */
    private List<UpdateState> updates = new ArrayList<>(); /**< Stores all objects needed to be updated */
    private List<Vehicle> allVehicles = new ArrayList<>(); /**< Stores all vehicles */
    private List<String> focusedLineStopsNames = new ArrayList<>(); /**< Stop names of selected line */
    private List<Coordinate> focusedLineStops = new ArrayList<>(); /**< Stop coordinates of selected line */
    private boolean inEditTrafficMode = false;      /**< Flag, true if editing traffic */
    private boolean inEditDetours = false;          /**< Flag, true if editing detours */
    private Timetable timeTable;                    /**< Timetable of the lines */
    private View view = new View(this);    /**< View object defines what to display */
    private Tooltip densityInfo = new Tooltip();    /**< Density */
    private Vehicle focusedVehicle = null;          /**< stores focused vehicle, if null then no vehicle is selected */
    private List<Drawable> allStreets;              /**< stores all streets typed Drawable */

    private Street beingDetoured = null;            /**< tells us which street is being detoured, if null then none */
    private Line lineDetoured = null;               /**< tells us which line is being detoured, if null then none */
    private List<Street> streetsToAddToLine = new ArrayList<>(); /**< while building detour stores all streets added to the line */

    private TimeManager timeManager = new TimeManager(view, this); /**< Controls time related calculations */

    /***
     * Returns all vehicles on the map
     * @return all vehicles on the map
     */
    public List<Vehicle> getAllVehicles() {
        return allVehicles;
    }

    /***
     * exits program
     */
    @FXML
    private void exitProgram() {
        Platform.exit();
        System.exit(0);
    }

    /***
     * return all elements of the type UpdateState
     *
     * @return returns all updates
     */
    public List<UpdateState> getUpdates() {
        return updates;
    }

    /***
     * Changes gui when editing mode for jams entered
     */
    @FXML
    private void startEditingTraffic() {
        timeManager.setScale(1);
        timeManager.changeSpeed();

        inEditTrafficMode = true;
        inEditDetours = false;

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
                                editJamsInfo,
                                editDetoursInfo);
    }

    /***
     * Changes gui when editing mode for detours entered
     */
    @FXML
    private void startEditingDetours() {
        timeManager.setScale(1);
        timeManager.changeSpeed();

        inEditTrafficMode = false;
        inEditDetours = true;

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
                editJamsInfo,
                editDetoursInfo);

        inEditTrafficMode = false;
    }

    /***
     * Exits editing mode
     */
    @FXML
    private void exitEditing() {
        timeManager.setScale((float)speedChange.getValue());
        timeManager.changeSpeed();

        if(inEditDetours) {
            List<Street> newStreets = new ArrayList<>();
            try {
                for (Street s : lineDetoured.getStreetList()){
                    if (s.getName().equals(beingDetoured.getName())){
                        newStreets.addAll(streetsToAddToLine);
                    }
                    else {
                        newStreets.add(s);
                    }
                }

                lineDetoured.deleteStops(beingDetoured);
                lineDetoured.setStreets(newStreets, streetsToAddToLine, beingDetoured);

            } catch (Exception ignore) {
            }


            beingDetoured = null;
            lineDetoured = null;
            streetsToAddToLine.clear();
        }

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
                editJamsInfo,
                editDetoursInfo);

        inEditTrafficMode = false;
        inEditDetours = false;
    }
    /***
     * Changes speed
     */
    @FXML
    private void speedChanged() {
        float scaleForSpeed = (float) speedChange.getValue();
        timeManager.setScale(scaleForSpeed);
        timeManager.changeSpeed();
    }

    /***
     * Adding one hour to the time of the app
     */
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

    /***
     * Subtracting one hour to the time of the app
     */
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

    /***
     * Adding one minute to the time of the app
     */
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
    /***
     * Subtracting one minute to the time of the app
     */
    @FXML
    private void minusMinute() {
        LocalTime time = timeManager.getCurrentTime();
        timeManager.timer.stop();
        timeManager.moveInTime(timeManager.formatTime(time.getHour(), time.getMinute()-1, time.getSecond()),updates, timeGUI, timeTable, mapContent,2, speedChange);
        normalizeAfterTimeShift();
    }


    /***
     * Adding one second to the time of the app
     */
    @FXML
    private void plusSecond() {
        LocalTime time = timeManager.getCurrentTime();
        timeManager.timer.stop();
        timeManager.moveInTime(timeManager.formatTime(time.getHour(), time.getMinute(), time.getSecond()+1),updates, timeGUI, timeTable, mapContent,3, speedChange);
        normalizeAfterTimeShift();
    }
    /***
     * Subtracting one second to the time of the app
     */
    @FXML
    private void minusSecond() {
        LocalTime time = timeManager.getCurrentTime();
        timeManager.timer.stop();
        timeManager.moveInTime(timeManager.formatTime(time.getHour(), time.getMinute(), time.getSecond()-2),updates, timeGUI, timeTable, mapContent,3, speedChange);
        normalizeAfterTimeShift();
    }

    /***
     * Sets all streets
     *
     * @param streets streets to add
     */
    public void setAllStreets(List<Drawable> streets) {
        this.allStreets = streets;
    }

    /***
     * reverting changes made because of time shift
     */
    private void normalizeAfterTimeShift() {
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

    /***
     * Set time to it's default which is "23:59:59" for us
     */
    @FXML
    private void setTimeDefault() {
        timeManager.setDefaultTime(timeGUI);
        timeManager.timer.stop();
        timeManager.moveInTime("23:59:59",updates, timeGUI, timeTable, mapContent,1, speedChange);
        float scaleForSpeed = (float) speedChange.getValue();
        timeManager.setScale(scaleForSpeed);
    }

    /***
     * Controlls zooming according user input ScrollEvent
     * @param event event of zooming
     */
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
                new KeyFrame(Duration.millis(200), new KeyValue(mapContent.scaleXProperty(), scale)),
                new KeyFrame(Duration.millis(200), new KeyValue(mapContent.scaleYProperty(), scale)),
                new KeyFrame(Duration.millis(200), new KeyValue(mapContent.translateXProperty(), mapContent.getTranslateX() - f * dx)),
                new KeyFrame(Duration.millis(200), new KeyValue(mapContent.translateYProperty(), mapContent.getTranslateY() - f * dy))
        );
        timeline.play();
    }

    /**
     *
     * @return
     */
    public boolean inEditTrafficMode() {
        return this.inEditTrafficMode;
    }

    public boolean isInEditDetours() { return  this.inEditDetours; }

    public void setScene(Scene scene) {
        this.mainScene = scene;
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

            vehicleOnRouteBus.setCenterX(50);
            vehicleOnRouteSub.setX(40);
            vehicleOnRouteTram.setX(42);

            view.cleanRouteFromStops(bottomWindow);
            view.clickedOnVoid(finalStopInfo, finalStopText, nextStopText, bottomWindow);
            delayText.setText("Delay: 0 min");
            focusedVehicle = null;
            vehicleOnRouteBus.setVisible(false);
            vehicleOnRouteSub.setVisible(false);
            vehicleOnRouteSub.setVisible(false);
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

    private void buildingDetour(Node sg){
        for(Drawable fromAll : this.allStreets) {
            if(fromAll instanceof Street) {
                Street s = (Street) fromAll;

                if(sg.getId().contains(s.getName())) {
                    if(this.streetsToAddToLine.size() == 0) {
                        if(s.follows(this.beingDetoured)) {
                            this.streetsToAddToLine.add(s);
//                            ((javafx.scene.shape.Line) sg).setStroke(this.lineDetoured.getColor());
                            javafx.scene.shape.Line singleStreet = new javafx.scene.shape.Line(s.begin().getX(), s.begin().getY(), s.end().getX(), s.end().getY());
                            singleStreet.setStroke(this.lineDetoured.getColor());
                            singleStreet.setStrokeWidth(2);
                            mapContent.getChildren().add(singleStreet);
                        }
                    } else {
                        if(s.follows(this.streetsToAddToLine.get(this.streetsToAddToLine.size()-1))) {
                            javafx.scene.shape.Line singleStreet = new javafx.scene.shape.Line(s.begin().getX(), s.begin().getY(), s.end().getX(), s.end().getY());
                            singleStreet.setStroke(this.lineDetoured.getColor());
                            singleStreet.setStrokeWidth(2);
                            mapContent.getChildren().add(singleStreet);
                            this.streetsToAddToLine.add(s);
                        }
                    }
                }
            }
        }
    }

    private void setTraficJam(Node sg, Street st){
        if(sg.getId().contains(st.getName())) {
            try {
                mainScene.getStylesheets().remove(normalLine);
            } catch (Exception ignored){
            }
            mainScene.getStylesheets().add(dashedLine);
            sg.getStyleClass().add("dashedLine");
            trafficSpinner.setVisible(true);
            setIntensity.setVisible(true);
            spinnerVal.setValue(st.getTrafficDensity());

            setIntensity.setOnMouseClicked(event2 -> {
                st.setTrafficDensity(trafficSpinner.getValue());

                if(st.getTrafficDensity() == 1) {
                    mainScene.getStylesheets().add(normalLine);
                    sg.getStyleClass().add("normalLine");
                    try {
                        mainScene.getStylesheets().remove(dashedLine);
                    } catch (Exception ignored){
                    }
                }
                trafficSpinner.setVisible(false);
                setIntensity.setVisible(false);
            });
        }
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
                    for(Line line : lines) {
                        for(Street st : line.getStreetList()) {
                            if(sg.getId().contains(st.getName())) {
                                densityInfo.setText("Traffic density on "+ line.getName() + " on " + st.getName() + ": " + st.getTrafficDensity());
                                densityInfo.setFont(Font.font ("SansSerif", 14));
                                Tooltip.install(sg, densityInfo);
                            }
                        }
                    }

                });

                sg.setOnMouseClicked(event -> {
                    focusedVehicle = null;
                    vehicleOnRouteBus.setVisible(false);
                    vehicleOnRouteTram.setVisible(false);
                    vehicleOnRouteSub.setVisible(false);
                    vehicleOnRouteBus.setCenterX(50);
                    vehicleOnRouteSub.setX(40);
                    vehicleOnRouteTram.setX(42);

                    delayText.setText("Delay: 0 min");
                    boolean onLine = false;

                    if(this.beingDetoured != null) {
                        buildingDetour(sg);
                    }

                    for(Line line : lines) {
                        for(Street st : line.getStreetList()) {
                            if(!inEditTrafficMode && !inEditDetours){
                                if(sg.getId().contains(st.getName())) {
                                    bottomWindow.getChildren().removeIf(sg2 -> sg2.getId().contains("RouteStop"));
                                }
                                view.clickedOnLine(finalStopInfo, finalStopText);
                                onLine = true;
                            } else if(inEditTrafficMode && !inEditDetours) {
                                setTraficJam(sg, st);
                            } else {
                                if(sg.getId().contains(st.getName())) {
                                    if(this.beingDetoured == null) {
                                        try {
                                            mainScene.getStylesheets().remove(normalLine);
                                        } catch (Exception ignored){
                                        }
                                        mainScene.getStylesheets().add(dashedLine);
                                        sg.getStyleClass().add("dashedLine2");
                                        this.beingDetoured = st;
                                        this.lineDetoured = line;
                                    }
                                }
                            }
                        }
                    }
                    if(!onLine) {
                        view.clickedOnVoid(finalStopInfo, finalStopText, nextStopText, bottomWindow);
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
                               if (!otherLine.getName().equals(line.getName())) {
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

    /**
     * Returns vehicle on Route (Bottom line)
     *
     * @return vehicle on route when clicked on bus
     */
    public Circle getVehicleOnRouteBus() {
        return vehicleOnRouteBus;
    }

    /**
     * @return Vehicle on route when clicked on sub
     */
    public Rectangle getVehicleOnRouteSub() {
        return vehicleOnRouteSub;
    }

    /**
     * @return Vehicle on route when clicked on tram
     */
    public Rectangle getVehicleOnRouteTram() {
        return vehicleOnRouteTram;
    }

    /**
     * Returns Name of following stop
     *
     * @return name of the following stop
     */
    public Text getNextStopText() {
        return this.nextStopText;
    }

    /**
     * Returns focused vehicle
     *
     * @return focused vehicle
     */
    public Vehicle getFocusedVehicle() {
        return focusedVehicle;
    }

    /**
     * Sets info to object vehicle
     */
    public void setVehicleInfo() {
        ObservableList<Node> x = mapContent.getChildren();

        for(Node sg : x) {
            if((sg instanceof Rectangle && !(sg.equals(background))) || sg instanceof Circle || sg instanceof Polygon) {
                sg.setCursor(Cursor.HAND);
                sg.setOnMouseClicked(event -> {

                    for(Vehicle v : allVehicles) {
                        if(sg.getId().contains(v.getName())) {

                            focusedVehicle = v;
                            if(sg instanceof Circle)
                                vehicleOnRouteBus.setVisible(true);
                            if(sg instanceof Rectangle)
                                vehicleOnRouteSub.setVisible(true);
                            if(sg instanceof Polygon)
                                vehicleOnRouteTram.setVisible(true);
                        }
                    }
                    if (focusedVehicle != null) {
                        int delay =0;
                        int speed;
                        String type = focusedVehicle.getLine().getType();

                        if (type.equals("Bus"))
                            speed = 1;
                        else if (type.equals("Tram"))
                            speed = 2;
                        else
                            speed = 3;

                        for (Street s : focusedVehicle.getLine().getStreetList()){
                            if(s.getTrafficDensity() != 1){
                                //meskanie += (dlzka ulice / pixely za sekundu)*spomalenie*2 - (dlzka ulice / pixely za sekundu)
                                //(dlzka ulice / pixely za sekundu) odcitavame preto lebo pri spomaleni urovne 2 prechod bude trvat 4 nasobnu dobu a teda 1 nasobok odcitame lebo tolko by to trvalo normalne
                                delay += (s.begin().coordsDistance(s.end())/(5.31340759143*speed)) * s.getTrafficDensity()*2 - (s.begin().coordsDistance(s.end())/(5.31340759143*speed));
                            }
                        }
//                        System.out.println("delay:" + delay);
                        double delayInMins = delay/60.0;
                        int fullMins = (int)delayInMins;

                        if((delayInMins - fullMins) > 0.5)
                            ++fullMins;

//                        System.out.println(delayInMins + " " + fullMins);
                        focusedVehicle.setDelay(fullMins);
                        delayText.setText("Delay: " + fullMins + " min");
                    }

                    for(Line line : lines) {
                        if(sg.getId().contains(line.getName())) {
                            bottomWindow.getChildren().removeIf(sg2 -> sg2.getId().contains("RouteStop"));

                            generateStopsOnPath(line);

                            view.setLineInfoFocused(nextStopInfo, nextStopText, finalStopInfo, finalStopText, delayText);

                            view.colorRoute(vehicleRoute, line.getColor().saturate().saturate());
                            view.changeFinalStopText(finalStopText, line);

                            for(Line otherLine : lines) {
                                if (!otherLine.getName().equals(line.getName())) {
                                    view.changeLineColor(mapContent, otherLine, otherLine.getColor().desaturate().desaturate().desaturate().desaturate());
                                } else {
                                    view.changeLineColor(mapContent, otherLine, otherLine.getColor().saturate().saturate());
                                }
                            }
                            view.clickedOnLine(finalStopInfo, finalStopText);
                        }
                    }

                });
            }
        }
    }

    /**
     * Generaating stops on the path
     *
     * @param line public transport line
     */
    public void generateStopsOnPath(Line line) {
        focusedLineStopsNames.clear();
        focusedLineStops.clear();

        LinkedHashMap<String, Coordinate> path = line.getPath();

        path.forEach((k,v)-> {
            focusedLineStops.add(v);
            focusedLineStopsNames.add(k);
        });

        List<Stop> allStops = line.getStopList();

        double realToImPath = line.totalPathLength()/850;
        double distFromStart = 0;
        int j = 0;

        for(int i = 1; i < focusedLineStops.size(); i++) {
            distFromStart += focusedLineStops.get(i).coordsDistance(focusedLineStops.get(i-1));

            if(focusedLineStopsNames.get(i).contains("stop")) {
                LocalTime stopTime = null;
                
                if(focusedVehicle != null) {
                    double secondsOfPassed;

                    if(focusedVehicle instanceof Bus) {
                        secondsOfPassed = distFromStart/4.64;
                    } else if(focusedVehicle instanceof Tram) {
                        secondsOfPassed = distFromStart/(4.64*2);
                    } else {
                        secondsOfPassed = distFromStart/(4.64*3);
                    }

                    int wholeSecs = (int) secondsOfPassed;
                    double decSecs = secondsOfPassed - wholeSecs;
                    long nanos = (long) (decSecs * 1000000000);

                    stopTime = focusedVehicle.getTimeofDeparture().plusSeconds(wholeSecs);
                    stopTime = stopTime.plusNanos(nanos);
                    int stopsToCount = j;
                    while(stopsToCount > 0) {
                        stopTime = stopTime.plusSeconds(34);
                        stopTime = stopTime.plusNanos(449667100);
                        stopsToCount--;
                    }

                    stopTime = stopTime.plusSeconds(34);
                    stopTime = stopTime.plusNanos(449667100);
                }

                view.addStopToRoute(vehicleRoute, distFromStart, realToImPath, j, allStops, bottomWindow, stopTime);
                j++;
            }
        }
    }

    /**
     * Highlights route from the list and fades others
     *
     * @param lines list of all lines
     */
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
                        if (!otherLine.getName().equals(line.getName())) {
                            view.changeLineColor(mapContent, otherLine, otherLine.getColor().desaturate().desaturate().desaturate().desaturate());
                        } else {
                            view.changeLineColor(mapContent, otherLine, otherLine.getColor().saturate().saturate());
                        }
                    }
                }
            }
        });
    }

    /**
     * showing route of selected vehicle
     */
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

    /**
     * Sets current time to the application
     */
    public void setCurrentTime() {
        timeManager.timer.stop();
        timeManager.moveInTime( timeManager.formatTime(LocalTime.now().getHour(),LocalTime.now().getMinute(),LocalTime.now().getSecond()), updates, timeGUI,timeTable, mapContent,1, speedChange);
        timeManager.setScale(1);
        timeManager.changeSpeed();
    }

    /**
     * Returns mapContent
     *
     * @return mapContent
     */
    public Pane getMapContent() {
        return this.mapContent;
    }
}
