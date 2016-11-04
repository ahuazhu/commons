package com.smzdm.commons.job;

/**
 * Created by zhengwenzhu on 16/10/10.
 */
public interface TaskParser<T> {
    Runnable parse(T task);
}
