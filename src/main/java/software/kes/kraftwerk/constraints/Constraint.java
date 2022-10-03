package software.kes.kraftwerk.constraints;

/**
 * A constraint on the possible values of type {@code A}.
 *
 * @param <A> the value type
 */
public interface Constraint<A> {

    /**
     * Tests if a value is included by this {@code Constraint}.
     *
     * @param value the value
     * @return a {@code boolean}
     */
    boolean includes(A value);
}
