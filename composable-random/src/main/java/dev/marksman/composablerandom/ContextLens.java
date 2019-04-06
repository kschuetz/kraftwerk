package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.lens.Lens;

import static com.jnape.palatable.lambda.lens.Lens.simpleLens;

public class ContextLens {

    public static Lens.Simple<Context, SizeParameters> sizeParameters = simpleLens(Context::getSizeParameters,
            Context::withSizeParameters);

}
