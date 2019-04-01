package dev.marksman.composablerandom.legacy;

import dev.marksman.composablerandom.SizeParameters;
import dev.marksman.composablerandom.tracing.TraceContext;

public interface OldContext {
    SizeParameters getSizeParameters();

    TraceContext getTraceContext();

    Object getApplicationData();

    OldContext withSizeParameters(SizeParameters sizeParameters);

    OldContext withTraceContext(TraceContext traceContext);

    OldContext withApplicationData(Object applicationData);

}
