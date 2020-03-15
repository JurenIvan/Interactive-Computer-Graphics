package hr.fer.zemris.irg.math.view;

import hr.fer.zemris.irg.math.matrix.IMatrix;
import hr.fer.zemris.irg.math.vector.Vector;
import hr.fer.zemris.irg.math.views.MatrixVectorView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MatrixVectorViewTest {

    @Test
    public void getRowsCountTrue() {
        MatrixVectorView matrixVectorView = new MatrixVectorView(Vector.parseSimple("1 2 3 4 5"), true);
        assertEquals(5, matrixVectorView.getColsCount());
        assertEquals(1, matrixVectorView.getRowsCount());
    }

    @Test
    public void getRowsCountFalse() {
        MatrixVectorView matrixVectorView = new MatrixVectorView(Vector.parseSimple("1 2 3 4 5"), false);
        assertEquals(5, matrixVectorView.getRowsCount());
        assertEquals(1, matrixVectorView.getColsCount());
    }

    @Test
    public void getTrue() {
        MatrixVectorView matrixVectorView = new MatrixVectorView(Vector.parseSimple("1 2 3 4 5"), true);
        assertEquals(1, matrixVectorView.get(0, 0));
        assertEquals(2, matrixVectorView.get(0, 1));
        assertEquals(3, matrixVectorView.get(0, 2));
        assertEquals(4, matrixVectorView.get(0, 3));
        assertEquals(5, matrixVectorView.get(0, 4));
    }

    @Test
    public void getFalse() {
        MatrixVectorView matrixVectorView = new MatrixVectorView(Vector.parseSimple("1 2 3 4 5"), false);
        assertEquals(1, matrixVectorView.get(0, 0));
        assertEquals(2, matrixVectorView.get(1, 0));
        assertEquals(3, matrixVectorView.get(2, 0));
        assertEquals(4, matrixVectorView.get(3, 0));
        assertEquals(5, matrixVectorView.get(4, 0));
    }

    @Test
    public void setFalse() {
        MatrixVectorView matrixVectorView = new MatrixVectorView(Vector.parseSimple("1 2 3 4 5"), false);
        matrixVectorView.set(1, 0, 0);
        assertEquals(0, matrixVectorView.get(1, 0));
        matrixVectorView.set(1, 0, 10);
        assertEquals(10, matrixVectorView.get(1, 0));
    }

    @Test
    public void setThrows() {
        MatrixVectorView matrixVectorView = new MatrixVectorView(Vector.parseSimple("1 2 3 4 5"), false);
        assertThrows(UnsupportedOperationException.class,()->matrixVectorView.set(1, 1, 0));

    }

    @Test
    public void setTrue() {
        MatrixVectorView matrixVectorView = new MatrixVectorView(Vector.parseSimple("1 2 3 4 5"), true);
        matrixVectorView.set(0, 1, 0);
        assertEquals(0, matrixVectorView.get(0, 1));
        matrixVectorView.set(0, 1, 10);
        assertEquals(10, matrixVectorView.get(0, 1));
    }

    @Test
    public void copy() {
        MatrixVectorView matrixVectorView = new MatrixVectorView(Vector.parseSimple("1 2 3 4 5"), true);
        IMatrix m2 = matrixVectorView.copy();

        assertEquals(5, m2.getColsCount());
        assertEquals(1, m2.getRowsCount());
    }


    @Test
    public void newInstance() {
        MatrixVectorView matrixVectorView = new MatrixVectorView(Vector.parseSimple("1 2 3 4 5"), true);
        IMatrix m2 = matrixVectorView.newInstance(1, 3);

        assertEquals(3, m2.getColsCount());
        assertEquals(1, m2.getRowsCount());
    }

    @Test
    public void newInstanceThrows() {
        MatrixVectorView matrixVectorView = new MatrixVectorView(Vector.parseSimple("1 2 3 4 5"), true);
        assertThrows(UnsupportedOperationException.class,()->matrixVectorView.newInstance(4, 3));
    }

    @Test
    public void toArrayTrue() {
        MatrixVectorView matrixVectorView = new MatrixVectorView(Vector.parseSimple("1 2 3 4 5"), true);
        var m2 = matrixVectorView.toArray();

        Assertions.assertEquals(1,m2[0][0]);
        Assertions.assertEquals(2,m2[0][1]);
        Assertions.assertEquals(3,m2[0][2]);
        Assertions.assertEquals(4,m2[0][3]);
        Assertions.assertEquals(5,m2[0][4]);
    }

    @Test
    public void toArrayFalse() {
        MatrixVectorView matrixVectorView = new MatrixVectorView(Vector.parseSimple("1 2 3 4 5"), false);
        var m2 = matrixVectorView.toArray();

        Assertions.assertEquals(1,m2[0][0]);
        Assertions.assertEquals(2,m2[1][0]);
        Assertions.assertEquals(3,m2[2][0]);
        Assertions.assertEquals(4,m2[3][0]);
        Assertions.assertEquals(5,m2[4][0]);
    }
}
