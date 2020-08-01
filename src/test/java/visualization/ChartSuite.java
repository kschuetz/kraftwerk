package visualization;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.builtin.fn2.Sequence;
import com.jnape.palatable.lambda.io.IO;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.internal.chartpart.Chart;

import java.nio.file.Files;
import java.nio.file.Path;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static dev.marksman.enhancediterables.ImmutableFiniteIterable.emptyImmutableFiniteIterable;

public class ChartSuite {
    private final ImmutableFiniteIterable<Tuple2<ChartGenerator, String>> items;
    private final Path outputDir;

    private ChartSuite(ImmutableFiniteIterable<Tuple2<ChartGenerator, String>> items, Path outputDir) {
        this.items = items;
        this.outputDir = outputDir;
    }

    public static ChartSuite chartSuite(Path outputDir) {
        return new ChartSuite(emptyImmutableFiniteIterable(), outputDir);
    }

    public ChartSuite add(String fileBaseName, ChartGenerator chartGenerator) {
        return new ChartSuite(items.append(tuple(chartGenerator, fileBaseName)), outputDir);
    }

    private IO<Unit> generateChart(ChartGenerator chartGenerator, String fileBaseName) {
        Path outputFile = outputDir.resolve(fileBaseName);
        return IO.io(() -> {
            Chart<?, ?> run = chartGenerator.run();
            BitmapEncoder.saveBitmap(run, outputFile.toFile().getCanonicalPath(), BitmapEncoder.BitmapFormat.PNG);
        });
    }

    private IO<Unit> generateIndex() {
        StringBuilder output = new StringBuilder();
        output.append("<!DOCTYPE html>\n<html><head><meta charset='UTF-8'><title>Kraftwerk Charts</title></head><body>\n");
        items.forEach(t -> renderIndexSection(output, t._1(), t._2()));
        output.append("</body></html>\n");
        String content = output.toString();
        return IO.io(() -> {
            Files.write(outputDir.resolve("index.html"), content.getBytes());
            return UNIT;
        });
    }

    private void renderIndexSection(StringBuilder output, ChartGenerator chartGenerator, String fileBaseName) {
        output.append("<div><img src='")
                .append(fileBaseName)
                .append(".png'></div>\n");
    }

    public IO<Unit> run() {
        ImmutableFiniteIterable<IO<Unit>> fmap = items.fmap(into(this::generateChart))
                .append(generateIndex());
        IO<Iterable<Unit>> sequence = Sequence.sequence(fmap, IO::io);
        return sequence.fmap(__ -> UNIT);
    }

}
