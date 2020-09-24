package testsupport;

import java.util.Iterator;

public final class StringIterable implements Iterable<Character> {
    private final String underlying;

    private StringIterable(String underlying) {
        this.underlying = underlying;
    }

    public static StringIterable stringIterable(String underlying) {
        return new StringIterable(underlying);
    }

    @Override
    public Iterator<Character> iterator() {
        return new StringIterator();
    }

    class StringIterator implements Iterator<Character> {
        private int current;

        @Override
        public boolean hasNext() {
            return current < underlying.length();
        }

        @Override
        public Character next() {
            char result = underlying.charAt(current);
            current += 1;
            return result;
        }
    }
}
