package cg;

import cg.algebra.Matrix;
import cg.algebra.Vector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/3d.fxml"));
        VBox root = loader.load();
        root.setBackground(new Background(new BackgroundFill(Paint.valueOf("black"), null, null)));
        var scene = new Scene(root, 1280, 768);

//        var v1 = new Vector(1, 2, 3, 4);
//        var v2 = new Vector(1, 2, 3, 4);
//        var v3 = new Vector(1, 2, 3, 4);
//        var v4 = new Vector(1, 2, 3, 4);

        var v1 = new Vector(1, 0, 0, 0);
        var v2 = new Vector(0, 1, 0, 0);
        var v3 = new Vector(0, 0, 1, 0);
        var v4 = new Vector(0, 0, 0, 1);

        var matrix = new Matrix(new Vector[] {v1, v2, v3, v4});

//        System.out.println(matrix.multiply(matrix));

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

