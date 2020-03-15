package hr.fer.zemris.irg.math.matrix;

import hr.fer.zemris.irg.math.vector.IVector;
import hr.fer.zemris.irg.math.views.VectorMatrixView;

import java.util.Arrays;

public abstract class AbstractMatrix implements IMatrix {

    @Override
    public IMatrix nTranspose(boolean liveView) {
        return liveView ? new MatrixTransposeView(this) : new MatrixTransposeView(this.copy());
    }

    @Override
    public IMatrix subMatrix(int row, int column, boolean liveView) {
        return liveView ? new MatrixSubMatrixView(this, column, row) : new MatrixSubMatrixView(this.copy(), column, row);
    }

    @Override
    public IMatrix add(IMatrix other) {
        checkDimensions(other);
        for (int i = 0; i < getRowsCount(); i++)
            for (int j = 0; j < getColsCount(); j++)
                set(i, j, get(i, j) + other.get(i, j));
        return this;
    }

    @Override
    public IMatrix nAdd(IMatrix other) {
        checkDimensions(other);
        return copy().add(other);
    }

    @Override
    public IMatrix sub(IMatrix other) {
        checkDimensions(other);
        for (int i = 0; i < getRowsCount(); i++)
            for (int j = 0; j < getColsCount(); j++)
                set(i, j, get(i, j) - other.get(i, j));
        return this;
    }

    @Override
    public IMatrix nSub(IMatrix other) {
        checkDimensions(other);
        return copy().sub(other);
    }

    @Override
    public IMatrix nMultiply(IMatrix other) {
        checkMultiplicationDimensions(other);
        IMatrix matrix = newInstance(getRowsCount(), other.getColsCount());
        for (int row = 0; row < getRowsCount(); row++) {
            for (int col = 0; col < other.getColsCount(); col++) {
                double cell = 0;
                for (int i = 0; i < other.getRowsCount(); i++)
                    cell += get(row, i) * other.get(i, col);
                matrix.set(row, col, cell);
            }
        }
        return matrix;
    }

    @Override
    public double determinant() {
        if (getColsCount() == 1)
            return get(0, 0);
        if (getColsCount() == 2)
            return get(0, 0) * get(1, 1) - get(1, 0) * get(0, 1);

        double determinant = 0;
        int sign = 1;
        for (int i = 0; i < getColsCount(); i++, sign *= -1)
            determinant += sign * get(0, i) * this.subMatrix(0, i, true).determinant();

        return determinant;
    }

    @Override
    public IMatrix nInvert() {
        double determinant = 1 / (Math.abs(determinant()));
        IMatrix transposed = this.nTranspose(false);
        for (int i = 0; i < getRowsCount(); i++) {
            for (int j = 0; j < getColsCount(); j++) {
                transposed.set(i, j, Math.pow(-1, i + j) * determinant * subMatrix(i, j, true).determinant());
            }
        }
        return transposed.nTranspose(true);
    }

    @Override
    public IVector toVector(boolean flag) {
        return flag ? new VectorMatrixView(this) : new VectorMatrixView(this.copy());
    }

    private void checkDimensions(IMatrix other) {
        if (getColsCount() != other.getColsCount() || getRowsCount() != other.getRowsCount())
            throw new UnsupportedOperationException();
    }

    private void checkMultiplicationDimensions(IMatrix other) {
        if (getColsCount() != other.getRowsCount())
            throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return Arrays.deepToString(toArray()).replace("], ", "]\n");
    }
}
