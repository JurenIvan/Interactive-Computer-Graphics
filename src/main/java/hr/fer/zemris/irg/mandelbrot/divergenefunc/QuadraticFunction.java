package hr.fer.zemris.irg.mandelbrot.divergenefunc;

import hr.fer.zemris.irg.mandelbrot.Complex;

import java.util.function.Function;

public class QuadraticFunction implements Function<Complex, Short> {

    private final int maxIterations;

    public QuadraticFunction(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    @Override
    public Short apply(Complex complex) {
        Complex c = Complex.ZERO;
        for (short i = 0; i < maxIterations; i++) {
            c = c.multiply(c).add(complex);
            if (c.module() > 2) return i;
        }
        return -1;
    }
}
