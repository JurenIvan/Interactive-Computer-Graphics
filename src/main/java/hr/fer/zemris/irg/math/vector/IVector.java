package hr.fer.zemris.irg.math.vector;

import hr.fer.zemris.irg.math.matrix.IMatrix;

public interface IVector {

    double get(int orderOfComponent);

    IVector set(int orderOfComponent, double value);

    int getDimension();

    IVector copy();

    IVector copyPart(int numberOfElements);

    IVector newInstance(int dimension);

    IVector add(IVector vector);

    IVector nAdd(IVector vector);

    IVector sub(IVector vector);

    IVector nSub(IVector vector);

    IVector scalarMultiply(double scalar);

    IVector nScalarMultiply(double scalar);

    double norm();

    IVector normalize();

    IVector nNormalize();

    double cosine(IVector vector);

    double scalarProduct(IVector vector);

    IVector nVectorProduct(IVector vector);

    IVector nFromHomogeneus();

    IMatrix toRowMatrix(boolean flag);

    IMatrix toColumnMatrix(boolean flag);

    double[] toArray();
}
