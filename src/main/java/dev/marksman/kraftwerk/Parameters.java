package dev.marksman.kraftwerk;

import dev.marksman.kraftwerk.bias.BiasSettings;

public interface Parameters {
    SizeParameters getSizeParameters();

    Parameters withSizeParameters(SizeParameters sizeParameters);

    BiasSettings getBiasSettings();

    Parameters withBiasSettings(BiasSettings biasSettings);

    static Parameters parameters() {
        return StandardParameters.defaultParameters();
    }
}

