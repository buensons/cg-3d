package cg.models3d;

import cg.algebra.Vector;
import cg.graphics2d.Point;
import cg.graphics2d.Polygon;
import javafx.scene.image.Image;

public class Sphere {
    private Vector[] vertices;
    private Polygon[] mesh;
    private Point[] textureCoordinates;
    private Image texture;
    private final int m, n, r;

    public Sphere(int m, int n, int r) {
        this.r = r;
        this.m = m;
        this.n = n;
        generateVertices();
        generateMesh(vertices);
    }

    public void generateTextureCoords() {
        int textureWidth = (int)texture.getWidth();
        int textureHeight = (int)texture.getHeight();

        textureCoordinates = new Point [m*n + 2];
        textureCoordinates[0] = new Point(textureWidth-1, 0.5 * textureHeight-1);
        textureCoordinates[m*n+1] = new Point(0, 0.5 * textureHeight-1);

        for(int i = 0; i < n; ++i) {
            for(int j = 0; j < m; ++j) {
                textureCoordinates[i*m+j+1] = new Point(
                        (double)j/(m - 1) * (textureWidth-1),
                        (double)(i+1)/(n+1) * (textureHeight-1));
            }
        }
    }

    private void generateVertices() {
        vertices = new Vector [m * n + 2];

        vertices[0] = new Vector(0,r,0,1);
        vertices[m*n + 1] = new Vector(0, -r, 0, 1);

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < m; j++) {
                vertices[i*m + j + 1] = new Vector(
                        r * Math.cos(2 * Math.PI * j / m) * Math.sin(Math.PI / (n + 1) * (i + 1)),
                        r * Math.cos(Math.PI / (n + 1) * (i + 1)),
                        r * Math.sin(2 * Math.PI * j / m) * Math.sin(Math.PI / (n + 1) * (i + 1)),
                        1
                );
            }
        }
    }

    public void generateMesh(Vector[] shape) {
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
    }

    public Vector[] getVertices() {
        return vertices;
    }

    public void setVertices(Vector[] vertices) {
        this.vertices = vertices;
    }

    public Polygon[] getMesh() {
        return mesh;
    }

    public void setMesh(Polygon[] mesh) {
        this.mesh = mesh;
    }

    public Point[] getTextureCoordinates() {
        return textureCoordinates;
    }

    public void setTextureCoordinates(Point[] textureCoordinates) {
        this.textureCoordinates = textureCoordinates;
    }

    public Image getTexture() {
        return texture;
    }

    public void setTexture(Image texture) {
        this.texture = texture;
    }
}
