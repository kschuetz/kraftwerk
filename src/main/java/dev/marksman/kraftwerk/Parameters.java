package dev.marksman.kraftwerk;

public interface Parameters {
    SizeParameters getSizeParameters();

    Parameters withSizeParameters(SizeParameters sizeParameters);

    BiasSettings getBiasSettings();

    Parameters withBiasSettings(BiasSettings biasSettings);

}

