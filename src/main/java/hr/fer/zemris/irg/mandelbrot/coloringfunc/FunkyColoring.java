package hr.fer.zemris.irg.mandelbrot.coloringfunc;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.awt.GLCanvas;

import java.util.HashMap;
import java.util.List;

import static com.jogamp.opengl.GL.GL_POINTS;

/**
 *  Much nicer color palate than one presented in book.
 *
 *  Found this on:
 *  https://stackoverflow.com/questions/16500656/which-color-gradient-is-used-to-color-mandelbrot-in-wikipedia
 *
 * As this is was NOT necessary for this lab task, I may have been talking to other students.
 */
public class FunkyColoring extends ColoringConsumer {

    private static HashMap<Integer, List<Integer>> coloring;

    {
        coloring = new HashMap<>(64);
        coloring.put(0, List.of(66, 30, 15));
        coloring.put(1, List.of(25, 7, 26));
        coloring.put(2, List.of(9, 1, 47));
        coloring.put(3, List.of(4, 4, 73));
        coloring.put(4, List.of(0, 7, 100));
        coloring.put(5, List.of(12, 44, 138));
        coloring.put(6, List.of(24, 82, 177));
        coloring.put(7, List.of(57, 125, 209));
        coloring.put(8, List.of(134, 181, 229));
        coloring.put(9, List.of(211, 236, 248));
        coloring.put(10, List.of(241, 233, 191));
        coloring.put(11, List.of(248, 201, 95));
        coloring.put(12, List.of(255, 170, 0));
        coloring.put(13, List.of(204, 128, 0));
        coloring.put(14, List.of(153, 87, 0));
        coloring.put(15, List.of(106, 52, 3));
    }

    public FunkyColoring(GLCanvas glCanvas, int maxIterations) {
        super(glCanvas, maxIterations);
    }

    @Override
    public void accept(short[][] div) {
        GL2 gl2 = glCanvas.getGL().getGL2();
        gl2.glBegin(GL_POINTS);
        for (int y = 0; y < div.length; y++) {
            for (int x = 0; x < div[0].length; x++) {
                int n = div[y][x];
                if (n == -1)
                    gl2.glColor3d(0, 0, 0);
                else {
                    List<Integer> colors = coloring.get(n % 16);
                    gl2.glColor3d(colors.get(0)/256.0, colors.get(1)/256.0, colors.get(2)/256.0);
                }
                gl2.glVertex2i(x, y);
            }
        }
        gl2.glEnd();
    }
}
