package hr.fer.zemris.irg.math.views;

import hr.fer.zemris.irg.math.matrix.IMatrix;
import hr.fer.zemris.irg.math.vector.AbstractVector;
import hr.fer.zemris.irg.math.vector.IVector;
import hr.fer.zemris.irg.math.vector.Vector;

public class VectorMatrixView extends AbstractVector {

    private IMatrix matrix;
    private boolean rowMatrix;

    public VectorMatrixView(IMatrix matrix) {
        this.matrix = matrix;
        rowMatrix = matrix.getRowsCount() == 1;
    }

    @Override
    public double get(int orderOfComponent) {
        return rowMatrix ? matrix.get(0, orderOfComponent) : matrix.get(orderOfComponent, 0);
    }

    @Override
    public IVector set(int orderOfComponent, double value) {
        if (rowMatrix)
            matrix.set(0, orderOfComponent, value);
        else
            matrix.set(orderOfComponent, 0, value);
        return this;
    }

    @Override
    public int getDimension() {
        return rowMatrix ? matrix.getColsCount() : matrix.getRowsCount();
    }

    @Override
    public IVector copy() {
        return matrix.toVector(false);
    }

    @Override
    public IVector copyPart(int numberOfElements) {
        double[] elements = new double[numberOfElements];
        for (int i = 0; i < numberOfElements; i++) {
            elements[i] = get(i);
        }
        return new Vector(elements, false, true);
    }

    @Override
    public IVector newInstance(int dimension) {
        return new VectorMatrixView(matrix.newInstance(rowMatrix ? 1 : dimension, rowMatrix ? dimension : 1));
    }
}
