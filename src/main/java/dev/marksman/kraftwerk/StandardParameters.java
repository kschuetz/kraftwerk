package dev.marksman.kraftwerk;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static dev.marksman.kraftwerk.NoBias.noBias;
import static dev.marksman.kraftwerk.SizeParameters.noSizeLimits;
import static dev.marksman.kraftwerk.SizeSelectors.sizeSelector;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StandardParameters implements Parameters {

    private static final StandardParameters DEFAULT_PARAMETERS = standardParameters(sizeSelector(noSizeLimits()),
            noBias());

    @Getter
    private final SizeSelector sizeSelector;
    @Getter
    private final BiasSettings biasSettings;

    public Parameters withSizeSelector(SizeSelector sizeSelector) {
        return standardParameters(sizeSelector, biasSettings);
    }

    @Override
    public Parameters withBiasSettings(BiasSettings biasSettings) {
        return null;
    }

    private static StandardParameters standardParameters(SizeSelector sizeSelector, BiasSettings biasSettings) {
        return new StandardParameters(sizeSelector, biasSettings);
    }

    public static StandardParameters defaultParameters() {
        return DEFAULT_PARAMETERS;
    }
}
