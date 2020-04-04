package hr.fer.zemris.irg.objects;

import static java.lang.String.format;
import static java.util.Arrays.copyOf;

public class Vertex3D {

    private double[] cords;

    public static Vertex3D of(double x, double y, double z) {
        Vertex3D vertex = new Vertex3D();
        vertex.cords = new double[3];
        vertex.cords[0] = x;
        vertex.cords[1] = y;
        vertex.cords[2] = z;
        return vertex;
    }

    public static Vertex3D of(double[] cords) {
        Vertex3D vertex = new Vertex3D();
        vertex.cords = copyOf(cords, 3);
        return vertex;
    }

    public double[] getCords() {
        return cords;
    }

    public double getX() {
        return cords[0];
    }

    public double getY() {
        return cords[1];
    }

    public double getZ() {
        return cords[2];
    }

    public Vertex3D copy() {
        return of(copyOf(cords, 3));
    }

    @Override
    public String toString() {
        return format("Tocka {x=%s, y=%s, z=%s}", cords[0], cords[1], cords[2]);
    }

    public String toOBJ() {
        return format("v %s %s %s", cords[0], cords[1], cords[2]);
    }
}
