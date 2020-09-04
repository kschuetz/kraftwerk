package dev.marksman.kraftwerk;

import dev.marksman.kraftwerk.bias.BiasSettings;

import static dev.marksman.kraftwerk.SizeParameters.noSizeLimits;
import static dev.marksman.kraftwerk.bias.EmptyBiasSettings.emptyBiasSettings;


final class StandardGeneratorParameters implements GeneratorParameters {
    private static final StandardGeneratorParameters DEFAULT_PARAMETERS = standardGeneratorParameters(noSizeLimits(),
            emptyBiasSettings());

    private final SizeParameters sizeParameters;
    private final BiasSettings biasSettings;

    private StandardGeneratorParameters(SizeParameters sizeParameters, BiasSettings biasSettings) {
        this.sizeParameters = sizeParameters;
        this.biasSettings = biasSettings;
    }

    private static StandardGeneratorParameters standardGeneratorParameters(SizeParameters sizeParameters, BiasSettings biasSettings) {
        return new StandardGeneratorParameters(sizeParameters, biasSettings);
    }

    public static StandardGeneratorParameters defaultGeneratorParameters() {
        return DEFAULT_PARAMETERS;
    }

    @Override
    public GeneratorParameters withSizeParameters(SizeParameters sizeParameters) {
        return standardGeneratorParameters(sizeParameters, biasSettings);
    }

    @Override
    public GeneratorParameters withBiasSettings(BiasSettings biasSettings) {
        return standardGeneratorParameters(sizeParameters, biasSettings);
    }

    @Override
    public GeneratorParameters withNoBias() {
        return standardGeneratorParameters(sizeParameters, emptyBiasSettings());
    }

    public SizeParameters getSizeParameters() {
        return this.sizeParameters;
    }

    public BiasSettings getBiasSettings() {
        return this.biasSettings;
    }
}
