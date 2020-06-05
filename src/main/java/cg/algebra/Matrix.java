package cg.algebra;

public class Matrix {

    private Vector [] columns;

    public Matrix() {
        columns = new Vector[4];
        for(int i = 0; i < 4; i++) {
            columns[i] = new Vector();
        }
    }

    public Matrix(Vector [] columns) {
        this.columns = columns;
    }

    public Vector multiply(Vector vector) {
        Vector result = new Vector();

        for(int i = 0; i < 4; i++) {
            var temp = new Vector(columns[0].getValues()[i],
                            columns[1].getValues()[i],
                            columns[2].getValues()[i],
                             columns[3].getValues()[i] );

            result.getValues()[i] = temp.dot(vector);
        }

        return result;
    }

    public Matrix multiply(Matrix m) {
        var result = new Matrix();

        for(int i = 0; i < 4; i++) {
            var temp = new Vector(columns[0].getValues()[i],
                    columns[1].getValues()[i],
                    columns[2].getValues()[i],
                    columns[3].getValues()[i] );

            for(int j = 0; j < 4; j++) {
                result.columns[j].getValues()[i] = temp.dot(m.columns[j]);
            }
        }
        return result;
    }

    public Matrix scalar(float n) {
        Matrix result = new Matrix();

        for(int i = 0; i < 4; i++) {
            columns[i] = columns[i].scalar(n);
        }
        return result;
    }

    public Vector[] getColumns() {
        return columns;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < 4; i++) {
            for(Vector v : columns) {
                s.append(v.getValues()[i]).append(" ");
            }
            s.append("\n");
        }
        return s.toString();
    }
}
