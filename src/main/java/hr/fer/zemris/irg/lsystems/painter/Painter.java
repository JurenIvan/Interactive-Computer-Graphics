package hr.fer.zemris.irg.lsystems.painter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Painter {

    List<PainterLine> lines = new ArrayList<>();

    public void drawLine(double x, double y, double x1, double y1, Color color, float efectiveLenght, double penSize) {
        lines.add(new PainterLine(x, y, x1, y1, color, efectiveLenght, penSize));
    }

    public void reset() {
        lines.clear();
    }

    public List<PainterLine> getLines() {
        return lines;
    }
}
