package hr.fer.zemris.irg.math;

import hr.fer.zemris.irg.math.vector.IVector;
import hr.fer.zemris.irg.math.vector.Vector;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class BaricentricKords {

    /**
     * Insert this "1 0 0 5 0 0 3 8 0"
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        List<Double> numbers = Arrays.stream(line.trim().split("\\s+")).map(Double::parseDouble).collect(Collectors.toList());

        IVector a = new Vector(numbers.get(0), numbers.get(1), numbers.get(2));
        IVector b = new Vector(numbers.get(3), numbers.get(4), numbers.get(5));
        IVector c = new Vector(numbers.get(6), numbers.get(7), numbers.get(8));

        IVector t = Vector.parseSimple("3 4 0");
        double pov = b.nSub(a).nVectorProduct(c.nSub(a)).norm() / 2.0;
        double povA = b.nSub(t).nVectorProduct(c.nSub(t)).norm() / 2.0;
        double povB = a.nSub(t).nVectorProduct(c.nSub(t)).norm() / 2.0;
        double povC = a.nSub(t).nVectorProduct(b.nSub(t)).norm() / 2.0;

        double t1 = povA / pov;
        double t2 = povB / pov;
        double t3 = povC / pov;

        System.out.println(" Baricentricne koordinate su : (" + t1 + "," + t2 + "," + t3 + "). ");
    }
}
