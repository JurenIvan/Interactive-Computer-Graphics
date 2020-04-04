package hr.fer.zemris.irg.shapes;

import com.jogamp.opengl.GL2;
import hr.fer.zemris.irg.color.Color;
import hr.fer.zemris.irg.math.vector.Vector;

import java.util.ArrayList;
import java.util.List;

import static com.jogamp.opengl.GL.GL_POINTS;
import static hr.fer.zemris.irg.color.Color.BLUE;

public class Polynom {

    public static boolean isPointInsideOfPolygone(List<Point> points, int x, int y) {
        return checker(points, x, y);
    }

    public static boolean willPolygonRemainConvex(List<Point> points, int x, int y) {
        var list = new ArrayList<>(points);
        list.add(new Point(x, y));
        return isPolygonConvex(list);
    }

    public static boolean checker(List<Point> points, int x, int y) {
        double direction = 0;

        for (int i = 0; i < points.size(); i++) {
            Point first = points.get(i);
            Point second = points.get((i + 1) % points.size());

            Vector edge = new Vector(second.getX() - first.getX(), second.getY() - first.getY(), 0);
            Vector connect = new Vector(x - second.getX(), y - second.getY(), 0);

            double product = connect.nVectorProduct(edge).get(2);

            if (direction < 0 && product > 0 || direction > 0 && product < 0)
                return false;
            direction = product != 0 ? product : direction;
        }
        return true;
    }

    public static boolean isPolygonConvex(List<Point> points) {
        double direction = 0;

        for (int i = 0; i < points.size(); i++) {
            Point first = points.get(i);
            Point second = points.get((i + 1) % points.size());
            Point third = points.get((i + 2) % points.size());

            Vector edge = new Vector(second.getX() - first.getX(), second.getY() - first.getY(), 0);
            Vector connect = new Vector(third.getX() - second.getX(), third.getY() - second.getY(), 0);

            double product = connect.nVectorProduct(edge).get(2);

            if (direction < 0 && product > 0 || direction > 0 && product < 0)
                return false;
            direction = product != 0 ? product : direction;
        }
        return true;
    }

    public static void paintAcceptableStates(GL2 gl2, List<Point> points, Point pointing) {
        var lista = new ArrayList<>(points);
        if (pointing != null) lista.add(pointing);
        gl2.glBegin(GL_POINTS);
        Color color = BLUE;
        gl2.glColor3f(color.getR(), color.getG(), color.getB());

        int ymin;
        int xmin = ymin = Integer.MAX_VALUE;
        int xmax;
        int ymax = xmax = Integer.MIN_VALUE;

        for (Point point : lista) {
            if (ymin > point.getY()) ymin = point.getY();
            if (xmin > point.getX()) xmin = point.getX();
            if (ymax < point.getY()) ymax = point.getY();
            if (xmax < point.getX()) xmax = point.getX();
        }

        for (int i = xmin; i < xmax; i++)
            for (int j = ymin; j < ymax; j++)
                if (checker(lista, i, j)) gl2.glVertex2i(i, j);
        gl2.glEnd();
    }


}
