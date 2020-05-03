package ija.proj;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;

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
    private Rectangle background;

    @FXML
    private Slider speedChange;

    @FXML
    private Accordion linesInfo;

    @FXML
    private Circle vehicleOnRoute;

    @FXML
    private javafx.scene.shape.Line vehicleRoute;

    private List<Drawable> GUIelements = new ArrayList<>();
    private Timer timer;
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
        mapContent.setScaleX(zoomValue * mapContent.getScaleX());
        mapContent.setScaleY(zoomValue * mapContent.getScaleY());
        mapContent.layout();
    }

    private Circle getVehicleOnRoute() {
        return this.vehicleOnRoute;
    }

    /***
     * displays gui elements
     * @param GUIelements - list of elements to display
     */
    public void setGUIelements(List<Drawable> GUIelements) {
        this.GUIelements = GUIelements;
        for(Drawable obj : GUIelements) {
            mapContent.getChildren().addAll(obj.getGUI());

            if(obj instanceof UpdateState) {
                updates.add((UpdateState) obj);
            }
        }
    }

    public void setBasicSettings(List<Line> lines) {
        background.setOnMouseClicked(event -> {
            linesInfo.setExpandedPane(null);
            vehicleRoute.setStroke(Color.rgb(50,50,50));
            vehicleRoute.setOpacity(0.5);
            vehicleOnRoute.setRadius(8);
            vehicleOnRoute.setLayoutY(113);
            for(int i = 0; i < lines.size(); i++) {
                ChangeLineColor(lines.get(i), colorsForLines.get(i));
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
        timer = new Timer(false);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                currentTime = currentTime.plusSeconds(1);
                for(UpdateState update : updates) {
                    update.update(currentTime);
                }
            }
        }, 0, (long) (1000 / scale));
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
                    vehicleRoute.setStroke(Color.rgb(50,50,50));
                    vehicleRoute.setOpacity(0.5);

                   for(Line line : lines) {
                     if(sg.getId().contains(line.getName())) {
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

    public void showVehicleRoute(List<Vehicle> allVehicles) {
        ObservableList<Node> x = mapContent.getChildren();
        for(Node sg : x) {
            if(sg instanceof Circle) {
                sg.setOnMouseClicked(event ->{
                        for(Vehicle singleV : allVehicles) {
                            if(singleV.getName().equals(sg.getId())) {
                                vehicleRoute.setStroke(singleV.getLine().getColor().saturate().saturate());
                                vehicleRoute.setOpacity(1.0);
                                vehicleOnRoute.setRadius(12);
                                vehicleOnRoute.setLayoutY(699);
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
