package com.smzdm.commons.job;

/**
 * Created by zhengwenzhu on 16/10/10.
 */
public interface TaskParser {
    Runnable parse(Task task);
}
