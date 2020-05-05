package ija.proj;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

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

        View view = new View();

        // allElements containg all Drawable elements such as streets, stops, etc
        List<Drawable> allElements = new ArrayList<>();

        Loader loader = new Loader();

        // Loading all streets from XML input into Drawable objects + adding them to all drawable elements
        List<Drawable> streets = loader.loadMapData(allElements);

        // Loading all stops, lines, etc from XML input into Drawable objects + adding them to all drawable elements
        List<Line> lines = loader.loadLinesData(allElements, streets);
        loader.loadTimetableData(lines);

        TimeManager timeManager = new TimeManager(view, controller);

        timeManager.setLines(lines);

        Timetable timeTable = new Timetable(allElements, lines, view);
        controller.setTimeTable(timeTable);

        controller.setLines(lines);

        view.setDefaultLineColors(lines);

        // Setting list into gui
        controller.setGUIelements(allElements);

        controller.setCurrentTime();

        controller.setLinesInfo(lines);

        controller.setCursor(lines);

        controller.highlightRouteFromList(lines);

        controller.setBasicSettings(lines);

        controller.showVehicleRoute();
    }
}
