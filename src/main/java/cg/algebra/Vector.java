package cg.algebra;

import java.util.Arrays;

public class Vector {

    private float [] values;

    public Vector() {
        values = new float [] {0,0,0,0};
    }

    public Vector(float x, float y, float z, float t) {
        values = new float [] {x, y, z, t};
    }

    public float dot(Vector v) {
        float result = 0;

        for(int i = 0; i < 4; i++) {
            result += values[i] * v.values[i];
        }
        return result;
    }

    public Vector cross(Vector v) {
        float a = values[1] * v.values[2] - values[2] * v.values[1];
        float b = values[0] * v.values[2] - values[2] * v.values[0];
        float c = values[0] * v.values[1] - values[2] * v.values[0];

        return new Vector(a,-1 * b,c,1);
    }

    public Vector scalar(float n) {
        var result = new Vector();

        for(int i = 0; i < 4; i++) {
            result.values[i] = values[i] * n;
        }
        return result;
    }

    public float[] getValues() {
        return values;
    }

    public void setValues(float[] values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return values[0] + " " + values[1] + " " + values[2] + " " + values[3];
    }
}
