package dev.marksman.composablerandom;

import dev.marksman.composablerandom.metadata.Metadata;
import dev.marksman.composablerandom.tracing.Trace;

import static dev.marksman.composablerandom.Result.result;
import static dev.marksman.composablerandom.metadata.PrimitiveMetadata.primitiveMetadata;

public class TracingInterpreter {

    private static final Metadata PURE = primitiveMetadata("pure");
    private static final Metadata CUSTOM = primitiveMetadata("custom");

    private <A> Result<RandomState, Trace<A>> traceResult(RandomState randomState, Metadata metadata, A resultValue) {
        return result(randomState, Trace.trace(resultValue, metadata));
    }

    private <A> Result<RandomState, Trace<A>> traceResult(Metadata metadata, Result<RandomState, A> resultValue) {
        return result(resultValue.getNextState(), Trace.trace(resultValue.getValue(), metadata));
    }

    public <A> Result<RandomState, Trace<A>> execute(RandomState input, Instruction<A> instruction) {
        if (instruction instanceof Instruction.Pure) {
            //noinspection unchecked
            return traceResult(input, PURE, (A) ((Instruction.Pure) instruction).getValue());
        }

        if (instruction instanceof Instruction.Custom) {
            Instruction.Custom instruction1 = (Instruction.Custom) instruction;
            //noinspection unchecked
            return traceResult(CUSTOM, (Result<RandomState, A>) instruction1.getFn().apply(input));
        }


        throw new IllegalStateException("Unimplemented instruction");


    }


}
