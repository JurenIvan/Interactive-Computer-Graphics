package hr.fer.zemris.irg.math.views;

import hr.fer.zemris.irg.math.matrix.AbstractMatrix;
import hr.fer.zemris.irg.math.matrix.IMatrix;
import hr.fer.zemris.irg.math.vector.IVector;

public class MatrixVectorView extends AbstractMatrix {

    private IVector vector;
    private boolean asRowMatrix;

    public MatrixVectorView(IVector vector, boolean asRowMatrix) {
        this.vector = vector;
        this.asRowMatrix = asRowMatrix;
    }

    @Override
    public int getRowsCount() {
        return asRowMatrix ? 1 : vector.getDimension();
    }

    @Override
    public int getColsCount() {
        return asRowMatrix ? vector.getDimension() : 1;
    }

    @Override
    public double get(int row, int column) {
        illegalFieldCheck(row, column);
        return vector.get(asRowMatrix ? column : row);
    }

    @Override
    public IMatrix set(int row, int column, double value) {
        illegalFieldCheck(row, column);
        vector.set(asRowMatrix ? column : row, value);
        return this;
    }

    @Override
    public IMatrix copy() {
        return new MatrixVectorView(vector.copy(), asRowMatrix);
    }

    @Override
    public IMatrix newInstance(int row, int column) {
        illegalFieldCheckForNewInstance(row, column);
        return new MatrixVectorView(vector.newInstance(Math.max(row, column)), row == 1);
    }

    @Override
    public double[][] toArray() {
        double[][] elements = new double[asRowMatrix ? 1 : vector.getDimension()][asRowMatrix ? vector.getDimension() : 1];
        if (asRowMatrix)
            elements[0] = vector.copy().toArray();
        else
            for (int i = 0; i < vector.getDimension(); i++)
                elements[i][0] = vector.get(i);
        return elements;
    }

    private void illegalFieldCheck(int row, int column) {
        if (asRowMatrix && row > 0 || !asRowMatrix && column > 0)
            throw new UnsupportedOperationException();
    }

    private void illegalFieldCheckForNewInstance(int row, int column) {
        illegalFieldCheck(row - 1, column - 1);
    }
}
