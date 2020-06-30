package cg.models3d;

import cg.algebra.Vector;
import cg.graphics2d.Point;

public class Vertex {
    private Vector position;
    private Vector projectedPosition;
    private Point textureCoords;

    public Vertex(Vector position, double r) {
        this.position = position;
    }

    public double[] getPositionValues() {
        return position.getValues();
    }

    public double[] getProjectedValues() { return projectedPosition.getValues(); }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public Point getTextureCoords() {
        return textureCoords;
    }

    public void setTextureCoords(Point textureCoords) {
        this.textureCoords = textureCoords;
    }

    public Vector getProjectedPosition() {
        return projectedPosition;
    }

    public void setProjectedPosition(Vector projectedPosition) {
        this.projectedPosition = projectedPosition;
    }
}
