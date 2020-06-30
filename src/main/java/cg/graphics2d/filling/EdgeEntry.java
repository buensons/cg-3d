package cg.graphics2d.filling;

import cg.models3d.Vertex;

public class EdgeEntry {
    private int yMax, yMin;
    private float xMin, xMax;
    private float inverseSlope;
    private Vertex v1, v2;
    private float textureX, textureY;
    private float length;

    public EdgeEntry() {}

    public EdgeEntry(int y, float x, float inverseSlope) {
        yMax = y;
        xMin = x;
        this.inverseSlope = inverseSlope;
    }

    public int getyMax() {
        return yMax;
    }

    public void setyMax(int yMax) {
        this.yMax = yMax;
    }

    public float getxMin() {
        return xMin;
    }

    public void setxMin(float xMin) {
        this.xMin = xMin;
    }

    public float getInverseSlope() {
        return inverseSlope;
    }

    public void setInverseSlope(float inverseSlope) {
        this.inverseSlope = inverseSlope;
    }

    public Vertex getV1() {
        return v1;
    }

    public void setV1(Vertex v1) {
        this.v1 = v1;
    }

    public Vertex getV2() {
        return v2;
    }

    public void setV2(Vertex v2) {
        this.v2 = v2;
    }

    public float getTextureX() {
        return textureX;
    }

    public void setTextureX(float textureX) {
        this.textureX = textureX;
    }

    public float getTextureY() {
        return textureY;
    }

    public void setTextureY(float textureY) {
        this.textureY = textureY;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public int getyMin() {
        return yMin;
    }

    public void setyMin(int yMin) {
        this.yMin = yMin;
    }

    public float getxMax() {
        return xMax;
    }

    public void setxMax(float xMax) {
        this.xMax = xMax;
    }
}
