package hr.fer.zemris.irg;

import hr.fer.zemris.irg.objects.ObjectModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ObjectModelerShell {

    private static final String STANDARD_PATH_TO_RESOURCES = "src/main/resources/";
    private static final String IN_FILE = STANDARD_PATH_TO_RESOURCES + "kocka.obj";

    public static void main(String[] args) throws IOException {

        ObjectModel objectModel = ObjectModel.parse(Files.readAllLines(Path.of(IN_FILE)));

        var results = objectModel.getFaceCoefficients();

        objectModel.normalize();
        System.out.println(objectModel.dumpToOBJ());

        System.out.println(results);

    }
}
