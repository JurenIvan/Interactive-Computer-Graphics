package hr.fer.zemris.irg.math;

import hr.fer.zemris.irg.math.matrix.IMatrix;
import hr.fer.zemris.irg.math.matrix.Matrix;
import hr.fer.zemris.irg.math.vector.IVector;
import hr.fer.zemris.irg.math.vector.Vector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Demo {

    @Test
    public void demonstration() {
        IVector v1 = Vector.parseSimple("2 3 -4").nAdd(Vector.parseSimple("-1 4 -3"));
        assertEquals(Vector.parseSimple("1 7 -7"), v1);

        double s = v1.scalarProduct(Vector.parseSimple("-1 4 -3"));
        assertEquals(48, s);

        IVector v2 = v1.nVectorProduct(Vector.parseSimple("2 2 4"));
        assertEquals(Vector.parseSimple("42 -18 -12"), v2);

        IVector v3 = v2.copy().normalize();
        assertEquals(0.889, v3.get(0), 1e-3);
        assertEquals(-0.381, v3.get(1), 1e-3);
        assertEquals(-0.254, v3.get(2), 1e-3);

        IVector v4 = v2.scalarMultiply(-1);
        assertEquals(-42, v4.get(0), 1e-3);
        assertEquals(18, v4.get(1), 1e-3);
        assertEquals(12, v4.get(2), 1e-3);

        IMatrix m1 = Matrix.parseString("1 2 3 |2 1 3| 4 5 1").nAdd(Matrix.parseString("-1 2 -3 |5 -2 7|-4 -1 3"));
        IMatrix m2 = Matrix.parseString("1 2 3 |2 1 3| 4 5 1").nMultiply(Matrix.parseString("-1 2 -3 |5 -2 7|-4 -1 3").nTranspose(false));
        IMatrix m3 = Matrix.parseString("-24 18 5 | 20 -15 -4 |-5 4 1").nInvert().nMultiply(Matrix.parseString("1 2 3 | 0 1 4 | 5 6 0").nInvert());

        //   assertEquals(Matrix.parseString("0 4 0 |7 -1 10| 0 4 4"), m1);
        //   assertEquals(Matrix.parseString("-6 22  3  |-9 29  0  |3  17  -18"), m2);
        //   assertEquals(Matrix.parseString("1 0 0 |0 1 0| 0 0 1"), m3);
    }
}
