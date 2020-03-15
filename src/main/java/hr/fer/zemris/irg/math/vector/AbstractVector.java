package hr.fer.zemris.irg.math.vector;

import hr.fer.zemris.irg.math.matrix.IMatrix;
import hr.fer.zemris.irg.math.views.MatrixVectorView;

import java.util.Objects;

public abstract class AbstractVector implements IVector {

    @Override
    public IVector add(IVector vector) {
        checkVectorDimension(vector);

        for (int i = 0; i < getDimension(); i++)
            set(i, get(i) + vector.get(i));

        return this;
    }

    @Override
    public IVector nAdd(IVector vector) {
        return copy().add(vector);
    }

    @Override
    public IVector sub(IVector vector) {
        checkVectorDimension(vector);

        for (int i = 0; i < getDimension(); i++)
            set(i, get(i) - vector.get(i));

        return this;
    }

    @Override
    public IVector nSub(IVector vector) {
        return copy().sub(vector);
    }

    @Override
    public IVector scalarMultiply(double scalar) {
        for (int i = 0; i < getDimension(); i++)
            set(i, get(i) * scalar);

        return this;
    }

    @Override
    public IVector nScalarMultiply(double scalar) {
        return copy().scalarMultiply(scalar);
    }

    @Override
    public double norm() {
        double sum = 0;
        for (int i = 0; i < getDimension(); i++)
            sum += Math.pow(get(i), 2);

        return Math.sqrt(sum);
    }

    @Override
    public IVector normalize() {
        double vectorLength = norm();

        for (int i = 0; i < getDimension(); i++)
            set(i, get(i) / vectorLength);

        return this;
    }

    @Override
    public IVector nNormalize() {
        return copy().normalize();
    }

    @Override
    public double cosine(IVector vector) {
        return scalarProduct(vector) / (norm() * vector.norm());
    }

    @Override
    public double scalarProduct(IVector vector) {
        checkVectorDimension(vector);
        double sum = 0;
        for (int i = 0; i < getDimension(); i++)
            sum += get(i) * vector.get(i);
        return sum;
    }

    @Override
    public IVector nVectorProduct(IVector vector) {
        if (vector.getDimension() < 3)
            vector = vector.copyPart(3);

        IVector newInstance = newInstance(3);
        newInstance.set(0, get(1) * vector.get(2) - get(2) * vector.get(1));
        newInstance.set(1, get(2) * vector.get(0) - get(0) * vector.get(2));
        newInstance.set(2, get(0) * vector.get(1) - get(1) * vector.get(0));

        return newInstance;
    }

    @Override
    public IVector nFromHomogeneus() {
        if (this.getDimension() < 1)
            throw new UnsupportedOperationException();

        IVector newInstance = newInstance(getDimension() - 1);
        for (int i = 0; i < getDimension() - 1; i++)
            newInstance.set(i, get(i) / get(getDimension() - 1));
        return newInstance;
    }

    @Override
    public IMatrix toRowMatrix(boolean flag) {
        return new MatrixVectorView(this, true);
    }

    @Override
    public IMatrix toColumnMatrix(boolean flag) {
        return new MatrixVectorView(this, false);
    }

    @Override
    public double[] toArray() {
        double[] elements = new double[getDimension()];
        for (int i = 0; i < getDimension(); i++)
            elements[i] = get(i);
        return elements;
    }

    private void checkVectorDimension(IVector vector) {
        Objects.requireNonNull(vector);
        if (this.getDimension() != vector.getDimension())
            throw new UnsupportedOperationException("Vectors have to have same dimension");
    }
}
