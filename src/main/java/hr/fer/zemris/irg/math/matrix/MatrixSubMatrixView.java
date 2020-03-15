package hr.fer.zemris.irg.math.matrix;

public class MatrixSubMatrixView extends AbstractMatrix {

    private IMatrix matrix;
    private int deletedColumn;
    private int deletedRow;

    public MatrixSubMatrixView(IMatrix matrix, int deletedColumn, int deletedRow) {
        this.matrix = matrix;
        this.deletedColumn = deletedColumn;
        this.deletedRow = deletedRow;
    }

    @Override
    public int getRowsCount() {
        return matrix.getRowsCount() - 1;
    }

    @Override
    public int getColsCount() {
        return matrix.getColsCount() - 1;
    }

    @Override
    public double get(int row, int column) {
        return matrix.get(row >= deletedRow ? row + 1 : row, column >= deletedColumn ? column + 1 : column);
    }

    @Override
    public IMatrix set(int row, int column, double value) {
        return matrix.set(row >= deletedRow ? row + 1 : row, column >= deletedColumn ? column + 1 : column, value);
    }

    @Override
    public IMatrix copy() {
        return new MatrixSubMatrixView(this.matrix.copy(), deletedColumn, deletedRow);
    }

    @Override
    public IMatrix newInstance(int rows, int columns) {
        return matrix.newInstance(rows - 1, columns - 1);
    }

    @Override
    public double[][] toArray() {
        double[][] result = new double[getRowsCount()][getColsCount()];

        for (int i = 0; i < getRowsCount(); i++)
            for (int j = 0; j < getColsCount(); j++)
                result[i][j] = get(i, j);

        return result;
    }
}
