package ija.proj;

import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Loading resource for map layout
        FXMLLoader layoutLoader = new FXMLLoader(getClass().getResource("/mapLayout.fxml"));
        BorderPane rootElement = layoutLoader.load();
        Scene mainScene = new Scene(rootElement);       // loads root element from GUI

        primaryStage.setScene(mainScene);               // setting scene into stage
        primaryStage.show();

        Controller controller = layoutLoader.getController();

        // allElements containg all Drawable elements such as streets, stops, etc
        List<Drawable> allElements = new ArrayList<>();

        Loader loader = new Loader();
        // Loading all streets from XML input into Drawable objects + adding them to all drawable elements
        List<Drawable> streets = loader.loadMapData(allElements);

        // Loading all stops, lines, etc from XML input into Drawable objects + adding them to all drawable elements
        List<Line> lines = loader.loadLinesData(allElements, streets);

        Subway sub = new Subway(new Coordinate(50, 80),2);

        Bus bus = new Bus(new Coordinate(50, 400), 0.5);

        Tram tram = new Tram(new Coordinate(800, 100), 10);

     //   allElements.add(sub);
        allElements.add(bus);
      //  allElements.add(tram);
        // Setting list into gui
        controller.setGUIelements(allElements);

        controller.setLinesInfo(lines);
        controller.setCursor();

        controller.ChangeLineColor(lines.get(0), Color.ORCHID);
        controller.ChangeLineColor(lines.get(1), Color.TURQUOISE);
        controller.ChangeLineColor(lines.get(2), Color.CRIMSON);

        controller.startTimer(1);
    }
}
