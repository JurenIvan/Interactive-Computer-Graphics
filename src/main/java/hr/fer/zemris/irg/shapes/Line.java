package hr.fer.zemris.irg.shapes;

import hr.fer.zemris.irg.color.Color;
import hr.fer.zemris.irg.math.vector.Vector;

import static java.lang.String.format;

public class Line extends Shape {

    private Point start;
    private Point end;

    public Line(Point start, Point end, Color color) {
        super(color);
        this.start = start;
        this.end = end;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return format("Line{pointStart=%s pointEnd=%s, color=%s}", start, end, color);
    }

    public Line translate(int distance) {
        int dx = end.getX() - start.getX();
        int dy = end.getY() - start.getY();

        var vector = new Vector(dx, dy, 0).nVectorProduct(new Vector(0, 0, -1)).normalize().scalarMultiply(distance);

        var translatedStartX = (int) Math.round(vector.get(0)) + start.getX();
        var translatedStartY = (int) Math.round(vector.get(1)) + start.getY();

        return new Line(new Point(translatedStartX, translatedStartY), new Point(translatedStartX + dx, translatedStartY + dy), color);
    }

    public Line copy() {
        return new Line(start.copy(), end.copy(), color);
    }
}
