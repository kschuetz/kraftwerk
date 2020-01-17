package visualization;

import org.knowm.xchart.internal.chartpart.Chart;

public interface ChartGenerator {
    Chart<?, ?> run();

    String getTitle();
}
