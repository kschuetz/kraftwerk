package visualization;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.Generators;
import dev.marksman.kraftwerk.constraints.IntRange;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static dev.marksman.kraftwerk.Distributions.linearRampDown;
import static dev.marksman.kraftwerk.Distributions.linearRampUp;
import static dev.marksman.kraftwerk.Distributions.triangularInt;
import static dev.marksman.kraftwerk.Generators.generateBoolean;
import static dev.marksman.kraftwerk.Generators.generateByte;
import static dev.marksman.kraftwerk.Generators.generateGaussian;
import static dev.marksman.kraftwerk.Generators.generateInt;
import static dev.marksman.kraftwerk.Generators.generateShort;
import static dev.marksman.kraftwerk.Weighted.weighted;
import static dev.marksman.kraftwerk.frequency.FrequencyMap.frequencyMapValue;
import static visualization.ChartSuite.chartSuite;
import static visualization.HistogramGenerator.histogram;

public class GenerateCharts {

    public static void main(String[] args) {
        Path outputDir = Paths.get("target", "charts");
        outputDir.toFile().mkdirs();

        Fn1<ChartSuite, ChartSuite> builder =
                primitives()
                        .fmap(freqMaps())
                        .fmap(distributions());

        builder.apply(chartSuite(outputDir))
                .run()
                .unsafePerformIO();

    }

    private static Fn1<ChartSuite, ChartSuite> primitives() {
        return cs -> cs
                .add("int-byte0", histogram(generateInt(), 256, n -> n & 255))
                .add("int-byte1", histogram(generateInt(), 256, n -> (n >> 8) & 255))
                .add("int-byte2", histogram(generateInt(), 256, n -> (n >> 16) & 255))
                .add("int-byte3", histogram(generateInt(), 256, n -> (n >> 24) & 255))
                .add("int-inclusive", histogram(generateInt(IntRange.from(0).to(255)), 256, id()))
                .add("byte", histogram(generateByte(), 256, n -> 128 + n))
                .add("short", histogram(generateShort(), 256, n -> (n >> 8) & 255))
                .add("boolean", histogram(generateBoolean(), 2, b -> b ? 0 : 1))
                .add("double", histogram(Generators.generateDoubleFractional(), 256, n -> (int) (n * 256)))
                .add("float", histogram(Generators.generateFloatFractional(), 256, n -> (int) (n * 256)))
                .add("gaussian", histogram(generateGaussian(), 512, n -> 256 + (int) (90 * n)));
    }

    private static Fn1<ChartSuite, ChartSuite> freqMaps() {
        return cs -> cs
                .add("freqmap1", histogram(frequencyMapValue(weighted(1, 0))
                        .addValue(weighted(2, 1))
                        .addValue(weighted(3, 2))
                        .addValue(weighted(4, 3))
                        .addValue(weighted(5, 4))
                        .addValue(weighted(6, 5))
                        .addValue(weighted(7, 6))
                        .addValue(weighted(8, 7))
                        .addValue(weighted(9, 8))
                        .addValue(weighted(10, 9))
                        .toGenerator(), 10, id()));
    }

    private static Fn1<ChartSuite, ChartSuite> distributions() {
        return cs -> cs
                .add("linear-ramp-up-int", histogram(linearRampUp(generateInt(IntRange.from(0).to(255))), 256, id()))
                .add("linear-ramp-down-int", histogram(linearRampDown(generateInt(IntRange.from(0).to(255))), 256, id()))
                .add("linear-ramp-up-down-int", histogram(linearRampUp(linearRampDown(generateInt(IntRange.from(0).to(255)))), 256, id()))
                .add("triangular-int", histogram(triangularInt(generateInt(IntRange.from(0).to(255))), 256, id()));
    }
}
