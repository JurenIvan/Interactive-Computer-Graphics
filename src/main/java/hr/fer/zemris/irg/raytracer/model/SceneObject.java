package hr.fer.zemris.irg.raytracer.model;

import hr.fer.zemris.irg.math.vector.IVector;

public abstract class SceneObject {

    IVector ambFrontRGB, ambBackRGB;
    IVector difFrontRGB, difBackRGB;
    IVector refFrontRGB, refBackRGB;

    double fn, fkRef, bn, bkRef;

    public abstract void updateIntersection(Intersection intersection, IVector start, IVector d);

    public abstract IVector getNormalInPoint(IVector point);

}
