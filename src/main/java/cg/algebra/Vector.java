package cg.algebra;

import java.util.Arrays;

public class Vector {

    private double [] values;

    public Vector() {
        values = new double [] {0,0,0,0};
    }

    public Vector(double x, double y, double z, double t) {
        values = new double [] {x, y, z, t};
    }

    public double dot(Vector v) {
        float result = 0;

        for(int i = 0; i < 4; i++) {
            result += values[i] * v.values[i];
        }
        return result;
    }

    public Vector cross(Vector v) {
        double a = values[1] * v.values[2] - values[2] * v.values[1];
        double b = values[0] * v.values[2] - values[2] * v.values[0];
        double c = values[0] * v.values[1] - values[2] * v.values[0];

        return new Vector(a,-1 * b,c,1);
    }

    public Vector scalar(double n) {
        var result = new Vector();

        for(int i = 0; i < 4; i++) {
            result.values[i] = values[i] * n;
        }
        return result;
    }

    public double[] getValues() {
        return values;
    }

    public void setValues(double[] values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return values[0] + " " + values[1] + " " + values[2] + " " + values[3];
    }
}
