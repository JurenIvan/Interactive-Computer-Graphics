package hr.fer.zemris.irg;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import hr.fer.zemris.irg.math.vector.Vector;
import hr.fer.zemris.irg.shapes.Point;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2.GL_PROJECTION;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static hr.fer.zemris.irg.color.Color.*;
import static java.awt.BorderLayout.CENTER;
import static java.awt.event.KeyEvent.*;

public class PolygonDrawer extends JFrame {

    static {
        GLProfile.initSingleton();
    }

    private final GLCanvas glCanvas = new GLCanvas(new GLCapabilities(GLProfile.getDefault()));
    private List<Point> points = new ArrayList<>();
    private Point pointing;

    private boolean fill = false;
    private boolean convexity = false;
    private boolean isItInState = false;
    private boolean kurac = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PolygonDrawer::new);
    }

    public PolygonDrawer() {
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

        statesAndFlagsListeners();
        addNewTriangleListener();

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
                if (convexity) gl2.glClearColor(1, 1, 1, 1);
                else gl2.glClearColor(GREEN.getR(), GREEN.getG(), GREEN.getB(), 1);
                gl2.glClear(GL_COLOR_BUFFER_BIT);

                if (!fill) drawLines(gl2);
                else drawPolyGone(gl2);

                gl2.glLoadIdentity();
            }

            private void drawPolyGone(GL2 gl2) {
                var lista = new ArrayList<>(points);
                if (pointing != null) lista.add(pointing);
                gl2.glBegin(GL_POINTS);
                gl2.glColor3f(RED.getR(), RED.getG(), RED.getB());

                int ymin;
                int xmin = ymin = Integer.MAX_VALUE;
                int xmax;
                int ymax = xmax = Integer.MIN_VALUE;

                for (Point point : lista) {
                    if (ymin > point.getY()) ymin = point.getY();
                    if (xmin > point.getX()) xmin = point.getX();
                    if (ymax < point.getY()) ymax = point.getY();
                    if (xmax < point.getX()) xmax = point.getX();
                }

                for (int i = xmin; i < xmax; i++) {
                    for (int j = ymin; j < ymax; j++) {
                        if (checker(lista, i, j)) {
                            gl2.glVertex2i(i, j);
                        }
                    }
                }

                gl2.glEnd();
            }


            private void drawLines(GL2 gl2) {
                gl2.glBegin(GL_LINE_LOOP);
                gl2.glColor3f(BLACK.getR(), BLACK.getG(), BLACK.getB());
                for (var point : points)
                    gl2.glVertex2i(point.getX(), point.getY());
                if (pointing != null)
                    gl2.glVertex2i(pointing.getX(), pointing.getY());
                gl2.glEnd();
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

    private void addNewTriangleListener() {
        glCanvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isItInState) {
                    if (points.size() == 0) {
                        points.add(new Point(e.getX(), e.getY()));
                        pointing = new Point(e.getX(), e.getY());
                        return;
                    }
                    if (convexity && !convexityCheck(points, e.getX(), e.getY())) return;

                    if (pointing != null) points.add(pointing);
                    pointing = new Point(e.getX(), e.getY());

                    glCanvas.display();
                } else
                    convexityCheck(points, e.getX(), e.getY());
            }
        });

        glCanvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (pointing != null) {
                    pointing.setX(e.getX());
                    pointing.setY(e.getY());
                    glCanvas.display();
                }
            }
        });
    }

    private void statesAndFlagsListeners() {
        glCanvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case VK_K:
                        if (isItInState) {
                            System.out.println("Not possible in this state");
                            break;
                        }
                        if (convexity) {
                            convexity = false;
                            break;
                        }
                        if (convexityCheck(points)) {
                            System.out.println("Not possible, current polygone not convex");
                            break;
                        }
                        convexity = true;
                        break;
                    case VK_P:
                        if (!isItInState) fill = !fill;
                        else System.out.println("Not possible in this state");
                        break;
                    case VK_N:
                        pointing = null;
                        if (isItInState) points.clear();
                        isItInState = !isItInState;
                        break;
                }
                glCanvas.display();
            }
        });
    }

    private boolean convexityCheck(List<Point> points) {
        boolean isInside = checker(points, points.get(0).getX(), points.get(0).getY());
        System.out.println("polygon" + (isInside ? " IS " : " IS NOT ") + "convex.");
        return isInside;
    }

    private boolean convexityCheck(List<Point> points, int x, int y) {
        boolean isInside = checker(points, x, y);
        System.out.println("Point x=" + x + ", y=" + y + (isInside ? " IS " : " IS NOT ") + "inside of polygon");
        return isInside;
    }

    private boolean checker(List<Point> points, int x, int y) {
        double direction = 0;

        for (int i = 0; i < points.size(); i++) {
            Point first = points.get(i);
            Point second = points.get((i + 1) % points.size());

            Vector edge = new Vector(second.getX() - first.getX(), second.getY() - first.getY(), 0);
            Vector connect = new Vector(x - second.getX(), y - second.getY(), 0);

            double product = connect.nVectorProduct(edge).get(2);

            if (direction < 0 && product > 0 || direction > 0 && product < 0)
                return false;
            direction = product != 0 ? product : direction;
        }
        return true;
    }
}
