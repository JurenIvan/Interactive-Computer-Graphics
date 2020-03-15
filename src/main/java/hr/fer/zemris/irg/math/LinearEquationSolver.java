package hr.fer.zemris.irg.math;

import hr.fer.zemris.irg.math.matrix.Matrix;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class LinearEquationSolver {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        List<Double> numbers = Arrays.stream(line.trim().split("\\s+")).map(Double::parseDouble).collect(Collectors.toList());
        Matrix a = new Matrix(3, 3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                a.set(i, j, numbers.get(i * 4 + j));
            }
        }
        Matrix b = new Matrix(3, 1);
        for (int i = 0; i < 3; i++) {
            b.set(i, 0, numbers.get(3 + i * 4));
        }

        System.out.println(a.nInvert().nMultiply(b));
    }
}
