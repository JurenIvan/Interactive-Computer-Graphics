package hr.fer.zemris.irg.math.matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Matrix extends AbstractMatrix {

    private double[][] elements;

    public Matrix(int rows, int columns) {
        this.elements = new double[rows][columns];
    }

    public Matrix(int rows, int columns, double[][] elements, boolean flag) {
        if (flag)
            this.elements = elements;
        else
            this.elements = copy2DArray(elements, rows, columns);
    }

    @Override
    public int getRowsCount() {
        return elements.length;
    }

    @Override
    public int getColsCount() {
        return elements[0].length;
    }

    @Override
    public double get(int row, int column) {
        return elements[row][column];
    }

    @Override
    public IMatrix set(int row, int column, double value) {
        elements[row][column] = value;
        return this;
    }

    @Override
    public IMatrix copy() {
        Matrix matrix = new Matrix(getRowsCount(), getColsCount(), toArray(), true);
        matrix.elements = toArray();
        return matrix;
    }

    @Override
    public IMatrix newInstance(int rows, int columns) {
        return new Matrix(rows, columns);
    }

    @Override
    public double[][] toArray() {
        return copy2DArray(this.elements, getRowsCount(), getColsCount());
    }

    private static double[][] copy2DArray(double[][] oldElements, int rows, int columns) {
        double[][] elements = new double[rows][columns];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                elements[i][j] = oldElements[i][j];
        return elements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Matrix)) return false;
        Matrix matrix = (Matrix) o;
        return Arrays.equals(elements, matrix.elements);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(elements);
    }

    public static Matrix parseString(String matrix) {
        List<List<Double>> matrixElements = new ArrayList<>();
        String[] lines = matrix.trim().split("\\|");
        for (int i = 0; i < lines.length; i++) {
            String[] elements = lines[i].trim().split("\\s+");
            matrixElements.add(new ArrayList<>(elements.length));
            for (int j = 0; j < elements.length; j++) {
                matrixElements.get(i).add(Double.parseDouble(elements[j]));
            }
        }
        double[][] elements = new double[matrixElements.size()][matrixElements.get(0).size()];
        for (int i = 0; i < matrixElements.size(); i++)
            for (int j = 0; j < matrixElements.get(0).size(); j++)
                elements[i][j] = matrixElements.get(i).get(j);

        return new Matrix(matrixElements.size(), matrixElements.get(0).size(), elements, true);
    }
}
