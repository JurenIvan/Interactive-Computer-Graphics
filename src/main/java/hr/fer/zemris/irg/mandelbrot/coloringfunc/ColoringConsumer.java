package hr.fer.zemris.irg.mandelbrot.coloringfunc;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.awt.GLCanvas;

import java.util.function.Consumer;

public abstract class ColoringConsumer implements Consumer<short[][]> {

    protected final GLCanvas glCanvas;
    protected final int maxIterations;

    public ColoringConsumer(GLCanvas glCanvas, int maxIterations) {
        this.glCanvas = glCanvas;
        this.maxIterations = maxIterations;
    }

    public abstract void accept(short[][] div);
}
