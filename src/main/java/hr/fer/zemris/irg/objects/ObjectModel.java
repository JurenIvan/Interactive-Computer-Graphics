package hr.fer.zemris.irg.objects;

import hr.fer.zemris.irg.math.vector.Vector;
import hr.fer.zemris.irg.shapes.Point;

import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.Double.*;
import static java.util.stream.Collectors.toList;

public class ObjectModel {

    List<Vertex3D> vertices;
    List<Face3D> faces;

    public ObjectModel copy() {
        ObjectModel copy = new ObjectModel();
        copy.vertices = vertices.stream().map(Vertex3D::copy).collect(toList());
        copy.faces = faces.stream().map(Face3D::copy).collect(toList());
        return copy;
    }

    public String dumpToOBJ() {
        StringBuilder sb = new StringBuilder();

        vertices.forEach(e -> sb.append(e.toOBJ()).append('\n'));
        faces.forEach(e -> sb.append(e.toOBJ()).append('\n'));

        return sb.toString();
    }

    public ObjectModel normalize() {
        double xMin, xMax, yMin, yMax, zMin, zMax;

        xMax = yMax = zMax = MIN_VALUE;
        xMin = yMin = zMin = MAX_VALUE;

        for (Vertex3D vertex3D : vertices) {
            if (vertex3D.getX() > xMax) xMax = vertex3D.getX();
            if (vertex3D.getX() < xMin) xMin = vertex3D.getX();
            if (vertex3D.getY() > yMax) yMax = vertex3D.getY();
            if (vertex3D.getY() < yMin) yMin = vertex3D.getY();
            if (vertex3D.getZ() > zMax) zMax = vertex3D.getZ();
            if (vertex3D.getZ() < zMin) zMin = vertex3D.getZ();
        }

        double xAvg = (xMax + xMin) / 2;
        double yAvg = (yMax + yMin) / 2;
        double zAvg = (zMax + zMin) / 2;

        double m = Math.max(Math.max(xMax - xMin, yMax - yMin), zMax - zMin);

        translateVertices(vertices, -xAvg, -yAvg, -zAvg);
        scaleVertices(vertices, 2 / m);
        return this;
    }

    public static ObjectModel parse(List<String> lines) {
        ObjectModel om = new ObjectModel();
        om.vertices = lines.stream().filter(e -> e.startsWith("v")).map(e -> e.trim().split(" ")).map(e -> Vertex3D.of(parseDouble(e[1]), parseDouble(e[2]), parseDouble(e[3]))).collect(toList());
        om.faces = lines.stream().filter(e -> e.startsWith("f")).map(e -> e.trim().split(" ")).map(e -> Face3D.of(Integer.parseInt(e[1]), Integer.parseInt(e[2]), Integer.parseInt(e[3]))).collect(toList());
        return om;
    }

    private void scaleVertices(List<Vertex3D> vertices, double factor) {
        for (Vertex3D vertex : vertices) {
            double[] points = vertex.getCords();
            points[0] *= factor;
            points[1] *= factor;
            points[2] *= factor;
        }
    }

    private static void translateVertices(List<Vertex3D> vertices, double xTranslation, double yTranslation, double zTranslation) {
        for (Vertex3D vertex : vertices) {
            double[] points = vertex.getCords();
            points[0] += xTranslation;
            points[1] += yTranslation;
            points[2] += zTranslation;
        }
    }

    public List<FaceCoeficient> getFaceCoefficients() {
        return faces.stream().map(e -> e.calculateCoefficients(vertices)).collect(toList());
    }

    public Boolean calculatePosition(Vertex3D point) {
        Boolean inside = TRUE;
        for (var face : faces) {
            var params = face.calculateCoefficients(vertices);
            var pos = params.getA() * point.getX() + params.getB() * point.getY() + params.getC() * point.getZ() + params.getD();

            if (pos > 0) inside = FALSE;
            else if (pos == 0 && onAFace(point)) inside = null;
        }
        return inside;
    }

    private boolean onAFace(Vertex3D point) {
        double direction = 0;

        for (int i = 0; i < vertices.size(); i++) {
            Vertex3D first = vertices.get(i);
            Vertex3D second = vertices.get((i + 1) % vertices.size());

            Vector edge = new Vector(second.getX() - first.getX(), second.getY() - first.getY(), second.getZ() - first.getZ());
            Vector connect = new Vector(point.getX() - second.getX(), point.getY() - second.getY(), point.getZ() - second.getZ());

            double product = connect.nVectorProduct(edge).get(2);

            if (direction < 0 && product > 0 || direction > 0 && product < 0)
                return false;
            direction = product != 0 ? product : direction;
        }
        return true;
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
}
