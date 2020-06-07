package cg.algebra;

public class AlgebraUtils {

    public static Matrix getRotationXMatrix(double alpha) {
        return new Matrix(new Vector[] {
                new Vector(1, 0, 0, 0),
                new Vector(0, Math.cos(alpha), -1 * Math.sin(alpha), 0),
                new Vector(0, Math.sin(alpha), Math.cos(alpha), 0),
                new Vector(0, 0, 0, 1)});
    }

    public static Matrix getRotationYMatrix(double alpha) {
        return new Matrix(new Vector[] {
                new Vector(Math.cos(alpha), 0, -1 * Math.sin(alpha), 0),
                new Vector(0, 1, 0, 0),
                new Vector(Math.sin(alpha), 0, Math.cos(alpha), 0),
                new Vector(0, 0, 0, 1)});
    }

    public static Matrix getRotationZMatrix(double alpha) {
        return new Matrix(new Vector[] {
                new Vector(Math.cos(alpha), Math.sin(alpha), 0, 0),
                new Vector(-1 * Math.sin(alpha), Math.cos(alpha), 0, 0),
                new Vector(0, 0, 1, 0),
                new Vector(0, 0, 0, 1)});
    }

    public static Matrix getTranslationMatrix(Vector translate) {
        var m = getIdentityMatrix();
        m.getColumns()[3] = translate;
        return m;
    }

    public static Matrix getIdentityMatrix() {
        return new Matrix(new Vector[] {
                new Vector(1, 0, 0, 0),
                new Vector(0, 1, 0, 0),
                new Vector(0, 0, 1, 0),
                new Vector(0, 0, 0, 1)});
    }

    public static Matrix getScaleMatrix(Vector scale) {
        var values = scale.getValues();
        return new Matrix(new Vector[] {
                new Vector(values[0], 0, 0, 0),
                new Vector(0, values[1], 0, 0),
                new Vector(0, 0, values[2], 0),
                new Vector(0, 0, 0, 1)});
    }

    public static Matrix getProjectionMatrix(double w, double h) {
        var d = (w / 2.0) * 1.0/Math.tan(Math.PI / 4.0);

        return new Matrix(new Vector[] {
                new Vector(d, 0, 0, 0),
                new Vector(0, -d, 0, 0),
                new Vector(w/2.0, h/2.0, 0, 1),
                new Vector(0, 0, 1, 0)});
    }

}
