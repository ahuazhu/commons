package com.smzdm.commons.rpc.monitor;

/**
 * Created by zhengwenzhu on 16/10/10.
 */
public interface Monitor {
    void init();

    void logEvent(String var1, String var2, String var3);

    void logError(String var1, Throwable var2);

    MonitorTransaction newTransaction(String var1, String var2);
}
