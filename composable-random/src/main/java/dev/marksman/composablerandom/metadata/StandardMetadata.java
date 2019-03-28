package dev.marksman.composablerandom.metadata;

import com.jnape.palatable.lambda.adt.Maybe;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StandardMetadata implements Metadata {
    private static final StandardMetadata DEFAULT = standardMetadata(nothing());

    private final Maybe<String> label;

    @Override
    public Maybe<String> getLabel() {
        return label;
    }

    @Override
    public StandardMetadata withLabel(String text) {
        return standardMetadata(just(text));
    }

    @Override
    public StandardMetadata removeLabel() {
        return label.match(_1 -> this, _2 -> standardMetadata(nothing()));
    }

    public static StandardMetadata standardMetadata(Maybe<String> label) {
        return new StandardMetadata(label);
    }

    public static StandardMetadata defaultMetadata() {
        return DEFAULT;
    }
}
