package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import mapData.Line;
import timeActions.TimeManager;
import timeActions.Timetable;
import vehicles.Drawable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class servers for Initialisation of the application and creating
 * controller object as well as object view which run the application
 */
public class Main extends Application {
    private FXMLLoader layoutLoader = new FXMLLoader(getClass().getResource("/mapLayout.fxml"));    /**< Loads layout of the application */
    private String normalLine = getClass().getResource("/normalLine.css").toExternalForm();

    private Loader loader;      /** < Loads data about map, lines, stops */
    private View view;          /**< Object used for manipulation with UI */
    private Controller controller;  /**< Object resposible for workflow of the application */
    private BorderPane rootElement; /** < UI element */
    private Scene mainScene;        /**<  UI element */
    private List<Drawable> allElements = new ArrayList<>(); /**< All elements to display */
    private List<Drawable> streets = new ArrayList<>();     /**< List of street on the map */
    private List<Line> lines = new ArrayList<>();           /**< List of Public transport lines */
    private Timetable timeTable;                            /**< departures of specific lines */

    /**
     * Method initialises all important information for GUI setup
     *
     * @param primaryStage Stage of application
     */
    private void loadMapLayout(Stage primaryStage) {
        primaryStage.setTitle("Public transport system");
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


    /**
     * Method initialises main controller, loader and some additional information for controller
     */
    private void initControllers() {
        this.controller = layoutLoader.getController();
        this.loader = new Loader();


        this.controller.setBackground();
        this.controller.setScene(mainScene);

    }

    /**
     * Method initialises application's view
     */
    private void initView() {
        this.view = new View(this.controller);
    }

    /**
     * Method calls method from Loader which loads all information about city streets
     */
    private void loadMapData() {
        // Loading all streets from XML input into Drawable objects + adding them to all drawable elements
        this.streets = loader.loadMapData(allElements);
        controller.setAllStreets(allElements);

    }

    /**
     * Method calls method from Loader which loads all information about lines of public transport of given city
     */
    private void loadLinesData() {
        // Loading all stops, lines, etc from XML input into Drawable objects + adding them to all drawable elements
        this.lines = loader.loadLinesData(allElements, streets);
        loader.loadTimetableData(lines);
    }

    /**
     * Method calls controller methods which set all important information
     */
    private void setController() {
        controller.setTimeTable(timeTable);     // Setting controller's timeTable attribute

        controller.setLines(lines);             // Setting pared city lines into controller

        view.setDefaultLineColors(lines);       // setting default colors for lines

        controller.setGUIelements(allElements); // Setting list of drawables into gui

        controller.setCurrentTime();            // Setting current time when application is started

        controller.setLinesInfo(lines);         // Setting lines information that are set into the bottom part of app

        controller.actionsAfterClickedOnLine(lines);            // Setting controller method that defines behaviour after clicking on streets

        controller.highlightRouteFromList(lines);// Setting method that highlights line route when necessary

        controller.setBasicSettings(lines);      // Setting app properties when default app settings

        controller.showVehicleRoute();           // Setting event method when route of vehicle should be shown

        controller.setVehicleInfo();            // Setting event method when vehicle information should be displayed in app
    }

    /**
     * Main start method
     *
     * @param primaryStage GUI app stage
     */
    @Override
    public void start(Stage primaryStage) {
        loadMapLayout(primaryStage);

        initControllers();
        initView();

        loadMapData();
        loadLinesData();

        TimeManager timeManager = new TimeManager(this.view, this.controller);
        timeManager.setLines(lines);
        this.timeTable = new Timetable(allElements, lines, controller);

        setController();
    }
}
