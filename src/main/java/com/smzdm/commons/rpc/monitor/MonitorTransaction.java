package com.smzdm.commons.rpc.monitor;

/**
 * Created by zhengwenzhu on 16/10/10.
 */
public interface MonitorTransaction {
    void success();

    void failure(Throwable var1);

    void complete();
}
