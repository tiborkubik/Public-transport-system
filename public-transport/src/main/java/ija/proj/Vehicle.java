package ija.proj;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

import java.time.LocalTime;
import java.util.*;


/**
 * Class vehicle represents vehicle, which moves on map on certain line.
 * Contains all important information, which define the movement and dynamics of program
 */
public class Vehicle implements Drawable, UpdateState {
    private String identifier;                              /**< Unique identifier of vehicle */
    private Coordinate position;                            /**< Position of vehicle on map */
    private Street currentStreet;                           /**< Street on which a vehicle currently occurs */
    private Line onLine;                                    /**< Line on which the vehicle transports passengers */
    private Stop nextStop;                                  /**< Next stop where vehicle stops */

    private Controller controller;                          /**< Main controller */

    private double speed;                                   /**< Speed of movement of vehicle */
    private double constantSpeed;                           /**< Value of speed not effected by speedUp, etc */
    private double distance = 0;                            /**< Distance in pixels from the start of vehicle's route */

    protected List<Shape> GUI = new ArrayList<>();          /**< List of GUI elements to be drawn on canvas */
    private List<Coordinate> path = new ArrayList<>();      /**< List of all stops and ends of lines */

    private LocalTime timeofDeparture;
    private int cnt_time = 0;                               /**< Counter that ensures that vehicles stop on stops */


    /***
     * Method creates a new car of public transport in the given city
     *
     * @param position initial position of the car
     * @param speed default speed of the car
     * @param onLine specifies line of the car
     * @param identifier car name
     * @param street initial street of the car
     * @param firstStop first stop of the car
     * @param controller instance of controller
     */
    public Vehicle(Coordinate position, double speed, Line onLine, String identifier, Street street, Stop firstStop, Controller controller, LocalTime timeOfDeparture) {
        this.position = position;
        this.speed = speed;
        this.onLine = onLine;
        this.identifier = identifier;
        this.constantSpeed = speed;
        this.currentStreet = street;
        this.nextStop = firstStop;
        this.controller = controller;
        this.timeofDeparture = timeOfDeparture;
    }

    /***
     * Returns path of the vehicle
     *
     * @return path
     */
    public List<Coordinate> getPath() {
        return path;
    }

    /***
     * Sets first stop of the vehicle
     */
    public void setInitialStop() {
        this.nextStop = this.onLine.getStopList().get(0);
    }

    /***
     * Sets stops of the vehicle
     */
    protected void setStops() {
        LinkedHashMap<String, Coordinate> tempPath = this.onLine.getPath();

        tempPath.forEach((k,v)-> this.path.add(v));
    }

    /**
     * @return Time of vehicle departure
     */
    public LocalTime getTimeofDeparture() {
        return this.timeofDeparture;
    }

    /***
     * Returns following stop of the vehicle
     *
     * @return next stop of the vehicle
     */
    public Stop getNextStop() {
        return this.nextStop;
    }

    /***
     * Returns name the vehicle
     *
     * @return returns identifier of the vehicle
     */
    public String getName() {
        return this.identifier;
    }

    /***
     * Returns current Line of the vehicle
     *
     * @return line of the vehicle
     */
    public Line getLine() {
        return this.onLine;
    }

    /***
     * Returns street on which is vehicle at the time
     *
     * @return current street of the vehicle
     */
    public Street getCurrentStreet() {
        return this.currentStreet;
    }

    /***
     * Returns vehicle's passed distance
     *
     * @return passed distance
     */
    public double getPassedDistance() {
        return this.distance;
    }

    /***
     *  Returns GUI
     *
     * @return GUI
     */
    @Override
    public List<Shape> getGUI() {
        return GUI;
    }

    /***
     * Moves car by modifying GUI
     *
     * @param coordinate according which translation will be calculated
     */
    private void modifyGUI(Coordinate coordinate) {
        try{
            for(Shape shape : GUI) {
                shape.setTranslateX(coordinate.getX() - position.getX() + shape.getTranslateX());
                shape.setTranslateY(coordinate.getY() - position.getY() + shape.getTranslateY());
            }
        } catch (Exception e) {
            System.out.println("Accessing non-existing element");
        }
    }



