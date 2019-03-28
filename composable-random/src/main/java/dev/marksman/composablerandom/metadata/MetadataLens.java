package dev.marksman.composablerandom.metadata;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.lens.Lens;

import static com.jnape.palatable.lambda.lens.Lens.simpleLens;

public class MetadataLens {
    public static Lens.Simple<Metadata, Maybe<String>> label = simpleLens(Metadata::getLabel,
            (md, ml) -> ml.match(__ -> md.removeLabel(), md::withLabel));
}
