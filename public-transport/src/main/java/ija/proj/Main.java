package ija.proj;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.events.DTD;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader layoutLoader = new FXMLLoader(getClass().getResource("/mapLayout.fxml"));
        BorderPane rootElement = layoutLoader.load();
        Scene mainScene = new Scene(rootElement);       // loads root element from GUI

        primaryStage.setScene(mainScene);               // setting scene into stage
        primaryStage.show();

        Controller controller = layoutLoader.getController();

        List<Drawable> allElements = new ArrayList<>();

        List<Drawable> streets = loadMapData(allElements);
        List<Drawable> stops = loadLinesData(allElements);

        // draws all streets on canvas
        allElements.addAll(streets);

        // draws all stops on canvas
        allElements.addAll(stops);

        // setting list into gui
        controller.setGUIelements(allElements);
        controller.startTimer(1);
    }

    private List<Drawable> loadMapData(List<Drawable> allElements) {
        try {
            File fXmlFile = new File("src/main/resources/mapData.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("street");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    String temp_start_x = eElement.getElementsByTagName("start_x").item(0).getTextContent();
                    int start_x = Integer.parseInt(temp_start_x);
                    String temp_start_y = eElement.getElementsByTagName("start_y").item(0).getTextContent();
                    int start_y = Integer.parseInt(temp_start_y);
                    String temp_end_x = eElement.getElementsByTagName("end_x").item(0).getTextContent();
                    int end_x = Integer.parseInt(temp_end_x);
                    String temp_end_y = eElement.getElementsByTagName("end_y").item(0).getTextContent();
                    int end_y = Integer.parseInt(temp_end_y);

                    // adding streets
                    allElements.add(new Street(eElement.getAttribute("id"), new Coordinate(start_x, start_y), new Coordinate(end_x, end_y)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allElements;
    }

    private List<Drawable> loadLinesData(List<Drawable> allElements) {
        try {
            File fXmlFile = new File("src/main/resources/lineData.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("stop");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    String temp_start_x = eElement.getElementsByTagName("x").item(0).getTextContent();
                    int start_x = Integer.parseInt(temp_start_x);
                    String temp_start_y = eElement.getElementsByTagName("y").item(0).getTextContent();
                    int start_y = Integer.parseInt(temp_start_y);

                    // adding streets
                    allElements.add(new Stop(eElement.getAttribute("id"), new Coordinate(start_x, start_y), null));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allElements;
    }
}
