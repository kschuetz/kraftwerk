package dev.marksman.kraftwerk;

import dev.marksman.kraftwerk.bias.BiasSettings;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static dev.marksman.kraftwerk.SizeParameters.noSizeLimits;
import static dev.marksman.kraftwerk.bias.EmptyBiasSettings.emptyBiasSettings;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StandardGeneratorParameters implements GeneratorParameters {

    private static final StandardGeneratorParameters DEFAULT_PARAMETERS = standardGeneratorParameters(noSizeLimits(),
            emptyBiasSettings());

    @Getter
    private final SizeParameters sizeParameters;
    @Getter
    private final BiasSettings biasSettings;

    @Override
    public GeneratorParameters withSizeParameters(SizeParameters sizeParameters) {
        return standardGeneratorParameters(sizeParameters, biasSettings);
    }

    @Override
    public GeneratorParameters withBiasSettings(BiasSettings biasSettings) {
        return standardGeneratorParameters(sizeParameters, biasSettings);
    }

    private static StandardGeneratorParameters standardGeneratorParameters(SizeParameters sizeParameters, BiasSettings biasSettings) {
        return new StandardGeneratorParameters(sizeParameters, biasSettings);
    }

    public static StandardGeneratorParameters defaultGeneratorParameters() {
        return DEFAULT_PARAMETERS;
    }
}
