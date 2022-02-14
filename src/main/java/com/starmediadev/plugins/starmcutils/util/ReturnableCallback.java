package com.starmediadev.plugins.starmcutils.util;

/**
 * A callback that returns something
 * @param <T>
 * @param <R>
 */
public interface ReturnableCallback<T, R> {
    R callback(T t);
}
