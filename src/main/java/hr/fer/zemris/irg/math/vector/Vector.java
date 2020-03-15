package hr.fer.zemris.irg.math.vector;

import java.util.Arrays;

public class Vector extends AbstractVector {

    private double[] elements;
    private boolean readOnly;

    private Vector(int dimension) {
        this.elements = new double[dimension];
        this.readOnly = false;
    }

    public Vector(double... elements) {
        this.elements = elements;
        readOnly = false;
    }

    public Vector(double[] elements, boolean readOnly, boolean referenceArray) {
        this.elements = referenceArray ? elements : Arrays.copyOf(elements, elements.length);
        this.readOnly = readOnly;
    }

    @Override
    public double get(int orderOfComponent) {
        return elements[orderOfComponent];
    }

    @Override
    public IVector set(int orderOfComponent, double value) {
        if (readOnly)
            throw new IllegalStateException();

        elements[orderOfComponent] = value;
        return this;
    }

    @Override
    public int getDimension() {
        return elements.length;
    }

    @Override
    public IVector copy() {
        return copyPart(elements.length);
    }

    @Override
    public IVector copyPart(int numberOfElements) {
        double[] elements = new double[numberOfElements];
        System.arraycopy(this.elements, 0, elements, 0, numberOfElements < getDimension() ? numberOfElements : getDimension());
        return new Vector(Arrays.copyOf(elements, numberOfElements), false, true);
    }

    @Override
    public IVector newInstance(int dimension) {
        if (dimension < 0)
            throw new IllegalArgumentException();
        return new Vector(dimension);
    }

    public static Vector parseSimple(String line) {
        double[] elements = Arrays.stream(line.trim().split("\\s+")).mapToDouble(Double::parseDouble).toArray();
        return new Vector(elements, false, true);
    }

    public String toString(int precision) {
        String numberFormat = "%." + precision + "f";
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[");
        for (var element : this.elements)
            stringBuilder.append(String.format(numberFormat, element));
        stringBuilder.append("]");
        return String.format("Vector{elements=%s, readOnly=%s}", stringBuilder.toString(), readOnly);
    }

    @Override
    public String toString() {
        return toString(3);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector)) return false;
        Vector vector = (Vector) o;
        return Arrays.equals(elements, vector.elements);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(elements);
    }
}
