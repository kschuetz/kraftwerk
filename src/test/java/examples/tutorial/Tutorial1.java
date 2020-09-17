package examples.tutorial;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;
import dev.marksman.kraftwerk.Seed;
import dev.marksman.kraftwerk.constraints.IntRange;

import java.time.LocalDate;

public class Tutorial1 {
    public static class IntegerExample {
        public static void main(String[] args) {
            Generators.generateInt()
                    .run()
                    .take(5)
                    .forEach(System.out::println);
        }
    }

    public static class IntegerWithinRangeExample {
        public static void main(String[] args) {
            Generators.generateInt(IntRange.from(1).to(100))
                    .run()
                    .take(5)
                    .forEach(System.out::println);
        }
    }

    public static class InitialSeedExample {
        public static void main(String[] args) {
            Seed initialSeed = Seed.create(123456L);
            Generators.generateInt(IntRange.from(1).to(100))
                    .run(initialSeed)
                    .take(5)
                    .forEach(System.out::println);
        }
    }

    public static class MappingExample {
        public static void main(String[] args) {
            Generators.generateInt(IntRange.from(0).to(100))
                    .fmap(n -> n * 1000)
                    .run()
                    .take(5)
                    .forEach(System.out::println);

        }
    }

    public static class MappingToADifferentType {
        public static void main(String[] args) {
            Generators.generateInt(IntRange.from(0).to(100))
                    .fmap(n -> LocalDate.of(2020, 1, 1).plusDays(n))
                    .run()
                    .take(5)
                    .forEach(System.out::println);

        }
    }

    public static class CombiningTwoGenerators {
        public static void main(String[] args) {
            Generator<Tuple2<Integer, String>> generator = Generators.tupled(Generators.generateInt(),
                    Generators.generateString());

            generator.run()
                    .take(5)
                    .forEach(System.out::println);

        }
    }

    public static class CombiningThreeGenerators {
        public static void main(String[] args) {
            Generator<Tuple3<Integer, String, Double>> generator = Generators.tupled(Generators.generateInt(),
                    Generators.generateString(),
                    Generators.generateDoubleFractional());

            generator.run()
                    .take(5)
                    .forEach(System.out::println);
        }
    }

    public static class CustomProductTypes {
        public static void main(String[] args) {
            Generator<Integer> component = Generators.generateInt(IntRange.inclusive(0, 255));
            Generator<RGB> generateRGB = Generators.product(component, component, component, RGB::new);

            generateRGB.run()
                    .take(5)
                    .forEach(System.out::println);
        }

        public static class RGB {
            private final int red;
            private final int green;
            private final int blue;

            public RGB(int red, int green, int blue) {
                this.red = red;
                this.green = green;
                this.blue = blue;
            }

            public int getRed() {
                return red;
            }

            public int getGreen() {
                return green;
            }

            public int getBlue() {
                return blue;
            }

            @Override
            public String toString() {
                return "RGB{" +
                        "red=" + red +
                        ", green=" + green +
                        ", blue=" + blue +
                        '}';
            }
        }
    }
}
