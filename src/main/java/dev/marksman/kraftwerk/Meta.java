package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.adt.Maybe;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

class Meta {

    static <A> Generator<A> withMetadata(Maybe<String> label, Maybe<Object> applicationData, Generator<A> operand) {
        while (operand instanceof WithMetadata<?>) {
            operand = ((WithMetadata<A>) operand).getOperand();
        }
        return new WithMetadata<>(label, applicationData, operand);
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class WithMetadata<A> implements Generator<A> {
        private final Maybe<String> label;
        private final Maybe<Object> applicationData;
        private final Generator<A> operand;

        @Override
        public Maybe<String> getLabel() {
            return label;
        }

        @Override
        public Maybe<Object> getApplicationData() {
            return applicationData;
        }

        Generator<A> getOperand() {
            return operand;
        }

        @Override
        public Generate<A> prepare(Parameters parameters) {
            return operand.prepare(parameters);
        }

    }

}
