package hr.fer.zemris.irg.raytracer.model;

import hr.fer.zemris.irg.math.vector.IVector;

public class Sphere extends SceneObject {

    IVector center;
    double radius;

    @Override
    public void updateIntersection(Intersection intersection, IVector start, IVector d) {

    }

    @Override
    public IVector getNormalInPoint(IVector point) {
        return null;
    }
}
