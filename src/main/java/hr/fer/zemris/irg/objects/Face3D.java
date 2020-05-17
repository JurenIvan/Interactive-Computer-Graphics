package hr.fer.zemris.irg.objects;

import hr.fer.zemris.irg.math.vector.IVector;
import hr.fer.zemris.irg.math.vector.Vector;

import java.util.List;

import static java.lang.String.format;

public class Face3D {

    private int x0, x1, x2;
    private FaceCoeficient faceCoeficient;

    private Face3D(int x0, int x1, int x2) {
        this.x0 = x0;
        this.x1 = x1;
        this.x2 = x2;
    }

    static Face3D of(int x0, int x1, int x2) {
        return new Face3D(x0, x1, x2);
    }

    public Face3D copy() {
        return of(x0, x1, x2);
    }

    public FaceCoeficient calculateCoefficients(List<Vertex3D> vertices) {
        if (this.faceCoeficient != null)
            return this.faceCoeficient;

        Vertex3D p0 = vertices.get(x0 - 1);
        Vertex3D p1 = vertices.get(x1 - 1);
        Vertex3D p2 = vertices.get(x2 - 1);

        IVector v1 = new Vector(p1.getCords()).nSub(new Vector(p0.getCords()));
        IVector v2 = new Vector(p2.getCords()).nSub(new Vector(p0.getCords()));
        IVector n = v1.nVectorProduct(v2);

        double d = -(n.get(0) * p0.getX() + n.get(1) * p0.getY() + n.get(2) * p0.getZ());

        return this.faceCoeficient = new FaceCoeficient(n.get(0), n.get(1), n.get(2), d);
    }

    public String toOBJ() {
        return format("f %s %s %s", x0, x1, x2);
    }

    public int getX0() {
        return x0;
    }

    public int getX1() {
        return x1;
    }

    public int getX2() {
        return x2;
    }

    public void resetCoefficients() {
        this.faceCoeficient = null;
    }

    public boolean containsVertice(int i) {
        return x0 - 1 == i || x1 - 1 == i || x2 - 1 == i;
    }
}
