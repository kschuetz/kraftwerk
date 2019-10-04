package dev.marksman.shrink.util;

import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.optics.Iso;

public class ProductIso {

    public static <A, B, T> Iso.Simple<T, Product2<A, B>> productIso(Fn1<T, Product2<A, B>> f1,
                                                                     Fn1<Product2<A, B>, T> f2) {
        return Iso.simpleIso(f1, f2);
    }

    public static <A, B, T> Iso<T, T, Product2<A, B>, Product2<A, B>> productIso2(Fn1<T, Product2<A, B>> f1,
                                                                                  Fn1<Product2<A, B>, T> f2) {
        return Iso.simpleIso(f1, f2);
    }
}
