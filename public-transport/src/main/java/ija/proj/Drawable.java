package ija.proj;

import javafx.scene.shape.Shape;

import java.util.List;

/**
 * Interface drawable specifies all elements that are going to be drawn on canvas.
 */
public interface Drawable {
    /**
     * Method specifies GUI elements of method instances, that can be drawn. Method is called e.g on a Stop.
     * It uses attributes information and then creates new JavaFx shapes and put it into a list that is returned.
     *
     * @return List of all elements, that should be drawn on canvas
     */
    List<Shape> getGUI();
}
