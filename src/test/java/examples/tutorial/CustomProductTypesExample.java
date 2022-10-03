package examples.tutorial;

import software.kes.kraftwerk.Generator;
import software.kes.kraftwerk.constraints.IntRange;

import static software.kes.kraftwerk.Generators.generateInt;
import static software.kes.kraftwerk.Generators.generateProduct;

public class CustomProductTypesExample {
    public static void main(String[] args) {
        Generator<Integer> component = generateInt(IntRange.inclusive(0, 255));
        Generator<RGB> generateRGB = generateProduct(component, component, component, RGB::new);

        generateRGB.run()
                .take(5)
                .forEach(System.out::println);

        // sample output:
        // RGB{red=121, green=48, blue=174}
        // RGB{red=193, green=0, blue=18}
        // RGB{red=201, green=76, blue=22}
        // RGB{red=221, green=221, blue=118}
        // RGB{red=188, green=169, blue=66}
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
