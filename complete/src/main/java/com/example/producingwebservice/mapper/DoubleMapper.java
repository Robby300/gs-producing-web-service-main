package com.example.producingwebservice.mapper;

public interface DoubleMapper<T, V> {

    T fromView(V view);

    V toView(T ent);

}
