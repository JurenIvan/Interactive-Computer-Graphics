package hr.fer.zemris.irg.objects;

import hr.fer.zemris.irg.math.vector.IVector;
import hr.fer.zemris.irg.math.vector.Vector;

import java.util.List;

public class Face3D {

    int[] vertexIndex;

    static Face3D of(int x0, int x1, int x2) {
        Face3D face = new Face3D();
        var vertexIndex = new int[3];
        vertexIndex[0] = x0;
        vertexIndex[1] = x1;
        vertexIndex[2] = x2;
        face.vertexIndex = vertexIndex;
        return face;
    }

    public int[] getVertexIndex() {
        return vertexIndex;
    }

    public Face3D copy() {
        return of(vertexIndex[0], vertexIndex[1], vertexIndex[2]);
    }

    public FaceCoeficient calculateCoefficients(List<Vertex3D> vertices) {

        Vertex3D p0 = vertices.get(vertexIndex[0]);
        Vertex3D p1 = vertices.get(vertexIndex[1]);
        Vertex3D p2 = vertices.get(vertexIndex[2]);

        IVector v1 = new Vector(p1.getCords()).sub(new Vector(p0.getCords()));
        IVector v2 = new Vector(p2.getCords()).sub(new Vector(p0.getCords()));

        IVector n = v1.nVectorProduct(v2);

        double d = n.get(0) * p0.getX() - n.get(1) * p0.getY() - n.get(2) * p0.getZ();

        return new FaceCoeficient(n.get(0), n.get(1), n.get(2), d);
    }
}
