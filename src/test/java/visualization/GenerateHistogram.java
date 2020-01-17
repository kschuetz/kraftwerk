package visualization;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.Vector;
import dev.marksman.kraftwerk.Generator;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.Wither;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Value
@Wither
@AllArgsConstructor
public class GenerateHistogram<A> {
    private final String title;
    private final Generator<A> generator;
    private final int bucketCount;
    private final Fn1<A, Integer> getBucket;
    private final int sampleCount;

    public CategoryChart run() {
        CategoryChart chart = new CategoryChartBuilder()
                .width(800)
                .height(600)
                .title(title)
                .yAxisTitle("Frequency")
                .build();

        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setHasAnnotations(false);
        chart.getStyler().setXAxisTicksVisible(false);

        Integer[] frequencies = new Integer[bucketCount];

        generator
                .run()
                .take(100000)
                .forEach(i -> {
                    int bucket = getBucket.apply(i);
                    frequencies[bucket] = frequencies[bucket] == null ? 0 : frequencies[bucket] + 1;
                });

        ArrayList<Integer> xs = Vector.range(bucketCount).toCollection(ArrayList::new);
        List<Integer> ys = Arrays.asList(frequencies);

        // Series
        chart.addSeries("frequency", xs, ys);

        return chart;
    }

    public static <A> GenerateHistogram<A> generateHistogram(Generator<A> generator,
                                                             int bucketCount,
                                                             Fn1<A, Integer> getBucket) {
        return new GenerateHistogram<>(generator.getLabel().orElse("untitled"),
                generator, bucketCount, getBucket, 100000);
    }

}
