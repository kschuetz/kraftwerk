package dev.marksman.kraftwerk.bias;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.NonEmptyVector;
import dev.marksman.collectionviews.VectorBuilder;
import dev.marksman.enhancediterables.NonEmptyIterable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;

public abstract class BiasSetting<A> implements CoProduct2<BiasSetting.NoBias<A>,
        BiasSetting.InjectSpecialValues<A>, BiasSetting<A>> {

    private static final BiasSettingBuilder<?> SIMPLE_BUILDER = new BiasSettingBuilder<>(constantly(true),
            VectorBuilder.builder());

    public abstract BiasSetting<A> addSpecialValues(Iterable<A> specialValues);

    @EqualsAndHashCode(callSuper = true)
    @Value
    public static class NoBias<A> extends BiasSetting<A> {
        private static NoBias<?> INSTANCE = new NoBias<>();

        @Override
        public BiasSetting<A> addSpecialValues(Iterable<A> specialValues) {
            return NonEmptyVector.maybeCopyFrom(specialValues)
                    .match(__ -> this, InjectSpecialValues::new);
        }

        @Override
        public <R> R match(Fn1<? super NoBias<A>, ? extends R> aFn, Fn1<? super InjectSpecialValues<A>, ? extends R> bFn) {
            return aFn.apply(this);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class InjectSpecialValues<A> extends BiasSetting<A> {
        ImmutableNonEmptyVector<A> specialValues;

        @Override
        public BiasSetting<A> addSpecialValues(Iterable<A> specialValues) {
            return NonEmptyVector.maybeCopyFrom(specialValues)
                    .match(__ -> this, sv -> new InjectSpecialValues<>(NonEmptyVector.nonEmptyCopyFrom(this.specialValues.concat(sv))));
        }

        @Override
        public <R> R match(Fn1<? super NoBias<A>, ? extends R> aFn, Fn1<? super InjectSpecialValues<A>, ? extends R> bFn) {
            return bFn.apply(this);
        }
    }

    @SuppressWarnings("unchecked")
    public static <A> BiasSetting<A> noBias() {
        return (BiasSetting<A>) NoBias.INSTANCE;
    }

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

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class BiasSettingBuilder<A> {
        private final Fn1<A, Boolean> filter;
        private final VectorBuilder<A> specialValues;

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
