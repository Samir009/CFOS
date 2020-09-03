package com.samir.cfos.helpers;

public class DefineClassType {
    //generic class in java
    //cast the object
    public static <T> T getType(Object type, Class<T> classes) {
        try {
            return classes.cast(type);
        } catch (ClassCastException ex) {
            return null;
        }
    }
}
