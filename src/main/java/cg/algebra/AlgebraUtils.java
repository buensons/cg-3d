package cg.algebra;

public class AlgebraUtils {

    public static Matrix getRotationX(double alpha) {
        return new Matrix(new Vector[] {
                new Vector(1, 0, 0, 0),
                new Vector(0, Math.cos(alpha), Math.sin(alpha), 0),
                new Vector(0, -1 * Math.sin(alpha), Math.cos(alpha), 0),
                new Vector(0, 0, 0, 1)});
    }

    public static Matrix getRotationY(double alpha) {
        return new Matrix(new Vector[] {
                new Vector(Math.cos(alpha), 0, Math.sin(alpha), 0),
                new Vector(0, 1, 0, 0),
                new Vector(-1 * Math.sin(alpha), 0, Math.cos(alpha), 0),
                new Vector(0, 0, 0, 1)});
    }

    public static Matrix getRotationZ(double alpha) {
        return new Matrix(new Vector[] {
                new Vector(Math.cos(alpha), -1 * Math.sin(alpha), 0, 0),
                new Vector(Math.sin(alpha), Math.cos(alpha), 0, 0),
                new Vector(0, 0, 1, 0),
                new Vector(0, 0, 0, 1)});
    }

    public static Matrix getTranslation(Vector translate) {
        var m = getIdentity();
        m.getColumns()[3] = translate;
        return m;
    }

    public static Matrix getIdentity() {
        return new Matrix(new Vector[] {
                new Vector(1, 0, 0, 0),
                new Vector(0, 1, 0, 0),
                new Vector(0, 0, 1, 0),
                new Vector(0, 0, 0, 1)});
    }

    public static Matrix getScale(Vector scale) {
        var values = scale.getValues();
        return new Matrix(new Vector[] {
                new Vector(values[0], 0, 0, 0),
                new Vector(0, values[1], 0, 0),
                new Vector(0, 0, values[2], 0),
                new Vector(0, 0, 0, 1)});
    }

}
