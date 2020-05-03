package hr.fer.zemris.irg.math;

import hr.fer.zemris.irg.math.matrix.IMatrix;
import hr.fer.zemris.irg.math.vector.IVector;
import hr.fer.zemris.irg.math.vector.Vector;
import hr.fer.zemris.irg.objects.Vertex3D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class IRGTest {

    @Test
    public void test() {
        List<IVector> tvs = new ArrayList<>();
        IMatrix m = IRG.lookAtMatrix(new Vector(3, 4, 1), new Vector(0, 0, 0), new Vector(0, 1, 0));

        for (var vert : List.of(
                new Vertex3D(-1, -1, -1),
                new Vertex3D(-1, -1, 1),
                new Vertex3D(1, -1, -1),
                new Vertex3D(1, -1, 1),
                new Vertex3D(1, 1, 1),
                new Vertex3D(-1, 1, -1),
                new Vertex3D(-1, 1, 1))) {

            IVector vector = new Vector(vert.getX(), vert.getY(), vert.getZ(), 1.0);
            tvs.add(vector.toRowMatrix(false).nMultiply(m).toVector(false).nFromHomogeneus());
        }
    }

//            (−1.0, −1.0, −1.0)    →   (0.632, 0.372, −6.668)
//            (−1.0, −1.0, 1.0)     →   (−1.265, −0.124, −6.276)
//            (1.0, −1.0, −1.0)     →   (1.265, −1.116, −5.491)
//            (1.0, −1.0, 1.0)      →   (−0.632, −1.612, −5.099)
//            (1.0, 1.0, −1.0)      →   (1.265, 0.124, −3.922)
//            (1.0, 1.0, 1.0)       →   (−0.632, −0.372, −3.530)
//            (−1.0, 1.0, −1.0)     →   (0.632, 1.612, −5.099)
//            (−1.0, 1.0, 1.0)      →   (−1.265, 1.116, −4.707)

    @Test
    public void test2() {
        List<IVector> tvs = new ArrayList<>();
        IMatrix tp = IRG.lookAtMatrix(new Vector(3, 4, 1), new Vector(0, 0, 0), new Vector(0, 1, 0));
        IMatrix pr = IRG.buildFrustumMatrix(-0.5, 0.5, -0.5, 0.5, 1, 100);
        IMatrix m = tp.nMultiply(pr);

        for (var vert : List.of(
                new Vertex3D(-1, -1, -1),
                new Vertex3D(-1, -1, 1),
                new Vertex3D(1, -1, -1),
                new Vertex3D(1, -1, 1),
                new Vertex3D(1, 1, 1),
                new Vertex3D(-1, 1, -1),
                new Vertex3D(-1, 1, 1))) {

            IVector vector = new Vector(vert.getX(), vert.getY(), vert.getZ(), 1.0);
            tvs.add(vector.toRowMatrix(false).nMultiply(m).toVector(false));
        }
    }

//            (−1.0, −1.0, −1.0)    →    (0.190, 0.112, 0.700)
//            (−1.0, −1.0, 1.0)     →    (−0.403, −0.040, 0.681)
//            (1.0, −1.0, −1.0)     →    (0.461, −0.407, 0.636)
//            (1.0, −1.0, 1.0)      →    (−0.248, −0.632, 0.608)
//            (1.0, 1.0, −1.0)      →    (0.645, 0.063, 0.490)
//            (1.0, 1.0, 1.0)       →    (−0.358, −0.211, 0.433)
//            (−1.0, 1.0, −1.0)     →    (0.248, 0.632, 0.608)
//            (−1.0, 1.0, 1.0)      →    (−0.537, 0.474, 0.575)
}
