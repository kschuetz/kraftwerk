package dev.marksman.composablerandom.spike;

import dev.marksman.composablerandom.Generator;
import dev.marksman.shrink.Shrink;
import dev.marksman.shrink.builtins.ShrinkNumerics;

public class ShrinkBuilder {

    public <A> Shrink<A> generatorToShrink(Generator<A> gen) {
        if (gen instanceof Generator.Constant) {
            return Shrink.none();
        }

//        if (gen instanceof Generator.Custom) {
//            return customImpl(((Generator.Custom<A>) gen).getFn());
//        }
//
//        if (gen instanceof Generator.Mapped) {
//            return handleMapped(context, (Generator.Mapped<?, A>) gen);
//        }
//
//        if (gen instanceof Generator.FlatMapped) {
//            return handleFlatMapped(context, (Generator.FlatMapped<?, A>) gen);
//        }
//
//        if (gen instanceof Generator.Tap) {
//            return handleTap(context, (Generator.Tap<?, A>) gen);
//        }

        if (gen instanceof Generator.NextInt) {
            //noinspection unchecked
            return (Shrink<A>) ShrinkNumerics.shrinkInt();
        }

        if (gen instanceof Generator.NextLong) {
            //noinspection unchecked
            return (Shrink<A>) ShrinkNumerics.shrinkLong();
        }
//
//        if (gen instanceof Generator.NextBoolean) {
//            //noinspection unchecked
//            return (GeneratorState<A>) nextBooleanImpl();
//        }
//
//        if (gen instanceof Generator.NextDouble) {
//            //noinspection unchecked
//            return (GeneratorState<A>) nextDoubleImpl();
//        }
//
//        if (gen instanceof Generator.NextFloat) {
//            //noinspection unchecked
//            return (GeneratorState<A>) nextFloatImpl();
//        }

        if (gen instanceof Generator.NextIntBounded) {
            Generator.NextIntBounded g1 = (Generator.NextIntBounded) gen;
            //noinspection unchecked
            return (Shrink<A>) ShrinkNumerics.shrinkIntBounded(g1.getBound());
        }

        if (gen instanceof Generator.NextIntExclusive) {
            Generator.NextIntExclusive g1 = (Generator.NextIntExclusive) gen;
            //noinspection unchecked
            return (Shrink<A>) ShrinkNumerics.shrinkIntExclusive(g1.getOrigin(), g1.getBound());
        }

        if (gen instanceof Generator.NextIntBetween) {
            Generator.NextIntBetween g1 = (Generator.NextIntBetween) gen;
            //noinspection unchecked
            return (Shrink<A>) ShrinkNumerics.shrinkIntBetween(g1.getMin(), g1.getMax());
        }

        if (gen instanceof Generator.NextIntIndex) {
            Generator.NextIntIndex g1 = (Generator.NextIntIndex) gen;
            //noinspection unchecked
            return (Shrink<A>) ShrinkNumerics.shrinkIntBounded(g1.getBound());
        }

        if (gen instanceof Generator.NextLongBounded) {
            Generator.NextLongBounded g1 = (Generator.NextLongBounded) gen;
            //noinspection unchecked
            return (Shrink<A>) ShrinkNumerics.shrinkLongBounded(g1.getBound());
        }

        if (gen instanceof Generator.NextLongExclusive) {
            Generator.NextLongExclusive g1 = (Generator.NextLongExclusive) gen;
            //noinspection unchecked
            return (Shrink<A>) ShrinkNumerics.shrinkLongExclusive(g1.getOrigin(), g1.getBound());
        }

        if (gen instanceof Generator.NextLongBetween) {
            Generator.NextLongBetween g1 = (Generator.NextLongBetween) gen;
            //noinspection unchecked
            return (Shrink<A>) ShrinkNumerics.shrinkLongBetween(g1.getMin(), g1.getMax());
        }

        if (gen instanceof Generator.NextLongIndex) {
            Generator.NextLongIndex g1 = (Generator.NextLongIndex) gen;
            //noinspection unchecked
            return (Shrink<A>) ShrinkNumerics.shrinkLongBounded(g1.getBound());
        }

//        if (gen instanceof Generator.NextGaussian) {
//            //noinspection unchecked
//            return (GeneratorState<A>) nextGaussianImpl();
//        }
//
//        if (gen instanceof Generator.NextByte) {
//            //noinspection unchecked
//            return (GeneratorState<A>) nextByteImpl();
//        }
//
//        if (gen instanceof Generator.NextShort) {
//            //noinspection unchecked
//            return (GeneratorState<A>) nextShortImpl();
//        }
//
//        if (gen instanceof Generator.NextBytes) {
//            Generator.NextBytes g1 = (Generator.NextBytes) gen;
//            //noinspection unchecked
//            return (GeneratorState<A>) nextBytesImpl(g1.getCount());
//        }

        if (gen instanceof Generator.WithMetadata) {
            Generator.WithMetadata g1 = (Generator.WithMetadata) gen;
            //noinspection unchecked
            return generatorToShrink(g1.getOperand());
        }

//        if (gen instanceof Generator.Sized) {
//            Generator.Sized g1 = (Generator.Sized) gen;
//
//            //noinspection unchecked
//            return sizedImpl(context.getParameters().getSizeSelector(),
//                    rs -> context.recurse((Generator<A>) g1.getFn().apply(rs)));
//        }

//        if (gen instanceof Generator.Aggregate) {
//            Generator.Aggregate g1 = (Generator.Aggregate) gen;
//            //noinspection unchecked
//            Iterable<Generator<A>> elements = g1.getElements();
//
//            //noinspection unchecked
//            return (GeneratorState<A>) aggregateImpl(g1.getInitialBuilderSupplier(), g1.getAddFn(),
//                    g1.getBuildFn(), map(context::recurse, elements));
//        }

//        if (gen instanceof Generator.Product2) {
//            Generator.Product2 g1 = (Generator.Product2) gen;
//            //noinspection unchecked
//            return (GeneratorState<A>) product2Impl(context.recurse(g1.getA()),
//                    context.recurse(g1.getB()),
//                    g1.getCombine());
//        }
//
//        if (gen instanceof Generator.Product3) {
//            Generator.Product3 g1 = (Generator.Product3) gen;
//            //noinspection unchecked
//            return (GeneratorState<A>) product3Impl(context.recurse(g1.getA()),
//                    context.recurse(g1.getB()),
//                    context.recurse(g1.getC()),
//                    g1.getCombine());
//        }
//
//        if (gen instanceof Generator.Product4) {
//            Generator.Product4 g1 = (Generator.Product4) gen;
//            //noinspection unchecked
//            return (GeneratorState<A>) product4Impl(context.recurse(g1.getA()),
//                    context.recurse(g1.getB()),
//                    context.recurse(g1.getC()),
//                    context.recurse(g1.getD()),
//                    g1.getCombine());
//        }
//
//        if (gen instanceof Generator.Product5) {
//            Generator.Product5 g1 = (Generator.Product5) gen;
//            //noinspection unchecked
//            return (GeneratorState<A>) product5Impl(context.recurse(g1.getA()),
//                    context.recurse(g1.getB()),
//                    context.recurse(g1.getC()),
//                    context.recurse(g1.getD()),
//                    context.recurse(g1.getE()),
//                    g1.getCombine());
//        }
//
//        if (gen instanceof Generator.Product6) {
//            Generator.Product6 g1 = (Generator.Product6) gen;
//            //noinspection unchecked
//            return (GeneratorState<A>) product6Impl(context.recurse(g1.getA()),
//                    context.recurse(g1.getB()),
//                    context.recurse(g1.getC()),
//                    context.recurse(g1.getD()),
//                    context.recurse(g1.getE()),
//                    context.recurse(g1.getF()),
//                    g1.getCombine());
//        }
//
//        if (gen instanceof Generator.Product7) {
//            Generator.Product7 g1 = (Generator.Product7) gen;
//            //noinspection unchecked
//            return (GeneratorState<A>) product7Impl(context.recurse(g1.getA()),
//                    context.recurse(g1.getB()),
//                    context.recurse(g1.getC()),
//                    context.recurse(g1.getD()),
//                    context.recurse(g1.getE()),
//                    context.recurse(g1.getF()),
//                    context.recurse(g1.getG()),
//                    g1.getCombine());
//        }
//
//        if (gen instanceof Generator.Product8) {
//            Generator.Product8 g1 = (Generator.Product8) gen;
//            //noinspection unchecked
//            return (GeneratorState<A>) product8Impl(context.recurse(g1.getA()),
//                    context.recurse(g1.getB()),
//                    context.recurse(g1.getC()),
//                    context.recurse(g1.getD()),
//                    context.recurse(g1.getE()),
//                    context.recurse(g1.getF()),
//                    context.recurse(g1.getG()),
//                    context.recurse(g1.getH()),
//                    g1.getCombine());
//        }
//
//        if (gen instanceof Generator.InjectSpecialValues) {
//            Generator.InjectSpecialValues<A> g1 = (Generator.InjectSpecialValues<A>) gen;
//            NonEmptyFiniteIterable<A> acc = g1.getSpecialValues();
//            while (g1.getInner() instanceof Generator.InjectSpecialValues) {
//                g1 = (Generator.InjectSpecialValues<A>) g1.getInner();
//                acc = acc.concat(g1.getSpecialValues());
//            }
//            ImmutableNonEmptyVector<A> specialValues = NonEmptyVector.copyFromOrThrow(acc);
//            return mixInSpecialValuesImpl(specialValues, 20 + 3 * specialValues.size(),
//                    context.recurse(g1.getInner()));
//        }

        return Shrink.none();
    }
}
