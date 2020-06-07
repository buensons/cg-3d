package cg.graphics2d;

import cg.Controller;
import javafx.scene.paint.Color;

public abstract class AbstractShape implements Shape {

//    @JsonIgnore
    protected Color color = Color.WHITE;

    // for serialization purposes
    protected String stringColor = Color.WHITE.toString();

    protected void drawPixel(int x, int y) {
        Controller.getWriter().setColor(x, y, color);
    }

    protected void drawPixel(int x, int y, Color c) {
        Controller.getWriter().setColor(x, y, c);
    }

    protected void clearPixel(int x, int y) {
        Controller.getWriter().setColor(x, y, Color.BLACK);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        stringColor = color.toString();
    }

    public String getStringColor() {
        return stringColor;
    }

    public void setStringColor(String stringColor) {
        this.stringColor = stringColor;
        color = Color.valueOf(stringColor);
    }
}