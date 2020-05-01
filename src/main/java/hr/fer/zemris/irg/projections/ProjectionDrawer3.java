package hr.fer.zemris.irg.projections;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import hr.fer.zemris.irg.math.matrix.IMatrix;
import hr.fer.zemris.irg.math.vector.IVector;
import hr.fer.zemris.irg.math.vector.Vector;
import hr.fer.zemris.irg.objects.ObjectModel;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_LINE_LOOP;
import static com.jogamp.opengl.GL2.GL_PROJECTION;
import static hr.fer.zemris.irg.color.Color.RED;
import static hr.fer.zemris.irg.math.IRG.buildFrustumMatrix;
import static hr.fer.zemris.irg.math.IRG.lookAtMatrix;
import static java.awt.BorderLayout.CENTER;
import static java.awt.event.KeyEvent.*;
import static java.lang.Math.atan;
import static java.lang.Math.sin;

public class ProjectionDrawer3 extends JFrame {

    private static final String STANDARD_PATH_TO_RESOURCES = "src/main/resources/";

    static {
        GLProfile.initSingleton();
    }

    private double angle = atan(1 / 3f);
    private double increment = 1;
    private double r = 1 / sin(angle);

    private final GLCanvas glCanvas = new GLCanvas(new GLCapabilities(GLProfile.getDefault()));
    private ObjectModel objectModel;

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(STANDARD_PATH_TO_RESOURCES + args[0]));
        SwingUtilities.invokeLater(() -> new ProjectionDrawer3(lines));
    }

    public ProjectionDrawer3(List<String> lines) {
        initLookVariables();
        objectModel = ObjectModel.parse(lines);
        objectModel.normalize();

        setVisible(true);
        setSize(640, 480);
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

        addKeyBoardListeners();


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

                gl2.glClearColor(0, 1, 0, 0);
                gl2.glClear(GL_COLOR_BUFFER_BIT);
                gl2.glPushMatrix();

                double x = r * Math.cos(Math.toRadians(angle));
                double y = 4;
                double z = r * Math.sin(Math.toRadians(angle));

                IMatrix lookAtMatrix = lookAtMatrix(new Vector(x, y, z), new Vector(0, 0, 0), new Vector(0, 1, 0));
                IMatrix frustumMatrix = buildFrustumMatrix(-0.5, 0.5, -0.5, 0.5, 1, 100);
                IMatrix m = lookAtMatrix.nMultiply(frustumMatrix);

                for (var face : objectModel.getFaces()) {
                    gl2.glBegin(GL_LINE_LOOP);
                    gl2.glColor3i(RED.getR(), RED.getG(), RED.getB());
                    for (var vertices : objectModel.getVerticesOfFace(face)) {
                        IVector wrapped = new Vector(vertices.getX(), vertices.getY(), vertices.getZ(), 1);
                        IVector projected = wrapped.toRowMatrix(false).nMultiply(m).toVector(false).nFromHomogeneus();
                        gl2.glVertex2f((float) projected.get(0), (float) projected.get(1));
                    }
                    gl2.glEnd();
                }
            }

            @Override
            public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
                GL2 gl2 = glAutoDrawable.getGL().getGL2();
                gl2.glMatrixMode(GL_PROJECTION);
                gl2.glLoadIdentity();
                gl2.glViewport(0, 0, width, height);
            }
        });
    }

    private void addKeyBoardListeners() {
        glCanvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case VK_R:
                        calculateLookVariables(increment);
                        break;
                    case VK_L:
                        calculateLookVariables(-increment);
                        break;
                    case VK_ESCAPE:
                        initLookVariables();
                        break;
                    default:
                        System.out.println("Unsupported key");
                }
                glCanvas.display();
            }
        });
    }

    private void calculateLookVariables(double change) {
        angle += change;
        r = 1 / sin(Math.toRadians(angle));
    }

    private void initLookVariables() {
        increment = 1;
        angle = Math.toDegrees(atan(1 / 3.50));
        r = 1 / sin(Math.toRadians(angle));
    }
}
