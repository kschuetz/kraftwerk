package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.monoid.builtin.Concat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.ArrayList;

import static com.jnape.palatable.lambda.functions.builtin.fn2.ToCollection.toCollection;
import static java.util.Collections.singletonList;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TracePrinter {

    // TODO: TracePrinter needs work
    private Iterable<String> renderLevel(int level, boolean isLast, Trace<?> current) {
        StringBuilder out = new StringBuilder();
        if (level > 0) {
            for (int i = 0; i < level - 1; i++) {
//                out.append(" │ ");
                out.append("   ");
            }
            if (isLast) {
                out.append(" └─");
            } else {
                out.append(" ├─");
            }
        }
        out.append('[');
        out.append(current.getMetadata().getLabel().orElse(" "));
        out.append("]: ");
        out.append(current.getResult().toString());
        String line = out.toString();
        ArrayList<Trace<?>> children = toCollection(ArrayList::new, current.getChildren());
        int childCount = children.size();
        int nextLevel = level + 1;
        Iterable<String> result = singletonList(line);
        for (int i = 0; i < childCount; i++) {
            result = Concat.concat(result, renderLevel(nextLevel, i == childCount - 1, children.get(i)));
        }
        return result;
    }

    public Iterable<String> render(Trace<?> trace) {
        return renderLevel(0, false, trace);
    }

    public static TracePrinter tracePrinter() {
        return new TracePrinter();
    }
}
