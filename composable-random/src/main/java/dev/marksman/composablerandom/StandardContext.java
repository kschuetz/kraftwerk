package dev.marksman.composablerandom;

import lombok.AllArgsConstructor;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Head.head;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Uncons.uncons;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static java.util.Collections.emptyList;

@AllArgsConstructor
public class StandardContext implements Context {
    private static final StandardContext DEFAULT_CONTEXT = context(emptyList());

    private final Iterable<SizeParameters> sizeParametersStack;

    @Override
    public SizeParameters getSizeParameters() {
        return head(sizeParametersStack).orElse(SizeParameters.noSizeLimits());
    }

    @Override
    public Context withSizeParameters(SizeParameters sizeParameters) {
        return new StandardContext(cons(sizeParameters, sizeParametersStack));
    }

    @Override
    public Context restoreSizeParameters() {
        return new StandardContext(uncons(sizeParametersStack)
                .orElseThrow(() -> new IllegalStateException("SizeParameters stack is empty"))._2());
    }

    private static StandardContext context(Iterable<SizeParameters> sizeStack) {
        return new StandardContext(sizeStack);
    }

    public static StandardContext defaultContext() {
        return DEFAULT_CONTEXT;
    }

}
