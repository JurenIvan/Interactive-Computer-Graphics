package hr.fer.zemris.irg.objects;

import hr.fer.zemris.irg.math.vector.IVector;

import static java.lang.String.format;

public class Vertex3D {

    private double x;
    private double y;
    private double z;

    private IVector normal;

    public Vertex3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public IVector getNormal() {
        if (normal == null)
            throw new IllegalStateException("normal should have been initialized by ObjectModel");
        return normal;
    }

    public void setNormal(IVector normal) {
        this.normal = normal;
    }

    public static Vertex3D of(double x, double y, double z) {
        return new Vertex3D(x, y, z);
    }

    public static Vertex3D of(double[] cords) {
        return new Vertex3D(cords[0], cords[1], cords[2]);
    }

    public double[] getCords() {
        var arr = new double[3];
        arr[0] = x;
        arr[1] = y;
        arr[2] = z;
        return arr;
    }

    public Vertex3D copy() {
        return new Vertex3D(x, y, z);
    }

    public String toOBJ() {
        return format("v %s %s %s", x, y, z);
    }

    @Override
    public String toString() {
        return format("Tocka {x=%s, y=%s, z=%s}", x, y, z);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
