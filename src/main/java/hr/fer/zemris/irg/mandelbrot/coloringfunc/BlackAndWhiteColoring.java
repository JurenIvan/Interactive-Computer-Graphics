package hr.fer.zemris.irg.mandelbrot.coloringfunc;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.awt.GLCanvas;

import static com.jogamp.opengl.GL.GL_POINTS;

public class BlackAndWhiteColoring extends ColoringConsumer {

    public BlackAndWhiteColoring(GLCanvas glCanvas, int maxIterations) {
        super(glCanvas, maxIterations);
    }

    @Override
    public void accept(short[][] div) {
        GL2 gl2 = glCanvas.getGL().getGL2();
        gl2.glBegin(GL_POINTS);
        for (int i = 0; i < div.length; i++) {
            for (int j = 0; j < div[0].length; j++) {
                if (div[i][j] == -1)
                    gl2.glColor3f(0, 0, 0);
                else
                    gl2.glColor3f(1, 1, 1);
                gl2.glVertex2i(i, j);
            }
        }
    }
}
