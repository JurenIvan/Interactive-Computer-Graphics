package hr.fer.zemris.irg.objects;

import hr.fer.zemris.irg.math.BaricentricKords;
import hr.fer.zemris.irg.math.vector.IVector;
import hr.fer.zemris.irg.math.vector.Vector;

import java.util.List;

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
        int intersections = 0;
        Vector p0 = new Vector(point.getCords(), true, false);
        Vector unit = new Vector(3, 5, 7);

        for (Face3D face : faces) {

            IVector norm = face.calculateCoefficients(vertices).toVector().copyPart(3);
            FaceCoeficient fc = face.calculateCoefficients(vertices);
            double s = -(fc.getA() * point.getX() + fc.getB() * point.getY() + fc.getC() * point.getZ() + fc.getD()) / (norm.scalarProduct(unit));
            if (s < 0) continue;
            IVector intersectionPoint = p0.nAdd(unit.nScalarMultiply(s));

            Boolean onAFaceResult = onAFace(intersectionPoint, face);
            if (onAFaceResult == TRUE) intersections++;

            if (onAFaceResult == null) {
                if (onAFace(p0, face) == TRUE || onAFace(p0, face) == null) return null;
            }
        }
        return intersections % 2 == 1;

    }

    private Boolean onAFace(IVector point, Face3D face) {

        var fc = face.calculateCoefficients(vertices);
        if (Math.abs(fc.getA() * point.get(0) + fc.getB() * point.get(1) + fc.getC() * point.get(2) + fc.getD()) > 0.01)
            return false;

        Vertex3D a = vertices.get(face.getX0() - 1);
        Vertex3D b = vertices.get(face.getX1() - 1);
        Vertex3D c = vertices.get(face.getX2() - 1);

        Vector result = BaricentricKords.calculateBaricentricCords(new Vector(a.getCords()), new Vector(b.getCords()), new Vector(c.getCords()), point);

        for (int i = 0; i < 3; i++)
            if (result.get(i) > 1 || result.get(i) < 0) return false;

        if (result.get(0) > 0 && result.get(0) < 1
                && result.get(1) > 0 && result.get(1) < 1
                && result.get(2) > 0 && result.get(2) < 1)
            return true;

        return null;
    }


//works for convex
//    public Boolean calculatePosition(Vertex3D point) {
//        Boolean inside = TRUE;
//        for (var face : faces) {
//            var params = face.calculateCoefficients(vertices);
//            var pos = params.getA() * point.getX() + params.getB() * point.getY() + params.getC() * point.getZ() + params.getD();
//
//            if (pos > 0) inside = FALSE;
//            else if (pos == 0 && onAFace(point)) inside = null;
//        }
//        return inside;
//    }
//
//    private boolean onAFace(Vertex3D point) {
//        double direction = 0;
//
//        for (int i = 0; i < vertices.size(); i++) {
//            Vertex3D first = vertices.get(i);
//            Vertex3D second = vertices.get((i + 1) % vertices.size());
//
//            Vector edge = new Vector(second.getX() - first.getX(), second.getY() - first.getY(), second.getZ() - first.getZ());
//            Vector connect = new Vector(point.getX() - second.getX(), point.getY() - second.getY(), point.getZ() - second.getZ());
//
//            double product = connect.nVectorProduct(edge).get(2);
//
//            if (direction < 0 && product > 0 || direction > 0 && product < 0) return false;
//            direction = product != 0 ? product : direction;
//        }
//        return true;
//    }
}
