package hr.fer.zemris.irg.objects;

import java.util.List;

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

        vertices.forEach(e -> sb.append("v ")
                .append(e.getX()).append(' ')
                .append(e.getY()).append(' ')
                .append(e.getZ()).append('\n'));

        faces.forEach(e -> sb.append("f ")
                .append(e.vertexIndex[0]).append(' ')
                .append(e.vertexIndex[1]).append(' ')
                .append(e.vertexIndex[2]).append('\n'));

        return sb.toString();
    }

    public void normalize() {
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
}
