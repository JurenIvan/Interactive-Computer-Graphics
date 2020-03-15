package hr.fer.zemris.irg;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import hr.fer.zemris.irg.color.ColorSupplier;
import hr.fer.zemris.irg.shapes.Line;
import hr.fer.zemris.irg.shapes.Point;
import hr.fer.zemris.irg.shapes.Quad;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL2.GL_PROJECTION;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static hr.fer.zemris.irg.color.Color.*;
import static hr.fer.zemris.irg.drawers.LineDrawer.drawLine;
import static hr.fer.zemris.irg.drawers.LineDrawer.drawSegment;
import static hr.fer.zemris.irg.drawers.QuadDrawers.outLineQuad;
import static java.awt.BorderLayout.CENTER;
import static java.awt.event.KeyEvent.*;
import static java.util.List.of;

public class BresenhamLines extends JFrame {
    static {
        GLProfile.initSingleton();
    }

    private final GLCanvas glCanvas = new GLCanvas(new GLCapabilities(GLProfile.getDefault()));
    private final ColorSupplier colorSupplier = new ColorSupplier();
    private final List<Line> linesList = new ArrayList<>();
    private final Point lowerLeft = new Point(glCanvas.getSurfaceWidth() / 4, glCanvas.getSurfaceHeight() / 4);
    private final Point upperRight = new Point(glCanvas.getSurfaceWidth() * 3 / 4, glCanvas.getSurfaceHeight() * 3 / 4);

    private Point start;
    private Point pointing;

    private boolean control = true;
    private boolean cutOff = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BresenhamLines::new);
    }

    public BresenhamLines() {
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
        addControlTriggerListener();
        addCutOffTriggerListener();
        addNewLineListener();

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
                gl2.glClearColor(1, 1, 1, 1);
                gl2.glClear(GL_COLOR_BUFFER_BIT);
                drawScene(gl2);

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

                lowerLeft.setX(width / 4);
                lowerLeft.setY(height / 4);
                upperRight.setX(width * 3 / 4);
                upperRight.setY(height * 3 / 4);
            }

            private void drawScene(GL2 gl2) {
                if (cutOff)
                    outLineQuad(gl2, getHalfOfScreen(), GREEN);

                for (Line line : linesList) {
                    if (cutOff) drawSegment(gl2, line, lowerLeft, upperRight);
                    else drawLine(gl2, line);

                    if (control) drawLine(gl2, line.copy().translate(4), RED);
                }

                if (start != null) {
                    if (cutOff) drawSegment(gl2, new Line(start, pointing, colorSupplier.get()), lowerLeft, upperRight);
                    else drawLine(gl2, new Line(start, pointing, colorSupplier.get()));

                    if (control)
                        drawLine(gl2, new Line(start, pointing, RED).translate(4));
                }
            }
        });
    }

    private Quad getHalfOfScreen() {
        int qWidth = glCanvas.getSurfaceWidth() / 4;
        int qHeight = glCanvas.getSurfaceHeight() / 4;
        return new Quad(of(
                new Point(qWidth, qHeight),
                new Point(qWidth * 3, qHeight),
                new Point(qWidth * 3, qHeight * 3),
                new Point(qWidth, qHeight * 3)
        ));
    }

    private void addCutOffTriggerListener() {
        glCanvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_K) cutOff = !cutOff;
            }
        });
    }

    private void addControlTriggerListener() {
        glCanvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_O) control = !control;
            }
        });
    }

    private void addNewLineListener() {
        glCanvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point newPoint = new Point(e.getX(), e.getY());
                if (start == null) {
                    start = newPoint;
                    pointing = new Point(e.getX(), e.getY());
                } else {
                    linesList.add(new Line(start, pointing, colorSupplier.get()));
                    start = null;
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
                if (e.getKeyCode() == VK_R) colorSupplier.set(RED);
                else if (e.getKeyCode() == VK_G) colorSupplier.set(GREEN);
                else if (e.getKeyCode() == VK_B) colorSupplier.set(BLUE);
                glCanvas.display();
            }
        });
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
