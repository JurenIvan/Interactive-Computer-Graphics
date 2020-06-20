package hr.fer.zemris.irg.drawers;

import com.jogamp.opengl.GL2;
import hr.fer.zemris.irg.color.Color;
import hr.fer.zemris.irg.shapes.Line;
import hr.fer.zemris.irg.shapes.Point;

import java.util.Optional;

import static com.jogamp.opengl.GL.GL_POINTS;

public class LineDrawer {

    public static final int BIT_MASK_T = 0b1000;
    public static final int BIT_MASK_B = 0b0100;
    public static final int BIT_MASK_R = 0b0010;
    public static final int BIT_MASK_L = 0b0001;

    public static void drawSegment(GL2 gl2, Line line, Point lowerLeftBorder, Point upperRightBorder) {
        var segment = segment(line, lowerLeftBorder, upperRightBorder);
        segment.ifPresent(segmentOfLine -> drawLine(gl2, segmentOfLine));
    }


    public static void drawLine(GL2 gl2, Line line) {
        drawLine(gl2, line.getStart(), line.getEnd(), line.getColor());
    }

    public static void drawLine(GL2 gl2, Line line, Color color) {
        drawLine(gl2, line.getStart(), line.getEnd(), color);
    }

    public static void drawLine(GL2 gl2, Point pointStart, Point pointEnd, Color color) {
        gl2.glBegin(GL_POINTS);
        gl2.glColor3f(color.getR(), color.getG(), color.getB());

        if (Math.abs(pointEnd.getY() - pointStart.getY()) < Math.abs(pointEnd.getX() - pointStart.getX())) {
            if (pointStart.getX() > pointEnd.getX())
                plotLineForEachX(gl2, pointEnd.getX(), pointEnd.getY(), pointStart.getX(), pointStart.getY());
            else
                plotLineForEachX(gl2, pointStart.getX(), pointStart.getY(), pointEnd.getX(), pointEnd.getY());
        } else {
            if (pointStart.getY() > pointEnd.getY())
                plotLineForEachY(gl2, pointEnd.getX(), pointEnd.getY(), pointStart.getX(), pointStart.getY());
            else
                plotLineForEachY(gl2, pointStart.getX(), pointStart.getY(), pointEnd.getX(), pointEnd.getY());
        }
        gl2.glEnd();
    }

    private static void plotLineForEachX(GL2 gl2, int x0, int y0, int x1, int y1) {
        int dx = x1 - x0;
        int dy = y1 - y0;
        int yi = 1;
        if (dy < 0) {
            yi = -1;
            dy = -dy;
        }
        int d = 2 * dy - dx;
        int y = y0;

        for (int x = x0; x < x1; x++) {
            gl2.glVertex2i(x, y);
            if (d > 0) {
                y = y + yi;
                d = d - 2 * dx;
            }
            d = d + 2 * dy;
        }
    }

    private static void plotLineForEachY(GL2 gl2, int x0, int y0, int x1, int y1) {
        int dx = x1 - x0;
        int dy = y1 - y0;
        int xi = 1;
        if (dx < 0) {
            xi = -1;
            dx = -dx;
        }
        int D = 2 * dx - dy;
        int x = x0;

        for (int y = y0; y < y1; y++) {
            gl2.glVertex2i(x, y);
            if (D > 0) {
                x = x + xi;
                D = D - 2 * dy;
            }
            D = D + 2 * dx;
        }
    }


    public static Optional<Line> segment(Line line, Point lowerLeftBorder, Point upperRightBorder) {

        int c0 = checkTBRL(line.getStart(), lowerLeftBorder.getX(), lowerLeftBorder.getY(), upperRightBorder.getX(), upperRightBorder.getY());
        int c1 = checkTBRL(line.getEnd(), lowerLeftBorder.getX(), lowerLeftBorder.getY(), upperRightBorder.getX(), upperRightBorder.getY());

        if ((c0 | c1) == 0) {
            return Optional.of(line);
        }

        if ((c0 & c1) != 0) {
            return Optional.empty();
        }

        return Optional.of(new Line(
                calculateNewPc0(line.getStart().copy(), line.getEnd().copy(), lowerLeftBorder, upperRightBorder),
                calculateNewPc0(line.getEnd().copy(), line.getStart().copy(), lowerLeftBorder, upperRightBorder),
                line.getColor()));
    }

    private static Point calculateNewPc0(Point point1, Point point2, Point lowerLeftBorder, Point upperRightBorder) {
        double x0 = point1.getX();
        double y0 = point1.getY();
        double y1 = point2.getY();
        double x1 = point2.getX();

        double yMin = lowerLeftBorder.getY();
        double yMax = upperRightBorder.getY();
        double xMin = lowerLeftBorder.getX();
        double xMax = upperRightBorder.getX();
        double slope = (y1 - y0) / (x1 - x0);

        int c = checkTBRL(point1, lowerLeftBorder.getX(), lowerLeftBorder.getY(), upperRightBorder.getX(), upperRightBorder.getY());

        if ((c & 0b1000) != 0) {
            x0 = (x0 + (yMin - y0) / slope);
            y0 = yMin;
            c = updatePoint(point1, x0, y0, lowerLeftBorder, upperRightBorder);
        }

        if ((c & 0b0100) != 0) {
            x0 = (x0 + (yMax - y0) / slope);
            y0 = yMax;
            c = updatePoint(point1, x0, y0, lowerLeftBorder, upperRightBorder);
        }

        if ((c & 0b0010) != 0) {
            y0 = (y0 + slope * (xMax - x0));
            x0 = xMax;
            c = updatePoint(point1, x0, y0, lowerLeftBorder, upperRightBorder);
        }

        if ((c & 0b0001) != 0) {
            y0 = (y0 + slope * (xMin - x0));
            x0 = xMin;
            c = updatePoint(point1, x0, y0, lowerLeftBorder, upperRightBorder);
        }

        return point1;
    }

    private static int updatePoint(Point point, double x, double y, Point lowerLeftBorder, Point upperRightBorder) {
        point.setX((int) x);
        point.setY((int) y);
        return checkTBRL(point, lowerLeftBorder.getX(), lowerLeftBorder.getY(), upperRightBorder.getX(), upperRightBorder.getY());
    }

    private static int checkTBRL(Point point, int xMin, int yMin, int xMax, int yMax) {
        int result = 0;
        if (point.getY() > yMax) result |= BIT_MASK_B;
        if (point.getY() < yMin) result |= BIT_MASK_T;
        if (point.getX() > xMax) result |= BIT_MASK_R;
        if (point.getX() < xMin) result |= BIT_MASK_L;
        return result;
    }

}
