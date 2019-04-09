package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.lens.Lens;

import static com.jnape.palatable.lambda.lens.Lens.simpleLens;

public class ParametersLens {

    public static Lens.Simple<Parameters, SizeSelector> sizeSelector = simpleLens(Parameters::getSizeSelector,
            Parameters::withSizeSelector);

}
