package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.Vector;
import dev.marksman.enhancediterables.ImmutableIterable;
import lombok.AllArgsConstructor;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.kraftwerk.Generators.constant;

abstract class ConcreteCompoundStringBuilder implements CoProduct2<ConcreteCompoundStringBuilder.Plain,
        ConcreteCompoundStringBuilder.Maybes, ConcreteCompoundStringBuilder>, CompoundStringBuilder {

    @AllArgsConstructor
    static class Plain extends ConcreteCompoundStringBuilder {
        static ConcreteCompoundStringBuilder EMPTY = new Plain(Vector.empty(), nothing(), nothing(), nothing());

        private final ImmutableIterable<Generator<String>> components;
        private final Maybe<Generator<String>> separator;
        private final Maybe<Generator<String>> startDelimiter;
        private final Maybe<Generator<String>> endDelimiter;

        @Override
        public <R> R match(Fn1<? super Plain, ? extends R> aFn, Fn1<? super Maybes, ? extends R> bFn) {
            return aFn.apply(this);
        }

        Maybes toMaybes() {
            return new Maybes(components.fmap(Generator::just), separator, startDelimiter, endDelimiter);
        }

        @Override
        public CompoundStringBuilder add(Generator<String> g) {
            return new Plain(components.append(g), separator, startDelimiter, endDelimiter);
        }

        @Override
        public CompoundStringBuilder add(String s) {
            return null == s || s.equals("")
                    ? this
                    : add(constant(s));
        }

        @Override
        public CompoundStringBuilder addMaybe(Generator<Maybe<String>> g) {
            return toMaybes().addMaybe(g);
        }

        @Override
        public CompoundStringBuilder addMaybe(Maybe<String> s) {
            return s.match(__ -> this, this::add);
        }

        @Override
        public CompoundStringBuilder addMany(Iterable<Generator<String>> gs) {
            return new Plain(components.concat(Vector.copyFrom(gs)), separator, startDelimiter, endDelimiter);
        }

        @Override
        public CompoundStringBuilder addManyValues(Iterable<String> gs) {
            return new Plain(components.concat(Vector.copyFrom(gs).fmap(Generators::constant)),
                    separator, startDelimiter, endDelimiter);

        }

        @Override
        public CompoundStringBuilder addManyMaybe(Iterable<Generator<Maybe<String>>> g) {
            ImmutableVector<Generator<Maybe<String>>> gs = Vector.copyFrom(g);
            if (gs.isEmpty()) {
                return this;
            } else {
                return this.toMaybes().addManyMaybe(gs);
            }
        }

        @Override
        public CompoundStringBuilder addManyMaybeValues(Iterable<Maybe<String>> g) {
            ImmutableVector<Maybe<String>> gs = Vector.copyFrom(g);
            if (gs.isEmpty()) {
                return this;
            } else {
                return this.toMaybes().addManyMaybeValues(gs);
            }
        }

        @Override
        public CompoundStringBuilder withSeparator(Generator<String> newSeparator) {
            return new Plain(components, just(newSeparator), startDelimiter, endDelimiter);
        }

        @Override
        public CompoundStringBuilder withSeparator(String newSeparator) {
            if (null == newSeparator || newSeparator.equals("")) {
                return separator.match(__ -> this,
                        __ -> new Plain(components, nothing(), startDelimiter, endDelimiter));
            } else {
                return withSeparator(constant(newSeparator));
            }
        }

        @Override
        public CompoundStringBuilder withStartDelimiter(Generator<String> newDelimiter) {
            return new Plain(components, separator, just(newDelimiter), endDelimiter);
        }

        @Override
        public CompoundStringBuilder withStartDelimiter(String newDelimiter) {
            if (null == newDelimiter || newDelimiter.equals("")) {
                return separator.match(__ -> this,
                        __ -> new Plain(components, separator, nothing(), endDelimiter));
            } else {
                return withStartDelimiter(constant(newDelimiter));
            }
        }

        @Override
        public CompoundStringBuilder withEndDelimiter(Generator<String> newDelimiter) {
            return new Plain(components, separator, startDelimiter, just(newDelimiter));
        }

        @Override
        public CompoundStringBuilder withEndDelimiter(String newDelimiter) {
            if (null == newDelimiter || newDelimiter.equals("")) {
                return separator.match(__ -> this,
                        __ -> new Plain(components, separator, startDelimiter, nothing()));
            } else {
                return withEndDelimiter(constant(newDelimiter));
            }
        }

        @Override
        public Generator<String> build() {
            Generator<String> inner = separator.match(__ -> Strings.concatStrings(components),
                    sep -> Strings.concatStrings(sep, components));
            return ConcreteCompoundStringBuilder.applyStartDelimiter(startDelimiter,
                    ConcreteCompoundStringBuilder.applyEndDelimiter(endDelimiter, inner));
        }
    }

    @AllArgsConstructor
    class Maybes extends ConcreteCompoundStringBuilder {
        ImmutableIterable<Generator<Maybe<String>>> components;
        private final Maybe<Generator<String>> separator;
        private final Maybe<Generator<String>> startDelimiter;
        private final Maybe<Generator<String>> endDelimiter;

        @Override
        public <R> R match(Fn1<? super Plain, ? extends R> aFn, Fn1<? super Maybes, ? extends R> bFn) {
            return bFn.apply(this);
        }

        @Override
        public CompoundStringBuilder add(Generator<String> g) {
            return addMaybe(g.just());
        }

        @Override
        public CompoundStringBuilder add(String s) {
            return null == s || s.equals("")
                    ? this
                    : add(constant(s));
        }

        @Override
        public CompoundStringBuilder addMaybe(Generator<Maybe<String>> g) {
            return new Maybes(components.append(g), separator, startDelimiter, endDelimiter);
        }

        @Override
        public CompoundStringBuilder addMaybe(Maybe<String> s) {
            return s.match(__ -> this, this::add);
        }

        @Override
        public CompoundStringBuilder addMany(Iterable<Generator<String>> gs) {
            return new Maybes(components.concat(Vector.copyFrom(gs).fmap(Generator::just)),
                    separator, startDelimiter, endDelimiter);
        }

        @Override
        public CompoundStringBuilder addManyValues(Iterable<String> gs) {
            return new Maybes(components.concat(Vector.copyFrom(gs).fmap(s -> Generators.constant(s).just())),
                    separator, startDelimiter, endDelimiter);
        }

        @Override
        public CompoundStringBuilder addManyMaybe(Iterable<Generator<Maybe<String>>> g) {
            return new Maybes(components.concat(Vector.copyFrom(g)),
                    separator, startDelimiter, endDelimiter);

        }

        @Override
        public CompoundStringBuilder addManyMaybeValues(Iterable<Maybe<String>> g) {
            return new Maybes(components.concat(Vector.copyFrom(g).fmap(Generators::constant)),
                    separator, startDelimiter, endDelimiter);
        }

        @Override
        public CompoundStringBuilder withSeparator(Generator<String> newSeparator) {
            return new Maybes(components, just(newSeparator), startDelimiter, endDelimiter);
        }

        @Override
        public CompoundStringBuilder withSeparator(String newSeparator) {
            if (null == newSeparator || newSeparator.equals("")) {
                return separator.match(__ -> this, __ -> new Maybes(components, nothing(), startDelimiter, endDelimiter));
            } else {
                return withSeparator(constant(newSeparator));
            }
        }

        @Override
        public CompoundStringBuilder withStartDelimiter(Generator<String> newDelimiter) {
            return new Maybes(components, separator, just(newDelimiter), endDelimiter);
        }

        @Override
        public CompoundStringBuilder withStartDelimiter(String newDelimiter) {
            if (null == newDelimiter || newDelimiter.equals("")) {
                return separator.match(__ -> this,
                        __ -> new Maybes(components, separator, nothing(), endDelimiter));
            } else {
                return withStartDelimiter(constant(newDelimiter));
            }
        }

        @Override
        public CompoundStringBuilder withEndDelimiter(Generator<String> newDelimiter) {
            return new Maybes(components, separator, startDelimiter, just(newDelimiter));
        }

        @Override
        public CompoundStringBuilder withEndDelimiter(String newDelimiter) {
            if (null == newDelimiter || newDelimiter.equals("")) {
                return separator.match(__ -> this,
                        __ -> new Maybes(components, separator, startDelimiter, nothing()));
            } else {
                return withEndDelimiter(constant(newDelimiter));
            }
        }

        @Override
        public Generator<String> build() {
            Generator<String> inner = separator.match(__ -> Strings.concatMaybeStrings(components),
                    sep -> Strings.concatMaybeStrings(sep, components));
            return ConcreteCompoundStringBuilder.applyStartDelimiter(startDelimiter,
                    ConcreteCompoundStringBuilder.applyEndDelimiter(endDelimiter, inner));
        }
    }

    private static Generator<String> applyStartDelimiter(Maybe<Generator<String>> startDelimiter,
                                                         Generator<String> gen) {
        return startDelimiter.match(__ -> gen, d -> Generators.generateString(d, gen));
    }

    private static Generator<String> applyEndDelimiter(Maybe<Generator<String>> endDelimiter,
                                                       Generator<String> gen) {
        return endDelimiter.match(__ -> gen, d -> Generators.generateString(gen, d));
    }

    static ConcreteCompoundStringBuilder builder() {
        return Plain.EMPTY;
    }

}
