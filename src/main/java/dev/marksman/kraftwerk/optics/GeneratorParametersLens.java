package dev.marksman.kraftwerk.optics;

import com.jnape.palatable.lambda.optics.Lens;
import dev.marksman.kraftwerk.GeneratorParameters;
import dev.marksman.kraftwerk.SizeParameters;

import static com.jnape.palatable.lambda.optics.Lens.simpleLens;

public class GeneratorParametersLens {

    public static Lens.Simple<GeneratorParameters, SizeParameters> sizeParameters = simpleLens(GeneratorParameters::getSizeParameters,
            GeneratorParameters::withSizeParameters);

}
