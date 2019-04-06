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
    public StandardMetadata withLabel(Maybe<String> label) {
        return standardMetadata(label, nothing());
    }

    @Override
    public Maybe<Object> getApplicationData() {
        return nothing();
    }

    @Override
    public StandardMetadata withApplicationData(Maybe<Object> data) {
        return standardMetadata(label, data);
    }

    @Override
    public boolean isPrimitive() {
        return true;
    }

    public static PrimitiveMetadata primitiveMetadata(String label) {
        return new PrimitiveMetadata(just(label));
    }
}
