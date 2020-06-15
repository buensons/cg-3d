package cg;

import cg.algebra.AlgebraUtils;
import cg.algebra.Matrix;
import cg.algebra.Vector;
import cg.graphics2d.Polygon;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.Arrays;

public class Controller {

    private final int m = 15;
    private final int n = 15;

    @FXML public Canvas canvas;
    @FXML public Slider slider;

    private static PixelWriter pixelWriter;
    private double mousePosX, mousePosY;
    private Vector [] shape;
    private Vector [] projectedShape;
    private Matrix translate;
    private Polygon[] mesh;

    @FXML
    public void initialize() {
        var gc = canvas.getGraphicsContext2D();
        pixelWriter = gc.getPixelWriter();

        shape = generateSphere();
        projectedShape = Arrays.copyOf(shape, shape.length);
        mesh = generateMesh(projectedShape);

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

        for(var triangle : mesh) {
            if(triangle == null) continue;
            triangle.clear();
        }

        for(int i = 0; i < projectedShape.length; i++) {
            pixelWriter.setColor((int)projectedShape[i].getValues()[0], (int)projectedShape[i].getValues()[1], Color.BLACK);

            projectedShape[i] = AlgebraUtils.getProjectionMatrix(1280, 800)
                    .multiply(translate)
                    .multiply(AlgebraUtils.getRotationXMatrix(alphaX))
                    .multiply(AlgebraUtils.getRotationYMatrix(alphaY))
                    .multiply(shape[i])
                    .scalarProduct(1.0/(shape[i].getValues()[2] + slider.getValue()));

            shape[i] = AlgebraUtils.getRotationXMatrix(alphaX)
                    .multiply(AlgebraUtils.getRotationYMatrix(alphaY))
                    .multiply(shape[i]);

            pixelWriter.setColor((int)projectedShape[i].getValues()[0], (int)projectedShape[i].getValues()[1], Color.WHITE);
        }
        mesh = generateMesh(projectedShape);

        for(var triangle : mesh) {
            if(triangle == null) continue;
            triangle.draw();
        }
    }

    private Vector[] generateSphere() {
        int r = 1;
        var sphere = new Vector [m * n + 2];

        sphere[0] = new Vector(0,r,0,1);
        sphere[m*n + 1] = new Vector(0, -r, 0, 1);

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < m; j++) {
                sphere[i*m + j + 1] = new Vector(
                        r * Math.cos(2 * Math.PI * j / m) * Math.sin(Math.PI / (n + 1) * (i + 1)),
                        r * Math.cos(Math.PI / (n + 1) * (i + 1)),
                        r * Math.sin(2 * Math.PI * j / m) * Math.sin(Math.PI / (n + 1) * (i + 1)),
                        1
                );
            }
        }

        return sphere;
    }

    private Polygon[] generateMesh(Vector[] shape) {
        mesh = new Polygon[2*m*n];

        for(int i = 0; i < m - 1; i++) {
            mesh[i] = new Polygon(new Vector[] {shape[0], shape[i+2], shape[i+1]});
            mesh[(2*n-1)*m + i] = new Polygon(new Vector[] {shape[m*n + 1], shape[(n-1)*m + i + 1], shape[(n-1)*m + i + 2]});
        }
        mesh[m - 1] = new Polygon(new Vector[] {shape[0], shape[1], shape[m]});
        mesh[(2*n-1)*m + m-1] = new Polygon(new Vector[] {shape[m*n + 1], shape[m*n], shape[(n-1)*m + 1]});

        for(int i = 0; i < n - 1; i++) {
            for(int j = 1; j < m; j++) {
                mesh[(2*i + 1)*m + j-1] = new Polygon(new Vector[] {shape[i*m + j], shape[i*m + j + 1], shape[(i+1)*m + j + 1]});
                mesh[(2*i + 2)*m + j-1] = new Polygon(new Vector[] {shape[i*m + j], shape[(i+1)*m + j + 1], shape[(i+1)*m + j]});
            }
            mesh[(2*i + 1)*m + m-1] = new Polygon(new Vector[] {shape[(i+1)*m], shape[i*m + 1], shape[(i+1)*m + 1]});
            mesh[(2*i + 2)*m + m-1] = new Polygon(new Vector[] {shape[(i+1)*m], shape[(i+1)*m + 1], shape[(i+2)*m]});
        }
        return mesh;
    }

    public static PixelWriter getWriter() {
        return pixelWriter;
    }

}
