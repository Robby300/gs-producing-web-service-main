package com.example.producingwebservice.api;

public interface DoubleMapper<T, V> { //todo перенести в пакет api // done

    T fromView(V view);

    V toView(T ent);

}
