package hr.fer.zemris.irg.raytracer.model;

import hr.fer.zemris.irg.math.vector.IVector;

public class Light {

    private IVector position;
    private IVector rgb;

    public Light(IVector position, IVector rgb) {
        this.position = position;
        this.rgb = rgb;
    }

    public IVector getPosition() {
        return position;
    }

    public void setPosition(IVector position) {
        this.position = position;
    }

    public IVector getRgb() {
        return rgb;
    }
}
