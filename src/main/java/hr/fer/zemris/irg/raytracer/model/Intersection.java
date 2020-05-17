package hr.fer.zemris.irg.raytracer.model;

import hr.fer.zemris.irg.math.vector.IVector;

public class Intersection {
    SceneObject object; //closest object that ray intersects
    double lambda;  //for which lambda that happens
    boolean front;  //is it on front?
    IVector point;  //whats the point
}
