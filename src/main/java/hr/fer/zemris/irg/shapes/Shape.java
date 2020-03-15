package hr.fer.zemris.irg.shapes;

import hr.fer.zemris.irg.color.Color;

public abstract class Shape {

    protected Color color;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Shape(Color color) {
        this.color = color;
    }
}
