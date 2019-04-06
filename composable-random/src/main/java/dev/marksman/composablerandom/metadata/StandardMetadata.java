package dev.marksman.composablerandom.metadata;

import com.jnape.palatable.lambda.adt.Maybe;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StandardMetadata implements Metadata {
    private static final StandardMetadata DEFAULT = standardMetadata(nothing(), nothing());

    private final Maybe<String> label;
    private final Maybe<Object> applicationData;

    @Override
    public Maybe<String> getLabel() {
        return label;
    }

    @Override
    public StandardMetadata withLabel(Maybe<String> label) {
        return standardMetadata(label, applicationData);
    }

    @Override
    public Maybe<Object> getApplicationData() {
        return applicationData;
    }

    @Override
    public StandardMetadata withApplicationData(Maybe<Object> data) {
        return standardMetadata(label, applicationData);
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    public static StandardMetadata standardMetadata(Maybe<String> label, Maybe<Object> applicationData) {
        return new StandardMetadata(label, applicationData);
    }

    public static StandardMetadata defaultMetadata() {
        return DEFAULT;
    }

    public static StandardMetadata labeled(String text) {
        return defaultMetadata().withLabel(just(text));
    }

}
