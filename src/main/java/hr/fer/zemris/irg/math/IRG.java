package hr.fer.zemris.irg.math;

import hr.fer.zemris.irg.math.matrix.IMatrix;
import hr.fer.zemris.irg.math.matrix.Matrix;
import hr.fer.zemris.irg.math.vector.IVector;

import java.util.List;

public class IRG {

    public static IMatrix translate3D(double dx, double dy, double dz) {
        IMatrix matrix = new Matrix(4, 4);
        for (int i = 0; i < 4; i++) matrix.set(i, i, 1);
        matrix.set(3, 0, -dx);
        matrix.set(3, 1, -dy);
        matrix.set(3, 2, -dz);
        return matrix;
    }

    public static IMatrix scale3D(double dx, double dy, double dz) {
        double[][] matrix = new double[4][4];
        matrix[0][0] = dx;
        matrix[1][1] = dy;
        matrix[2][2] = dz;
        matrix[3][3] = 1;
        return new Matrix(4, 4, matrix, true);
    }

    public static IMatrix lookAtMatrix(IVector eye, IVector center, IVector viewUp) {
        IVector f = center.nSub(eye).normalize();
        IVector u = viewUp.nNormalize();
        IVector s = f.nVectorProduct(u).normalize();
        u = s.nVectorProduct(f).nNormalize();

        double[][] matrix = new double[4][4];
        matrix[0][0] = s.get(0);
        matrix[0][1] = u.get(0);
        matrix[0][2] = -f.get(0);

        matrix[1][0] = s.get(1);
        matrix[1][1] = u.get(1);
        matrix[1][2] = -f.get(1);

        matrix[2][0] = s.get(2);
        matrix[2][1] = u.get(2);
        matrix[2][2] = -f.get(2);

        matrix[3][3] = 1;

        IMatrix trans = IRG.translate3D(eye.get(0), eye.get(1), eye.get(2));

        return trans.nMultiply(new Matrix(4, 4, matrix, true));
    }

    public static IMatrix buildFrustumMatrix(double l, double r, double b, double t, double n, double f) {
        Matrix m = new Matrix(4, 4);

        m.set(0, 0, 2 * n / (r - l));
        m.set(1, 1, 2 * n / (t - b));

        m.set(2, 0, (r + l) / (r - l));
        m.set(2, 1, (t + b) / (t - b));
        m.set(2, 2, -((f + n) / (f - n)));
        m.set(2, 3, -1);

        m.set(3, 2, -2 * f * n / (f - n));

        return m;
    }

    public static boolean isAntiClockwise(List<IVector> points) {
        IVector v1 = points.get(1).nSub(points.get(0));
        IVector v2 = points.get(2).nSub(points.get(1));

        v1.set(2, 0);
        v2.set(2, 0);

        return v1.nVectorProduct(v2).get(2) > 0;
    }
}
