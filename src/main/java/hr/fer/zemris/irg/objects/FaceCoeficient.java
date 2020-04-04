package hr.fer.zemris.irg.objects;

import static java.lang.String.format;

public class FaceCoeficient {

    private double a;
    private double b;
    private double c;
    private double d;

    public FaceCoeficient(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    public double getD() {
        return d;
    }

    @Override
    public String toString() {
        return format("Plane{a=%s, b=%s, c=%s, d=%s}", a, b, c, d);
    }
}
