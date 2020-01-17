package visualization;

import dev.marksman.collectionviews.Vector;
import dev.marksman.kraftwerk.Generators;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.style.Styler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sandbox {

    public static void main(String[] args) throws IOException {

        File chartsDir = new File("target/charts");
        chartsDir.mkdirs();

        CategoryChart chart = getChart();
        BitmapEncoder.saveBitmap(chart, "target/charts/kraftwerk-test.png", BitmapEncoder.BitmapFormat.PNG);

//        new SwingWrapper<CategoryChart>(chart).displayChart();
    }

    private static CategoryChart getChart() {

        // Create Chart
        CategoryChart chart = new CategoryChartBuilder()
                .width(800)
                .height(600)
                .xAxisTitle("Bucket")
                .yAxisTitle("Frequency")
                .build();

        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setHasAnnotations(false);
        chart.getStyler().setXAxisTicksVisible(false);

        Integer[] frequencies = new Integer[256];

        Generators.generateInt(0, 255)
                .run()
                .take(100000)
                .forEach(i -> {
                    frequencies[i] = frequencies[i] == null ? 0 : frequencies[i] + 1;
                });

        ArrayList<Integer> xs = Vector.range(256).toCollection(ArrayList::new);
        List<Integer> ys = Arrays.asList(frequencies);

        // Series
        chart.addSeries("test 1", xs, ys);

        return chart;
    }
}
