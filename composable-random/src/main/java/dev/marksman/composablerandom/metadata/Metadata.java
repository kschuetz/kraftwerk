package dev.marksman.composablerandom.metadata;

import com.jnape.palatable.lambda.adt.Maybe;

public interface Metadata {

    Maybe<String> getLabel();

    Metadata withLabel(Maybe<String> text);

    Maybe<Object> getApplicationData();

    Metadata withApplicationData(Maybe<Object> data);

    boolean isPrimitive();

}
