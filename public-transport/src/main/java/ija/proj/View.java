package ija.proj;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.List;

public class View {
    List<Color> colorsForLines = new ArrayList<>();

    public View() {
        setColorsForLines();
    }

    private void setColorsForLines() {
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

    public void setLineInfoDefault(Text nextStopInfo, Text nextStopText, Text finalStopInfo, Text finalStopText, Text delayText, javafx.scene.shape.Line route){
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

        route.setStroke(Color.rgb(50,50,50));
        route.setOpacity(0.5);
    }

    /***
     * changes line to a certain color
     * @param line line to which it changes color
     * @param color to which color we are changing the line
     */
    public void changeLineColor(Pane mapContent, Line line, Color color) {
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

    public void showMapContent(List<Drawable> elements, Pane mapContent) {
        for(Drawable obj : elements) {
            mapContent.getChildren().addAll(obj.getGUI());
        }
    }

    public void addElement(Drawable element, Pane mapContent) {
        mapContent.getChildren().addAll(element.getGUI());
    }

    public void zoom(Pane mapContent, double zoomValue) {
        mapContent.setScaleX(zoomValue * mapContent.getScaleX());
        mapContent.setScaleY(zoomValue * mapContent.getScaleY());
    }

    public void viewLinesInfo(List<Line> lines, ListView linesInfo) {
        for(Line line : lines) {
            linesInfo.getItems().add(line.getName());
        }

    }

    public void clickedOnLine(Text finalStopInfo, Text finalStopText) {
        finalStopInfo.setOpacity(1);
        finalStopText.setOpacity(1);
    }

    public void clickedOnVoid(Text finalStopInfo, Text finalStopText, Pane bottomWindow) {
        finalStopInfo.setOpacity(0.5);
        finalStopText.setOpacity(0.5);
        finalStopText.setText("-");

        cleanRouteFromStops(bottomWindow);
    }

    public void cleanRouteFromStops(Pane bottomWindow) {
        try {
            bottomWindow.getChildren().removeIf(sg2 -> sg2.getId().contains("RouteStop"));
        } catch (Exception e) {

        }
    }

    public void changeFinalStopText(Text text, Line line) {
        text.setText(line.getStopList().get(line.getStopList().size()-1).getName());
    }

    public void addStopToRoute(javafx.scene.shape.Line vehicleRoute, double distFromStart, double realToImPath, int i, Vehicle singleV, Pane bottomWindow) {
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

    public void  addStopToRoute(javafx.scene.shape.Line vehicleRoute, double distFromStart, double realToImPath, int i, List<Stop> allStops, Pane bottomWindow) {
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
