package cg.graphics2d;

import cg.algebra.Vector;
import cg.graphics2d.filling.EdgeEntry;
import cg.graphics2d.filling.EdgeTable;
import cg.models3d.Vertex;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Polygon extends AbstractShape {

    protected List<Line> lines;
    protected int thickness;
    private int minY;
    private Color filling;
    private Image pattern;
    private boolean hasPatternFilling;
    private Vertex[] vertices;

    // for serialization purposes
    private String fillingString;
    private String imageName;

    public Polygon() {
        lines = new ArrayList<>();
        thickness = 1;
        minY = Integer.MAX_VALUE;
        filling = new Color(0,0,0,0);
        fillingString = filling.toString();
        hasPatternFilling = false;
    }

    public Polygon(Vertex[] vertices) {
        this();
        this.vertices = vertices;
        for(int i = 0; i < vertices.length; i++) {
            Vertex v1 = vertices[i];
            Vertex v2;
            if(i == vertices.length -1) {
                v2 = vertices[0];
            } else {
                v2 = vertices[i + 1];
            }
            lines.add(new Line(v1, v2));
        }
    }

    public void update(Circle source, Circle destination) {
        var p1 = lines
                .stream()
                .filter(e -> e.getX1() == source.getCenterX() && e.getY1() == source.getCenterY())
                .findFirst()
                .get();

        var p2 = lines
                .stream()
                .filter(e -> e.getX2() == source.getCenterX() && e.getY2() == source.getCenterY())
                .findFirst()
                .get();

        p1.setX1((int) destination.getCenterX());
        p1.setY1((int) destination.getCenterY());
        p2.setX2((int) destination.getCenterX());
        p2.setY2((int) destination.getCenterY());
    }

    @Override
    public void draw() {

        if(!filling.equals(new Color(0,0,0,0)) || hasPatternFilling) {
            fill();
        }
//        lines.forEach(Line::draw);
    }

    @Override
    public void clear() {
        lines.forEach(Line::clear);
        if(!filling.equals(new Color(0,0,0,0)) || hasPatternFilling) {
            var color = filling;
            var wasTrue = hasPatternFilling;
            hasPatternFilling = false;
            filling = new Color(0, 0, 0, 0);
            fill();
            filling = color;
            hasPatternFilling = wasTrue;
        }
    }

    @Override
    public List<Circle> generatePoints() {
        var list = new ArrayList<Circle>();
        lines.forEach(e -> {
            var circle = new Circle(e.getX1(), e.getY1(), 10, new Color(0,0,0,0));
            circle.setCursor(Cursor.CLOSED_HAND);
            list.add(circle);
        });
        return list;
    }

    private EdgeTable generateEdgeTable() {
        EdgeTable table = new EdgeTable();

        lines.forEach(line -> {
            if(line.getY1() == line.getY2()) return;

            var edge = new EdgeEntry();
            int yMax = Math.max(line.getY1(), line.getY2());
            int yMin = Math.min(line.getY1(), line.getY2());
            int xMin = line.getX1();
            float slope = (float)(line.getX2() - line.getX1()) / (float) (line.getY2() - line.getY1());

            edge.setV1(line.getP1());
            edge.setV2(line.getP2());
            edge.setTextureX((float)line.getP1().getTextureCoords().getX());
            edge.setTextureY((float)line.getP1().getTextureCoords().getY());
            edge.setxMax(line.getX2());

            if(line.getY1() > line.getY2()) {
                xMin = line.getX2();
                edge.setxMax(line.getX1());
                edge.setTextureY((float)line.getP2().getTextureCoords().getY());
                edge.setTextureX((float)line.getP2().getTextureCoords().getX());
                edge.setV1(line.getP2());
                edge.setV2(line.getP1());
            }
            edge.setxMin(xMin);
            edge.setyMax(yMax);
            edge.setyMin(yMin);
            edge.setInverseSlope(slope);
            edge.setLength(
                    (float)Math.sqrt(Math.pow(line.getX2() - line.getX1(), 2)
                    + Math.pow(line.getY2() - line.getY1(), 2)));
            table.add(yMin, edge);

            if(yMin < minY) minY = yMin;
        });

        return table;
    }

    public void fill() {
        List<EdgeEntry> activeEdgeTable = new LinkedList<>();
        var edgeTable = generateEdgeTable();
        int y = minY;

        while(!activeEdgeTable.isEmpty() || !edgeTable.isEmpty()) {
            int parity = 0;

            if(edgeTable.containsKey(y)) {
                var entry = edgeTable.remove(y);
                activeEdgeTable.addAll(entry);
            }

            activeEdgeTable.sort(new Comparator<EdgeEntry>() {
                @Override
                public int compare(EdgeEntry o1, EdgeEntry o2) {
                    return (int)(o1.getxMin() - o2.getxMin());
                }
            });

            for(int i = 0; i < activeEdgeTable.size() - 1; i++) {
//                System.out.println(activeEdgeTable.get(i).getTextureX() + " " + activeEdgeTable.get(i+1).getTextureX());
                if(parity++ % 2 == 0) {
                    fillTheLine(
                            activeEdgeTable.get(i),
                            activeEdgeTable.get(i+1),
                            y);
                }
            }
            ++y;

            int finalY = y;

            activeEdgeTable.removeIf(e -> e.getyMax() == finalY);
            activeEdgeTable.forEach(edge -> {
                edge.setxMin(edge.getxMin() + edge.getInverseSlope());
                float remainingLength = (float)Math.sqrt(
                        Math.pow(edge.getxMax() - edge.getxMin(), 2)
                                + Math.pow(edge.getyMax() - finalY, 2));

                float t = (edge.getLength() - remainingLength) / (edge.getLength());
                float z_1 = (float)edge.getV1().getProjectedValues()[2];
                float z_2 = (float)edge.getV2().getProjectedValues()[2];
                float z_t = (z_2 - z_1) * t + z_1;
                float u;
                if(z_1 == z_2) u = t;
                else u = ( (1/z_t) - (1/z_1) ) / ( (1/z_2) - (1/z_1) );

                edge.setTextureX((float)(u * (edge.getV2().getTextureCoords().getX()
                        - edge.getV1().getTextureCoords().getX()) + edge.getV1().getTextureCoords().getX()));
                edge.setTextureY((float)(u * (edge.getV2().getTextureCoords().getY()
                        - edge.getV1().getTextureCoords().getY()) + edge.getV1().getTextureCoords().getY()));
            });
        }
    }

    private void fillTheLine(EdgeEntry e1, EdgeEntry e2, int height) {
        int start = (int)Math.floor(e1.getxMin());
        int end = (int)Math.ceil(e2.getxMin());

        if(hasPatternFilling) {
            var reader = pattern.getPixelReader();
            int w = (int) pattern.getWidth();
            int h = (int) pattern.getHeight();

            float t, z_t, u;
            var v1 = e1.getV1();
            var v2 = e1.getV2();
            float z_1 = (float)v1.getProjectedValues()[2];
            float z_2 = (float)v2.getProjectedValues()[2];

            Point p1_g = new Point(e1.getTextureX(), e1.getTextureY());
            Point p2_g = new Point(e2.getTextureX(), e2.getTextureY());

            for(int i = start; i < end; i++) {
                t = (float)(i - start) / (end - start);
                z_t = (z_2 - z_1) * t + z_1;
                if(z_1 == z_2) u = t;
                else u = ( (1/z_t) - (1/z_1) ) / ( (1/z_2) - (1/z_1) );

                var point = new Point(
                        u * (p2_g.getX() - p1_g.getX()) + p1_g.getX(),
                        u * (p2_g.getY() - p1_g.getY()) + p1_g.getY()
                );

                drawPixel(i, height, reader.getColor((int)point.getX() % w, (int)point.getY() % h));
            }
        } else {
            for(int i = start; i <= end; i++) {
                drawPixel(i, height, filling);
            }
        }
    }

    public void clip(Polygon polygon) {
        var list = sutherlandHodgman(polygon);
        var color = lines.get(0).getColor();
        lines.clear();

        for(int i = 0; i < list.size(); i++) {
            var index = i == list.size() - 1 ? 0 : i + 1;
            var line = new Line(list.get(i), list.get(index));
            line.setThickness(thickness);
            line.setColor(color);
            lines.add(new Line(list.get(i), list.get(index)));
        }
    }

    private List<Point> sutherlandHodgman(Polygon polygon) {

        var outPoly = lines
                .stream()
                .map(line -> new Point(line.getX1(), line.getY1()))
                .collect(Collectors.toList());

        var clipBoundary = polygon.getLines()
                .stream()
                .map(line -> new Point(line.getX1(), line.getY1()))
                .collect(Collectors.toList());

        var prevClip = clipBoundary.get(clipBoundary.size()-1);

        for (Point currClip : clipBoundary) {

            var input = outPoly;
            outPoly = new ArrayList<>();

            var prevInput = input.get(input.size() - 1);

            for (Point currInput : input) {

                if (isInside(prevClip, currClip, currInput)) {
                    if (!isInside(prevClip, currClip, prevInput)) {
                        outPoly.add(intersection(prevClip, currClip, prevInput, currInput));
                    }
                    outPoly.add(currInput);
                } else if (isInside(prevClip, currClip, prevInput)) {
                    outPoly.add(intersection(prevClip, currClip, prevInput, currInput));
                }

                prevInput = currInput;
            }
            prevClip = currClip;
        }
        return outPoly;
    }

    private boolean isInside(Point a, Point b, Point c) {
        return (b.getX() - a.getX()) * (c.getY() - a.getY()) <= (b.getY() - a.getY()) * (c.getX() - a.getX());
    }

    private Point intersection(Point a, Point b, Point p, Point q) {
        double A1 = b.getY() - a.getY();
        double B1 = a.getX() - b.getX();
        double C1 = A1 * a.getX() + B1 * a.getY();

        double A2 = q.getY() - p.getY();
        double B2 = p.getX() - q.getX();
        double C2 = A2 * p.getX() + B2 * p.getY();

        double det = A1 * B2 - A2 * B1;
        int x = (int)((B2 * C1 - B1 * C2) / det);
        int y = (int)((A1 * C2 - A2 * C1) / det);

        return new Point(x,y);
    }

    public boolean isConvex() {
        boolean wasNegative = false;
        boolean wasPositive = false;
        int b, c;

        var points = lines.stream().map(line -> new Point(line.getX1(), line.getY1())).collect(Collectors.toList());

        for (int a = 0; a < lines.size(); a++)
        {
            b = (a + 1) % lines.size();
            c = (b + 1) % lines.size();

            double cross_product = crossProductMagnitude(points.get(a), points.get(b), points.get(c));

            if (cross_product < 0) wasNegative = true;
            else if (cross_product > 0) wasPositive = true;

            if (wasNegative && wasPositive) return false;
        }

        return true;
    }

    private double crossProductMagnitude(Point a, Point b, Point c) {
        return ((a.getX() - b.getX()) * (c.getY() - b.getY()))
                - ((a.getY() - b.getY()) * (c.getX() - b.getX()));
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public int getThickness() {
        return thickness;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
        lines.forEach(line -> line.setThickness(thickness));
    }

    @Override
    public void setColor(Color color) {
        super.setColor(color);
        lines.forEach(line -> line.setColor(color));
    }

    public Color getFilling() {
        return filling;
    }

    public void setFilling(Color filling) {
        this.filling = filling;
        fillingString = filling.toString();
    }

    public Image getPattern() {
        return pattern;
    }

    public void setPattern(Image pattern) {
        this.pattern = pattern;
        imageName = pattern.getUrl();
    }

    public boolean isHasPatternFilling() {
        return hasPatternFilling;
    }

    public void setHasPatternFilling(boolean hasPatternFilling) {
        this.hasPatternFilling = hasPatternFilling;
    }

    public String getFillingString() {
        return fillingString;
    }

    public void setFillingString(String fillingString) {
        this.fillingString = fillingString;
        filling = Color.valueOf(fillingString);
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
        if(imageName != null) pattern = new Image(imageName);
    }

    public Vertex[] getVertices() {
        return vertices;
    }

    public void setVertices(Vertex[] vertices) {
        this.vertices = vertices;
    }
}
