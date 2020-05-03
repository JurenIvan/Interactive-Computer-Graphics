package hr.fer.zemris.irg.math;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BinomialCoef {

    HashMap<Integer, List<Integer>> cache = new HashMap<>();

    public BinomialCoef(int n) {
        for (int i = 0; i < n; i++) computeFactors(i);
    }

    public List<Integer> computeFactors(int n) {
        if (cache.containsKey(n)) return cache.get(n);

        List<Integer> factors = new ArrayList<>();
        int a = 1;

        for (int i = 1; i <= n + 1; i++) {
            factors.add(a);
            a = a * (n - i + 1) / i;
        }
        cache.put(n, List.of(factors.toArray(new Integer[0])));
        return factors;
    }

}
