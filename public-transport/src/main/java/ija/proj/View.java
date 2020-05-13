package ija.proj;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class view modifies GUI elements when controller calls its methods. It contains bunch of methods defining the basic style in addition with
 * methods that show/hide certain elements, create new etc
 */
public class View {
    List<Color> colorsForLines = new ArrayList<>();         /**< List of prepared colors for lines */
    Controller controller;                                  /**< Main program controller */
    private Image bg = new Image("mapa1.jpg");          /**< Path to background image */
    private BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true); /**< Defines size of the background picture */

    /**
     * View constructor. Program controller must be specified in advance
     *
     * @param controller Main program controller
     */
    public View(Controller controller) {
        this.controller = controller;
        setColorsForLines();
    }

    /**
     * Colors that will define line colors are added into its list
     */
    private void setColorsForLines() {
        colorsForLines.add(Color.FORESTGREEN);
        colorsForLines.add(Color.ORANGERED);
        colorsForLines.add(Color.CORNFLOWERBLUE);
        colorsForLines.add(Color.YELLOW);
        colorsForLines.add(Color.SANDYBROWN);
        colorsForLines.add(Color.PURPLE);
        colorsForLines.add(Color.HOTPINK);
    }

    /**
     * Assigning unique color to each line
     *
     * @param lines List of all active lines
     */
    public void setDefaultLineColors(List<Line> lines) {
        for (int i = 0; i < lines.size(); i++) {
            lines.get(i).setColor(colorsForLines.get(i));
        }
    }

    /**
     * Setting wallpaper on map
     *
     * @param map GUI element representing map content
     */
    public void setBackground(Pane map) {
        map.setBackground(new Background(new BackgroundImage(bg,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                bSize)));
    }

    /**
     * Method changes opacity of certain elements when clicked on line
     *
     * @param nextStopInfo Next stop info GUI
     * @param nextStopText  Next stop value GUI
     * @param finalStopInfo Final stop info GUI
     * @param finalStopText Final stop text GUI
     * @param delayText Delay text GUI
     */
    public void setLineInfoFocused(Text nextStopInfo, Text nextStopText, Text finalStopInfo, Text finalStopText, Text delayText) {
        nextStopInfo.setOpacity(1);
        nextStopText.setOpacity(1);
        finalStopInfo.setOpacity(1);
        finalStopText.setOpacity(1);
        delayText.setOpacity(1);
    }

    /**
     * Bottom route is colored according to line color
     *
     * @param vehicleRoute Line representing scaled line path
     * @param color Color of line
     */
    public void colorRoute(javafx.scene.shape.Line vehicleRoute, Color color) {
        vehicleRoute.setStroke(color);
        vehicleRoute.setOpacity(1.0);
    }

    /**
     * Method changes opacity and value of certain elements when clicked on background
     *
     * @param nextStopInfo Next stop info GUI
     * @param nextStopText  Next stop value GUI
     * @param finalStopInfo Final stop info GUI
     * @param finalStopText Final stop text GUI
     * @param delayText Delay text GUI
     * @param route Line representing scaled line route
     */
    public void setLineInfoDefault(Text nextStopInfo, Text nextStopText, Text finalStopInfo, Text finalStopText, Text delayText, javafx.scene.shape.Line route) {
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

        finalStopText.setText("-");
        nextStopText.setText("-");

        route.setStroke(Color.rgb(50, 50, 50));
        route.setOpacity(0.5);
    }

    /***
     * changes line to a certain color
     * @param mapContent Pane GUI representing node where all map elements are
     * @param line line to which it changes color
     * @param color to which color we are changing the line
     */
    public void changeLineColor(Pane mapContent, Line line, Color color) {
        ObservableList<Node> x = mapContent.getChildren();
        for (Node sg : x) {
            Shape sp = (Shape) sg;
            for (Street ignored : line.getStreetList()) {
                if (sp.getId() != null && sp.getId().contains(line.getName())) {
                    sp.setStroke(color);
                }
            }
        }
    }

    /**
     * Method adds all elements from list of drawables on canvas
     *
     * @param elements All drawable elements
     * @param mapContent Pane representing node containing all drawable elements
     */
    public void showMapContent(List<Drawable> elements, Pane mapContent) {
        for (Drawable obj : elements) {
            mapContent.getChildren().addAll(obj.getGUI());
        }
    }

    /**
     * Adding single element on map
     *
     * @param element Single drawable element
     * @param mapContent Pane representing node containing all drawable elements
     */
    public void addElement(Drawable element, Pane mapContent) {
        mapContent.getChildren().addAll(element.getGUI());
    }

    /**
     * Lines info is filled by line names
     *
     * @param lines List of all lines
     * @param linesInfo GUI listView element where all lines will be in a list
     */
    public void viewLinesInfo(List<Line> lines, ListView<Object> linesInfo) {
        for (Line line : lines) {
            linesInfo.getItems().add(line.getName());
        }

    }

    /**
     * Opacity of final stop information is focused
     *
     * @param finalStopInfo Final stop Information text
     * @param finalStopText Final stop Value
     */
    public void clickedOnLine(Text finalStopInfo, Text finalStopText) {
        finalStopInfo.setOpacity(1);
        finalStopText.setOpacity(1);
    }

    /**
     * Information in bottom window are unfocused when clicked on background
     *
     * @param nextStopText  Next stop value GUI
     * @param finalStopInfo Final stop info GUI
     * @param finalStopText Final stop text GUI
     * @param bottomWindow GUI section of path information
     */
    public void clickedOnVoid(Text finalStopInfo, Text finalStopText, Text nextStopText, Pane bottomWindow) {
        finalStopInfo.setOpacity(0.5);
        finalStopText.setOpacity(0.5);
        finalStopText.setText("-");
        nextStopText.setText("-");

        cleanRouteFromStops(bottomWindow);
    }

    /**
     * Lines and texts representing stops in bottom window are removed
     *
     * @param bottomWindow GUI element representing bottom window
     */
    public void cleanRouteFromStops(Pane bottomWindow) {
        try {
            bottomWindow.getChildren().removeIf(sg2 -> sg2.getId().contains("RouteStop"));
        } catch (Exception ignored) {
        }
    }

    /**
     * Value of final stop text is changed according to last Line stop
     *
     * @param text Text GUI element
     * @param line Line of stop
     */
    public void changeFinalStopText(Text text, Line line) {
        text.setText(line.getStopList().get(line.getStopList().size() - 1).getName());
    }

    /**
     * Position of vehicle on route in bottom window is changed in proper scale according to the position from start on real line
     *
     * @param vehicleRoute Line representing scaled vehicle route
     * @param distFromStart Distance in pixels from start to current vehicle position
     * @param realToImPath Ratio of length of real line to route length
     * @param i Iterator
     * @param singleV Vehicle on which user clicked
     * @param bottomWindow Bottom GUI window
     */
    public void addStopToRoute(javafx.scene.shape.Line vehicleRoute, double distFromStart, double realToImPath, int i, Vehicle singleV, Pane bottomWindow) {
        javafx.scene.shape.Line stopLine = new javafx.scene.shape.Line(vehicleRoute.getStartX() + distFromStart / realToImPath, vehicleRoute.getStartY(), vehicleRoute.getStartX() + distFromStart / realToImPath + 10, vehicleRoute.getStartY() - 10);
        stopLine.setStroke(Color.rgb(50, 50, 50));
        stopLine.setStrokeLineCap(StrokeLineCap.ROUND);
        stopLine.setStrokeLineJoin(StrokeLineJoin.ROUND);
        stopLine.setStrokeWidth(4);
        stopLine.setId("RouteStop");

        Text stopName = new Text(stopLine.getEndX() + 10, stopLine.getEndY() - 5, singleV.getLine().getStopList().get(i).getName());

        stopName.setFont(Font.font("Impact", 14));
        stopName.getTransforms().add(new Rotate(-45, stopLine.getEndX() + 10, stopLine.getEndY() - 5));
        stopName.setFill(Color.rgb(50, 50, 50));
        stopName.setId("RouteStopName");
        bottomWindow.getChildren().add(stopLine);
        bottomWindow.getChildren().add(stopName);
    }

    /**
     * Position of vehicle on route in bottom window is changed in proper scale according to the position from start on real line
     *
     * @param vehicleRoute Line representing scaled vehicle route
     * @param distFromStart Distance in pixels from start to current vehicle position
     * @param realToImPath Ratio of length of real line to route length
     * @param i Iterator
     * @param bottomWindow Bottom GUI window
     * @param allStops List of all stops
     * @param printTime Time when car leaves given stop
     */
    public void addStopToRoute(javafx.scene.shape.Line vehicleRoute, double distFromStart, double realToImPath, int i, List<Stop> allStops, Pane bottomWindow, LocalTime printTime) {
        javafx.scene.shape.Line stopLine = new javafx.scene.shape.Line(vehicleRoute.getStartX() + distFromStart / realToImPath, vehicleRoute.getStartY(), vehicleRoute.getStartX() + distFromStart / realToImPath + 10, vehicleRoute.getStartY() - 10);
        stopLine.setStroke(Color.rgb(50, 50, 50));
        stopLine.setStrokeLineCap(StrokeLineCap.ROUND);
        stopLine.setStrokeLineJoin(StrokeLineJoin.ROUND);
        stopLine.setStrokeWidth(4);
        stopLine.setId("RouteStop");

        try {
            Text stopName;

            if (printTime != null) {
                String hours = String.valueOf(printTime.getHour());
                String minutes = String.valueOf(printTime.getMinute());
                if (printTime.getHour() < 10) hours = "0" + hours;
                if (printTime.getMinute() < 10) minutes = "0" + minutes;

                stopName = new Text(stopLine.getEndX() + 10, stopLine.getEndY() - 5, allStops.get(i).getName() + " - " + hours + ":" + minutes);
            } else {
                stopName = new Text(stopLine.getEndX() + 10, stopLine.getEndY() - 5, allStops.get(i).getName());
            }

            stopName.setFont(Font.font("Impact", 14));
            stopName.getTransforms().add(new Rotate(-45, stopLine.getEndX() + 10, stopLine.getEndY() - 5));
            stopName.setFill(Color.rgb(50, 50, 50));
            stopName.setId("RouteStopName");

            bottomWindow.getChildren().add(stopLine);
            bottomWindow.getChildren().add(stopName);
        } catch (Exception ignored) {

        }
    }

    /**
     * Generating of stops from line stop list onto vehicleRoute in bottomWindow
     *
     * @param singleV Focused vehicle
     * @param vehicleRoute Line representing scaled line path
     * @param bottomWindow GUI bottom window
     */
    public void generateStopsOnPath(Vehicle singleV, javafx.scene.shape.Line vehicleRoute, Pane bottomWindow) {
        double realToImPath = singleV.totalPathLength() / 850;

        double distFromStart = singleV.getLine().getStopList().get(0).getCoordinate().coordsDistance(singleV.getLine().getStreetList().get(0).begin());

        for (int i = 1; i < singleV.getLine().getStopList().size() - 1; i++) {
            if (i == 1) {
                addStopToRoute(vehicleRoute, distFromStart, realToImPath, i, singleV, bottomWindow);
            }
            distFromStart += singleV.getLine().getStopList().get(i).getCoordinate().coordsDistance(singleV.getLine().getStopList().get(i + 1).getCoordinate());

            addStopToRoute(vehicleRoute, distFromStart, realToImPath, i, singleV, bottomWindow);
        }

        distFromStart += singleV.getLine().getStopList().get(singleV.getLine().getStopList().size() - 1).getCoordinate().coordsDistance(singleV.getLine().getStopList().get(singleV.getLine().getStopList().size()).getCoordinate());
        addStopToRoute(vehicleRoute, distFromStart, realToImPath, singleV.getLine().getStopList().size(), singleV, bottomWindow);
    }

    /**
     * Method prepares visibility of all key GUI elements for admin
     */
    public void prepareGUIforAdmin(
            Rectangle rightBlur1,
            Rectangle rightBlur111,
            ListView linesInfo,
            Text linesSign,
            Slider speedChange,
            Pane bottomWindow,
            Button saveExitEditing,
            Rectangle saveBackground,
            Button plusH,
            Button plusM,
            Button plusS,
            Button minusH,
            Button minusM,
            Button minusS,
            TextArea editJamsInfo,
            TextArea editDetoursInfo) {

        rightBlur111.setVisible(false);
        linesInfo.setVisible(false);
        linesSign.setVisible(false);
        speedChange.setVisible(false);
        bottomWindow.setVisible(false);

        plusH.setVisible(false);
        plusH.setDisable(true);
        plusM.setVisible(false);
        plusM.setDisable(true);
        plusS.setVisible(false);
        plusS.setDisable(true);
        minusH.setVisible(false);
        minusH.setDisable(true);
        minusM.setVisible(false);
        minusM.setDisable(true);
        minusS.setVisible(false);
        minusS.setDisable(true);

        saveBackground.setVisible(true);
        rightBlur1.setLayoutY(-145);
        saveExitEditing.setVisible(true);

        if (this.controller.inEditTrafficMode()) {
            editJamsInfo.setVisible(true);
            editDetoursInfo.setVisible(false);
        }


        if (this.controller.isInEditDetours()) {
            editJamsInfo.setVisible(false);
            editDetoursInfo.setVisible(true);
        }

    }

    /**
     * Method prepares visibility of all key GUI elements when normal state of app - after admin section leaving
     */
    public void exitGUIAdmin(Rectangle rightBlur1,
                             Rectangle rightBlur111,
                             ListView linesInfo,
                             Text linesSign,
                             Slider speedChange,
                             Pane bottomWindow,
                             Button saveExitEditing,
                             Rectangle saveBackground,
                             Button plusH,
                             Button plusM,
                             Button plusS,
                             Button minusH,
                             Button minusM,
                             Button minusS,
                             TextArea editJamsInfo,
                             TextArea editDetoursInfo) {

        rightBlur111.setVisible(true);
        linesInfo.setVisible(true);
        linesSign.setVisible(true);
        speedChange.setVisible(true);
        bottomWindow.setVisible(true);

        plusH.setVisible(true);
        plusH.setDisable(false);
        plusM.setVisible(true);
        plusM.setDisable(false);
        plusS.setVisible(true);
        plusS.setDisable(false);
        minusH.setVisible(true);
        minusH.setDisable(false);
        minusM.setVisible(true);
        minusM.setDisable(false);
        minusS.setVisible(true);
        minusS.setDisable(false);

        saveBackground.setVisible(false);
        rightBlur1.setLayoutY(-101);
        saveExitEditing.setVisible(false);

        editJamsInfo.setVisible(false);
        editDetoursInfo.setVisible(false);
    }

    /**
     * Method is called when usre clicks on a vehicle. Position of scaled vehicle is moved, proper next/final stop is set, etc..
     */
    public void showVehicleRoute(Pane mapContent,
                                 Rectangle background,
                                 Pane bottomWindow,
                                 Text nextStopInfo,
                                 Text nextStopText,
                                 Text finalStopInfo,
                                 Text finalStopText,
                                 Text delayText,
                                 List<Vehicle> allVehicles,
                                 javafx.scene.shape.Line vehicleRoute,
                                 List<Line> lines) {

        ObservableList<Node> x = mapContent.getChildren();
        for (Node sg : x) {
            //System.out.println(sg.getId());
            if (sg instanceof Circle || sg instanceof Polygon || (sg instanceof Rectangle && !sg.equals(background))) {
                sg.setOnMouseClicked(event -> {
                    cleanRouteFromStops(bottomWindow);

                    setLineInfoFocused(nextStopInfo, nextStopText, finalStopInfo, finalStopText, delayText);

                    for (Vehicle singleV : allVehicles) {
                        if (singleV.getName().equals(sg.getId())) {
                            finalStopText.setText(singleV.getLine().getStopList().get(singleV.getLine().getStopList().size() - 1).getName());

                            generateStopsOnPath(singleV, vehicleRoute, bottomWindow);

                            colorRoute(vehicleRoute, singleV.getLine().getColor().saturate().saturate());

                            for (Line otherLine : lines) {
                                if (otherLine.getName() != singleV.getLine().getName()) {
                                    changeLineColor(mapContent, otherLine, otherLine.getColor().desaturate().desaturate().desaturate().desaturate());
                                } else {
                                    changeLineColor(mapContent, otherLine, otherLine.getColor().saturate().saturate());
                                }
                            }
                        }
                    }
                });
            }
        }
    }
}
