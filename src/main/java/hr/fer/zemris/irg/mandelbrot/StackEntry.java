package hr.fer.zemris.irg.mandelbrot;

public class StackEntry {

    public final double uMin;
    public final double uMax;
    public final double vMin;
    public final double vMax;

    public StackEntry(double uMin, double uMax, double vMin, double vMax) {
        this.uMin = uMin;
        this.uMax = uMax;
        this.vMin = vMin;
        this.vMax = vMax;
    }
}
