package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

public abstract class BiasSetting<A> implements CoProduct2<BiasSetting.NoBias,
        BiasSetting.InjectSpecialValues<A>, BiasSetting<A>> {

    @EqualsAndHashCode(callSuper = true)
    @Value
    public static class NoBias<A> extends BiasSetting<A> {
        private static NoBias<?> INSTANCE = new NoBias<>();

        @Override
        public <R> R match(Fn1<? super NoBias, ? extends R> aFn, Fn1<? super InjectSpecialValues<A>, ? extends R> bFn) {
            return aFn.apply(this);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class InjectSpecialValues<A> extends BiasSetting<A> {
        private final ImmutableNonEmptyVector<A> specialValues;

        @Override
        public <R> R match(Fn1<? super NoBias, ? extends R> aFn, Fn1<? super InjectSpecialValues<A>, ? extends R> bFn) {
            return bFn.apply(this);
        }
    }

    @SuppressWarnings("unchecked")
    public static <A> BiasSetting<A> noBias() {
        return (BiasSetting<A>) NoBias.INSTANCE;
    }

    public static <A> BiasSetting<A> injectSpecialValues(ImmutableNonEmptyVector<A> specialValues) {
        return new InjectSpecialValues<>(specialValues);
    }

}
