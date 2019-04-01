package dev.marksman.composablerandom;

public interface Context {
    SizeParameters getSizeParameters();

    Context withSizeParameters(SizeParameters sizeParameters);
}

