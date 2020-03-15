package hr.fer.zemris.irg.drawers;

import com.jogamp.opengl.GL2;
import hr.fer.zemris.irg.shapes.Point;
import hr.fer.zemris.irg.shapes.Triangle;

import static com.jogamp.opengl.GL.*;

public class TriangleDrawer {



    public static void drawTriangle(GL2 gl2, Triangle triangle) {
        var color = triangle.getColor();
        gl2.glBegin(GL_TRIANGLES);
        gl2.glColor3f(color.getR(), color.getG(), color.getB());

        for (Point point : triangle.getPointList())
            gl2.glVertex2f(point.getX(), point.getY());

        gl2.glEnd();
    }

}
