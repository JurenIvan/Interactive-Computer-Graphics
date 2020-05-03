package hr.fer.zemris.irg;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import hr.fer.zemris.irg.color.Color;
import hr.fer.zemris.irg.math.BinomialCoef;
import hr.fer.zemris.irg.math.matrix.IMatrix;
import hr.fer.zemris.irg.math.matrix.Matrix;
import hr.fer.zemris.irg.math.vector.IVector;
import hr.fer.zemris.irg.math.vector.Vector;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_LINE_STRIP;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import static hr.fer.zemris.irg.color.Color.*;
import static java.awt.BorderLayout.CENTER;
import static java.awt.event.KeyEvent.VK_ESCAPE;

public class BezierLines extends JFrame {

    static {
        GLProfile.initSingleton();
    }

    private final GLCanvas glCanvas = new GLCanvas(new GLCapabilities(GLProfile.getDefault()));
    private final List<IVector> points = new ArrayList<>();
    private final BinomialCoef binomialCoef = new BinomialCoef(10);

    private int selectedPointIndex = -1;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BezierLines::new);
    }

    public BezierLines() {
        setVisible(true);
        setSize(800, 600);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().add(glCanvas, CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });
        initGUI();
    }

    private void initGUI() {
        glCanvas.requestFocusInWindow();

        addMouseListeners();
        addKeyboardListeners();

        addGLEventListener();
    }

    private void addGLEventListener() {
        glCanvas.addGLEventListener(new GLEventListener() {
            @Override
            public void init(GLAutoDrawable glAutoDrawable) {
            }

            @Override
            public void dispose(GLAutoDrawable glAutoDrawable) {
            }

            @Override
            public void display(GLAutoDrawable glAutoDrawable) {
                GL2 gl2 = glAutoDrawable.getGL().getGL2();
                gl2.glClearColor(GREEN.getR(), GREEN.getG(), GREEN.getB(), 1);
                gl2.glClear(GL_COLOR_BUFFER_BIT);
                drawScene(gl2);
                gl2.glLoadIdentity();
            }

            private void drawScene(GL2 gl2) {
                gl2.glBegin(GL_LINE_STRIP);
                gl2.glColor3f(BLACK.getR(), BLACK.getG(), BLACK.getB());
                for (var point : points)   //control Poly
                    gl2.glVertex2d(point.get(0), point.get(1));
                gl2.glEnd();

                if (points.size() > 2) {
                    draw_curve(points, 100, gl2, BLUE);
                    draw_curve(inter(points), 100, gl2, RED);
                }
            }

            @Override
            public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
                GL2 gl2 = glAutoDrawable.getGL().getGL2();
                gl2.glMatrixMode(GL_PROJECTION);
                gl2.glLoadIdentity();

                GLU glu = new GLU();
                glu.gluOrtho2D(0, width, height, 0);

                gl2.glMatrixMode(GL_MODELVIEW);
                gl2.glLoadIdentity();
                gl2.glViewport(0, 0, width, height);
            }
        });
    }

    private void addKeyboardListeners() {
        glCanvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == VK_ESCAPE) {
                    points.clear();
                    glCanvas.display();
                }
            }
        });
    }

    private void addMouseListeners() {
        glCanvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 1) points.add(new Vector(e.getX(), e.getY()));
                else if (e.getButton() == 3) selectedPointIndex = findClosestPoint(e.getX(), e.getY());
                glCanvas.display();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == 3) selectedPointIndex = -1;
            }
        });

        glCanvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedPointIndex >= 0) {
                    IVector point = points.get(selectedPointIndex);

                    point.set(0, e.getX());
                    point.set(1, e.getY());

                    glCanvas.display();
                }
            }
        });
    }

    private int findClosestPoint(int x, int y) {
        Vector newPoint = new Vector(x, y);
        double minDistance = Double.MAX_VALUE;
        for (int i = 0; i < points.size(); i++) {
            double distance = points.get(i).nSub(newPoint).norm();
            if (distance < minDistance) {
                minDistance = distance;
                selectedPointIndex = i;
            }
        }
        return selectedPointIndex;
    }


    public void draw_curve(List<IVector> points, int divs, GL2 gl2, Color color) {
        int n = points.size() - 1;
        var factors = binomialCoef.computeFactors(n);

        gl2.glBegin(GL_LINE_STRIP);
        gl2.glColor3f(color.getR(), color.getG(), color.getB());

        for (int i = 0; i <= divs; i++) {
            double t = 1.0 / divs * i;
            double x = 0, y = 0, b;

            for (int j = 0; j <= n; j++) {

                if (j == 0) b = Math.pow(1 - t, n);
                else if (j == n) b = factors.get(j) * Math.pow(t, n);
                else b = factors.get(j) * Math.pow(t, j) * Math.pow(1 - t, n - j);

                x += b * points.get(j).get(0);
                y += b * points.get(j).get(1);
            }
            gl2.glVertex2d(x, y);
        }
        gl2.glEnd();
    }

    public List<IVector> inter(List<IVector> points) {
        int n = points.size() - 1;

        List<Integer> factors = binomialCoef.computeFactors(n);
        IMatrix B = new Matrix(n + 1, n + 1);
        for (int i = 0; i < n + 1; i++) {
            double t = 1.0 / n * i;

            for (int j = 0; j <= n; j++) {
                if      (j == 0)    B.set(i, j, factors.get(j) * Math.pow(1 - t, n));
                else if (j == n)    B.set(i, j, factors.get(j) * Math.pow(t, n));
                else                B.set(i, j, factors.get(j) * Math.pow(t, j) * Math.pow(1 - t, n - j));
            }
        }

        IMatrix P = new Matrix(n + 1, 2);
        for (int i = 0; i < n + 1; i++) for (int j = 0; j < 2; j++) P.set(i, j, points.get(i).get(j));

        IMatrix r = B.nInvert().nMultiply(P);
        List<IVector> result = new ArrayList<>();
        for (int i = 0; i <= n; i++)
            result.add(new Vector(r.get(i, 0), r.get(i, 1)));
        return result;
    }
}
