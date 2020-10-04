package examples.tutorial;

import static dev.marksman.kraftwerk.Generators.generateAlphaChar;
import static dev.marksman.kraftwerk.Generators.generateSize;

public class SizedExample1 {
    public static void main(String[] args) {
        generateSize()
                .flatMap(size -> generateAlphaChar().vectorOfSize(size))
                .run()
                .take(10)
                .forEach(System.out::println);

        //sample output:
        //Vector(n, P, e)
        //Vector(w, f, o, W, i, t, y, v, p, i, K, O)
        //Vector(q, W, X, Q, D, S, T, M, D, l, P, p, E, O, K)
        //Vector(b, c, H, N, H, m, I, K)
        //Vector(m, J, Q, F, I, d, Z, C, b)
        //Vector(Q)
        //Vector(d, i, U, o, M, T)
        //Vector(F, S, I, R, s, l, t, V)
        //Vector(Q, V)
        //Vector(B, p, Q)
    }
}
