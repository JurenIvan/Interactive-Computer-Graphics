package hr.fer.zemris.irg.shapes;

import hr.fer.zemris.irg.color.Color;

import java.util.List;

import static java.lang.String.format;

public class Triangle extends Shape {

    private List<Point> pointList;

    public Triangle(Point point1, Point point2, Point point3, Color color) {
        super(color);
        pointList = List.of(point1, point2, point3);
    }

    public List<Point> getPointList() {
        return pointList;
    }

    @Override
    public String toString() {
        return format("Triangle{pointList=%s, color=%s}", pointList, color);
    }
}
