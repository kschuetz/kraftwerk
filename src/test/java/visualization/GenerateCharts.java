package visualization;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.Generators;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static dev.marksman.kraftwerk.frequency.FrequencyMap.frequencyMap;
import static visualization.ChartSuite.chartSuite;
import static visualization.HistogramGenerator.histogram;

public class GenerateCharts {

    public static void main(String[] args) {
        Path outputDir = Paths.get("target", "charts");
        outputDir.toFile().mkdirs();

        Fn1<ChartSuite, ChartSuite> builder =
                primitives()
                        .fmap(freqMaps());

        builder.apply(chartSuite(outputDir))
                .run()
                .unsafePerformIO();

    }

    private static Fn1<ChartSuite, ChartSuite> primitives() {
        return cs -> cs
                .add("int1", histogram(Generators.generateInt(0, 255), 256, id()))
                .add("double", histogram(Generators.generateDouble(), 256, n -> (int) (n * 256)));
    }

    private static Fn1<ChartSuite, ChartSuite> freqMaps() {
        return cs -> cs
                .add("freqmap1", histogram(frequencyMap(1, 0)
                        .addValue(2, 1)
                        .addValue(3, 2)
                        .addValue(4, 3)
                        .addValue(5, 4)
                        .addValue(6, 5)
                        .addValue(7, 6)
                        .addValue(8, 7)
                        .addValue(9, 8)
                        .addValue(10, 9)
                        .toGenerator(), 10, id()));
    }

}
