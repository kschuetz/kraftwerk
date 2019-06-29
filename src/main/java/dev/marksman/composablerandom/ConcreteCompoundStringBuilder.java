package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.Vector;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import lombok.AllArgsConstructor;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.composablerandom.Generate.constant;

abstract class ConcreteCompoundStringBuilder implements CoProduct2<ConcreteCompoundStringBuilder.Plain,
        ConcreteCompoundStringBuilder.Maybes, ConcreteCompoundStringBuilder>, CompoundStringBuilder {

    @AllArgsConstructor
    static class Plain extends ConcreteCompoundStringBuilder {
        static ConcreteCompoundStringBuilder EMPTY = new Plain(Vector.empty(), nothing());

        private final ImmutableFiniteIterable<Generate<String>> components;
        private final Maybe<Generate<String>> separator;

        @Override
        public <R> R match(Fn1<? super Plain, ? extends R> aFn, Fn1<? super Maybes, ? extends R> bFn) {
            return aFn.apply(this);
        }

        Maybes toMaybes() {
            return new Maybes(components.fmap(Generate::just), separator);
        }

        @Override
        public CompoundStringBuilder add(Generate<String> g) {
            return new Plain(components.append(g), separator);
        }

        @Override
        public CompoundStringBuilder add(String s) {
            return null == s || s.equals("")
                    ? this
                    : add(constant(s));
        }

        @Override
        public CompoundStringBuilder addMaybe(Generate<Maybe<String>> g) {
            return toMaybes().addMaybe(g);
        }

        @Override
        public CompoundStringBuilder addMaybe(Maybe<String> s) {
            return s.match(__ -> this, this::add);
        }

        @Override
        public CompoundStringBuilder withSeparator(Generate<String> newSeparator) {
            return new Plain(components, just(newSeparator));
        }

        @Override
        public CompoundStringBuilder withSeparator(String newSeparator) {
            if (null == newSeparator || newSeparator.equals("")) {
                return separator.match(__ -> this, __ -> new Plain(components, nothing()));
            } else {
                return withSeparator(constant(newSeparator));
            }
        }

        @Override
        public Generate<String> build() {
            return separator.match(__ -> Strings.concatStrings(components),
                    sep -> Strings.concatStrings(sep, components));
        }
    }

    @AllArgsConstructor
    class Maybes extends ConcreteCompoundStringBuilder {
        ImmutableFiniteIterable<Generate<Maybe<String>>> components;
        private final Maybe<Generate<String>> separator;

        @Override
        public <R> R match(Fn1<? super Plain, ? extends R> aFn, Fn1<? super Maybes, ? extends R> bFn) {
            return bFn.apply(this);
        }

        @Override
        public CompoundStringBuilder add(Generate<String> g) {
            return addMaybe(g.just());
        }

        @Override
        public CompoundStringBuilder add(String s) {
            return null == s || s.equals("")
                    ? this
                    : add(constant(s));
        }

        @Override
        public CompoundStringBuilder addMaybe(Generate<Maybe<String>> g) {
            return new Maybes(components.append(g), separator);
        }

        @Override
        public CompoundStringBuilder addMaybe(Maybe<String> s) {
            return s.match(__ -> this, this::add);
        }

        @Override
        public CompoundStringBuilder withSeparator(Generate<String> newSeparator) {
            return new Maybes(components, just(newSeparator));
        }

        @Override
        public CompoundStringBuilder withSeparator(String newSeparator) {
            if (null == newSeparator || newSeparator.equals("")) {
                return separator.match(__ -> this, __ -> new Maybes(components, nothing()));
            } else {
                return withSeparator(constant(newSeparator));
            }
        }

        @Override
        public Generate<String> build() {
            return separator.match(__ -> Strings.concatMaybeStrings(components),
                    sep -> Strings.concatMaybeStrings(sep, components));
        }
    }

    static ConcreteCompoundStringBuilder builder() {
        return Plain.EMPTY;
    }

}
