package ija.proj;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Loader {
    /** Method loads all streets from input XML file. Streets are parsed from XML and added into a list of Drawable objects, which is then returned
     *
     * @param allElements List of objects that will be put in canvas
     */
    public List<Drawable> loadMapData(List<Drawable> allElements) {
        try {
            File fXmlFile = new File("public-transport/src/main/resources/mapData.xml");
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


    /**
     * Method loads all data about lines and stops and adds them to elements for drawing
     * @param allElements List of drawable elements into which new found stops will be appended
     * @param streets List of streets that are already defined
     * @return List of parsed lines, containg all information about streets and stops, too
     */
    public List<Line> loadLinesData(List<Drawable> allElements, List<Drawable> streets) {
        List<Line> allLines = new ArrayList<>();

        try {
            File fXmlFile = new File("public-transport/src/main/resources/lineData.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            // All lines in
            NodeList lineList = doc.getElementsByTagName("line");

            // Iterating through lines
            for (int temp = 0; temp < lineList.getLength(); temp++) {
                Node singleLine = lineList.item(temp);

                if (singleLine.getNodeType() == Node.ELEMENT_NODE) {

                    // Name and ID of line are located in its attributes, need to extract them
                    NamedNodeMap lineAttributes = singleLine.getAttributes();

                    // preparing values for new line constructor
                    String lineName = lineAttributes.item(0).getNodeValue();
                    String lineType = lineAttributes.item(1).getNodeValue();
                    List<Street> streetsOnLine = new ArrayList<>();
                    List<Stop> stopsOnLine = new ArrayList<>();

                    NodeList streetsList = singleLine.getChildNodes();

                    // Iterating through all streets of given line
                    for (int i = 0; i < streetsList.getLength(); i++) {
                        Node singleStreet = streetsList.item(i);

                        if(singleStreet.getNodeType() == Node.ELEMENT_NODE) {
                            NamedNodeMap streetAttribute = singleStreet.getAttributes();
                            String streetName = streetAttribute.item(0).getNodeValue();

                            // Getting given street from streetList, which is already on canvas
                            Street sStreet = new Street();

                            sStreet.findStreetByName(streets, streetName);

                            // Street must be defined on map, otherwise error
                            if(sStreet == null) {
                                System.out.println("Line is going through non-existing street.");
                                System.exit(-1);
                            }
                            if (streetsOnLine.size() != 0){
                                Coordinate lastEnd = streetsOnLine.get(streetsOnLine.size()-1).end();

                                if(!lastEnd.equals(sStreet.begin()))
                                    sStreet.end().swap_coordinates(sStreet.begin());
                            }

                            streetsOnLine.add(sStreet); // street is in Line list

                            // getting List of Stops of given Street
                            NodeList stopList = singleStreet.getChildNodes();
                            for(int j = 0; j < stopList.getLength(); j++) {
                                Node singleStop = stopList.item(j);

                                if(singleStop.getNodeType() == Node.ELEMENT_NODE) {
                                    Element eElement = (Element) singleStop;
                                    String stopName = eElement.getAttribute("id");

                                    String temp_start_x = eElement.getElementsByTagName("x").item(0).getTextContent();
                                    int start_x = Integer.parseInt(temp_start_x);
                                    String temp_start_y = eElement.getElementsByTagName("y").item(0).getTextContent();
                                    int start_y = Integer.parseInt(temp_start_y);

                                    Stop newStop = new Stop(stopName, new Coordinate(start_x, start_y), sStreet);

                                    // Checking whether stop is on a street
                                    boolean stopToStreet = sStreet.addStop(newStop);
                                    if(!stopToStreet) {
                                        System.out.println("Stop " + newStop.getName() + " lies outside of street " + sStreet.getName());
                                        System.exit(-1);
                                    }

                                    // adding stop into list of stops of given instance
                                    stopsOnLine.add(newStop);
                                }
                            }
                        }
                    }
                    Line newLine = new Line(lineName, lineType, streetsOnLine, stopsOnLine);

                    for(Street street : streetsOnLine) {

                        for(Drawable mapStreet : streets) {
                            Street mapS = (Street)mapStreet;
                            if(street.getName() == mapS.getName()) {

                                ((Street) mapStreet).add_line();

                            }
                        }
                        newLine.addCoordinates(street.begin(),street.end());
                    }

                    allLines.add(newLine);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Extracting stops only, typing into Drawable objects and adding on cavas
        for(Line line : allLines) {
            List<Stop> stopsInLine = line.getStopList();
            for(Stop stop : stopsInLine) {
                Drawable drStop = stop;
                allElements.add(drStop);
            }
            Drawable drLine = line;
            allElements.add(drLine);
        }

        return allLines;
    }
}
