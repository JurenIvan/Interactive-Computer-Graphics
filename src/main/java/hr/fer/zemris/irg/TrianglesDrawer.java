package hr.fer.zemris.irg;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import hr.fer.zemris.irg.color.ColorSupplier;
import hr.fer.zemris.irg.shapes.Point;
import hr.fer.zemris.irg.shapes.Quad;
import hr.fer.zemris.irg.shapes.Triangle;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL2.GL_PROJECTION;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static hr.fer.zemris.irg.drawers.QuadDrawers.drawSquare;
import static hr.fer.zemris.irg.drawers.TriangleDrawer.*;
import static hr.fer.zemris.irg.drawers.LineDrawer.drawLine;
import static java.awt.BorderLayout.CENTER;
import static java.awt.event.KeyEvent.VK_N;
import static java.awt.event.KeyEvent.VK_P;

public class TrianglesDrawer extends JFrame {
    static {
        GLProfile.initSingleton();
    }

    private final GLCanvas glCanvas = new GLCanvas(new GLCapabilities(GLProfile.getDefault()));
    private ColorSupplier colorSupplier = new ColorSupplier();
    private List<Triangle> triangleList = new ArrayList<>();

    private Point point1;
    private Point point2;
    private Point pointing;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TrianglesDrawer::new);
    }

    public TrianglesDrawer() {
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

        addColorSelectorListeners();
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
                gl2.glClearColor(1, 1, 1, 1);
                gl2.glClear(GL_COLOR_BUFFER_BIT);
                drawScene(gl2);
                gl2.glLoadIdentity();
            }

            private void drawScene(GL2 gl2) {
                for (Triangle triangle : triangleList)
                    drawTriangle(gl2, triangle);

                if (point2 != null)
                    drawTriangle(gl2, new Triangle(point1, point2, pointing, colorSupplier.get()));
                else if (point1 != null)
                    drawLine(gl2, point1, pointing, colorSupplier.get());

                drawSquare(gl2, getSquare(), colorSupplier.get());
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

    private Quad getSquare() {
        int width = glCanvas.getSurfaceWidth();
        return new Quad(
                new Point(width - 50, 25),
                new Point(width - 25, 25),
                new Point(width - 25, 50),
                new Point(width - 50, 50),
                colorSupplier.get());
    }

    private void addNewTriangleListener() {
        glCanvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point newPoint = new Point(e.getX(), e.getY());
                if (point1 == null) {
                    point1 = newPoint;
                    pointing = new Point(e.getX(), e.getY());
                } else if (point2 == null) point2 = newPoint;
                else {
                    triangleList.add(new Triangle(point1, point2, newPoint, colorSupplier.get()));
                    point1 = null;
                    point2 = null;
                    pointing = null;
                }
                glCanvas.display();
            }
        });

        glCanvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (pointing != null) {
                    pointing.setX(e.getX());
                    pointing.setY(e.getY());
                }
                glCanvas.display();
            }
        });
    }

    private void addColorSelectorListeners() {
        glCanvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == VK_P) {
                    colorSupplier.setPrevious();
                    glCanvas.display();
                } else if (e.getKeyCode() == VK_N) {
                    colorSupplier.setNext();
                    glCanvas.display();
                }
            }
        });
    }
}
