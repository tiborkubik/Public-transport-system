package ija.proj;

import javafx.application.Application;
import javafx.application.Platform;
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

        // allElements containg all Drawable elements such as streets, stops, etc
        List<Drawable> allElements = new ArrayList<>();

        Loader loader = new Loader();
        // Loading all streets from XML input into Drawable objects + adding them to all drawable elements
        List<Drawable> streets = loader.loadMapData(allElements);

        // Loading all stops, lines, etc from XML input into Drawable objects + adding them to all drawable elements
        List<Line> lines = loader.loadLinesData(allElements, streets);
        controller.setLines(lines);
        controller.setDefaultLineColors(lines);

        List<Vehicle> allVehicles = new ArrayList<>();

        Bus bus = new Bus(new Coordinate(lines.get(0).getStreetList().get(0).begin().getX(), lines.get(0).getStreetList().get(0).begin().getY()), 0.5, lines.get(0), "Bus#45001");
        Tram tram = new Tram(new Coordinate(lines.get(1).getStreetList().get(0).begin().getX(), lines.get(1).getStreetList().get(0).begin().getY()), 1, lines.get(1), "Tram#45000");
        Subway sub = new Subway(new Coordinate(lines.get(2).getStreetList().get(0).begin().getX(), lines.get(2).getStreetList().get(0).begin().getY()), 2, lines.get(2), "Sub#693");
        //Tram tram = new Tram(new Coordinate(800, 100), 10);
        allVehicles.add(bus);
        allVehicles.add(tram);
        allVehicles.add(sub);

        for(Vehicle v : allVehicles)
            allElements.add(v);

        // Setting list into gui
        controller.setGUIelements(allElements);

        controller.setLinesInfo(lines);

        controller.setCursor(lines);

        controller.setBasicSettings(lines);
        controller.showVehicleRoute(allVehicles);


        controller.startTimer(1);
    }
}
