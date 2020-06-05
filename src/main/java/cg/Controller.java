package cg;

import cg.algebra.AlgebraUtils;
import cg.algebra.Vector;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class Controller {

    @FXML public Canvas canvas;

    @FXML
    public void initialize() {
        var gc = canvas.getGraphicsContext2D();

        gc.setStroke(Color.WHITE);
        gc.strokeLine(500, 100, 500, 300);
        gc.strokePolygon(new double [] {100, 200, 100}, new double [] {100, 100, 200}, 3);

        var px = new Vector(0,0,0,1);
        px = AlgebraUtils.getTranslation(new Vector(300, 300, 300, 0)).multiply(px);

        System.out.println(px);

    }

}
