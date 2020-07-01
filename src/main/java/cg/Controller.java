package cg;

import cg.algebra.AlgebraUtils;
import cg.algebra.Matrix;
import cg.algebra.Vector;
import cg.graphics2d.Polygon;
import cg.models3d.Sphere;
import cg.models3d.Vertex;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Arrays;

public class Controller {

    private final int m = 30;
    private final int n = 30;
    private final int r = 1;

    @FXML public Canvas canvas;
    @FXML public Button textureButton;
    @FXML public Slider slider;

    private static PixelWriter pixelWriter;
    private double mousePosX, mousePosY;
    private Sphere sphere;

    @FXML
    public void initialize() {
        var gc = canvas.getGraphicsContext2D();
        pixelWriter = gc.getPixelWriter();
        sphere = new Sphere(m,n,r);

        var t = new Vector(0, 0, slider.getValue(), 1);
        sphere.setTranslate(AlgebraUtils.getTranslationMatrix(t));
        sphere.display(0, 0);

        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number oldValue,
                    Number newValue) {

                var t = new Vector(0, 0, slider.getValue(), 1);
                sphere.setTranslate(AlgebraUtils.getTranslationMatrix(t));
                sphere.display(0, 0);
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
                    sphere.display(-dy * Math.PI / 720, dx * Math.PI / 720);
            }
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
        });

        textureButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("image", Arrays.asList("*.jpeg", "*.jpg", "*.png"));
            fileChooser.getExtensionFilters().add(filter);
            File file = fileChooser.showOpenDialog(App.getMainStage());

            if(file != null) {
                sphere.setTexture(new Image(file.toURI().toString()));
            }
        });
    }

    public static PixelWriter getWriter() {
        return pixelWriter;
    }
}
