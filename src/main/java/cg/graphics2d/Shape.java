package cg.graphics2d;

import javafx.scene.shape.Circle;
import java.util.List;

public interface Shape {
    void draw();
    void clear();
    List<Circle> generatePoints();
}