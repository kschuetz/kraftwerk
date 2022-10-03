package software.kes.kraftwerk;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Functor;

/**
 * Associates a weight value with an object.  A weight value can be any non-negative integer.
 *
 * @param <A> the type of the underlying object
 */
public final class Weighted<A> implements Functor<A, Weighted<?>> {
    private final int weight;
    private final A value;

    private Weighted(int weight, A value) {
        this.weight = weight;
        this.value = value;
    }

    /**
     * Decorates a value with {@code Weighted}.
     *
     * @param weight the weight value; must be &gt;= 0
     * @param value  the underlying object
     * @param <A>    the type of the underlying object
     * @return a {@code Weighted<A>}
     */
    public static <A> Weighted<A> weighted(int weight, A value) {
        if (weight < 0) {
            throw new IllegalArgumentException("weight must be >= 0");
        }
        return new Weighted<>(weight, value);
    }

    /**
     * Decorates a value with {@code Weighted}, with a weight of 1.
     *
     * @param value the underlying object
     * @param <A>   the type of the underlying object
     * @return a {@code Weighted<A>}
     */
    public static <A> Weighted<A> weighted(A value) {
        return weighted(1, value);
    }

    @Override
    public <B> Weighted<B> fmap(Fn1<? super A, ? extends B> fn) {
        return weighted(weight, fn.apply(value));
    }

    /**
     * Creates a new {@code Weighted} for the same underlying object, with the weight value multiplied by {@code positiveFactor}.
     *
     * @param positiveFactor the factor; must be &gt;= 1
     * @return a {@code Weighted<A>}
     */
    public Weighted<A> multiplyBy(int positiveFactor) {
        if (positiveFactor < 1) {
            throw new IllegalArgumentException("factor must be positive");
        } else if (positiveFactor == 1) {
            return this;
        } else {
            return weighted(weight * positiveFactor, value);
        }
    }

    public int getWeight() {
        return this.weight;
    }

    public A getValue() {
        return this.value;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Weighted)) return false;
        final Weighted<?> other = (Weighted<?>) o;
        if (this.getWeight() != other.getWeight()) return false;
        final Object this$value = this.getValue();
        final Object other$value = other.getValue();
        return this$value == null ? other$value == null : this$value.equals(other$value);
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getWeight();
        final Object $value = this.getValue();
        result = result * PRIME + ($value == null ? 43 : $value.hashCode());
        return result;
    }

    public String toString() {
        return "Weighted(weight=" + this.getWeight() + ", value=" + this.getValue() + ")";
    }
}
