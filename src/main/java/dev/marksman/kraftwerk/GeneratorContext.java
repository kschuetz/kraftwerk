package dev.marksman.kraftwerk;

import static dev.marksman.kraftwerk.Result.result;

public class GeneratorContext {
    public SizeSelector getSizeSelector() {
        // TODO
        return input -> result(input, 5);
    }

    public static GeneratorContext defaultContext() {
        return new GeneratorContext();
    }
}
