package dev.marksman.composablerandom.metadata;

import com.jnape.palatable.lambda.adt.Maybe;

public interface Metadata {

    Maybe<String> getLabel();

    Metadata withLabel(String text);

    Metadata removeLabel();

    boolean isPrimitive();

}
