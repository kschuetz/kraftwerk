package dev.marksman.composablerandom.examples;

import com.jnape.palatable.lambda.adt.Maybe;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Address {
    private final String number;
    private final Street street;
    private final Maybe<String> unit;
}
