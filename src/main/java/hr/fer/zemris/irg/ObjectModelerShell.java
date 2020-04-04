package hr.fer.zemris.irg;

import hr.fer.zemris.irg.objects.FaceCoeficient;
import hr.fer.zemris.irg.objects.ObjectModel;
import hr.fer.zemris.irg.objects.Vertex3D;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Collectors;

import static hr.fer.zemris.irg.objects.Vertex3D.of;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.Double.parseDouble;

public class ObjectModelerShell {

    private static final String STANDARD_PATH_TO_RESOURCES = "src/main/resources/";

    public static void main(String[] args) throws IOException {
        if (args.length != 1)
            throw new IllegalArgumentException("One filename expected.");

        ObjectModel objectModel = ObjectModel.parse(Files.readAllLines(Paths.get(STANDARD_PATH_TO_RESOURCES + args[0])));

        System.out.println("Loaded file:" + args[0]);
        System.out.println(objectModel.getFaceCoefficients().stream().map(FaceCoeficient::toString).collect(Collectors.joining("\n")) + "\nInsert commands: (quit, normiraj, 1 2 3)\n");

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            switch (line.toLowerCase()) {
                case "quit":
                    return;
                case "normiraj":
                    System.out.println(objectModel.copy().normalize().dumpToOBJ());
                    break;
                default:
                    System.out.println(calculatePosition(objectModel, line));
            }
        }
    }

    private static String calculatePosition(ObjectModel objectModel, String line) {
        var lineSplitted = line.trim().split(" ");
        Vertex3D point = of(parseDouble(lineSplitted[0]), parseDouble(lineSplitted[1]), parseDouble(lineSplitted[2]));

        var result = objectModel.calculatePosition(point);

        if (result == TRUE) return point + " je UNUTAR tijela.";
        if (result == FALSE) return point + " je IZVAN tijela.";
        return point.toString() + " je NA OBODU tijela.";
    }
}
