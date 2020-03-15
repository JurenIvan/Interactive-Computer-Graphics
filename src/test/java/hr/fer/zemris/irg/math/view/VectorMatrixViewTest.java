package hr.fer.zemris.irg.math.view;

import hr.fer.zemris.irg.math.matrix.Matrix;
import hr.fer.zemris.irg.math.vector.IVector;
import hr.fer.zemris.irg.math.views.VectorMatrixView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VectorMatrixViewTest {

    @Test
    public void getTrue() {
        VectorMatrixView vectorMatrixView = new VectorMatrixView(Matrix.parseString("1 | 2 | 3"));
        assertEquals(1, vectorMatrixView.get(0));
        assertEquals(2, vectorMatrixView.get(1));
        assertEquals(3, vectorMatrixView.get(2));
    }

    @Test
    public void getFalse() {
        VectorMatrixView vectorMatrixView = new VectorMatrixView(Matrix.parseString("1  2  3"));
        assertEquals(1, vectorMatrixView.get(0));
        assertEquals(2, vectorMatrixView.get(1));
        assertEquals(3, vectorMatrixView.get(2));
    }

    @Test
    public void setTrue() {
        VectorMatrixView vectorMatrixView = new VectorMatrixView(Matrix.parseString("1  2 3"));
        vectorMatrixView.set(1, 10);
        assertEquals(1, vectorMatrixView.get(0));
        assertEquals(10, vectorMatrixView.get(1));
        assertEquals(3, vectorMatrixView.get(2));
    }

    @Test
    public void setFalse() {
        VectorMatrixView vectorMatrixView = new VectorMatrixView(Matrix.parseString("1 | 2 | 3"));
        vectorMatrixView.set(1, 10);
        assertEquals(1, vectorMatrixView.get(0));
        assertEquals(10, vectorMatrixView.get(1));
        assertEquals(3, vectorMatrixView.get(2));
    }

    @Test
    public void getDimensionTrue() {
        VectorMatrixView vectorMatrixView = new VectorMatrixView(Matrix.parseString("1  2 3"));
        assertEquals(3, vectorMatrixView.getDimension());
    }

    @Test
    public void getDimensionFalse() {
        VectorMatrixView vectorMatrixView = new VectorMatrixView(Matrix.parseString("1 | 2 |3"));
        assertEquals(3, vectorMatrixView.getDimension());
    }

    @Test
    public void copyTrue() {
        VectorMatrixView vectorMatrixView = new VectorMatrixView(Matrix.parseString("1 | 2 |3"));

        IVector vector = vectorMatrixView.copy();
        assertEquals(1, vector.get(0));
        assertEquals(2, vector.get(1));
        assertEquals(3, vector.get(2));

        vector.set(1, 10);

        assertEquals(1, vectorMatrixView.get(0));
        assertEquals(2, vectorMatrixView.get(1));
        assertEquals(3, vectorMatrixView.get(2));

        assertEquals(1, vector.get(0));
        assertEquals(10, vector.get(1));
        assertEquals(3, vector.get(2));
    }

    @Test
    public void copyFalse() {
        VectorMatrixView vectorMatrixView = new VectorMatrixView(Matrix.parseString("1 2 3"));

        IVector vector = vectorMatrixView.copy();
        assertEquals(1, vector.get(0));
        assertEquals(2, vector.get(1));
        assertEquals(3, vector.get(2));

        vector.set(1, 10);

        assertEquals(1, vectorMatrixView.get(0));
        assertEquals(2, vectorMatrixView.get(1));
        assertEquals(3, vectorMatrixView.get(2));

        assertEquals(1, vector.get(0));
        assertEquals(10, vector.get(1));
        assertEquals(3, vector.get(2));
    }

    @Test
    public void copyPartTrue() {
        VectorMatrixView vectorMatrixView = new VectorMatrixView(Matrix.parseString("1  2 3"));

        IVector vector = vectorMatrixView.copyPart(2);
        assertEquals(1, vector.get(0));
        assertEquals(2, vector.get(1));
        assertEquals(2, vector.getDimension());

        vector.set(1, 10);

        assertEquals(1, vectorMatrixView.get(0));
        assertEquals(2, vectorMatrixView.get(1));
        assertEquals(3, vectorMatrixView.get(2));

        assertEquals(1, vector.get(0));
        assertEquals(10, vector.get(1));
        assertEquals(2, vector.getDimension());
    }

    @Test
    public void copyPartFalse() {
        VectorMatrixView vectorMatrixView = new VectorMatrixView(Matrix.parseString("1 | 2 | 3"));

        IVector vector = vectorMatrixView.copyPart(2);
        assertEquals(1, vector.get(0));
        assertEquals(2, vector.get(1));
        assertEquals(2, vector.getDimension());

        vector.set(1, 10);

        assertEquals(1, vectorMatrixView.get(0));
        assertEquals(2, vectorMatrixView.get(1));
        assertEquals(3, vectorMatrixView.get(2));

        assertEquals(1, vector.get(0));
        assertEquals(10, vector.get(1));
        assertEquals(2, vector.getDimension());
    }

    @Test
    public void newInstance() {
        VectorMatrixView vectorMatrixView = new VectorMatrixView(Matrix.parseString("1 | 2 | 3"));

        assertEquals(6, vectorMatrixView.newInstance(6).getDimension());
    }

    @Test
    public void newInstanceFalse() {
        VectorMatrixView vectorMatrixView = new VectorMatrixView(Matrix.parseString("1  2  3"));

        assertEquals(6, vectorMatrixView.newInstance(6).getDimension());
    }
}
