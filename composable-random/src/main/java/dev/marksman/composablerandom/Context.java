package dev.marksman.composablerandom;

import dev.marksman.composablerandom.tracing.TraceContext;

public interface Context {
    SizeParameters getSizeParameters();

    TraceContext getTraceContext();

    Object getApplicationData();

    Context withSizeParameters(SizeParameters sizeParameters);

    Context withTraceContext(TraceContext traceContext);

    Context withApplicationData(Object applicationData);

}
