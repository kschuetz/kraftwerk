package dev.marksman.composablerandom;

public interface Context {
    SizeParameters getSizeParameters();

    Object getApplicationData();

    Context withSizeParameters(SizeParameters sizeParameters);

    Context withApplicationData(Object applicationData);

    Context restoreSizeParameters();

    Context restoreApplicationData();
}
