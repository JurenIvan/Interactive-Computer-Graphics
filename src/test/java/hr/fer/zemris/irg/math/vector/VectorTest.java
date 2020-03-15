package hr.fer.zemris.irg.math.vector;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VectorTest {

    @Test
    public void scalarProduct_add_parse() {
        IVector a = Vector.parseSimple("3 1 3");
        IVector b = new Vector(-2, 4, 1);

        double n = a.add(new Vector(1, 1, 1))
                .add(b)
                .scalarProduct(new Vector(2, 3, 2));

        assertEquals(32, n);
    }

    @Test
    public void copyPart() {
        IVector a = new Vector(-2, 4, 1);

        IVector b = a.copyPart(2);
        IVector c = a.copyPart(5);

        assertEquals(-2, b.get(0));
        assertEquals(4, b.get(1));

        assertEquals(-2, c.get(0));
        assertEquals(4, c.get(1));
        assertEquals(1, c.get(2));
        assertEquals(0, c.get(3));
        assertEquals(0, c.get(4));
    }

    @Test
    public void nHomo() {
        IVector vector = new Vector(1, 3, 7, 2);
        IVector homogeneus = vector.nFromHomogeneus();

        Assertions.assertEquals(3, homogeneus.getDimension());
        Assertions.assertEquals(0.5, homogeneus.get(0));
        Assertions.assertEquals(1.5, homogeneus.get(1));
        Assertions.assertEquals(3.5, homogeneus.get(2));
    }

    @Test
    public void baricentricneKoridnate() {
        IVector a = Vector.parseSimple("1 0 0");
        IVector b = Vector.parseSimple("5 0 0");
        IVector c = Vector.parseSimple("3 8 0");

        IVector t = Vector.parseSimple("3 4 0");
        double pov = b.nSub(a).nVectorProduct(c.nSub(a)).norm() / 2.0;
        double povA = b.nSub(t).nVectorProduct(c.nSub(t)).norm() / 2.0;
        double povB = a.nSub(t).nVectorProduct(c.nSub(t)).norm() / 2.0;
        double povC = a.nSub(t).nVectorProduct(b.nSub(t)).norm() / 2.0;

        double t1 = povA / pov;
        double t2 = povB / pov;
        double t3 = povC / pov;

        System.out.println(" Baricentricne koordinate su : (" + t1 + "," + t2 + "," + t3 + "). ");

        Assertions.assertEquals(0.25, t1);
        Assertions.assertEquals(0.25, t2);
        Assertions.assertEquals(0.5, t3);

    }
}



