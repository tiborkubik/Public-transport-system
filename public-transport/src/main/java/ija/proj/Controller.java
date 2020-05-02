package ija.proj;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

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

    private List<Drawable> GUIelements = new ArrayList<>();
    private Timer timer;
    private LocalTime currentTime = LocalTime.now();

    private List<Line> lines = new ArrayList<>();

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

    /***
     * displays gui elements
     * @param GUIelements - list of elements to display
     */
    public void setGUIelements(List<Drawable> GUIelements) {
        this.GUIelements = GUIelements;
        for(Drawable obj : GUIelements) {
            mapContent.getChildren().addAll(obj.getGUI());
        }
    }

    @FXML
    public void setBasicSettings() {
        linesInfo.setExpandedPane(null);

        setDefaultLineColors(this.lines);
    }

    public void setLines(List<Line> lines){
        this.lines = lines;
    }

    public void setDefaultLineColors(List<Line> lines) {
        System.out.println(lines.get(0).getName());

        List<Color> colorsForLines = new ArrayList<>();
        colorsForLines.add(Color.FORESTGREEN);
        colorsForLines.add(Color.ORANGERED);
        colorsForLines.add(Color.CORNFLOWERBLUE);
        colorsForLines.add(Color.YELLOW);
        colorsForLines.add(Color.SANDYBROWN);
        colorsForLines.add(Color.ROYALBLUE);
        colorsForLines.add(Color.OLIVE);

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
            ObservableList<String> items = FXCollections.observableArrayList ();

            TitledPane pane1 = new TitledPane(name, new Label("Stops in line " + name + " are following:\n"));
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
                sg.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                       for(Line line : lines) {
                         if(sg.getId().contains(line.getName())) {
                               for(Line otherLine : lines) {
                                   if (otherLine.getName() != line.getName()) {
                                       //otherLine.getColor().brighter()
                                       ChangeLineColor(otherLine, otherLine.getColor().desaturate().desaturate().desaturate().desaturate());
                                   } else {
                                       ChangeLineColor(otherLine, otherLine.getColor().saturate().saturate());
                                   }
                               }

                             ObservableList<TitledPane> panes = linesInfo.getPanes();
                             for(TitledPane pane : panes) {
                                 System.out.println("---as");
                                 if(line.getName().contains(pane.getId()))
                                     linesInfo.setExpandedPane(pane);
                             }
                           }
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
            for(Street street : line.getStreetList()) {
                if(sp.getId() != null && sp.getId().contains(line.getName())) {
                    sp.setStroke(color);
                }
            }
        }
    }
}
