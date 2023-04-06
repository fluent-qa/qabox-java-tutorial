package io.fluent.qabox.util;


public class CloneSupport<T> implements Cloneable {

    @Override
    public T clone() {
        try {
            return (T) super.clone();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
