package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.Maybe;

public interface CompoundStringBuilder {

    CompoundStringBuilder add(Generate<String> g);

    CompoundStringBuilder add(String s);

    CompoundStringBuilder addMaybe(Generate<Maybe<String>> g);

    CompoundStringBuilder addMaybe(Maybe<String> s);

    CompoundStringBuilder addMany(Iterable<Generate<String>> gs);

    CompoundStringBuilder addManyValues(Iterable<String> gs);

    CompoundStringBuilder addManyMaybe(Iterable<Generate<Maybe<String>>> g);

    CompoundStringBuilder addManyMaybeValues(Iterable<Maybe<String>> g);

    CompoundStringBuilder withSeparator(Generate<String> newSeparator);

    CompoundStringBuilder withSeparator(String newSeparator);

    CompoundStringBuilder withStartDelimiter(Generate<String> newDelimiter);

    CompoundStringBuilder withStartDelimiter(String newDelimiter);

    CompoundStringBuilder withEndDelimiter(Generate<String> newDelimiter);

    CompoundStringBuilder withEndDelimiter(String newDelimiter);

    Generate<String> build();

}
