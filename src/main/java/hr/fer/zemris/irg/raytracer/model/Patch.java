package hr.fer.zemris.irg.raytracer.model;

import hr.fer.zemris.irg.math.vector.IVector;

public class Patch extends SceneObject {


    IVector center;
    IVector v1;
    IVector v2;
    IVector normal;

    double w, h;


    @Override
    public void updateIntersection(Intersection intersection, IVector start, IVector d) {

    }

    @Override
    public IVector getNormalInPoint(IVector point) {
        return null;
    }
}
