package dev.marksman.kraftwerk;

public interface Parameters {
    SizeSelector getSizeSelector();

    Parameters withSizeSelector(SizeSelector sizeSelector);

    BiasSettings getBiasSettings();

    Parameters withBiasSettings(BiasSettings biasSettings);

}

