package com.starmediadev.starmcutils.util;

public interface ReturnableCallback<T, R> {
    R callback(T t);
}
