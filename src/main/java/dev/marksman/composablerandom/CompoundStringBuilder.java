package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.Maybe;

public interface CompoundStringBuilder {

    CompoundStringBuilder add(Generate<String> g);

    CompoundStringBuilder add(String s);

    CompoundStringBuilder addMaybe(Generate<Maybe<String>> g);

    CompoundStringBuilder addMaybe(Maybe<String> s);

    CompoundStringBuilder withSeparator(Generate<String> newSeparator);

    CompoundStringBuilder withSeparator(String newSeparator);

    Generate<String> build();

}
