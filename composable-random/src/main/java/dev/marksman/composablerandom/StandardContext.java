package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.Unit;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.SizeParameters.noSizeLimits;

@AllArgsConstructor
public class StandardContext implements Context {
    private static final StandardContext DEFAULT_CONTEXT = standardContext(noSizeLimits(), Unit.UNIT);

    private final SizeParameters sizeParameters;
    private final Object applicationData;

    @Override
    public SizeParameters getSizeParameters() {
        return sizeParameters;
    }

    @Override
    public Object getApplicationData() {
        return applicationData;
    }

    @Override
    public Context withSizeParameters(SizeParameters sizeParameters) {
        return standardContext(sizeParameters, applicationData);
    }

    @Override
    public Context withApplicationData(Object applicationData) {
        return standardContext(sizeParameters, applicationData);
    }

    private static StandardContext standardContext(SizeParameters sizeParameters, Object applicationData) {
        return new StandardContext(sizeParameters, applicationData);
    }

    public static StandardContext defaultContext() {
        return DEFAULT_CONTEXT;
    }

}
