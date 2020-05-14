package hr.fer.zemris.irg;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import hr.fer.zemris.lsystems.LSystemBuilderImpl;
import hr.fer.zemris.lsystems.painter.Painter;
import hr.fer.zemris.lsystems.painter.PainterLine;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.jogamp.opengl.GL.GL_LINE_STRIP;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import static java.awt.BorderLayout.CENTER;

/**
 * Class that contains main method so it can be run and demonstrates
 * functionalities of project when commands for L-systems are given directly as
 * method calls.
 *
 * @author juren
 */
public class LSystems extends JFrame {

    static {
        GLProfile.initSingleton();
    }

    private final GLCanvas glCanvas = new GLCanvas(new GLCapabilities(GLProfile.getDefault()));
    private LSystemBuilder lSystemBuilder = new LSystemBuilderImpl();
    private Painter painter = new Painter();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LSystems::new);
    }

    public LSystems() {
        setVisible(true);
        setSize(1200, 800);
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
        LSystem lSystem = createKochCurve2();
        lSystem.draw(13, painter);

        glCanvas.requestFocusInWindow();
        addGLEventListener();
        glCanvas.display();
    }

    private static LSystem createKochCurve2() {
        String[] data = ("origin                 0.5 0.0\n" +
                "angle                  90\n" +
                "unitLength             0.75\n" +
                "unitLengthDegreeScaler 1.0 /2.05\n" +
                "\n" +
                "command F draw 1\n" +
                "command + rotate 20\n" +
                "command - rotate -20\n" +
                "command [ push\n" +
                "command ] pop\n" +
                "command G color 00FF00\n" +
                "\n" +
                "axiom GB\n" +
                "\n" +
                "production B F[+B]F[-B]+B\n" +
                "production F FF\n").split("\n");
        return new LSystemBuilderImpl().configureFromText(data).build();
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
                gl2.glClear(GL.GL_COLOR_BUFFER_BIT);

                gl2.glLoadIdentity();

                for (PainterLine line : painter.getLines()) {
                    var color = line.getColor();
                    gl2.glColor3d(color.getRed(), color.getGreen(), color.getBlue());
                    gl2.glBegin(GL_LINE_STRIP);
                    gl2.glLineWidth((float) line.getPenSize());
                    gl2.glVertex2d(line.getX() * glAutoDrawable.getSurfaceWidth(), line.getY() * glAutoDrawable.getSurfaceHeight());
                    gl2.glVertex2d(line.getX1() * glAutoDrawable.getSurfaceWidth(), line.getY1() * glAutoDrawable.getSurfaceHeight());
                    gl2.glEnd();
                }

            }

            @Override
            public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
                GL2 gl2 = glAutoDrawable.getGL().getGL2();
                gl2.glMatrixMode(GL_PROJECTION);
                gl2.glLoadIdentity();

                GLU glu = new GLU();
                glu.gluOrtho2D(0, width, height, 0);

                gl2.glViewport(0, 0, width, height);
                gl2.glMatrixMode(GL_MODELVIEW);
                gl2.glLoadIdentity();
            }
        });
    }


}
