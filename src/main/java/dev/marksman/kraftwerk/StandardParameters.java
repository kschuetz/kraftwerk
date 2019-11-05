package dev.marksman.kraftwerk;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static dev.marksman.kraftwerk.SizeParameters.noSizeLimits;
import static dev.marksman.kraftwerk.SizeSelectors.sizeSelector;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StandardParameters implements Parameters {

    private static final StandardParameters DEFAULT_PARAMETERS = standardParameters(sizeSelector(noSizeLimits()));

    private final SizeSelector sizeSelector;

    public Parameters withSizeSelector(SizeSelector sizeSelector) {
        return standardParameters(sizeSelector);
    }

    private static StandardParameters standardParameters(SizeSelector sizeSelector) {
        return new StandardParameters(sizeSelector);
    }

    public static StandardParameters defaultParameters() {
        return DEFAULT_PARAMETERS;
    }
}
