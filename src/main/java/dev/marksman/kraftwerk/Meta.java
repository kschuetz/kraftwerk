package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.adt.Maybe;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

class Meta {

    static <A> Generator<A> withMetadata(Maybe<String> label, Maybe<Object> applicationData, Generator<A> operand) {
        // TODO: withMetadata
        //        if (operand instanceof Primitives.WithMetadata) {
//            Primitives.WithMetadata<A> target1 = (Primitives.WithMetadata<A>) operand;
//            return new Primitives.WithMetadata<>(label, applicationData, target1.getOperand());
//        } else {
//            return new Primitives.WithMetadata<>(label, applicationData, operand);
//        }
        return operand;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class WithMetadata<A> implements Generator<A> {
        private final Maybe<String> label;
        private final Maybe<Object> applicationData;
        private final Generator<A> operand;

        @Override
        public Generate<A> prepare(Parameters parameters) {
            return operand.prepare(parameters);
        }

        @Override
        public boolean isPrimitive() {
            return false;
        }
    }
}
