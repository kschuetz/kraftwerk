package dev.marksman.composablerandom.metadata;

import com.jnape.palatable.lambda.adt.Maybe;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.composablerandom.metadata.StandardMetadata.standardMetadata;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PrimitiveMetadata implements Metadata {

    private final Maybe<String> label;

    @Override
    public Maybe<String> getLabel() {
        return label;
    }

    @Override
    public boolean isPrimitive() {
        return true;
    }

    @Override
    public StandardMetadata withLabel(String text) {
        return standardMetadata(just(text));
    }

    @Override
    public Metadata removeLabel() {
        return label.match(_1 -> this, _2 -> standardMetadata(nothing()));
    }


    public static PrimitiveMetadata primitiveMetadata(String label) {
        return new PrimitiveMetadata(just(label));
    }
}

