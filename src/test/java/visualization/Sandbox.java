package visualization;

import com.jnape.palatable.lambda.functions.builtin.fn1.Id;
import dev.marksman.kraftwerk.Generators;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;

import java.io.File;
import java.io.IOException;

import static dev.marksman.kraftwerk.frequency.FrequencyMap.frequencyMap;
import static visualization.GenerateHistogram.generateHistogram;

public class Sandbox {

    public static void main(String[] args) throws IOException {

        File chartsDir = new File("target/charts");
        chartsDir.mkdirs();

        CategoryChart chart = generateHistogram(Generators.generateInt(0, 255), 256, Id.id()).run();
        BitmapEncoder.saveBitmap(chart, "target/charts/kraftwerk-test2.png", BitmapEncoder.BitmapFormat.PNG);

        CategoryChart chart2 = generateHistogram(Generators.generateDouble(), 256, n -> (int) (n * 256)).run();
        BitmapEncoder.saveBitmap(chart2, "target/charts/double.png", BitmapEncoder.BitmapFormat.PNG);

        CategoryChart chart3 = generateHistogram(frequencyMap(1, 0)
                .addValue(2, 1)
                .addValue(3, 2)
                .addValue(4, 3)
                .addValue(5, 4)
                .addValue(6, 5)
                .addValue(7, 6)
                .addValue(8, 7)
                .addValue(9, 8)
                .addValue(10, 9)
                .toGenerator(), 10, Id.id()).run();
        BitmapEncoder.saveBitmap(chart3, "target/charts/freqmap.png", BitmapEncoder.BitmapFormat.PNG);
    }

}
