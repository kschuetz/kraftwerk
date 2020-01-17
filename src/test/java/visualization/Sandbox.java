package visualization;

import com.jnape.palatable.lambda.functions.builtin.fn1.Id;
import dev.marksman.kraftwerk.Generators;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;

import java.io.File;
import java.io.IOException;

import static visualization.GenerateHistogram.generateHistogram;

public class Sandbox {

    public static void main(String[] args) throws IOException {

        File chartsDir = new File("target/charts");
        chartsDir.mkdirs();

        CategoryChart chart = generateHistogram(Generators.generateInt(0, 255), 256, Id.id()).run();
        BitmapEncoder.saveBitmap(chart, "target/charts/kraftwerk-test2.png", BitmapEncoder.BitmapFormat.PNG);

        CategoryChart chart2 = generateHistogram(Generators.generateDouble(), 256, n -> (int) (n * 256)).run();
        BitmapEncoder.saveBitmap(chart2, "target/charts/double.png", BitmapEncoder.BitmapFormat.PNG);
    }


}
