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
import static dev.marksman.kraftwerk.Generators.choiceBuilder;
import static dev.marksman.kraftwerk.Generators.generateBoolean;
import static dev.marksman.kraftwerk.Generators.generateByte;
import static dev.marksman.kraftwerk.Generators.generateGaussian;
import static dev.marksman.kraftwerk.Generators.generateInt;
import static dev.marksman.kraftwerk.Generators.generateShort;
import static dev.marksman.kraftwerk.Generators.generateUnit;
import static dev.marksman.kraftwerk.Weighted.weighted;
import static dev.marksman.kraftwerk.frequency.FrequencyMap.frequencyMapValue;
import static visualization.ChartSuite.chartSuite;
import static visualization.HistogramGenerator.histogram;

public class GenerateCharts {

    public static void main(String[] args) {
        Path outputDir = Paths.get("target", "charts");
        //noinspection ResultOfMethodCallIgnored
        outputDir.toFile().mkdirs();

        Fn1<ChartSuite, ChartSuite> builder =
                primitives()
                        .fmap(freqMaps())
                        .fmap(distributions())
                        .fmap(choiceBuilders());

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
                .add("double-with-NaNs", histogram(Generators.generateDoubleFractional().withNaNs()
                                .labeled("double with NaNs"), 2,
                        n -> Double.isNaN(n) ? 1 : 0))
                .add("float", histogram(Generators.generateFloatFractional(), 256, n -> (int) (n * 256)))
                .add("float-with-NaNs", histogram(Generators.generateFloatFractional().withNaNs()
                                .labeled("float with NaNs"), 2,
                        n -> Float.isNaN(n) ? 1 : 0))
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
                .add("linear-ramp-up-int", histogram(linearRampUp(generateInt(IntRange.from(0).to(255))).labeled("linear-ramp-up"), 256, id()))
                .add("linear-ramp-down-int", histogram(linearRampDown(generateInt(IntRange.from(0).to(255))).labeled("linear-ramp-down"), 256, id()))
                .add("linear-ramp-up-down-int", histogram(linearRampUp(linearRampDown(generateInt(IntRange.from(0).to(255)))).labeled("linear-ramp-up-down"), 256, id()))
                .add("triangular-int", histogram(triangularInt(generateInt(IntRange.from(0).to(255))).labeled("triangular"), 256, id()));
    }

    private static Fn1<ChartSuite, ChartSuite> choiceBuilders() {
        return cs -> cs
                .add("choiceBuilder2",
                        histogram(choiceBuilder(generateUnit().weighted(1))
                                        .or(generateUnit().weighted(2))
                                        .toGenerator()
                                        .labeled("choiceBuilder2"), 2,
                                c -> c.match(__ -> 0, __ -> 1)))
                .add("choiceBuilder3",
                        histogram(choiceBuilder(generateUnit().weighted(1))
                                        .or(generateUnit().weighted(2))
                                        .or(generateUnit().weighted(3))
                                        .toGenerator()
                                        .labeled("choiceBuilder3"), 3,
                                c -> c.match(__ -> 0, __ -> 1, __ -> 2)))
                .add("choiceBuilder4",
                        histogram(choiceBuilder(generateUnit().weighted(1))
                                        .or(generateUnit().weighted(2))
                                        .or(generateUnit().weighted(3))
                                        .or(generateUnit().weighted(4))
                                        .toGenerator()
                                        .labeled("choiceBuilder4"), 4,
                                c -> c.match(__ -> 0, __ -> 1, __ -> 2, __ -> 3)))
                .add("choiceBuilder5",
                        histogram(choiceBuilder(generateUnit().weighted(1))
                                        .or(generateUnit().weighted(2))
                                        .or(generateUnit().weighted(3))
                                        .or(generateUnit().weighted(4))
                                        .or(generateUnit().weighted(5))
                                        .toGenerator()
                                        .labeled("choiceBuilder5"), 5,
                                c -> c.match(__ -> 0, __ -> 1, __ -> 2, __ -> 3,
                                        __ -> 4)))
                .add("choiceBuilder6",
                        histogram(choiceBuilder(generateUnit().weighted(1))
                                        .or(generateUnit().weighted(2))
                                        .or(generateUnit().weighted(3))
                                        .or(generateUnit().weighted(4))
                                        .or(generateUnit().weighted(5))
                                        .or(generateUnit().weighted(6))
                                        .toGenerator()
                                        .labeled("choiceBuilder6"), 6,
                                c -> c.match(__ -> 0, __ -> 1, __ -> 2, __ -> 3,
                                        __ -> 4, __ -> 5)))
                .add("choiceBuilder7",
                        histogram(choiceBuilder(generateUnit().weighted(1))
                                        .or(generateUnit().weighted(2))
                                        .or(generateUnit().weighted(3))
                                        .or(generateUnit().weighted(4))
                                        .or(generateUnit().weighted(5))
                                        .or(generateUnit().weighted(6))
                                        .or(generateUnit().weighted(7))
                                        .toGenerator()
                                        .labeled("choiceBuilder7"), 7,
                                c -> c.match(__ -> 0, __ -> 1, __ -> 2, __ -> 3,
                                        __ -> 4, __ -> 5, __ -> 6)))
                .add("choiceBuilder8",
                        histogram(choiceBuilder(generateUnit().weighted(1))
                                        .or(generateUnit().weighted(2))
                                        .or(generateUnit().weighted(3))
                                        .or(generateUnit().weighted(4))
                                        .or(generateUnit().weighted(5))
                                        .or(generateUnit().weighted(6))
                                        .or(generateUnit().weighted(7))
                                        .or(generateUnit().weighted(8))
                                        .toGenerator()
                                        .labeled("choiceBuilder8"), 8,
                                c -> c.match(__ -> 0, __ -> 1, __ -> 2, __ -> 3,
                                        __ -> 4, __ -> 5, __ -> 6, __ -> 7)));
    }
}
