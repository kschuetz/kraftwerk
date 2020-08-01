package visualization;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.Vector;
import dev.marksman.kraftwerk.Generator;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.style.CategoryStyler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class HistogramGenerator<A> implements ChartGenerator {
    private final String title;
    private final Generator<A> generator;
    private final int bucketCount;
    private final Fn1<A, Integer> getBucket;
    private final int sampleCount;

    public HistogramGenerator(String title, Generator<A> generator, int bucketCount, Fn1<A, Integer> getBucket, int sampleCount) {
        this.title = title;
        this.generator = generator;
        this.bucketCount = bucketCount;
        this.getBucket = getBucket;
        this.sampleCount = sampleCount;
    }

    public static <A> HistogramGenerator<A> histogram(Generator<A> generator,
                                                      int bucketCount,
                                                      Fn1<A, Integer> getBucket) {
        return new HistogramGenerator<>(generator.getLabel().orElse("untitled"),
                generator, bucketCount, getBucket, 100000);
    }

    public CategoryChart run() {
        CategoryChart chart = new CategoryChartBuilder()
                .width(800)
                .height(600)
                .title(title)
                .yAxisTitle("Frequency")
                .build();

        CategoryStyler styler = chart.getStyler();
        styler.setLegendVisible(false);
        styler.setHasAnnotations(false);
        styler.setXAxisTicksVisible(false);
        styler.setPlotGridLinesVisible(false);

        Integer[] frequencies = new Integer[bucketCount];

        generator
                .run()
                .take(sampleCount)
                .forEach(i -> {
                    int bucket = getBucket.apply(i);
                    if (bucket >= 0 && bucket < bucketCount) {
                        frequencies[bucket] = frequencies[bucket] == null ? 0 : frequencies[bucket] + 1;
                    }
                });

        ArrayList<Integer> xs = Vector.range(bucketCount).toCollection(ArrayList::new);
        List<Integer> ys = Arrays.asList(frequencies);

        chart.addSeries("frequency", xs, ys);

        return chart;
    }

    public String getTitle() {
        return this.title;
    }

    public Generator<A> getGenerator() {
        return this.generator;
    }

    public int getBucketCount() {
        return this.bucketCount;
    }

    public Fn1<A, Integer> getGetBucket() {
        return this.getBucket;
    }

    public int getSampleCount() {
        return this.sampleCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HistogramGenerator<?> that = (HistogramGenerator<?>) o;

        if (bucketCount != that.bucketCount) return false;
        if (sampleCount != that.sampleCount) return false;
        if (!title.equals(that.title)) return false;
        if (!generator.equals(that.generator)) return false;
        return getBucket.equals(that.getBucket);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + generator.hashCode();
        result = 31 * result + bucketCount;
        result = 31 * result + getBucket.hashCode();
        result = 31 * result + sampleCount;
        return result;
    }

    @Override
    public String toString() {
        return "HistogramGenerator{" +
                "title='" + title + '\'' +
                ", generator=" + generator +
                ", bucketCount=" + bucketCount +
                ", getBucket=" + getBucket +
                ", sampleCount=" + sampleCount +
                '}';
    }

    public HistogramGenerator<A> withTitle(String title) {
        return this.title.equals(title) ? this : new HistogramGenerator<>(title, this.generator, this.bucketCount, this.getBucket, this.sampleCount);
    }

    public HistogramGenerator<A> withGenerator(Generator<A> generator) {
        return this.generator == generator ? this : new HistogramGenerator<>(this.title, generator, this.bucketCount, this.getBucket, this.sampleCount);
    }

    public HistogramGenerator<A> withBucketCount(int bucketCount) {
        return this.bucketCount == bucketCount ? this : new HistogramGenerator<>(this.title, this.generator, bucketCount, this.getBucket, this.sampleCount);
    }

    public HistogramGenerator<A> withGetBucket(Fn1<A, Integer> getBucket) {
        return this.getBucket == getBucket ? this : new HistogramGenerator<>(this.title, this.generator, this.bucketCount, getBucket, this.sampleCount);
    }

    public HistogramGenerator<A> withSampleCount(int sampleCount) {
        return this.sampleCount == sampleCount ? this : new HistogramGenerator<>(this.title, this.generator, this.bucketCount, this.getBucket, sampleCount);
    }
}
