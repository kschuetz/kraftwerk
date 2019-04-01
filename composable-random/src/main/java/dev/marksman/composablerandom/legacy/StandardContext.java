package dev.marksman.composablerandom.legacy;

import dev.marksman.composablerandom.SizeParameters;
import dev.marksman.composablerandom.tracing.TraceContext;
import lombok.AllArgsConstructor;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static dev.marksman.composablerandom.SizeParameters.noSizeLimits;
import static dev.marksman.composablerandom.tracing.TraceContext.noTrace;

@AllArgsConstructor
public class StandardContext implements OldContext {
    private static final StandardContext DEFAULT_CONTEXT = standardContext(noSizeLimits(), noTrace(), UNIT);

    private final SizeParameters sizeParameters;
    private final TraceContext traceContext;
    private final Object applicationData;

    @Override
    public SizeParameters getSizeParameters() {
        return sizeParameters;
    }

    @Override
    public TraceContext getTraceContext() {
        return traceContext;
    }

    @Override
    public Object getApplicationData() {
        return applicationData;
    }

    @Override
    public OldContext withSizeParameters(SizeParameters sizeParameters) {
        return standardContext(sizeParameters, traceContext, applicationData);
    }

    @Override
    public OldContext withTraceContext(TraceContext traceContext) {
        return standardContext(sizeParameters, traceContext, applicationData);
    }

    @Override
    public OldContext withApplicationData(Object applicationData) {
        return standardContext(sizeParameters, traceContext, applicationData);
    }

    private static StandardContext standardContext(SizeParameters sizeParameters, TraceContext traceContext,
                                                   Object applicationData) {
        return new StandardContext(sizeParameters, traceContext, applicationData);
    }

    public static StandardContext defaultContext() {
        return DEFAULT_CONTEXT;
    }

}
