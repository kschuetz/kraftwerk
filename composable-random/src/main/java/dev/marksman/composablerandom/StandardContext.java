package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.Unit;
import lombok.AllArgsConstructor;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Head.head;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Uncons.uncons;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static java.util.Collections.emptyList;

@AllArgsConstructor
public class StandardContext implements Context {
    private static final StandardContext DEFAULT_CONTEXT = context(emptyList(), emptyList());

    private final Iterable<SizeParameters> sizeParametersStack;
    private final Iterable<Object> applicationDataStack;

    @Override
    public SizeParameters getSizeParameters() {
        return head(sizeParametersStack).orElse(SizeParameters.noSizeLimits());
    }

    @Override
    public Object getApplicationData() {
        return head(applicationDataStack).orElse(Unit.UNIT);
    }

    @Override
    public Context withSizeParameters(SizeParameters sizeParameters) {
        return new StandardContext(cons(sizeParameters, sizeParametersStack), applicationDataStack);
    }

    @Override
    public Context withApplicationData(Object applicationData) {
        return new StandardContext(sizeParametersStack, cons(applicationData, applicationDataStack));
    }

    @Override
    public Context restoreSizeParameters() {
        return new StandardContext(uncons(sizeParametersStack)
                .orElseThrow(() -> new IllegalStateException("SizeParameters stack is empty"))._2(),
                applicationDataStack);
    }

    @Override
    public Context restoreApplicationData() {
        return new StandardContext(sizeParametersStack,
                uncons(applicationDataStack)
                        .orElseThrow(() -> new IllegalStateException("ApplicationData stack is empty"))._2());
    }

    private static StandardContext context(Iterable<SizeParameters> sizeStack, Iterable<Object> applicationDataStack) {
        return new StandardContext(sizeStack, applicationDataStack);
    }

    public static StandardContext defaultContext() {
        return DEFAULT_CONTEXT;
    }

}
