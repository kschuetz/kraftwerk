package dev.marksman.kraftwerk.bias;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.NonEmptyVector;
import dev.marksman.collectionviews.VectorBuilder;
import dev.marksman.enhancediterables.NonEmptyIterable;
import dev.marksman.kraftwerk.Generator;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;

/**
 * A strategy for injecting special values in a the output of {@link Generator}s of a specific type.
 *
 * @param <A> the output type
 */
public abstract class BiasSetting<A> implements CoProduct2<BiasSetting.NoBias<A>,
        BiasSetting.InjectSpecialValues<A>, BiasSetting<A>> {

    private static final BiasSettingBuilder<?> SIMPLE_BUILDER = new BiasSettingBuilder<>(constantly(true),
            VectorBuilder.builder());

    /**
     * A {@code BiasSetting} that instructs {@link Generator}s to inject no special values.
     */
    @SuppressWarnings("unchecked")
    public static <A> BiasSetting<A> noBias() {
        return (BiasSetting<A>) NoBias.INSTANCE;
    }

    /**
     * A {@code BiasSetting} that instructs {@link Generator}s to inject special values into their output.
     * <p>
     * These values will artificially occur more frequently in the output of a {@code Generator}, provided they are in its range.
     *
     * @param specialValues the collection of special values
     */
    public static <A> BiasSetting<A> injectSpecialValues(NonEmptyIterable<A> specialValues) {
        return new InjectSpecialValues<>(NonEmptyVector.nonEmptyCopyFrom(specialValues));
    }

    @SuppressWarnings("unchecked")
    public static <A> BiasSettingBuilder<A> builder() {
        return (BiasSettingBuilder<A>) SIMPLE_BUILDER;
    }

    public static <A> BiasSettingBuilder<A> builder(Fn1<A, Boolean> filter) {
        return new BiasSettingBuilder<>(filter, VectorBuilder.builder());
    }

    public abstract BiasSetting<A> addSpecialValues(Iterable<A> specialValues);

    public static final class NoBias<A> extends BiasSetting<A> {
        private static final NoBias<?> INSTANCE = new NoBias<>();

        public NoBias() {
        }

        @Override
        public BiasSetting<A> addSpecialValues(Iterable<A> specialValues) {
            return NonEmptyVector.maybeCopyFrom(specialValues)
                    .match(__ -> this, InjectSpecialValues::new);
        }

        @Override
        public <R> R match(Fn1<? super NoBias<A>, ? extends R> aFn, Fn1<? super InjectSpecialValues<A>, ? extends R> bFn) {
            return aFn.apply(this);
        }

        @Override
        public String toString() {
            return "NoBias{}";
        }
    }

    public static final class InjectSpecialValues<A> extends BiasSetting<A> {
        private final ImmutableNonEmptyVector<A> specialValues;

        private InjectSpecialValues(ImmutableNonEmptyVector<A> specialValues) {
            this.specialValues = specialValues;
        }

        @Override
        public BiasSetting<A> addSpecialValues(Iterable<A> specialValues) {
            return NonEmptyVector.maybeCopyFrom(specialValues)
                    .match(__ -> this, sv -> new InjectSpecialValues<>(NonEmptyVector.nonEmptyCopyFrom(this.specialValues.concat(sv))));
        }

        @Override
        public <R> R match(Fn1<? super NoBias<A>, ? extends R> aFn, Fn1<? super InjectSpecialValues<A>, ? extends R> bFn) {
            return bFn.apply(this);
        }

        public ImmutableNonEmptyVector<A> getSpecialValues() {
            return this.specialValues;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            InjectSpecialValues<?> that = (InjectSpecialValues<?>) o;

            return specialValues.equals(that.specialValues);
        }

        @Override
        public int hashCode() {
            return specialValues.hashCode();
        }

        @Override
        public String toString() {
            return "InjectSpecialValues{" +
                    "specialValues=" + specialValues +
                    '}';
        }
    }

    public static class BiasSettingBuilder<A> {
        private final Fn1<A, Boolean> filter;
        private final VectorBuilder<A> specialValues;

        private BiasSettingBuilder(Fn1<A, Boolean> filter, VectorBuilder<A> specialValues) {
            this.filter = filter;
            this.specialValues = specialValues;
        }

        public BiasSettingBuilder<A> addSpecialValue(A value) {
            if (filter.apply(value)) {
                return new BiasSettingBuilder<>(filter, specialValues.add(value));
            } else {
                return this;
            }
        }

        public BiasSetting<A> build() {
            return specialValues.build().toNonEmpty()
                    .match(__ -> noBias(),
                            BiasSetting::injectSpecialValues);
        }
    }
}
