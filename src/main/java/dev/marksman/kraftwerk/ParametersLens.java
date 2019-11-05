package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.optics.Lens;

import static com.jnape.palatable.lambda.optics.Lens.simpleLens;

public class ParametersLens {

    public static Lens.Simple<Parameters, SizeSelector> sizeSelector = simpleLens(Parameters::getSizeSelector,
            Parameters::withSizeSelector);

}
