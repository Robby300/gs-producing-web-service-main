package com.example.producingwebservice.mapper;

public interface DoubleMapper<T, V> { //todo перенести в пакет api

    T fromView(V view);

    V toView(T ent);

}
