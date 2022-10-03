package software.kes.kraftwerk;

import com.jnape.palatable.lambda.adt.Maybe;

final class Meta {
    private Meta() {
    }

    static <A> Generator<A> withMetadata(Maybe<String> label, Maybe<Object> applicationData, Generator<A> underlying) {
        while (underlying instanceof WithMetadata<?>) {
            underlying = ((WithMetadata<A>) underlying).getUnderlying();
        }
        return new WithMetadata<>(label, applicationData, underlying);
    }

    private static class WithMetadata<A> implements Generator<A> {
        private final Maybe<String> label;
        private final Maybe<Object> applicationData;
        private final Generator<A> underlying;

        private WithMetadata(Maybe<String> label, Maybe<Object> applicationData, Generator<A> underlying) {
            this.label = label;
            this.applicationData = applicationData;
            this.underlying = underlying;
        }

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
        public GenerateFn<A> createGenerateFn(GeneratorParameters generatorParameters) {
            return underlying.createGenerateFn(generatorParameters);
        }
    }
}
