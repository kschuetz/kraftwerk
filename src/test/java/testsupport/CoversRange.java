package testsupport;

public class CoversRange {

    // This is a simple check that the histogram is at least 99% populated.
    // It doesn't check for uniform distribution.
    public static boolean coversRange(int[] frequencies) {
        int length = frequencies.length;
        int tolerance = 0;
        if (length >= 100) {
            tolerance = length - (length * 99) / 100;
        }
        for (int frequency : frequencies) {
            if (frequency < 1) {
                tolerance--;
                if (tolerance < 0) {
                    return false;
                }
            }
        }
        return true;
    }

}
