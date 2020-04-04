package hr.fer.zemris.irg;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import hr.fer.zemris.irg.drawers.LineDrawer;
import hr.fer.zemris.irg.drawers.PolyDrawer;
import hr.fer.zemris.irg.shapes.Line;
import hr.fer.zemris.irg.shapes.Point;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL2.GL_PROJECTION;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static hr.fer.zemris.irg.color.Color.BLACK;
import static hr.fer.zemris.irg.color.Color.GREEN;
import static hr.fer.zemris.irg.shapes.Polygon.*;
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
    private boolean mySpecialState = false;

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

                if (convexity) gl2.glClearColor(GREEN.getR(), GREEN.getG(), GREEN.getB(), 1);
                else gl2.glClearColor(1, 1, 1, 1);
                gl2.glClear(GL_COLOR_BUFFER_BIT);

                if (mySpecialState) paintAcceptableStates(gl2, points, pointing);

                if (!fill) drawLines(gl2);
                if (fill) drawPolygon(gl2);

                gl2.glLoadIdentity();
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

    private void drawPolygon(GL2 gl2) {
        if (points.size() == 0) return;
        if (points.size() == 1) {
            Line line = new Line(points.get(0), pointing, BLACK);
            LineDrawer.drawLine(gl2, line);
            return;
        }
        var list = new ArrayList<>(points);
        if (pointing != null) list.add(pointing);
        PolyDrawer.fillPoly(list, gl2);
    }


    private void drawLines(GL2 gl2) {
        List<Point> pointsExtended = new ArrayList<>(points);
        if (pointing != null) pointsExtended.add(pointing);
        PolyDrawer.encirclePoly(pointsExtended, gl2);
    }


    private void addNewTriangleListener() {
        glCanvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isItInState) {
                    if (convexity && !willPolygonRemainConvex(points, e.getX(), e.getY())) return;

                    if (points.size() == 0) {
                        points.add(new Point(e.getX(), e.getY()));
                        pointing = new Point(e.getX(), e.getY());
                        return;
                    }

                    if (pointing != null) points.add(pointing);
                    pointing = new Point(e.getX(), e.getY());

                    glCanvas.display();
                } else {
                    System.out.println("Point x=" + e.getX() + ", y=" + e.getY() + (isPointInsideOfPolygone(points, e.getX(), e.getY()) ? " IS " : " IS NOT ") + "inside of polygon");
                }
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
                        if (!isPolygonConvex(points)) {
                            System.out.println("Not possible, current polygone not convex");
                            break;
                        }
                        convexity = !convexity;
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

                    case VK_S:
                        mySpecialState = !mySpecialState;
                }
                glCanvas.display();
            }
        });
    }


}
