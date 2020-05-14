package hr.fer.zemris.irg.mandelbrot;

import java.util.concurrent.Callable;
import java.util.function.Function;

import static hr.fer.zemris.irg.Mandelbrot.covertPointToComplex;

public class PosaoIzracuna implements Callable<Void> {

    private final int width;
    private final int height;
    private final int yMin;
    private final int yMax;
    private final int m;
    private final short[][] data;
    private final StackEntry stackEntry;
    private final Function<Complex, Short> divergeFunction;

    public PosaoIzracuna(StackEntry stackEntry, int width, int height, int yMin, int yMax, int m, short[][] data, Function<Complex, Short> divergeFunction) {
        this.stackEntry = stackEntry;
        this.width = width;
        this.height = height;
        this.yMin = yMin;
        this.yMax = yMax;
        this.m = m;
        this.data = data;
        this.divergeFunction = divergeFunction;
    }

    @Override
    public Void call() {
        for (int y = yMin; y <= yMax; y++)
            for (int x = 0; x < width; x++)
                data[y][x] = divergeFunction.apply(covertPointToComplex(x, y, stackEntry, width, height));
        return null;
    }
}
