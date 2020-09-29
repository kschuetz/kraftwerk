package dev.marksman.kraftwerk;

import dev.marksman.kraftwerk.weights.BooleanWeights;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static dev.marksman.kraftwerk.CoProducts.generateBoolean;
import static dev.marksman.kraftwerk.weights.BooleanWeights.falses;
import static dev.marksman.kraftwerk.weights.BooleanWeights.trues;
import static testsupport.Assert.assertForAll;

class CoProductsTest {
    @Nested
    @DisplayName("generateBoolean")
    class GenerateBoolean {
        @Test
        void alwaysInRange() {
            BooleanWeights alwaysTrue = trues(1).toFalses(0);
            BooleanWeights alwaysFalse = falses(1).toTrues(0);

            assertForAll(generateBoolean(alwaysTrue), result -> result);
            assertForAll(generateBoolean(alwaysFalse), result -> !result);
        }
    }
}