    /***
     * Calculates new coordinate of the the vehicle
     *
     * @param distance passed distance of the car
     * @param path vehicle moves according path
     * @return new coordinate
     */
    public Coordinate getNewCoord(double distance, List<Coordinate> path) {
        double length = 0;
        double currentLength = 0;
        Coordinate a = null;
        Coordinate b = null;

        for(int i = 0; i < path.size() - 1; i++) {
            a = path.get(i);
            b = path.get(i + 1);
            currentLength = a.coordsDistance(b);
            if(length + currentLength >= distance) {
                break;
            }
            length += currentLength;
        }
        if(a == null || b == null) {
            return null;
        }

        double driven = (distance - length) / currentLength;
        return new Coordinate(a.getX() + (b.getX() - a.getX()) * driven, a.getY() + (b.getY() - a.getY()) * driven);
    }

    /***
     * Calculates length of total path of the vehicle
     *
     * @return Total path length
     */
    public double totalPathLength() {
        double total = 0;
        for(int i = 0; i < this.path.size()-1; i++) {
            total += this.path.get(i).coordsDistance(this.path.get(i+1));
        }
        return total;
    }

    /***
     * Updates vehicle's position over the time
     * @param time current time in the application
     * @param speedMultiplier how much fast vehicle goes
     * @param trafficCoefficient level of the traffic jam
     */
    @Override
    public void update(LocalTime time, double speedMultiplier, int trafficCoefficient) {
        if(trafficCoefficient != 1)
            this.speed = this.constantSpeed * speedMultiplier * ((double)1/(trafficCoefficient*2));
        else
            this.speed = this.constantSpeed * speedMultiplier;



        if (cnt_time >= 0){
            cnt_time--;
            if(cnt_time == 0){
                this.speed = this.constantSpeed;
            }else
                return;

        }

        distance += speed;
        double total = totalPathLength();

        if(distance > total) {
            System.out.println(time);
            for(Shape x : GUI) {
                Pane mapContent = controller.getMapContent();

                ObservableList<Node> mapNodes = mapContent.getChildren();
                for(Node singleNode : mapNodes) {
                    if(x.getId().equals(singleNode.getId())){
                        GUI.remove(x);
                        mapContent.getChildren().remove(singleNode);
                        controller.getUpdates().remove(this);
                        controller.getAllVehicles().remove(this);
                        return;
                    }
                }
            }
        }

        Coordinate newC = getNewCoord(distance, path);

        for(int i = 0; i < this.onLine.getStopList().size(); i++) {
            if(Math.abs(this.onLine.getStopList().get(i).getCoordinate().diffX(newC)) < this.speed/2 && Math.abs(this.onLine.getStopList().get(i).getCoordinate().diffY(newC)) < this.speed/2) {
                cnt_time = this.onLine.getStopList().get(i).getTime();
                if(i < this.onLine.getStopList().size())
                    this.nextStop = this.onLine.getStopList().get(i+1);
            }
        }

        for(Street s : this.onLine.getStreetList()) {
            if(Math.abs(s.begin().diffX(newC)) < this.speed/2 && Math.abs(s.begin().diffY(newC)) < this.speed/2) {
                double slope = s.getSlope();
                double rotation = GUI.get(0).getRotate();
                GUI.get(0).setRotate(-rotation);

                if(slope == 2.0){
                    GUI.get(0).setRotate(0);
                }
                else if(slope == 1.0) {
                    GUI.get(0).setRotate(-45);
                }
                else if(slope == 0.0) {
                    GUI.get(0).setRotate(90);
                } else {
                    if(this instanceof Subway) {
                        if (s.begin().getX() < s.end().getX()) {
                            GUI.get(0).setRotate(90-90*slope);
                        }
                        else{
                            if (s.begin().getY() < s.end().getY()){
                                GUI.get(0).setRotate(150+90*slope);
                            }
                            else{
                                GUI.get(0).setRotate(60-90*slope);
                            }
                        }
                    } else {
                        if (s.begin().getX() < s.end().getX()) {
                            if (s.begin().getY() < s.end().getY()){
                                GUI.get(0).setRotate(180-90*slope);
                            }
                            else{
                                GUI.get(0).setRotate(90+90*slope);
                            }
                        }
                        else{
                            if (s.begin().getY() < s.end().getY()){
                                GUI.get(0).setRotate(180+90*slope);
                            }
                            else{
                                GUI.get(0).setRotate(90-90*slope);
                            }
                        }
                    }

                }

                this.currentStreet = s;
            }
        }

        modifyGUI(newC);
        position = newC;
    }

}
