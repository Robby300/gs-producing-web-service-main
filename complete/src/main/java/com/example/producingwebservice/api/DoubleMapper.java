package com.example.producingwebservice.api;

public interface DoubleMapper<T, V> {

    T fromView(V view);

    V toView(T ent);

}
