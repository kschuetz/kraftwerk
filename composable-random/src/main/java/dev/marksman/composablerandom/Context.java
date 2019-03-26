package dev.marksman.composablerandom;

// TODO: stacks and restore methods not necessary
// updater of context should be responsible for remembering old value

public interface Context {
    SizeParameters getSizeParameters();

    Object getApplicationData();

    Context withSizeParameters(SizeParameters sizeParameters);

    Context withApplicationData(Object applicationData);

    Context restoreSizeParameters();

    Context restoreApplicationData();
}
