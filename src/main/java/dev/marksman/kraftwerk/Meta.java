package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.adt.Maybe;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

class Meta {

    static <A> Generator<A> withMetadata(Maybe<String> label, Maybe<Object> applicationData, Generator<A> underlying) {
        while (underlying instanceof WithMetadata<?>) {
            underlying = ((WithMetadata<A>) underlying).getUnderlying();
        }
        return new WithMetadata<>(label, applicationData, underlying);
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class WithMetadata<A> implements Generator<A> {
        private final Maybe<String> label;
        private final Maybe<Object> applicationData;
        private final Generator<A> underlying;

        @Override
        public Maybe<String> getLabel() {
            return label;
        }

        @Override
        public Maybe<Object> getApplicationData() {
            return applicationData;
        }

        Generator<A> getUnderlying() {
            return underlying;
        }

        @Override
        public Generate<A> prepare(Parameters parameters) {
            return underlying.prepare(parameters);
        }

    }

}
