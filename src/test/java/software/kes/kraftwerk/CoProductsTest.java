package software.kes.kraftwerk;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import software.kes.kraftwerk.weights.BooleanWeights;

import static software.kes.kraftwerk.Generators.generateBoolean;
import static software.kes.kraftwerk.weights.BooleanWeights.falses;
import static software.kes.kraftwerk.weights.BooleanWeights.trues;
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
