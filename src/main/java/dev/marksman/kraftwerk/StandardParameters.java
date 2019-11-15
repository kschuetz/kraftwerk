package dev.marksman.kraftwerk;

import dev.marksman.kraftwerk.bias.BiasSettings;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static dev.marksman.kraftwerk.SizeParameters.noSizeLimits;
import static dev.marksman.kraftwerk.bias.EmptyBiasSettings.emptyBiasSettings;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StandardParameters implements Parameters {

    private static final StandardParameters DEFAULT_PARAMETERS = standardParameters(noSizeLimits(),
            emptyBiasSettings());

    @Getter
    private final SizeParameters sizeParameters;
    @Getter
    private final BiasSettings biasSettings;

    @Override
    public Parameters withSizeParameters(SizeParameters sizeParameters) {
        return standardParameters(sizeParameters, biasSettings);
    }

    @Override
    public Parameters withBiasSettings(BiasSettings biasSettings) {
        return standardParameters(sizeParameters, biasSettings);
    }

    private static StandardParameters standardParameters(SizeParameters sizeParameters, BiasSettings biasSettings) {
        return new StandardParameters(sizeParameters, biasSettings);
    }

    public static StandardParameters defaultParameters() {
        return DEFAULT_PARAMETERS;
    }
}
