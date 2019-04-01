package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.lens.Lens;
import dev.marksman.composablerandom.legacy.OldContext;
import dev.marksman.composablerandom.tracing.TraceContext;

import static com.jnape.palatable.lambda.lens.Lens.simpleLens;

public class ContextLens {

    public static Lens.Simple<OldContext, SizeParameters> sizeParameters = simpleLens(OldContext::getSizeParameters,
            OldContext::withSizeParameters);

    public static Lens.Simple<OldContext, TraceContext> traceContext = simpleLens(OldContext::getTraceContext,
            OldContext::withTraceContext);

    public static Lens.Simple<OldContext, Object> applicationData = simpleLens(OldContext::getApplicationData,
            OldContext::withApplicationData);

}
