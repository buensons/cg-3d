package cg.models3d;

import cg.algebra.AlgebraUtils;
import cg.algebra.Matrix;
import cg.algebra.Vector;
import cg.graphics2d.Point;
import cg.graphics2d.Polygon;
import javafx.scene.image.Image;

import java.io.File;

public class Sphere {
    private Polygon[] mesh;
    private Image texture;
    private Vertex[] vertices;
    private Matrix translate;
    private final int m, n, r;

    public Sphere(int m, int n, int r) {
        this.r = r;
        this.m = m;
        this.n = n;

        File file = new File("/Users/damian/Desktop/brick.jpeg");
        texture = new Image(file.toURI().toString());

        generateVertices();
        generateMesh();
        generateTextureCoords();
    }

    private void generateVertices() {
        vertices = new Vertex[m * n + 2];

        vertices[0] = new Vertex(new Vector(0,r,0,1), r);
        vertices[m*n + 1] = new Vertex(new Vector(0, -r, 0, 1), r);

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < m; j++) {
                vertices[i*m + j + 1] = new Vertex(new Vector(
                        r * Math.cos(2 * Math.PI * j / m) * Math.sin(Math.PI / (n + 1) * (i + 1)),
                        r * Math.cos(Math.PI / (n + 1) * (i + 1)),
                        r * Math.sin(2 * Math.PI * j / m) * Math.sin(Math.PI / (n + 1) * (i + 1)),
                        1
                ), r);
            }
        }

        for (Vertex vertex : vertices) {
            double[] values = vertex.getPositionValues();
            vertex.setProjectedPosition(new Vector(values[0], values[1], values[2], 1));
        }
    }

    private void generateTextureCoords() {
        int textureWidth = (int)texture.getWidth();
        int textureHeight = (int)texture.getHeight();

        vertices[0].setTextureCoords(new Point(textureWidth-1, 0.5 * textureHeight-1));
        vertices[m*n+1].setTextureCoords(new Point(0, 0.5 * textureHeight-1));

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < m; j++) {
                vertices[i*m + j + 1].setTextureCoords(new Point(
                        (double)j/(m - 1) * (textureWidth-1),
                        (double)(i+1)/(n+1) * (textureHeight-1)));
            }
        }
    }

    public void generateMesh() {
        mesh = new Polygon[2*m*n];

        for(int i = 0; i < m - 1; i++) {
            mesh[i] = new Polygon(new Vertex[] {vertices[0], vertices[i+2], vertices[i+1]});
            mesh[(2*n-1)*m + i] = new Polygon(new Vertex[] {vertices[m*n + 1], vertices[(n-1)*m + i + 1], vertices[(n-1)*m + i + 2]});
        }
        mesh[m - 1] = new Polygon(new Vertex[] {vertices[0], vertices[1], vertices[m]});
        mesh[(2*n-1)*m + m-1] = new Polygon(new Vertex[] {vertices[m*n + 1], vertices[m*n], vertices[(n-1)*m + 1]});

        for(int i = 0; i < n - 1; i++) {
            for(int j = 1; j < m; j++) {
                mesh[(2*i + 1)*m + j-1] = new Polygon(new Vertex[] {vertices[i*m + j], vertices[i*m + j + 1], vertices[(i+1)*m + j + 1]});
                mesh[(2*i + 2)*m + j-1] = new Polygon(new Vertex[] {vertices[i*m + j], vertices[(i+1)*m + j + 1], vertices[(i+1)*m + j]});
            }
            mesh[(2*i + 1)*m + m-1] = new Polygon(new Vertex[] {vertices[(i+1)*m], vertices[i*m + 1], vertices[(i+1)*m + 1]});
            mesh[(2*i + 2)*m + m-1] = new Polygon(new Vertex[] {vertices[(i+1)*m], vertices[(i+1)*m + 1], vertices[(i+2)*m]});
        }

        for(var poly : mesh) {
            poly.setHasPatternFilling(true);
            poly.setPattern(texture);
        }
    }

    public void display(double alphaX, double alphaY) {
        for(var triangle : mesh) {
            if(isFacingBack(triangle)) continue;
            triangle.clear();
        }

        for (Vertex vertex : vertices) {
            vertex.setProjectedPosition(AlgebraUtils.getProjectionMatrix(1280, 800)
                    .multiply(translate)
                    .multiply(AlgebraUtils.getRotationXMatrix(alphaX))
                    .multiply(AlgebraUtils.getRotationYMatrix(alphaY))
                    .multiply(vertex.getPosition())
                    .scalarProduct(1.0 / (vertex.getPositionValues()[2] + translate.getColumns()[3].getValues()[2])));

            vertex.setPosition(AlgebraUtils.getRotationXMatrix(alphaX)
                    .multiply(AlgebraUtils.getRotationYMatrix(alphaY))
                    .multiply(vertex.getPosition()));
        }

        for(var triangle : mesh) {
            if(isFacingBack(triangle)) continue;
            triangle.draw();
        }
    }

    private boolean isFacingBack(Polygon poly) {
        var vertices = poly.getVertices();
        var v1 = vertices[0].getProjectedValues();
        var v2 = vertices[1].getProjectedValues();
        var v3 = vertices[2].getProjectedValues();

        var result = new Vector(v2[0] - v1[0], v2[1] - v1[1], 0, 0)
                .crossProduct(
                        new Vector(v3[0] - v1[0], v3[1] - v1[1], 0, 0));

        return result.getValues()[2] <= 0;
    }

    public Vertex[] getVertices() {
        return vertices;
    }

    public void setVertices(Vertex[] vertices) {
        this.vertices = vertices;
    }

    public Polygon[] getMesh() {
        return mesh;
    }

    public void setMesh(Polygon[] mesh) {
        this.mesh = mesh;
    }

    public Image getTexture() {
        return texture;
    }

    public void setTexture(Image texture) {
        this.texture = texture;
    }

    public Matrix getTranslate() {
        return translate;
    }

    public void setTranslate(Matrix translate) {
        this.translate = translate;
    }
}
