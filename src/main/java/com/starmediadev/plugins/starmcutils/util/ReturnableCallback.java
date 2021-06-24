package com.starmediadev.plugins.starmcutils.util;

public interface ReturnableCallback<T, R> {
    R callback(T t);
}
