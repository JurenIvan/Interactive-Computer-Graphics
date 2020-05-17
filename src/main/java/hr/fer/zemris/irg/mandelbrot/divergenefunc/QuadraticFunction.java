package hr.fer.zemris.irg.mandelbrot.divergenefunc;

import hr.fer.zemris.irg.mandelbrot.Complex;

import java.util.function.Function;

public class QuadraticFunction implements Function<Complex, Short> {

    private final int maxIterations;

    public QuadraticFunction(int maxIterations) {
        this.maxIterations = maxIterations;
    }

//    @Override
//    public Short apply(Complex complex) {
//        Complex c = Complex.ZERO;
//        for (short i = 0; i < maxIterations; i++) {
//            c = c.multiply(c).add(complex);
//            if (c.module() > 2) return i;
//        }
//        return -1;
//    }

    @Override
    public Short apply(Complex complex) {
        double reC = complex.getRe();
        double imC = complex.getIm();

        for (short i = 1; i <= maxIterations; i++) {

            double nextRe = reC * reC - imC * imC + complex.getRe();
            double nextIm = 2 * reC * imC + complex.getIm();
            reC = nextRe;
            imC = nextIm;

            if (reC * reC + imC * imC > 4) return i;
        }
        return -1;
    }
}
