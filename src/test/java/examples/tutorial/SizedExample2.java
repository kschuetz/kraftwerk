package examples.tutorial;

import static dev.marksman.kraftwerk.Generators.generateAlphaChar;
import static dev.marksman.kraftwerk.Generators.sized;

public class SizedExample2 {
    public static void main(String[] args) {
        sized(size -> generateAlphaChar().vectorOfSize(size))
                .run()
                .take(10)
                .forEach(System.out::println);

        // sample output:
        //Vector(E, t, z, h, G, Q, V, A, P, q, X)
        //Vector(d, W, E, y, p, F, l, N, u, Q, k, C)
        //Vector(d, x, e, C, Z, W, m, t, T, F, J, L, G, Q)
        //Vector(d, C)
        //Vector(G, p, I, T, q, c, X, l, R, w, v, e, m, G, F)
        //Vector(r, f, B, m, v, I)
        //Vector(r, q, o)
        //Vector(x, Q, N, b)
        //Vector(c, A, f, h, J, H, O, T, A, E, o, E, p, B)
        //Vector(z, N, P, F, X, B, B, q, u, U)
    }
}
