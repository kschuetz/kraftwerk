package dev.marksman.composablerandom;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static dev.marksman.composablerandom.SizeParameters.noSizeLimits;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StandardContext implements Context {

    private static final StandardContext DEFAULT_CONTEXT = standardContext(noSizeLimits());

    private final SizeParameters sizeParameters;

    @Override
    public Context withSizeParameters(SizeParameters sizeParameters) {
        return standardContext(sizeParameters);
    }

    private static StandardContext standardContext(SizeParameters sizeParameters) {
        return new StandardContext(sizeParameters);
    }

    public static StandardContext defaultContext() {
        return DEFAULT_CONTEXT;
    }
}
