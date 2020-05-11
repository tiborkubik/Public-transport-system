package ija.proj;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class servers for Initialisation of the application and creating
 * controller object as well as object view which run the application
 */
public class Main extends Application {
    private FXMLLoader layoutLoader = new FXMLLoader(getClass().getResource("/mapLayout.fxml")); /**< Loads layout of the application */
    private String normalLine = getClass().getResource("/normalLine.css").toExternalForm();

    private Loader loader;                                  /**< Loads data about map, lines, stops */
    private View view;                                      /**< Object used for manipulation with UI */
    private Controller controller;                          /**< Object resposible for workflow of the application */
    private BorderPane rootElement;                         /**< UI element */
    private Scene mainScene;                                /**<  UI element*/

    private List<Drawable> allElements = new ArrayList<>(); /**< All elements to display  */
    private List<Drawable> streets = new ArrayList<>();     /**< List of street on the map */
    private List<Line> lines = new ArrayList<>();           /**< List of Public transport lines */
    private Timetable timeTable;                            /**< departures of specific lines */

    private void loadMapLayout(Stage primaryStage) {
        try {
            this.rootElement = layoutLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.mainScene = new Scene(rootElement);       // loads root element from GUI
        this.mainScene.getStylesheets().add(normalLine);

        primaryStage.setScene(mainScene);               // setting scene into stage
        primaryStage.show();
    }


    private void initControllers() {
        this.controller = layoutLoader.getController();
        this.loader = new Loader();


        this.controller.setBackground();
        this.controller.setScene(mainScene);

    }

    private void initView() {
        this.view = new View(this.controller);
    }

    private void loadMapData() {
        // Loading all streets from XML input into Drawable objects + adding them to all drawable elements
        this.streets = loader.loadMapData(allElements);
        controller.setAllStreets(allElements);
    }

    private void loadLinesData() {
        // Loading all stops, lines, etc from XML input into Drawable objects + adding them to all drawable elements
        this.lines = loader.loadLinesData(allElements, streets);
        loader.loadTimetableData(lines);
    }

    private void setController(){
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

        controller.setVehicleInfo();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Loading resource for map layout
        loadMapLayout(primaryStage);

        initControllers();
        initView();

        loadMapData();
        loadLinesData();

        TimeManager timeManager = new TimeManager(this.view, this.controller);

        timeManager.setLines(lines);
        this.timeTable = new Timetable(allElements, lines, view, controller);
        setController();


    }
}
