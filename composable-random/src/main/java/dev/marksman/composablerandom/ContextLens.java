package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.lens.Lens;
import dev.marksman.composablerandom.tracing.TraceContext;

import static com.jnape.palatable.lambda.lens.Lens.simpleLens;

public class ContextLens {

    public static Lens.Simple<Context, SizeParameters> sizeParameters = simpleLens(Context::getSizeParameters,
            Context::withSizeParameters);

    public static Lens.Simple<Context, TraceContext> traceContext = simpleLens(Context::getTraceContext,
            Context::withTraceContext);

    public static Lens.Simple<Context, Object> applicationData = simpleLens(Context::getApplicationData,
            Context::withApplicationData);

}
