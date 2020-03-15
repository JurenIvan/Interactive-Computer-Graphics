package hr.fer.zemris.irg.math.matrix;

public class MatrixTransposeView extends AbstractMatrix {

    private IMatrix matrix;

    public MatrixTransposeView(IMatrix matrix) {
        this.matrix = matrix;
    }

    @Override
    public int getRowsCount() {
        return matrix.getColsCount();
    }

    @Override
    public int getColsCount() {
        return matrix.getRowsCount();
    }

    @Override
    public double get(int row, int column) {
        return matrix.get(column, row);
    }

    @Override
    public IMatrix set(int row, int column, double value) {
        return matrix.set(column, row, value);
    }

    @Override
    public IMatrix copy() {
        return new MatrixTransposeView(matrix.copy());
    }

    @Override
    public IMatrix newInstance(int rows, int columns) {
        return new MatrixTransposeView(matrix.newInstance(rows, columns));
    }

    @Override
    public double[][] toArray() {

        double[][] elements = new double[matrix.getColsCount()][matrix.getRowsCount()];
        for (int i = 0; i < matrix.getRowsCount(); i++)
            for (int j = 0; j < matrix.getColsCount(); j++)
                elements[j][i] = matrix.get(i, j);

        return elements;
    }
}
