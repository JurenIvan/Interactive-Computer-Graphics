package hr.fer.zemris.irg.math;

import hr.fer.zemris.irg.math.matrix.IMatrix;
import hr.fer.zemris.irg.math.vector.IVector;
import hr.fer.zemris.irg.math.vector.Vector;
import hr.fer.zemris.irg.objects.ObjectModel;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class IRGTest {

    private static final String STANDARD_PATH_TO_RESOURCES = "src/main/resources/";

    @Test
    public void test() throws IOException {
        ObjectModel objectModel = ObjectModel.parse(Files.readAllLines(Paths.get(STANDARD_PATH_TO_RESOURCES + "kocka.obj"))).normalize();
        IMatrix m = IRG.lookAtMatrix(new Vector(3, 4, 1), new Vector(0, 0, 0), new Vector(0, 1, 0));

        for (var vert : objectModel.getVertices()) {

            IVector vector = new Vector(vert.getX(), vert.getY(), vert.getZ(), 1.0);
            IVector tv = vector.toRowMatrix(false).nMultiply(m).toVector(false).nFromHomogeneus();
            System.out.println(tv.toString());

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
    public void test2() throws IOException {
        ObjectModel objectModel = ObjectModel.parse(Files.readAllLines(Paths.get(STANDARD_PATH_TO_RESOURCES + "kocka.obj"))).normalize();
        IMatrix tp = IRG.lookAtMatrix(new Vector(3, 4, 1), new Vector(0, 0, 0), new Vector(0, 1, 0));
        IMatrix pr = IRG.buildFrustumMatrix(-0.5, 0.5, -0.5, 0.5, 1, 100);
        IMatrix m = tp.nMultiply(pr);

        for (var vert : objectModel.getVertices()) {

            IVector vector = new Vector(vert.getX(), vert.getY(), vert.getZ(), 1.0);
            IVector tv = vector.toRowMatrix(false).nMultiply(m).toVector(false);
            System.out.println(tv.toString());

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
