package hr.fer.zemris.irg.drawers;

import com.jogamp.opengl.GL2;
import hr.fer.zemris.irg.color.Color;
import hr.fer.zemris.irg.shapes.Quad;

import static com.jogamp.opengl.GL.GL_LINE_LOOP;
import static com.jogamp.opengl.GL2ES3.GL_QUADS;

public class QuadDrawers {

    public static void outLineQuad(GL2 gl2, Quad quad) {
        outLineQuad(gl2, quad, quad.getColor());
    }

    public static void outLineQuad(GL2 gl2, Quad quad, Color color) {
        gl2.glBegin(GL_LINE_LOOP);
        gl2.glColor3f(color.getR(), color.getG(), color.getB());
        for (var point : quad.getPointList())
            gl2.glVertex2i(point.getX(), point.getY());
        gl2.glEnd();
    }

    public static void drawSquare(GL2 gl2, Quad quad) {
        drawSquare(gl2, quad, quad.getColor());
    }

    public static void drawSquare(GL2 gl2, Quad quad, Color color) {
        gl2.glBegin(GL_QUADS);
        gl2.glColor3f(color.getR(), color.getG(), color.getB());
        for (var point : quad.getPointList())
            gl2.glVertex2i(point.getX(), point.getY());
        gl2.glEnd();
    }
}
