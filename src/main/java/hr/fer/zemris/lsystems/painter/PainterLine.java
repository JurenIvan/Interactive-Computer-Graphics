package hr.fer.zemris.lsystems.painter;

import java.awt.*;

public class PainterLine {
    double x;
    double y;
    double x1;
    double y1;
    Color color;
    float effectiveLength;
    double penSize;

    public PainterLine(double x, double y, double x1, double y1, Color color, float effectiveLength, double penSize) {
        this.x = x;
        this.y = y;
        this.x1 = x1;
        this.y1 = y1;
        this.color = color;
        this.effectiveLength = effectiveLength;
        this.penSize = penSize;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getX1() {
        return x1;
    }

    public double getY1() {
        return y1;
    }

    public Color getColor() {
        return color;
    }

    public float getEffectiveLength() {
        return effectiveLength;
    }

    public double getPenSize() {
        return penSize;
    }
}
