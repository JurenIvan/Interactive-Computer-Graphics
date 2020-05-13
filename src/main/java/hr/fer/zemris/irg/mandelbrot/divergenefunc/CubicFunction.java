package hr.fer.zemris.irg.mandelbrot.divergenefunc;

import hr.fer.zemris.irg.mandelbrot.Complex;

import java.util.function.Function;

public class CubicFunction implements Function<Complex, Short> {

    private final int maxIterations;

    public CubicFunction(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    @Override
    public Short apply(Complex complex) {
        Complex c = Complex.ZERO;
        for (short i = 0; i < maxIterations; i++) {
            c = c.multiply(c).multiply(c).add(complex);
            if (c.module() > 4) return i;
        }
        return -1;
    }
}
