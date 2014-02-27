package com.oysteinstrand.javadonesimple.common;

public class Key<T> {
    private String name;

    public Key(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}