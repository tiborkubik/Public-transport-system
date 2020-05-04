package ija.proj;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

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
    private Accordion linesInfo;

    @FXML
    private javafx.scene.shape.Line vehicleRoute;

    public Timer timer;

    private LocalTime currentTime = LocalTime.now();

    private List<Line> lines = new ArrayList<>();

    List<Color> colorsForLines = new ArrayList<>();

    private List<UpdateState> updates = new ArrayList<>();

    /***
     * changes speed
     */
    @FXML
    private void speedChanged() {
        float scaleForSpeed = (float) speedChange.getValue();
        timer.cancel();
        startTimer(scaleForSpeed);
    }
    /***
     * Zooms map
     */
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
       // Platform.runLater({

        Platform.runLater(() -> {
            mapContent.setScaleX(zoomValue * mapContent.getScaleX());
            mapContent.setScaleY(zoomValue * mapContent.getScaleY());
        });

        mapContent.layout();
    }

    /***
     * displays gui elements
     * @param GUIelements - list of elements to display
     */
    public void setGUIelements(List<Drawable> GUIelements) {
        nextStopInfo.setId("nextStopInfo");
        nextStopText.setId("nextStopText");
        finalStopInfo.setId("finalStopInfo");
        finalStopText.setId("finalStopInfo");
        delayText.setId("delayText");

        nextStopInfo.setOpacity(0.5);
        nextStopText.setOpacity(0.5);
        finalStopInfo.setOpacity(0.5);
        finalStopText.setOpacity(0.5);
        delayText.setOpacity(0.5);

        for(Drawable obj : GUIelements) {
            mapContent.getChildren().addAll(obj.getGUI());

            if(obj instanceof UpdateState) {
                updates.add((UpdateState) obj);
            }
        }
    }

    public void setBasicSettings(List<Line> lines) {
        background.setOnMouseClicked(event -> {
            nextStopInfo.setOpacity(0.5);
            nextStopText.setOpacity(0.5);
            finalStopInfo.setOpacity(0.5);
            finalStopText.setOpacity(0.5);
            finalStopText.setText("-");
            delayText.setOpacity(0.5);

            linesInfo.setExpandedPane(null);
            vehicleRoute.setStroke(Color.rgb(50,50,50));
            vehicleRoute.setOpacity(0.5);

            for(int i = 0; i < lines.size(); i++) {
                ChangeLineColor(lines.get(i), colorsForLines.get(i));
            }

            try {
                bottomWindow.getChildren().removeIf(sg -> sg.getId().contains("RouteStop"));
            } catch (Exception e) {

            }
        });
    }

    public void setLines(List<Line> lines){
        this.lines = lines;

        colorsForLines.add(Color.FORESTGREEN);
        colorsForLines.add(Color.ORANGERED);
        colorsForLines.add(Color.CORNFLOWERBLUE);
        colorsForLines.add(Color.YELLOW);
        colorsForLines.add(Color.SANDYBROWN);
        colorsForLines.add(Color.ROYALBLUE);
        colorsForLines.add(Color.OLIVE);
    }

    public void setDefaultTime() {
        timeGUI.setText("00:00:00");
    }

    public void setCurrentTime() {
        timeGUI.setText(currentTime.getHour() + ":" + currentTime.getMinute() + ":" + currentTime.getSecond());
    }

    public void setDefaultLineColors(List<Line> lines) {
        for(int i = 0; i < lines.size(); i++) {
            lines.get(i).setColor(colorsForLines.get(i));
        }
    }

    /***
     * Adds information about line to the navigation
     * @param lines
     */
    public void setLinesInfo(List<Line> lines) {
        for(Line line : lines) {
            String name = line.getName();
            ListView<String> list = new ListView<>();
            ObservableList<String> items = FXCollections.observableArrayList();

            TitledPane pane1 = new TitledPane(name, new Label("Stops in line " + name + " are following:\n"));
            pane1.setFont(Font.font ("Impact", 14));
            for(Stop stop : line.getStopList()) {
                String stopName = stop.getName();
                items.add(stopName);
            }

            list.setItems(items);
            pane1.setContent(list);
            pane1.setId(name);
            linesInfo.getPanes().add(pane1);
        }
    }

    /***
     * Starts timer
     * @param scale
     */
    public void startTimer(double scale) {
        Platform.runLater(() -> {
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
        });
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

                    boolean f = false;
                    for(Line line : lines) {
                        for(Street st : line.getStreetList()) {
                            if(sg.getId().contains(st.getName())) {
                                try {
                                    bottomWindow.getChildren().removeIf(sg2 -> sg2.getId().contains("RouteStop"));
                                } catch (Exception e) {

                                }
                                finalStopInfo.setOpacity(1);
                                finalStopText.setOpacity(1);

                                f = true;
                            }
                        }
                    }
                    if(!f) {
                        finalStopInfo.setOpacity(0.5);
                        finalStopText.setOpacity(0.5);
                        finalStopText.setText("-");
                        try {
                            bottomWindow.getChildren().removeIf(sg2 -> sg2.getId().contains("RouteStop"));
                        } catch (Exception e) {

                        }
                    }
                    nextStopInfo.setOpacity(0.5);
                    nextStopText.setOpacity(0.5);
                    delayText.setOpacity(0.5);

                    vehicleRoute.setStroke(Color.rgb(50,50,50));
                    vehicleRoute.setOpacity(0.5);

                   for(Line line : lines) {
                     if(sg.getId().contains(line.getName())) {

                         generateStopsOnPath(line);
                         vehicleRoute.setStroke(line.getColor().saturate().saturate());
                         vehicleRoute.setOpacity(1.0);
                         finalStopText.setText(line.getStopList().get(line.getStopList().size()-1).getName());
                           for(Line otherLine : lines) {
                               if (otherLine.getName() != line.getName()) {
                                   ChangeLineColor(otherLine, otherLine.getColor().desaturate().desaturate().desaturate().desaturate());
                               } else {
                                   ChangeLineColor(otherLine, otherLine.getColor().saturate().saturate());
                               }
                           }
                         ObservableList<TitledPane> panes = linesInfo.getPanes();
                         for(TitledPane pane : panes) {
                             if(line.getName().contains(pane.getId()))
                                 linesInfo.setExpandedPane(pane);
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
                javafx.scene.shape.Line stopLine = new javafx.scene.shape.Line(vehicleRoute.getStartX()+distFromStart/realToImPath, vehicleRoute.getStartY() , vehicleRoute.getStartX()+distFromStart/realToImPath + 10, vehicleRoute.getStartY() - 10);
                stopLine.setStroke(Color.rgb(50,50,50));
                stopLine.setStrokeLineCap(StrokeLineCap.ROUND);
                stopLine.setStrokeLineJoin(StrokeLineJoin.ROUND);
                stopLine.setStrokeWidth(4);
                stopLine.setId("RouteStop");

                Text stopName = new Text(stopLine.getEndX() + 10, stopLine.getEndY() - 5, allStops.get(i).getName());

                stopName.setFont(Font.font ("Impact", 14));
                stopName.getTransforms().add(new Rotate(-45, stopLine.getEndX() + 10, stopLine.getEndY() - 5));
                stopName.setFill(Color.rgb(50, 50, 50));
                stopName.setId("RouteStopName");
                bottomWindow.getChildren().add(stopLine);
                bottomWindow.getChildren().add(stopName);
            }

            distFromStart += allStops.get(i).getCoordinate().coordDistance(allStops.get(i+1).getCoordinate());

            javafx.scene.shape.Line stopLine = new javafx.scene.shape.Line(vehicleRoute.getStartX()+distFromStart/realToImPath, vehicleRoute.getStartY() , vehicleRoute.getStartX()+distFromStart/realToImPath + 10, vehicleRoute.getStartY() - 10);
            stopLine.setStroke(Color.rgb(50,50,50));
            stopLine.setStrokeLineCap(StrokeLineCap.ROUND);
            stopLine.setStrokeLineJoin(StrokeLineJoin.ROUND);
            stopLine.setStrokeWidth(4);
            stopLine.setId("RouteStop");

            Text stopName = new Text(stopLine.getEndX() + 10, stopLine.getEndY() - 5, allStops.get(i).getName());
            stopName.setFont(Font.font ("Impact", 14));
            stopName.getTransforms().add(new Rotate(-45, stopLine.getEndX() + 10, stopLine.getEndY() - 5));

            stopName.setFill(Color.rgb(50, 50, 50));
            stopName.setId("RouteStopName");
            bottomWindow.getChildren().add(stopLine);
            bottomWindow.getChildren().add(stopName);
        }
    }

    public void generateStopsOnPath(Vehicle singleV) {
        double realToImPath = singleV.totalPathLength()/850;

        double distFromStart = singleV.getLine().getStopList().get(0).getCoordinate().coordDistance(singleV.getLine().getStreetList().get(0).begin());

        for(int i = 1; i < singleV.getLine().getStopList().size()-1; i++) {
            if(i == 1) {
                javafx.scene.shape.Line stopLine = new javafx.scene.shape.Line(vehicleRoute.getStartX()+distFromStart/realToImPath, vehicleRoute.getStartY() , vehicleRoute.getStartX()+distFromStart/realToImPath + 10, vehicleRoute.getStartY() - 10);
                stopLine.setStroke(Color.rgb(50,50,50));
                stopLine.setStrokeLineCap(StrokeLineCap.ROUND);
                stopLine.setStrokeLineJoin(StrokeLineJoin.ROUND);
                stopLine.setStrokeWidth(4);
                stopLine.setId("RouteStop");

                Text stopName = new Text(stopLine.getEndX() + 10, stopLine.getEndY() - 5, singleV.getLine().getStopList().get(i).getName());

                stopName.setFont(Font.font ("Impact", 14));
                stopName.getTransforms().add(new Rotate(-45, stopLine.getEndX() + 10, stopLine.getEndY() - 5));
                stopName.setFill(Color.rgb(50, 50, 50));
                stopName.setId("RouteStopName");
                bottomWindow.getChildren().add(stopLine);
                bottomWindow.getChildren().add(stopName);
            }

            distFromStart += singleV.getLine().getStopList().get(i).getCoordinate().coordDistance(singleV.getLine().getStopList().get(i+1).getCoordinate());

            javafx.scene.shape.Line stopLine = new javafx.scene.shape.Line(vehicleRoute.getStartX()+distFromStart/realToImPath, vehicleRoute.getStartY() , vehicleRoute.getStartX()+distFromStart/realToImPath + 10, vehicleRoute.getStartY() - 10);
            stopLine.setStroke(Color.rgb(50,50,50));
            stopLine.setStrokeLineCap(StrokeLineCap.ROUND);
            stopLine.setStrokeLineJoin(StrokeLineJoin.ROUND);
            stopLine.setStrokeWidth(4);
            stopLine.setId("RouteStop");

            Text stopName = new Text(stopLine.getEndX() + 10, stopLine.getEndY() - 5, singleV.getLine().getStopList().get(i).getName());
            stopName.setFont(Font.font ("Impact", 14));
            stopName.getTransforms().add(new Rotate(-45, stopLine.getEndX() + 10, stopLine.getEndY() - 5));

            stopName.setFill(Color.rgb(50, 50, 50));
            stopName.setId("RouteStopName");
            bottomWindow.getChildren().add(stopLine);
            bottomWindow.getChildren().add(stopName);
        }
    }

    public void showVehicleRoute(List<Vehicle> allVehicles) {
        ObservableList<Node> x = mapContent.getChildren();
        for(Node sg : x) {
            if(sg instanceof Circle || sg instanceof Polygon || (sg instanceof Rectangle && !sg.equals(background))) {
                sg.setOnMouseClicked(event ->{

                   // System.out.println(currentTime.getHour() + " " + currentTime.getMinute() + " " + currentTime.getSecond());
                    try{
                        bottomWindow.getChildren().removeIf(sg2 -> sg2.getId().contains("RouteStop"));
                    } catch (Exception e) {

                    }

                    nextStopInfo.setOpacity(1);
                    nextStopText.setOpacity(1);
                    finalStopInfo.setOpacity(1);
                    finalStopText.setOpacity(1);
                    delayText.setOpacity(1);

                        for(Vehicle singleV : allVehicles) {
                            if(singleV.getName().equals(sg.getId())) {
                                finalStopText.setText(singleV.getLine().getStopList().get(singleV.getLine().getStopList().size()-1).getName());
                                generateStopsOnPath(singleV);

                                vehicleRoute.setStroke(singleV.getLine().getColor().saturate().saturate());
                                vehicleRoute.setOpacity(1.0);

                                for(Line otherLine : lines) {
                                    if (otherLine.getName() != singleV.getLine().getName()) {
                                        ChangeLineColor(otherLine, otherLine.getColor().desaturate().desaturate().desaturate().desaturate());
                                    } else {
                                        ChangeLineColor(otherLine, otherLine.getColor().saturate().saturate());
                                    }
                                }
                            }

                            ObservableList<TitledPane> panes = linesInfo.getPanes();
                            for(TitledPane pane : panes) {
                                if(singleV.getLine().getName().contains(pane.getId()))
                                    linesInfo.setExpandedPane(pane);
                            }
                        }
                });
            }
        }
    }

    /***
     * changes line to a certain color
     * @param line line to which it changes color
     * @param color to which color we are changing the line
     */
    public void ChangeLineColor(Line line, Color color) {
        ObservableList<Node> x = mapContent.getChildren();
        for(Node sg : x) {
            Shape sp = (Shape) sg;
            for(Street ignored : line.getStreetList()) {
                if(sp.getId() != null && sp.getId().contains(line.getName())) {
                    sp.setStroke(color);
                }
            }
        }
    }
}
