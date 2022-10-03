package software.kes.kraftwerk.util;

public final class Labeling {
    private Labeling() {

    }

    public static String intInterval(int min, int max, boolean exclusive) {
        return interval("int", min, max, exclusive);
    }

    public static String longInterval(long min, long max, boolean exclusive) {
        return interval("long", min, max, exclusive);
    }

    public static String interval(String label, long min, long max, boolean exclusive) {
        return label + " [" + min + ", " + max + (exclusive ? ")" : "]");
    }
}
