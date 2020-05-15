package hr.fer.zemris.irg.raytracer.model;

import hr.fer.zemris.irg.math.vector.IVector;
import hr.fer.zemris.irg.math.vector.Vector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;

public class RTScene {

    IVector eye;
    IVector view;
    IVector viewUp;

    double h;
    double xAngle;
    double yAngle;
    Vector gaIntensity = new Vector(0, 0, 0);
    List<Light> sources = new ArrayList<>();
    List<SceneObject> sceneObjects = new ArrayList<>();

    IVector xAxis;
    IVector yAxis;

    double l;
    double r;
    double b;
    double t;

    private void computeKS() {

    }

    public static RTScene loadScene(Path path) throws IOException {
        RTScene rtScene = new RTScene();
        for (String line : Files.readAllLines(path)) {
            String[] splitted = line.trim().split("\\s+");
            switch (splitted[0]) {
                case "#":
                    continue;
                case "e":
                    rtScene.eye = new Vector(parseDouble(splitted[1]), parseDouble(splitted[2]), parseDouble(splitted[3]));
                    break;
                case "v":
                    rtScene.view = new Vector(parseDouble(splitted[1]), parseDouble(splitted[2]), parseDouble(splitted[3]));
                    break;
                case "vu":
                    rtScene.viewUp = new Vector(parseDouble(splitted[1]), parseDouble(splitted[2]), parseDouble(splitted[3]));
                    break;
                case "h":
                    rtScene.h = parseDouble(splitted[1]);
                    break;
                case "xa":
                    rtScene.xAngle = parseDouble(splitted[1]);
                    break;
                case "ya":
                    rtScene.yAngle = parseDouble(splitted[1]);
                    break;
                case "ga":
                    rtScene.gaIntensity = new Vector(parseDouble(splitted[1]), parseDouble(splitted[2]), parseDouble(splitted[3]));
                    break;
                case "i":
                    rtScene.sources.add(new Light(new Vector(parseDouble(splitted[1]), parseDouble(splitted[2]), parseDouble(splitted[3])), new Vector(parseDouble(splitted[4]), parseDouble(splitted[5]), parseDouble(splitted[6]))));
                    break;
                case "o": {
                    if (splitted[1].equals("p")) {
                        Patch patch = new Patch();
                        patch.center = new Vector(parseDouble(splitted[2]), parseDouble(splitted[3]), parseDouble(splitted[4]));
                        patch.v1 = new Vector(parseDouble(splitted[5]), parseDouble(splitted[6]), parseDouble(splitted[7]));
                        patch.v2 = new Vector(parseDouble(splitted[8]), parseDouble(splitted[9]), parseDouble(splitted[10]));
                        patch.w = parseDouble(splitted[11]);
                        patch.h = parseDouble(splitted[12]);
                        patch.ambFrontRGB = new Vector(parseDouble(splitted[13]), parseDouble(splitted[14]), parseDouble(splitted[15]));
                        patch.difFrontRGB = new Vector(parseDouble(splitted[16]), parseDouble(splitted[17]), parseDouble(splitted[18]));
                        patch.refFrontRGB = new Vector(parseDouble(splitted[19]), parseDouble(splitted[20]), parseDouble(splitted[21]));
                        patch.fn = parseDouble(splitted[22]);
                        patch.fkRef = parseDouble(splitted[23]);
                        patch.ambBackRGB = new Vector(parseDouble(splitted[24]), parseDouble(splitted[25]), parseDouble(splitted[26]));
                        patch.difBackRGB = new Vector(parseDouble(splitted[27]), parseDouble(splitted[28]), parseDouble(splitted[29]));
                        patch.refBackRGB = new Vector(parseDouble(splitted[30]), parseDouble(splitted[31]), parseDouble(splitted[32]));
                        patch.bn = parseDouble(splitted[33]);
                        patch.bkRef = parseDouble(splitted[34]);
                        rtScene.sceneObjects.add(patch);
                    } else if (splitted[1].equals("s")) {
                        Sphere sphere = new Sphere();
                        sphere.center = new Vector(parseDouble(splitted[2]), parseDouble(splitted[3]), parseDouble(splitted[4]));
                        sphere.radius = parseDouble(splitted[5]);
                        sphere.ambFrontRGB = new Vector(parseDouble(splitted[6]), parseDouble(splitted[7]), parseDouble(splitted[8]));
                        sphere.difFrontRGB = new Vector(parseDouble(splitted[9]), parseDouble(splitted[10]), parseDouble(splitted[11]));
                        sphere.refFrontRGB = new Vector(parseDouble(splitted[12]), parseDouble(splitted[13]), parseDouble(splitted[14]));
                        sphere.fn = parseDouble(splitted[15]);
                        sphere.fkRef = parseDouble(splitted[16]);

                        rtScene.sceneObjects.add(sphere);
                    }
                }
            }
        }
        return rtScene;
    }
}
