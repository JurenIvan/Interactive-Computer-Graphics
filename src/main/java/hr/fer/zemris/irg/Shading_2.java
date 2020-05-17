package hr.fer.zemris.irg;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import hr.fer.zemris.irg.math.IRG;
import hr.fer.zemris.irg.math.matrix.IMatrix;
import hr.fer.zemris.irg.math.vector.IVector;
import hr.fer.zemris.irg.math.vector.Vector;
import hr.fer.zemris.irg.objects.Face3D;
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
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2.GL_POLYGON;
import static com.jogamp.opengl.GL2GL3.GL_FILL;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import static hr.fer.zemris.irg.math.IRG.buildFrustumMatrix;
import static hr.fer.zemris.irg.math.IRG.lookAtMatrix;
import static java.awt.BorderLayout.CENTER;
import static java.awt.event.KeyEvent.*;
import static java.lang.Math.*;
import static java.util.stream.IntStream.range;

public class Shading_2 extends JFrame {

    private static final String STANDARD_PATH_TO_RESOURCES = "src/main/resources/";
    private BiFunction<Face3D, IVector, Boolean> preProjectionTester = (e, k) -> true;
    private Predicate<List<IVector>> postProjectionTester = e -> true;
    private boolean smoothColoring = false;

    private final IVector amb = new Vector(new double[]{0.2, 0.2, 0.2}, true, true);
    private final IVector dif = new Vector(new double[]{0.8, 0.8, 0}, true, true);
    private final IVector ref = new Vector(new double[]{0, 0, 0}, true, true);

    private final IVector matAmb = new Vector(new double[]{1, 1, 1}, true, true);
    private final IVector matDif = new Vector(new double[]{1, 1, 1}, true, true);
    private final IVector matRef = new Vector(new double[]{0.01, 0.01, 0.01}, true, true);
    private final int shininess = 96;

    private final IVector light = new Vector(new double[]{4, 5, 3}, true, true);

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
        SwingUtilities.invokeLater(() -> new Shading_2(lines));
    }

    public Shading_2(List<String> lines) {
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

                gl2.glClearColor(0, 1, 0, 0);
                gl2.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                gl2.glPushMatrix();

                gl2.glFrontFace(GL_CCW);        //this is default

                gl2.glEnable(GL_DEPTH_TEST);
                gl2.glPolygonMode(GL_FRONT, GL_FILL);
                gl2.glShadeModel(GL_SMOOTH);

                double x = r * cos(toRadians(angle));
                double y = 4;
                double z = r * sin(toRadians(angle));

                IVector eye = new Vector(x, y, z);
                IMatrix lookAtMatrix = lookAtMatrix(eye, new Vector(0, 0, 0), new Vector(0, 1, 0));
                IMatrix frustumMatrix = buildFrustumMatrix(-0.5, 0.5, -0.5, 0.5, 1, 100);
                IMatrix m = lookAtMatrix.nMultiply(frustumMatrix);

                List<IVector> edges = new ArrayList<>(3);
                for (var face : objectModel.getFaces().stream().filter(e -> preProjectionTester.apply(e, eye)).collect(Collectors.toList())) {
                    List<Vertex3D> vertices = objectModel.getVerticesOfFace(face);
                    for (int i = 0, verticesSize = vertices.size(); i < verticesSize; i++) {
                        Vertex3D vertex3D = vertices.get(i);
                        IVector wrapped = new Vector(vertex3D.getX(), vertex3D.getY(), vertex3D.getZ(), 1);
                        IVector projected = wrapped.toRowMatrix(false).nMultiply(m).toVector(false).nFromHomogeneus();
                        edges.add(projected);
                    }

                    if (postProjectionTester.test(edges)) {
                        gl2.glBegin(GL_POLYGON);
                        if (!smoothColoring) {
                            double[] rgb = calc(objectModel.getCentralForFace(face), objectModel.getFaceCoefficients(face).getNorm(), eye);
                            gl2.glColor3d(rgb[0], rgb[1], rgb[2]);
                            edges.forEach(e -> gl2.glVertex3d(e.get(0), e.get(1),e.get(2)));
                        } else {
                            for (int i = 0; i < edges.size(); i++) {
                                IVector e = edges.get(i);
                                double[] rgb = calc(vertices.get(i).getCords(), vertices.get(i).getNormal(), eye);
                                gl2.glColor3d(rgb[0], rgb[1], rgb[2]);
                                gl2.glVertex3d(e.get(0), e.get(1), e.get(2));
                            }
                        }
                        gl2.glEnd();
                    }
                    edges.clear();
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

    private static IVector helperVectorMultiply(IVector v1, IVector v2) {
        return new Vector(range(0, v1.getDimension()).mapToDouble(i -> v1.get(i) * v2.get(i)).toArray(), false, false);
    }

    private double[] calc(double[] point, IVector norm, IVector eye) {
        norm = norm.nNormalize();
        IVector ambComponent = helperVectorMultiply(amb, matAmb);

        IVector l = light.nSub(new Vector(point)).normalize();
        IVector difComponent = helperVectorMultiply(dif, matDif).scalarMultiply(max(l.scalarProduct(norm), 0));

        IVector d = new Vector(point).sub(light);
        IVector r = d.nSub(norm.scalarMultiply(2 * d.scalarProduct(norm)));

        IVector refComponent = helperVectorMultiply(ref, matRef).scalarMultiply(pow(r.scalarProduct(eye.nSub(new Vector(point))), shininess));

        return ambComponent
                .add(difComponent)
                .add(refComponent)
                .toArray();
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
                    case VK_NUMPAD1:
                        preProjectionTester = objectModel::algorithm1;
                        postProjectionTester = points -> true;
                        break;
                    case VK_NUMPAD2:
                        preProjectionTester = objectModel::algorithm2;
                        postProjectionTester = points -> true;
                        break;
                    case VK_NUMPAD3:
                        preProjectionTester = (face, eye) -> true;
                        postProjectionTester = IRG::isAntiClockwise;
                        break;
                    case VK_NUMPAD4:
                        preProjectionTester = (face, eye) -> true;
                        postProjectionTester = points -> true;
                        break;
                    case VK_K:
                        smoothColoring = false;
                        break;
                    case VK_S:
                        smoothColoring = true;
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
        r = 1 / sin(toRadians(angle));
    }

    private void initLookVariables() {
        increment = 1;
        angle = toDegrees(atan(1 / 3.50));
        r = 1 / sin(toRadians(angle));
    }
}
