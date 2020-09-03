package dev.marksman.kraftwerk;

import dev.marksman.kraftwerk.bias.BiasSettings;

/**
 * A set of parameters that can be interpreted by a {@link Generator} to affect its strategy for generating values.
 * <p>
 * For example, when a {@code Generator} is used in the context of a property-testing framework, it may be desirable
 * to represent edge-case values more frequently, while in other applications, you may want more uniform values
 * from the same {@code Generator}.  {@code GeneratorParameters} represents this context.
 * <p>
 * Call {@link GeneratorParameters#generatorParameters()} for a set of sane defaults.
 */
public interface GeneratorParameters {
    SizeParameters getSizeParameters();

    /**
     * Creates a new {@code GeneratorParameters} that is the same as this one, but with the specified {@link SizeParameters}.
     *
     * @param sizeParameters the new {@code SizeParameters}
     * @return a {@code GeneratorParameters}
     */
    GeneratorParameters withSizeParameters(SizeParameters sizeParameters);

    BiasSettings getBiasSettings();

    /**
     * Creates a new {@code GeneratorParameters} that is the same as this one, but with the specified {@link BiasSettings}.
     *
     * @param biasSettings the new {@code BiasSettings}
     * @return a {@code GeneratorParameters}
     */
    GeneratorParameters withBiasSettings(BiasSettings biasSettings);

    /**
     * Creates a new {@code GeneratorParameters} that is the same as this one, with all bias settings removed.
     *
     * @return a {@code GeneratorParameters}
     */
    GeneratorParameters withNoBias();

    static GeneratorParameters generatorParameters() {
        return StandardGeneratorParameters.defaultGeneratorParameters();
    }
}

