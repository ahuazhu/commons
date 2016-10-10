package com.smzdm.commons.job;

/**
 * Created by zhengwenzhu on 16/10/9.
 */
public interface Task {
    String getTaskName();

    int getTaskId();

    void fail(String msg);

    void success();

    boolean aborted();

    void abort();

    boolean hasSuccess();

    void triedOnce();

}
