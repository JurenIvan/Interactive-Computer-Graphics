package hr.fer.zemris.irg.drawers;

import com.jogamp.opengl.GL2;
import hr.fer.zemris.irg.math.vector.Vector;
import hr.fer.zemris.irg.shapes.Line;
import hr.fer.zemris.irg.shapes.Point;

import java.util.List;

import static hr.fer.zemris.irg.color.Color.BLACK;

public class PolyDrawer {

    public static void encirclePoly(List<Point> points, GL2 gl2) {
        for (int i = 0; i < points.size(); i++)
            LineDrawer.drawLine(gl2, new Line(new Point(points.get(i).getX(), points.get(i).getY()), new Point(points.get((i + 1) % points.size()).getX(), points.get((i + 1) % points.size()).getY()), BLACK));
    }

    private static PolyElem[] calcPolyCoef(List<Point> points) {
        PolyElem[] result = new PolyElem[points.size()];
        int i0 = points.size() - 1;
        Point p0, p1;
        double r = calculateOrientation(points);

        for (int i = 0; i < points.size(); i++) {
            p1 = points.get(i);
            p0 = points.get(i0);
            result[i0] = new PolyElem();
            result[i0].vrh = points.get(i);
            result[i0].brid.a = p0.getY() - p1.getY();
            result[i0].brid.b = -(p0.getX() - p1.getX());
            result[i0].brid.c = p0.getX() * p1.getY() - p0.getY() * p1.getX();
            result[i0].lijevi = p0.getY() > p1.getY();
            result[i0].lijevi = (r > 0) == result[i0].lijevi;
            i0 = i;
        }

        return result;
    }

    private static double calculateOrientation(List<Point> points) {
        Point p0 = points.get(0);
        Point p1 = points.get(1);
        Point p2 = points.get(2);

        Vector edge = new Vector(p1.getX() - p0.getX(), p1.getY() - p0.getY(), 0);
        Vector connect = new Vector(p2.getX() - p1.getX(), p2.getY() - p1.getY(), 0);

        return edge.nVectorProduct(connect).get(2);
    }

    public static void fillPoly(List<Point> points, GL2 gl2) {
        fillPoly(calcPolyCoef(points), gl2);
    }

    private static void fillPoly(PolyElem[] polyElements, GL2 gl2) {
        int n = polyElements.length;
        int i, i0, y;
        int xmin, xmax, ymin, ymax;
        double L, D, x;

        xmin = xmax = polyElements[0].vrh.getX();
        ymax = ymin = polyElements[0].vrh.getY();

        for (PolyElem polyElem : polyElements) {
            if (xmin > polyElem.vrh.getX()) xmin = polyElem.vrh.getX();
            if (xmax < polyElem.vrh.getX()) xmax = polyElem.vrh.getX();
            if (ymin > polyElem.vrh.getY()) ymin = polyElem.vrh.getY();
            if (ymax < polyElem.vrh.getY()) ymax = polyElem.vrh.getY();
        }
        for (y = ymin; y <= ymax; y++) {

            L = xmin;
            D = xmax;
            i0 = n - 1;
            for (i = 0; i < n; i0 = i++) {
                if (polyElements[i0].brid.a == 0) {
                    if (polyElements[i0].vrh.getY() == y) {
                        if (polyElements[i0].vrh.getX() < polyElements[i].vrh.getX()) {
                            L = polyElements[i0].vrh.getX();
                            D = polyElements[i].vrh.getX();
                        } else {
                            L = polyElements[i].vrh.getX();
                            D = polyElements[i0].vrh.getX();
                        }
                        break;
                    }
                } else {
                    x = (-polyElements[i0].brid.b * y - polyElements[i0].brid.c) / (double) polyElements[i0].brid.a;
                    if (polyElements[i0].lijevi) {
                        if (L < x) L = x;
                    } else {
                        if (D > x) D = x;
                    }
                }
            }
            LineDrawer.drawLine(gl2, new Line(new Point((int) L, y), new Point((int) D, y), BLACK));
        }
    }

    private static class Brid2D {
        int a = 0;
        int b = 0;
        int c = 0;
    }

    private static class PolyElem {
        Point vrh = new Point(0, 0);
        Brid2D brid = new Brid2D();
        boolean lijevi = false;
    }
}
