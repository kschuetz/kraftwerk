package dev.marksman.kraftwerk.optics;

import com.jnape.palatable.lambda.optics.Lens;
import dev.marksman.kraftwerk.Parameters;
import dev.marksman.kraftwerk.SizeParameters;

import static com.jnape.palatable.lambda.optics.Lens.simpleLens;

public class ParametersLens {

    public static Lens.Simple<Parameters, SizeParameters> sizeParameters = simpleLens(Parameters::getSizeParameters,
            Parameters::withSizeParameters);

}
