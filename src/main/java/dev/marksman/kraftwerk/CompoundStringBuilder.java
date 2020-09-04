package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.Vector;
import dev.marksman.enhancediterables.ImmutableIterable;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.kraftwerk.Generators.constant;

public abstract class CompoundStringBuilder implements CoProduct2<CompoundStringBuilder.Plain,
        CompoundStringBuilder.Maybes, CompoundStringBuilder> {
    private static Generator<String> applyStartDelimiter(Maybe<Generator<String>> startDelimiter,
                                                         Generator<String> gen) {
        return startDelimiter.match(__ -> gen, d -> Generators.generateString(d, gen));
    }

    private static Generator<String> applyEndDelimiter(Maybe<Generator<String>> endDelimiter,
                                                       Generator<String> gen) {
        return endDelimiter.match(__ -> gen, d -> Generators.generateString(gen, d));
    }

    static CompoundStringBuilder builder() {
        return Plain.EMPTY;
    }

    public abstract CompoundStringBuilder add(Generator<String> g);

    public abstract CompoundStringBuilder add(String s);

    public abstract CompoundStringBuilder addMaybe(Generator<Maybe<String>> g);

    public abstract CompoundStringBuilder addMaybe(Maybe<String> s);

    public abstract CompoundStringBuilder addMany(Iterable<Generator<String>> gs);

    public abstract CompoundStringBuilder addManyValues(Iterable<String> gs);

    public abstract CompoundStringBuilder addManyMaybe(Iterable<Generator<Maybe<String>>> g);

    public abstract CompoundStringBuilder addManyMaybeValues(Iterable<Maybe<String>> g);

    public abstract CompoundStringBuilder withSeparator(Generator<String> newSeparator);

    public abstract CompoundStringBuilder withSeparator(String newSeparator);

    public abstract CompoundStringBuilder withStartDelimiter(Generator<String> newDelimiter);

    public abstract CompoundStringBuilder withStartDelimiter(String newDelimiter);

    public abstract CompoundStringBuilder withEndDelimiter(Generator<String> newDelimiter);

    public abstract CompoundStringBuilder withEndDelimiter(String newDelimiter);

    public abstract Generator<String> build();

    static class Plain extends CompoundStringBuilder {
        static CompoundStringBuilder EMPTY = new Plain(Vector.empty(), nothing(), nothing(), nothing());

        private final ImmutableIterable<Generator<String>> components;
        private final Maybe<Generator<String>> separator;
        private final Maybe<Generator<String>> startDelimiter;
        private final Maybe<Generator<String>> endDelimiter;

        public Plain(ImmutableIterable<Generator<String>> components, Maybe<Generator<String>> separator, Maybe<Generator<String>> startDelimiter, Maybe<Generator<String>> endDelimiter) {
            this.components = components;
            this.separator = separator;
            this.startDelimiter = startDelimiter;
            this.endDelimiter = endDelimiter;
        }

        @Override
        public <R> R match(Fn1<? super Plain, ? extends R> aFn, Fn1<? super Maybes, ? extends R> bFn) {
            return aFn.apply(this);
        }

        Maybes toMaybes() {
            return new Maybes(components.fmap(Generator::just), separator, startDelimiter, endDelimiter);
        }

        public CompoundStringBuilder add(Generator<String> g) {
            return new Plain(components.append(g), separator, startDelimiter, endDelimiter);
        }

        public CompoundStringBuilder add(String s) {
            return null == s || s.equals("")
                    ? this
                    : add(constant(s));
        }

        public CompoundStringBuilder addMaybe(Generator<Maybe<String>> g) {
            return toMaybes().addMaybe(g);
        }

        public CompoundStringBuilder addMaybe(Maybe<String> s) {
            return s.match(__ -> this, this::add);
        }

        public CompoundStringBuilder addMany(Iterable<Generator<String>> gs) {
            return new Plain(components.concat(Vector.copyFrom(gs)), separator, startDelimiter, endDelimiter);
        }

        public CompoundStringBuilder addManyValues(Iterable<String> gs) {
            return new Plain(components.concat(Vector.copyFrom(gs).fmap(Generators::constant)),
                    separator, startDelimiter, endDelimiter);

        }

        public CompoundStringBuilder addManyMaybe(Iterable<Generator<Maybe<String>>> g) {
            ImmutableVector<Generator<Maybe<String>>> gs = Vector.copyFrom(g);
            if (gs.isEmpty()) {
                return this;
            } else {
                return this.toMaybes().addManyMaybe(gs);
            }
        }

        public CompoundStringBuilder addManyMaybeValues(Iterable<Maybe<String>> g) {
            ImmutableVector<Maybe<String>> gs = Vector.copyFrom(g);
            if (gs.isEmpty()) {
                return this;
            } else {
                return this.toMaybes().addManyMaybeValues(gs);
            }
        }

        public CompoundStringBuilder withSeparator(Generator<String> newSeparator) {
            return new Plain(components, just(newSeparator), startDelimiter, endDelimiter);
        }

        public CompoundStringBuilder withSeparator(String newSeparator) {
            if (null == newSeparator || newSeparator.equals("")) {
                return separator.match(__ -> this,
                        __ -> new Plain(components, nothing(), startDelimiter, endDelimiter));
            } else {
                return withSeparator(constant(newSeparator));
            }
        }

        public CompoundStringBuilder withStartDelimiter(Generator<String> newDelimiter) {
            return new Plain(components, separator, just(newDelimiter), endDelimiter);
        }

        public CompoundStringBuilder withStartDelimiter(String newDelimiter) {
            if (null == newDelimiter || newDelimiter.equals("")) {
                return separator.match(__ -> this,
                        __ -> new Plain(components, separator, nothing(), endDelimiter));
            } else {
                return withStartDelimiter(constant(newDelimiter));
            }
        }

        public CompoundStringBuilder withEndDelimiter(Generator<String> newDelimiter) {
            return new Plain(components, separator, startDelimiter, just(newDelimiter));
        }

        public CompoundStringBuilder withEndDelimiter(String newDelimiter) {
            if (null == newDelimiter || newDelimiter.equals("")) {
                return separator.match(__ -> this,
                        __ -> new Plain(components, separator, startDelimiter, nothing()));
            } else {
                return withEndDelimiter(constant(newDelimiter));
            }
        }

        public Generator<String> build() {
            Generator<String> inner = separator.match(__ -> Strings.concatStrings(components),
                    sep -> Strings.concatStrings(sep, components));
            return CompoundStringBuilder.applyStartDelimiter(startDelimiter,
                    CompoundStringBuilder.applyEndDelimiter(endDelimiter, inner));
        }
    }

    static class Maybes extends CompoundStringBuilder {
        private final Maybe<Generator<String>> separator;
        private final Maybe<Generator<String>> startDelimiter;
        private final Maybe<Generator<String>> endDelimiter;
        ImmutableIterable<Generator<Maybe<String>>> components;

        public Maybes(ImmutableIterable<Generator<Maybe<String>>> components, Maybe<Generator<String>> separator, Maybe<Generator<String>> startDelimiter, Maybe<Generator<String>> endDelimiter) {
            this.components = components;
            this.separator = separator;
            this.startDelimiter = startDelimiter;
            this.endDelimiter = endDelimiter;
        }

        @Override
        public <R> R match(Fn1<? super Plain, ? extends R> aFn, Fn1<? super Maybes, ? extends R> bFn) {
            return bFn.apply(this);
        }

        public CompoundStringBuilder add(Generator<String> g) {
            return addMaybe(g.just());
        }

        public CompoundStringBuilder add(String s) {
            return null == s || s.equals("")
                    ? this
                    : add(constant(s));
        }

        public CompoundStringBuilder addMaybe(Generator<Maybe<String>> g) {
            return new Maybes(components.append(g), separator, startDelimiter, endDelimiter);
        }

        public CompoundStringBuilder addMaybe(Maybe<String> s) {
            return s.match(__ -> this, this::add);
        }

        public CompoundStringBuilder addMany(Iterable<Generator<String>> gs) {
            return new Maybes(components.concat(Vector.copyFrom(gs).fmap(Generator::just)),
                    separator, startDelimiter, endDelimiter);
        }

        public CompoundStringBuilder addManyValues(Iterable<String> gs) {
            return new Maybes(components.concat(Vector.copyFrom(gs).fmap(s -> constant(s).just())),
                    separator, startDelimiter, endDelimiter);
        }

        public CompoundStringBuilder addManyMaybe(Iterable<Generator<Maybe<String>>> g) {
            return new Maybes(components.concat(Vector.copyFrom(g)),
                    separator, startDelimiter, endDelimiter);

        }

        public CompoundStringBuilder addManyMaybeValues(Iterable<Maybe<String>> g) {
            return new Maybes(components.concat(Vector.copyFrom(g).fmap(Generators::constant)),
                    separator, startDelimiter, endDelimiter);
        }

        public CompoundStringBuilder withSeparator(Generator<String> newSeparator) {
            return new Maybes(components, just(newSeparator), startDelimiter, endDelimiter);
        }

        public CompoundStringBuilder withSeparator(String newSeparator) {
            if (null == newSeparator || newSeparator.equals("")) {
                return separator.match(__ -> this, __ -> new Maybes(components, nothing(), startDelimiter, endDelimiter));
            } else {
                return withSeparator(constant(newSeparator));
            }
        }

        public CompoundStringBuilder withStartDelimiter(Generator<String> newDelimiter) {
            return new Maybes(components, separator, just(newDelimiter), endDelimiter);
        }

        public CompoundStringBuilder withStartDelimiter(String newDelimiter) {
            if (null == newDelimiter || newDelimiter.equals("")) {
                return separator.match(__ -> this,
                        __ -> new Maybes(components, separator, nothing(), endDelimiter));
            } else {
                return withStartDelimiter(constant(newDelimiter));
            }
        }

        public CompoundStringBuilder withEndDelimiter(Generator<String> newDelimiter) {
            return new Maybes(components, separator, startDelimiter, just(newDelimiter));
        }

        public CompoundStringBuilder withEndDelimiter(String newDelimiter) {
            if (null == newDelimiter || newDelimiter.equals("")) {
                return separator.match(__ -> this,
                        __ -> new Maybes(components, separator, startDelimiter, nothing()));
            } else {
                return withEndDelimiter(constant(newDelimiter));
            }
        }

        public Generator<String> build() {
            Generator<String> inner = separator.match(__ -> Strings.concatMaybeStrings(components),
                    sep -> Strings.concatMaybeStrings(sep, components));
            return CompoundStringBuilder.applyStartDelimiter(startDelimiter,
                    CompoundStringBuilder.applyEndDelimiter(endDelimiter, inner));
        }
    }
}
