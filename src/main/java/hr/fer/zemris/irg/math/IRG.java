package hr.fer.zemris.irg.math;

import hr.fer.zemris.irg.math.matrix.IMatrix;
import hr.fer.zemris.irg.math.matrix.Matrix;
import hr.fer.zemris.irg.math.vector.IVector;

public class IRG {

    public static IMatrix translate3D(double dx, double dy, double dz) {
        IMatrix matrix = new Matrix(4, 4);
        for (int i = 0; i < 4; i++) matrix.set(i,i,1);
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
        double[][] matrix = new double[4][4];

        IVector f = center.nSub(eye).normalize();
        IVector u = viewUp.nNormalize();
        IVector s = f.nVectorProduct(u).normalize();
        u = s.nVectorProduct(f).nNormalize();

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
        double[][] matrix = new double[4][4];


        matrix[0][0] = 2 * n / (r - l);
        matrix[1][1] = 2 * n / (t - b);

        matrix[0][2] = (r + l) / (r - l);
        matrix[1][2] = (t + b) / (t - b);
        matrix[2][2] = -((f + n) / (f - n));
        matrix[3][2] = -1;

        matrix[2][3] = -2 * f * n / (f - n);

        return new Matrix(4, 4, matrix, true);
    }
}
