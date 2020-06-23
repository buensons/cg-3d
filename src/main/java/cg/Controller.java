package cg;

import cg.algebra.AlgebraUtils;
import cg.algebra.Matrix;
import cg.algebra.Vector;
import cg.models3d.Sphere;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Slider;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.Arrays;

public class Controller {

    private final int m = 15;
    private final int n = 15;
    private final int r = 1;

    @FXML public Canvas canvas;
    @FXML public Slider slider;

    private static PixelWriter pixelWriter;
    private double mousePosX, mousePosY;
    private Sphere sphere;
    private Vector [] projectedShape;
    private Matrix translate;

    @FXML
    public void initialize() {
        var gc = canvas.getGraphicsContext2D();
        pixelWriter = gc.getPixelWriter();

        sphere = new Sphere(m,n,r);
        projectedShape = Arrays.copyOf(sphere.getVertices(), sphere.getVertices().length);

        var t = new Vector(0, 0, slider.getValue(), 1);
        translate = AlgebraUtils.getTranslationMatrix(t);
        displayShape(0, 0);

        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number oldValue,
                    Number newValue) {
                translate.getColumns()[3].getValues()[2] = slider.getValue();
                displayShape(0, 0);
            }
    });

        canvas.setOnMousePressed((MouseEvent me) -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
        });

        canvas.setOnMouseDragged((MouseEvent me) -> {
            double dx = (mousePosX - me.getSceneX());
            double dy = (mousePosY - me.getSceneY());

            if (me.isPrimaryButtonDown()) {
                    displayShape(-dy * Math.PI / 720, dx * Math.PI / 720);
            }
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
        });
    }

    private void displayShape(double alphaX, double alphaY) {

        for(var triangle : sphere.getMesh()) {
            if(triangle == null) continue;
            triangle.clear();
        }

        for(int i = 0; i < projectedShape.length; i++) {
            pixelWriter.setColor((int)projectedShape[i].getValues()[0], (int)projectedShape[i].getValues()[1], Color.BLACK);

            projectedShape[i] = AlgebraUtils.getProjectionMatrix(1280, 800)
                    .multiply(translate)
                    .multiply(AlgebraUtils.getRotationXMatrix(alphaX))
                    .multiply(AlgebraUtils.getRotationYMatrix(alphaY))
                    .multiply(sphere.getVertices()[i])
                    .scalarProduct(1.0/(sphere.getVertices()[i].getValues()[2] + slider.getValue()));

            sphere.getVertices()[i] = AlgebraUtils.getRotationXMatrix(alphaX)
                    .multiply(AlgebraUtils.getRotationYMatrix(alphaY))
                    .multiply(sphere.getVertices()[i]);

            pixelWriter.setColor((int)projectedShape[i].getValues()[0], (int)projectedShape[i].getValues()[1], Color.WHITE);
        }

        sphere.generateMesh(projectedShape);

        for(var triangle : sphere.getMesh()) {
            if(triangle == null) continue;
            triangle.draw();
        }
    }

    public static PixelWriter getWriter() {
        return pixelWriter;
    }

}
