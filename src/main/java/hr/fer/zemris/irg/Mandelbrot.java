package hr.fer.zemris.irg;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import hr.fer.zemris.irg.mandelbrot.Complex;
import hr.fer.zemris.irg.mandelbrot.PosaoIzracuna;
import hr.fer.zemris.irg.mandelbrot.StackEntry;
import hr.fer.zemris.irg.mandelbrot.coloringfunc.BlackAndWhiteColoring;
import hr.fer.zemris.irg.mandelbrot.coloringfunc.ColorColoring;
import hr.fer.zemris.irg.mandelbrot.coloringfunc.FunkyColoring;
import hr.fer.zemris.irg.mandelbrot.divergenefunc.CubicFunction;
import hr.fer.zemris.irg.mandelbrot.divergenefunc.QuadraticFunction;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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

    private static final int MAX_ITERATIONS = 16 * 16 * 16;
    private final GLCanvas glCanvas = new GLCanvas(new GLCapabilities(GLProfile.getDefault()));
    private final Stack<StackEntry> stack = new Stack<>();

    private Function<Complex, Short> divergeFunction = new QuadraticFunction(MAX_ITERATIONS);
    private Consumer<short[][]> coloringConsumer = new BlackAndWhiteColoring(glCanvas, MAX_ITERATIONS);

    private short[][] roots;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Mandelbrot::new);
    }

    public Mandelbrot() {

        setVisible(true);
        setSize(1200, 800);
        initStack();
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

                new Thread(multiThreadedRunnable).start();
            }
        });
    }

    private void addKeyboardListeners() {
        glCanvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == VK_B)
                    coloringConsumer = new BlackAndWhiteColoring(glCanvas, MAX_ITERATIONS);
                else if (e.getKeyCode() == VK_C)
                    coloringConsumer = new ColorColoring(glCanvas, MAX_ITERATIONS);
                else if (e.getKeyCode() == VK_F)
                    coloringConsumer = new FunkyColoring(glCanvas, MAX_ITERATIONS);
                else if (e.getKeyCode() == VK_1)
                    divergeFunction = new QuadraticFunction(MAX_ITERATIONS);
                else if (e.getKeyCode() == VK_2)
                    divergeFunction = new CubicFunction(MAX_ITERATIONS);
                else if (e.getKeyCode() == VK_X) {
                    if (stack.size() > 1)
                        stack.pop();        //todo
                    else System.err.println("Cannot go back any further");
                } else if (e.getKeyCode() == VK_ESCAPE) {
                    initStack();
                } else {
                    System.err.println("Key not supported");
                    return;
                }

                new Thread(multiThreadedRunnable).start();
            }
        });
    }

    private void addMouseListeners() {
        glCanvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                StackEntry curr = stack.peek();

                Complex c = covertPointToComplex(x, y, curr, glCanvas.getSurfaceWidth(), glCanvas.getHeight());
                double reR = curr.uMax - curr.uMin;
                double imR = curr.vMax - curr.vMin;

                StackEntry stackEntry = new StackEntry(c.getRe() - reR / 16, c.getRe() + reR / 16, c.getIm() - imR / 16, c.getIm() + imR / 16);
                stack.add(stackEntry);

                new Thread(multiThreadedRunnable).start();
            }
        });
    }

    public static Complex covertPointToComplex(int x, int y, StackEntry peek, int width, int height) {

        double re = peek.uMin + (peek.uMax - peek.uMin) / width * x;
        double im = peek.vMax - (peek.vMax - peek.vMin) / height * y;

        return new Complex(re, im);
    }

    private void initStack() {
        stack.clear();
        stack.push(new StackEntry(-2, 1, -1.2, 1.2));
    }

    private final Runnable singleThreadedRunnable = () -> {
        int surfaceWidth = glCanvas.getSurfaceWidth();
        int surfaceHeight = glCanvas.getSurfaceHeight();

        short[][] roots = new short[surfaceHeight][surfaceWidth];

        for (int y = 0; y < surfaceHeight; y++)
            for (int x = 0; x < surfaceWidth; x++)
                roots[y][x] = divergeFunction.apply(covertPointToComplex(x, y, stack.peek(), surfaceWidth, surfaceHeight));

        this.roots = roots;
        glCanvas.display();
    };

    private final Runnable multiThreadedRunnable = () -> {

        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), (r) -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });


        int width = glCanvas.getSurfaceWidth();
        int height = glCanvas.getSurfaceHeight();
        roots = new short[height][width];

        final int numberOfParallelTracks = 8 * Runtime.getRuntime().availableProcessors();
        int heightPerTrack = height / numberOfParallelTracks;

        List<Future<Void>> results = new ArrayList<>();

        for (int i = 0; i < numberOfParallelTracks; i++) {
            int yMin = i * heightPerTrack;
            int yMax = (i + 1) * heightPerTrack;
            if (i == numberOfParallelTracks - 1) {
                yMax = height - 1;
            }
            PosaoIzracuna job = new PosaoIzracuna(stack.peek(), width, height, yMin, yMax, MAX_ITERATIONS, roots, divergeFunction);
            results.add(pool.submit(job));
        }
        for (Future<Void> job : results) {
            try {
                job.get();
            } catch (InterruptedException | ExecutionException ignored) {
            }
        }
        glCanvas.display();
    };
}
