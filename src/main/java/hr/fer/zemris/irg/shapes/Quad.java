package hr.fer.zemris.irg.shapes;

import hr.fer.zemris.irg.color.Color;

import java.util.List;

import static java.lang.String.format;

public class Quad extends Shape {

    private List<Point> pointList;

    public Quad(List<Point> pointList) {
        super(null);
        this.pointList = pointList;
    }

    public Quad(Point point1, Point point2, Point point3, Point point4, Color color) {
        super(color);
        pointList = List.of(point1, point2, point3, point4);
    }

    public List<Point> getPointList() {
        return pointList;
    }

    @Override
    public String toString() {
        return format("Triangle{pointList=%s, color=%s}", pointList, color);
    }
}
