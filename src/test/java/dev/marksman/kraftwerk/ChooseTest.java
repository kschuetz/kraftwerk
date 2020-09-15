package dev.marksman.kraftwerk;

import dev.marksman.kraftwerk.constraints.IntRange;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn2.All.all;
import static dev.marksman.kraftwerk.Choose.chooseAtLeastOneOfValues;
import static dev.marksman.kraftwerk.Choose.chooseOneOfValues;
import static dev.marksman.kraftwerk.Generators.generateInt;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static testsupport.Assert.assertForAll;
import static testsupport.CoversRange.coversRange;

class ChooseTest {
    @Nested
    @DisplayName("chooseOneOfValues")
    class ChooseOneOfValues {
        @Test
        void alwaysInRange() {
            assertForAll(chooseOneOfValues(1, 2, 3), n -> n == 1 || n == 2 || n == 3);
        }

        @Test
        void coverage() {
            int[] f = new int[5];
            chooseOneOfValues(0, 1, 2, 3, 4).run().take(1000).forEach(n -> f[n] += 1);
            assertTrue(coversRange(f));
        }
    }

    @Nested
    @DisplayName("chooseOneOf")
    class ChooseOneOf {
        @Test
        void alwaysInRange() {
            assertForAll(Choose.chooseOneOf(generateInt(IntRange.from(0).to(10)),
                    generateInt(IntRange.from(20).to(30))),
                    n -> (n >= 0 && n <= 10) || (n >= 20 && n <= 30));
        }
    }

    @Nested
    @DisplayName("chooseAtLeastOneOfValues")
    class ChooseAtLeastOneOfValues {
        @Test
        void alwaysInRange() {
            assertForAll(chooseAtLeastOneOfValues(1, 2, 3), xs ->
                    !xs.isEmpty() && all(n -> (n >= 1) && (n <= 3), xs));
        }

        @Test
        @Disabled
        void coverage() {
            // TODO: fix reservoir sample
            int[] f = new int[4];
            chooseAtLeastOneOfValues(0, 1, 2).run().take(100).forEach(xs -> {
                System.out.println(xs);
                f[xs.foldLeft(Integer::sum, 0)] += 1;
            });
            for (int i = 0; i < 4; i++) System.out.println(f[i]);
            assertTrue(coversRange(f));
        }
    }
}
