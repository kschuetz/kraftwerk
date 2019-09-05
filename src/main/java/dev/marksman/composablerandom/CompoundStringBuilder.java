package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.Maybe;

public interface CompoundStringBuilder {

    CompoundStringBuilder add(Generator<String> g);

    CompoundStringBuilder add(String s);

    CompoundStringBuilder addMaybe(Generator<Maybe<String>> g);

    CompoundStringBuilder addMaybe(Maybe<String> s);

    CompoundStringBuilder addMany(Iterable<Generator<String>> gs);

    CompoundStringBuilder addManyValues(Iterable<String> gs);

    CompoundStringBuilder addManyMaybe(Iterable<Generator<Maybe<String>>> g);

    CompoundStringBuilder addManyMaybeValues(Iterable<Maybe<String>> g);

    CompoundStringBuilder withSeparator(Generator<String> newSeparator);

    CompoundStringBuilder withSeparator(String newSeparator);

    CompoundStringBuilder withStartDelimiter(Generator<String> newDelimiter);

    CompoundStringBuilder withStartDelimiter(String newDelimiter);

    CompoundStringBuilder withEndDelimiter(Generator<String> newDelimiter);

    CompoundStringBuilder withEndDelimiter(String newDelimiter);

    Generator<String> build();

}
