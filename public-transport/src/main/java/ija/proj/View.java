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

public class View {
    List<Color> colorsForLines = new ArrayList<>();
    Controller controller;
    private Image bg = new Image("mapa1.jpg");
    /**
     * < Path to background image
     */
    private BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);

    /**
     * < Defines size of the background picture
     */

    public View(Controller controller) {
        this.controller = controller;
        setColorsForLines();
    }

    private void setColorsForLines() {
        colorsForLines.add(Color.FORESTGREEN);
        colorsForLines.add(Color.ORANGERED);
        colorsForLines.add(Color.CORNFLOWERBLUE);
        colorsForLines.add(Color.YELLOW);
        colorsForLines.add(Color.SANDYBROWN);
        colorsForLines.add(Color.PURPLE);
        colorsForLines.add(Color.HOTPINK);
    }

    public void setDefaultLineColors(List<Line> lines) {
        for (int i = 0; i < lines.size(); i++) {
            lines.get(i).setColor(colorsForLines.get(i));
        }
    }

    public void setBackground(Pane map) {
        map.setBackground(new Background(new BackgroundImage(bg,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                bSize)));
    }

    public void setLineInfoFocused(Text nextStopInfo, Text nextStopText, Text finalStopInfo, Text finalStopText, Text delayText) {
        nextStopInfo.setOpacity(1);
        nextStopText.setOpacity(1);
        finalStopInfo.setOpacity(1);
        finalStopText.setOpacity(1);
        delayText.setOpacity(1);
    }

    public void colorRoute(javafx.scene.shape.Line vehicleRoute, Color color) {
        vehicleRoute.setStroke(color);
        vehicleRoute.setOpacity(1.0);
    }

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

    public void showMapContent(List<Drawable> elements, Pane mapContent) {
        for (Drawable obj : elements) {
            mapContent.getChildren().addAll(obj.getGUI());
        }
    }

    public void addElement(Drawable element, Pane mapContent) {
        mapContent.getChildren().addAll(element.getGUI());
    }

    public void viewLinesInfo(List<Line> lines, ListView<Object> linesInfo) {
        for (Line line : lines) {
            linesInfo.getItems().add(line.getName());
        }

    }

    public void clickedOnLine(Text finalStopInfo, Text finalStopText) {
        finalStopInfo.setOpacity(1);
        finalStopText.setOpacity(1);
    }

    public void clickedOnVoid(Text finalStopInfo, Text finalStopText, Text nextStopText, Pane bottomWindow) {
        finalStopInfo.setOpacity(0.5);
        finalStopText.setOpacity(0.5);
        finalStopText.setText("-");
        nextStopText.setText("-");

        cleanRouteFromStops(bottomWindow);
    }

    public void cleanRouteFromStops(Pane bottomWindow) {
        try {
            bottomWindow.getChildren().removeIf(sg2 -> sg2.getId().contains("RouteStop"));
        } catch (Exception ignored) {
        }
    }

    public void changeFinalStopText(Text text, Line line) {
        text.setText(line.getStopList().get(line.getStopList().size() - 1).getName());
    }

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
