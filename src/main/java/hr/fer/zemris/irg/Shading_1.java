package hr.fer.zemris.irg;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import hr.fer.zemris.irg.math.vector.IVector;
import hr.fer.zemris.irg.objects.ObjectModel;
import hr.fer.zemris.irg.objects.Vertex3D;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2.*;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import static java.awt.BorderLayout.CENTER;
import static java.awt.event.KeyEvent.*;
import static java.lang.Math.atan;
import static java.lang.Math.sin;

public class Shading_1 extends JFrame {

    private static final String STANDARD_PATH_TO_RESOURCES = "src/main/resources/";

    static {
        GLProfile.initSingleton();
    }

    private double angle = atan(1 / 3f);
    private double increment = 1;
    private double r = 1 / sin(angle);

    private final GLCanvas glCanvas = new GLCanvas(new GLCapabilities(GLProfile.getDefault()));
    private final ObjectModel objectModel;

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(STANDARD_PATH_TO_RESOURCES + args[0]));
        SwingUtilities.invokeLater(() -> new Shading_1(lines));
    }

    public Shading_1(List<String> lines) {
        initLookVariables();
        objectModel = ObjectModel.parse(lines);
        objectModel.normalize();

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
                GLU glu = GLU.createGLU(gl2);

                gl2.glClearColor(0, 1, 0, 0);
                gl2.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                gl2.glPushMatrix();

                gl2.glFrontFace(GL_CCW);        //this is default

                gl2.glEnable(GL_DEPTH_TEST);
                gl2.glPolygonMode(GL_FRONT, GL_FILL);
                gl2.glEnable(GL_CULL_FACE);
                gl2.glCullFace(GL_BACK);


                double x = r * Math.cos(Math.toRadians(angle));
                double y = 4;
                double z = r * Math.sin(Math.toRadians(angle));

                glu.gluLookAt(x, y, z, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);

                initLightningAndMaterials(gl2);
                for (var face : objectModel.getFaces()) {
                    gl2.glBegin(GL_POLYGON);

                    for (Vertex3D vertice : objectModel.getVerticesOfFace(face)) {
                        IVector normalForVertex = vertice.getNormal();
                        gl2.glNormal3d(normalForVertex.get(0), normalForVertex.get(1), normalForVertex.get(2));
                        gl2.glVertex3d(vertice.getX(), vertice.getY(), vertice.getZ());
                    }
                    gl2.glEnd();
                }
                gl2.glPopMatrix();
            }

            private void initLightningAndMaterials(GL2 gl2) {
                gl2.glEnable(GL_LIGHTING);
                gl2.glShadeModel(GL_SMOOTH);
                gl2.glEnable(GL_LIGHT0);

                gl2.glLightModelfv(GL_LIGHT_MODEL_AMBIENT, new float[]{0, 0, 0, 1}, 0);
                gl2.glLightfv(GL_LIGHT0, GL_POSITION, new float[]{4.0f, 5.0f, 3.0f, 1.0f}, 0);
                gl2.glLightfv(GL_LIGHT0, GL_AMBIENT, new float[]{0.2f, 0.2f, 0.2f, 1}, 0);
                gl2.glLightfv(GL_LIGHT0, GL_DIFFUSE, new float[]{0.8f, 0.8f, 0, 1}, 0);
                gl2.glLightfv(GL_LIGHT0, GL_SPECULAR, new float[]{0, 0, 0, 1}, 0);

                gl2.glMaterialfv(GL_FRONT, GL_AMBIENT, new float[]{1, 1, 1, 1}, 0);
                gl2.glMaterialfv(GL_FRONT, GL_DIFFUSE, new float[]{1, 1, 1, 1}, 0);
                gl2.glMaterialfv(GL_FRONT, GL_SPECULAR, new float[]{0.01f, 0.01f, 0.01f, 1}, 0);
                gl2.glMaterialf(GL_FRONT, GL_SHININESS, 96);   //prihvatljivo: od 0 do 128
            }

            @Override
            public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
                GL2 gl2 = glAutoDrawable.getGL().getGL2();
                gl2.glMatrixMode(GL_PROJECTION);
                gl2.glLoadIdentity();
                gl2.glFrustum(-0.5, 0.5, -0.5, 0.5, 1.0, 100.0);
                gl2.glMatrixMode(GL_MODELVIEW);
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
        angle = Math.toDegrees(atan(1 / 3.0));
        r = 1 / sin(Math.toRadians(angle));
    }
}
