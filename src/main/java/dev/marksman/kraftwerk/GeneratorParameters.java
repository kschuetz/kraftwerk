package dev.marksman.kraftwerk;

import dev.marksman.kraftwerk.bias.BiasSettings;

public interface GeneratorParameters {
    SizeParameters getSizeParameters();

    GeneratorParameters withSizeParameters(SizeParameters sizeParameters);

    BiasSettings getBiasSettings();

    GeneratorParameters withBiasSettings(BiasSettings biasSettings);

    GeneratorParameters withNoBias();

    static GeneratorParameters generatorParameters() {
        return StandardGeneratorParameters.defaultGeneratorParameters();
    }
}

