package hr.fer.zemris.irg;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import hr.fer.zemris.irg.mandelbrot.StackEntry;
import hr.fer.zemris.irg.mandelbrot.coloringfunc.BlackAndWhiteColoring;
import hr.fer.zemris.irg.mandelbrot.coloringfunc.ColorColoring;
import hr.fer.zemris.irg.mandelbrot.divergenefunc.CubicFunction;
import hr.fer.zemris.irg.mandelbrot.divergenefunc.QuadraticFunction;
import hr.fer.zemris.irg.mandelbrot.Complex;

import javax.swing.*;
import java.awt.event.*;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import static java.awt.BorderLayout.CENTER;
import static java.awt.event.KeyEvent.*;

public class Mandelbrot extends JFrame {

    static {
        GLProfile.initSingleton();
    }

    private static final int maxIterations = 128;
    private final GLCanvas glCanvas = new GLCanvas(new GLCapabilities(GLProfile.getDefault()));
    private final Stack<StackEntry> stack = new Stack<>();

    private Function<Complex, Short> divergeFunction = new QuadraticFunction(maxIterations);
    private Consumer<short[][]> coloringConsumer = new BlackAndWhiteColoring(glCanvas, maxIterations);
    private short[][] roots;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Mandelbrot::new);
    }

    public Mandelbrot() {
        setVisible(true);
        setSize(400, 300);
        stack.push(new StackEntry(-2, 1, -1.2, 1.2));
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
                if (roots != null)
                    coloringConsumer.accept(roots);
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
                refresh();
            }
        });
    }

    private Complex covertPointToComplex(int i, int j, StackEntry peek, int width, int height) {

        double re = peek.uMin + (peek.uMax - peek.uMin) / width * i;
        double im = peek.vMax - (peek.vMax - peek.vMin) / height * j;

        return new Complex(re, im);
    }

    private void addKeyboardListeners() {
        glCanvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == VK_B)
                    coloringConsumer = new BlackAndWhiteColoring(glCanvas, maxIterations);
                else if (e.getKeyCode() == VK_C)
                    coloringConsumer = new ColorColoring(glCanvas, maxIterations);
                else if (e.getKeyCode() == VK_1)
                    divergeFunction = new QuadraticFunction(maxIterations);
                else if (e.getKeyCode() == VK_2)
                    divergeFunction = new CubicFunction(maxIterations);
                else if (e.getKeyCode() == VK_ESCAPE)
                    stack.pop();
                else {
                    System.err.println("Key not supported");
                    return;
                }
                refresh();
            }
        });
    }

    private void addMouseListeners() {
        glCanvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                StackEntry stackEntry = new StackEntry(1, 1, 1, 1);
                stack.add(stackEntry);
                refresh();
            }
        });
    }

    private void refresh() {
        new Thread(() -> {
            roots = new short[glCanvas.getSurfaceHeight()][glCanvas.getSurfaceWidth()];

            int surfaceHeight = glCanvas.getSurfaceHeight();
            int surfaceWidth = glCanvas.getSurfaceWidth();

            for (int i = 0; i < surfaceHeight; i++)
                for (int j = 0; j < surfaceWidth; j++)
                    roots[i][j] = divergeFunction.apply(covertPointToComplex(i, j, stack.peek(), surfaceWidth, surfaceHeight));

            glCanvas.display();
        }).start();
    }
}